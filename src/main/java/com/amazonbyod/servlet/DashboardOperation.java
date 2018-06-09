package com.amazonbyod.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonbyod.awsprop.AWSProjectProperties;
import com.amazonbyod.kony.KonyMobilePushNotification;
import com.amazonbyod.listclass.CompanyProducts;
import com.amazonbyod.listclass.CompanyProfile;
import com.amazonbyod.listclass.Companyannouncements;
import com.amazonbyod.listclass.StockChanger;
import com.amazonbyod.mockupdata.DataMockupGenerator;
import com.amazonbyod.mysql.MySQLConnection;
import com.amazonbyod.redshift.AwsRedshiftOperations;
import com.amazonbyod.s3.S3Operations;
import com.amazonbyod.scheduler.StreamingMockupData;
import com.amazonbyod.scheduler.TriggerKonyNotification;

/**
 * Servlet implementation class DashboardOperation
 */
public class DashboardOperation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final long unixTime = System.currentTimeMillis() / 1000L;
	static AWSProjectProperties awscredentials = new AWSProjectProperties();
	static S3Operations s3 = new S3Operations();
	
	String companySymbol="";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DashboardOperation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		/*response.setContentType("application/json");*/
		
		//content type must be set to text/event-stream
		  response.setContentType("text/event-stream"); 

		  //encoding must be set to UTF-8
		  response.setCharacterEncoding("UTF-8");
		  String bucketName=awscredentials.getBucketName();
		
		  PrintWriter out = response.getWriter();
		
		
		//Mysql Connection
		MySQLConnection mysql = new MySQLConnection();
		//AWS Credentials 
		AWSCredentials credentials = new BasicAWSCredentials(awscredentials.getAccessKey(), awscredentials.getSecretKey());
	    //S3 Client
		AmazonS3 s3client = new AmazonS3Client(credentials);
		//Redshift 
	    AwsRedshiftOperations redShift = new AwsRedshiftOperations();
	    //Date format
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		
		DataMockupGenerator mockup = new DataMockupGenerator(); 
		
		//Create Redshift Table
		Connection redshiftconn = redShift.redShiftConnect();
		
        
		//Kony Push Notification
		KonyMobilePushNotification kony = new KonyMobilePushNotification();
		Calendar endcal = Calendar.getInstance();
		endcal.add(Calendar.DATE, -1);
		
		Calendar startcal = Calendar.getInstance();
		startcal.add(Calendar.YEAR, -1);

		String startDate = df.format(startcal.getTime());
		String endDate = df.format(endcal.getTime());

		Date startDt = startcal.getTime();
		Date endDt = endcal.getTime();
		
		Date date = new Date();
		
		if(request.getParameter("datatype")!=null){
		String datatype=request.getParameter("datatype").toString();
		System.out.println("Hello "+datatype);
		
		if(datatype.equals("weather")){
			
			redShift.startCreateTable(redshiftconn);
			
			String s3folder="weatherdata/weatherdata.csv";
			//s3client.setRegion(Region.getRegion(Regions.US_WEST_2));
			s3.S3Upload(s3client, bucketName, s3folder, awscredentials.getResourcePath()+"//Datasets//HistoricalWeatherData//weather_data_updated.csv");
			
			String S3weatherdataUpload=buildJson("weatherdata","green","<p style='color:green'>Successfully Completed</p> On:"+new Date());
			
			String[] filePathSplit=s3folder.split("/");
	 		String fileName=filePathSplit[filePathSplit.length-1];

			Connection redShiftConnect = redShift.redShiftConnect();
			redShift.loadDatafromS3(redShiftConnect, "weather_storm_data", bucketName+"/weatherdata", fileName);
			redShift.redShiftDisconnect(redShiftConnect);
			
			String redshiftweather = buildJson("redshift","green","<p style='color:green'>Successfully Completed</p> On:"+new Date());
			out.write(S3weatherdataUpload+"---"+redshiftweather);
		
			
		  }else if(datatype.equals("mockup")){
			  String weatherDataStart=buildJson("mockup","green","Start At:"+new Date());
			  Connection conn = mysql.mysqlConnect();
				
				// Generate Company Profile
				List<CompanyProfile> cplist = mockup.createCompanyProfile();
				String companyID = cplist.get(0).getCompanyId();
				companySymbol = cplist.get(0).getCompanySymbol();

				

				mockup.setCompanySymbol(companySymbol);
				// Generate Company Products
				List<CompanyProducts> cproList = mockup.createCompanyProduct(companyID, startDt, endDt);
				// Generate Company announcement
				List<Companyannouncements> calist = mockup.createCompanyannouncements(companyID, startDt, endDt);

				List<StockChanger> stockChanger = new ArrayList<StockChanger>();

				// Get all Events Date
				for (int i = 0; i < cproList.size(); i++) {
					StockChanger sc = new StockChanger(df.format(cproList.get(i).getProduct_initaldate()), 1);
					stockChanger.add(sc);
				}

				for (int i = 0; i < calist.size(); i++) {
					int flag = 0;
					if (calist.get(i).getAnnouncemnType() == "Product Lunch"
							|| calist.get(i).getAnnouncemnType() == "Revenue Increased") {
						flag = 1;
					}
					StockChanger sc = new StockChanger(df.format(calist.get(i).getAnnouncementDate()), flag);
					stockChanger.add(sc);
				}
			  
				// Insert Data Into Mysql Table
				 mysql.insertDataCompanyMaster(conn, cplist);
			     mysql.insertDataCompanyAnnouncement(conn, calist);
				 mysql.insertDataCompanyProduct(conn, cproList);
		         mysql.mysqlDisconnect(conn);
		         String mysqlstatus=buildJson("mysql","green","<p style='color:green'>Successfully Completed</p> On:"+new Date());
		         
		         String filePath=mockup.minMockUpData(awscredentials.getResourcePath()+"//StockData", startDate, endDate,
						companySymbol, 35, 42, 35000, 37541, stockChanger);
		 		
		 		 String[] filePathSplit=filePath.split("/");
		 		 String fileName=filePathSplit[filePathSplit.length-1];
		 		 String s3folder="stockdata/"+fileName;
		 		 s3.S3Upload(s3client, bucketName, s3folder , filePath);
		         String s3uplaod=buildJson("s3uploadmockup","green","<p style='color:green'>Successfully Completed</p> On:"+new Date()); 
		         
		        Connection redShiftConnect = redShift.redShiftConnect();
		 		redShift.loadDatafromS3(redShiftConnect, "stock_data", bucketName+"/stockdata", fileName);
		 		redShift.redShiftDisconnect(redShiftConnect);
		        String redshiftload=buildJson("redshiftmockup","green","<p style='color:green'>Successfully Completed</p> On:"+new Date());
			    out.write(weatherDataStart+"---"+mysqlstatus+"---"+s3uplaod+"---"+redshiftload);
			  
		  }else if (datatype.equals("incremental")){
			    String Incrementaldata=buildJson("incremental","green","<p style='color:green'>Successfully Completed</p> On:"+new Date());
			   // Initialized Quartz
				StreamingMockupData streaming = new StreamingMockupData(companySymbol);
				streaming.startStreaming();
				String IncrementaldataStart=buildJson("incrementalred","green","<p style='color:green'>Successfully Completed</p> On:"+new Date());
				out.write(Incrementaldata+"---"+IncrementaldataStart);
				
		  }else if(datatype.equals("cloudbeam")){
			  
			  String cloudbeamtask=buildJson("mysql","green","Start At:"+new Date());
			  String url = awscredentials.getCloudbeam_slave_url();
			  String taskname = awscredentials.getCloudbeam_taskname();
			  CloseableHttpClient client = HttpClients.createDefault();
			  HttpPost httpPost = new HttpPost(url);
			  List<NameValuePair> params = new ArrayList<NameValuePair>();
			  params.add(new BasicNameValuePair("taskname", taskname));
			  httpPost.setEntity(new UrlEncodedFormEntity(params));
			  CloseableHttpResponse httpresponse = client.execute(httpPost);
			  System.out.println(httpresponse.getStatusLine().getStatusCode());
			  String cloudbeamtaskstatus=buildJson("cloudbeamredshift","green","<p style='color:green'>Successfully Completed</p> On:"+new Date());
			  client.close();
			  out.write(cloudbeamtask+"---"+cloudbeamtaskstatus);
		  }else if(datatype.equals("prediction")){
			  String cloudbeamtaskstatus=buildJson("prediction","green","<p style='color:green'>Successfully Completed</p> On:"+new Date());
				
			    String pythonPath=awscredentials.getResourcePath()+"//Scripts//PythonScript//Scheduler.py "+awscredentials.getResourcePath()+" "+awscredentials.getAccessKey()+" "+awscredentials.getSecretKey()+" "+awscredentials.getBucketName()+" "+awscredentials.getRedshift_dbname()+" "+awscredentials.getMaster_username()+" "+awscredentials.getMaster_password()+" "+awscredentials.getRedshifturl()+" "+awscredentials.getRedshiftport();
			    System.out.println(pythonPath);
			    TriggerKonyNotification tkony = new TriggerKonyNotification();
			    Runtime runtime = Runtime.getRuntime();
			    Process processs = runtime.exec("python "+pythonPath);
				OutputStream output = processs.getOutputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(processs.getInputStream()));				
				tkony.startNotification();
				out.println(cloudbeamtaskstatus);
				//System.out.println(cloudbeamtaskstatus);
				
		  }else{
			  
		  }
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public String buildJson(String classname,String color,String status){
		String json = "{\"class\":\""+classname+"\",\"color\":\""+color+"\",\"status\":\""+status+"\"}";
		return json;
	}
	


}
