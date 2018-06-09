package com.amazonbyod.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazonbyod.awsprop.AWSProjectProperties;

/**
 * Servlet implementation class EditProperties
 */
public class EditProperties extends HttpServlet {
	private static final long serialVersionUID = 1L;
	AWSProjectProperties prop = new AWSProjectProperties();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditProperties() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/json");
		PrintWriter out = response.getWriter();
		
		String accessKey = prop.getAccessKey();
		String secretKey = prop.getSecretKey();
		String bucketName = prop.getBucketName();
		String redshift_jdbc_url = prop.getRedshift_jdbc_url();
		String master_username = prop.getMaster_username();
		String master_password = prop.getMaster_password();
		String mysql_dbname = prop.getMysql_dbname();
		String mysql_username = prop.getMysql_username();
		String mysql_password = prop.getMysql_password();
		String mysql_JDBC_DRIVER = prop.getMysql_JDBC_DRIVER();
		String mysql_DB_URL = prop.getMysql_DB_URL();
		String stockDatapath = prop.getStockDatapath();
		String weatherDatapath = prop.getWeatherDatapath();
		String cloudbeamurl = prop.getCloudbeam_slave_url();
		String cloudbeam_taskname = prop.getCloudbeam_taskname();
		String kony_url = prop.getKony_url();
		String prediction_path = prop.getPrediction_path();
		String resourcepath = prop.getResourcePath();
		String redshiftport = prop.getRedshiftport();
		String redshifturl = prop.getRedshifturl();
		String redshiftdbname = prop.getRedshift_dbname();
		String konyappid = prop.getKony_appid();
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("accessKey", accessKey);
		map.put("secretKey", secretKey);
		map.put("bucketName", bucketName);
		map.put("redshift_jdbc_url", redshift_jdbc_url);
		map.put("master_username", master_username);
		map.put("master_password", master_password);
		map.put("mysql_dbname", mysql_dbname);
		map.put("mysql_username", mysql_username);
		map.put("mysql_password", mysql_password);
		map.put("mysql_JDBC_DRIVER", mysql_JDBC_DRIVER);
		map.put("mysql_DB_URL", mysql_DB_URL);
		map.put("stockDatapath", stockDatapath);
		map.put("cloudbeamurl", cloudbeamurl);
		map.put("cloudbeam_taskname", cloudbeam_taskname);
		map.put("kony_url", kony_url);
		map.put("weatherDatapath", weatherDatapath);
		map.put("prediction_path", prediction_path);
		map.put("resourcepath", resourcepath);
		map.put("redshiftport", redshiftport);
		map.put("redshifturl", redshifturl);
		map.put("redshiftdbname", redshiftdbname);
		map.put("konyappid", konyappid);
		
		JSONObject obj = new JSONObject();
		try {
			obj.put("projectProp", map);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		out.print(obj.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		String accessKey = request.getParameter("accesskey");
		String secretKey = request.getParameter("secretkey");
		String bucketName = request.getParameter("bucketname");
		String redshift_jdbc_url = request.getParameter("redshiftjdbcurl");
		String master_username = request.getParameter("redshiftusername");
		String master_password = request.getParameter("redshiftuserpassword");
		String mysql_dbname = request.getParameter("mysqldbname");
		String mysql_username = request.getParameter("mysqlusername");
		String mysql_password = request.getParameter("mysqlpassword");
		String mysql_JDBC_DRIVER = request.getParameter("mysqljdbcclass");
		String mysql_DB_URL = request.getParameter("mysqljdbcurl");
		String stockDatapath = request.getParameter("stockdatapathvalue");
		String cloudbeamurl = request.getParameter("cloudbeam_auto_url");
		String cloudbeam_taskname = request.getParameter("cloudbeamtaskname");
		String kony_url = request.getParameter("kony_url");
		String weatherDatapath = request.getParameter("weatherdatapathvalue");
		String prediction_path = request.getParameter("predictionpath");
		
		prop.setAccessKey(accessKey);
		prop.setSecretKey(secretKey);
		prop.setBucketName(bucketName);
		prop.setRedshift_jdbc_url(redshift_jdbc_url);
		prop.setMaster_username(master_username);
		prop.setMaster_password(master_password);
		prop.setMysql_dbname(mysql_dbname);
		prop.setMysql_username(mysql_username);
		prop.setMysql_password(mysql_password);
		prop.setMysql_JDBC_DRIVER(mysql_JDBC_DRIVER);
		prop.setMysql_DB_URL(mysql_DB_URL);
		prop.setStockDatapath(stockDatapath);
		prop.setCloudbeam_slave_url(cloudbeamurl);
		prop.setCloudbeam_taskname(cloudbeam_taskname);
		prop.setKony_url(kony_url);
		prop.setWeatherDatapath(weatherDatapath);
		prop.setPrediction_path(prediction_path);
	}

}
