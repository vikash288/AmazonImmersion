package com.amazonbyod.mockupdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.fluttercode.datafactory.impl.DataFactory;
import org.joda.time.DateTime;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonbyod.awsprop.AWSProjectProperties;
import com.amazonbyod.listclass.CompanyProducts;
import com.amazonbyod.listclass.CompanyProfile;
import com.amazonbyod.listclass.Companyannouncements;
import com.amazonbyod.listclass.StockChanger;
import com.amazonbyod.listclass.StockData;
import com.amazonbyod.listclass.WeatherData;
import com.amazonbyod.mysql.MySQLConnection;
import com.amazonbyod.redshift.AwsRedshiftOperations;
import com.amazonbyod.s3.S3Operations;
import com.amazonbyod.scheduler.StreamingMockupData;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * @author abhinandan
 *
 */
public class DataMockupGenerator {

	static final long unixTime = System.currentTimeMillis() / 1000L;
	static AWSProjectProperties awscredentials = new AWSProjectProperties();
	static S3Operations s3 = new S3Operations();
	static String bucketName="amazon-aws-immersion-project";
	String companySymbol;

	/**
	 * @param directoryName
	 */

	public void createDirectoryIfNeeded(String directoryName) {
		File theDir = new File(directoryName);
		// if the directory does not exist, create it
		if (!theDir.exists() && !theDir.isDirectory()) {
			theDir.mkdirs();
		}
	}

	/**
	 * @param startDate
	 * @param endDate
	 * @param companySymbol
	 * @param openVal
	 * @param closeVal
	 * @param stockVol
	 * @throws IOException
	 */
	public String minMockUpData(String saveDir, String startDate, String endDate, String companySymbol, int openVal,
			int closeVal, int openStockVol, int closeStockVol, List<StockChanger> stockChanger) throws IOException {

		DataFactory df = new DataFactory();
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);
		DateFormat dformater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
		DateFormat tformater = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		String filePath="";
		if (saveDir != "") {
			createDirectoryIfNeeded(saveDir);
			filePath=saveDir + "/historicalstock_" + unixTime + ".csv";
			CSVWriter writer = new CSVWriter(new FileWriter(filePath),CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			while (!start.isAfter(end)) {
            
				if (stockChanger.contains(start.toString())) {
					int indexVal = stockChanger.indexOf(start.toString());
					int stockFlag = stockChanger.get(indexVal).getChangeType();

					int tempopenVal = 0;
					int tempcloseVal = 0;
					int tempopenStockVol = 0;
					int tempcloseStockVol = 0;

					if (stockFlag == 1) {
						tempopenVal = openVal + 5;
						tempcloseVal = closeVal + 5;
						tempopenStockVol = openStockVol + 5;
						tempcloseStockVol = closeStockVol + 5;
					} else {
						tempopenVal = openVal - 5;
						tempcloseVal = closeVal - 5;
						tempopenStockVol = openStockVol - 5;
						tempcloseStockVol = closeStockVol - 5;
					}

					try {
						Date startTime = dformater.parse(start.toString() + " 06:00:00");
						Date endTime = dformater.parse(start.toString() + " 14:00:00");
						DateTime dateTime = new DateTime(startTime);
						int seconds = (int) ((endTime.getTime() - startTime.getTime()) / 1000);

						for (int i = 5; i <= seconds; i += 5) {

							int openvalint = df.getNumberBetween(tempopenVal, tempcloseVal);
							int openvalpoint = df.getNumberBetween(00, 99);

							int highvalint = df.getNumberBetween(tempopenVal, tempcloseVal);
							int highvalpoint = df.getNumberBetween(00, 99);

							int lowvalint = df.getNumberBetween(tempopenVal, tempcloseVal);
							int lowvalpoint = df.getNumberBetween(00, 99);

							int closevalint = df.getNumberBetween(tempopenVal, tempcloseVal);
							int closevalpoint = df.getNumberBetween(00, 99);

							int stockVolume = df.getNumberBetween(tempopenStockVol, tempcloseStockVol);

							float openStockVal = Float.parseFloat(openvalint + "." + openvalpoint);
							float highStockVal = Float.parseFloat(highvalint + "." + highvalpoint);
							float lowStockVal = Float.parseFloat(lowvalint + "." + lowvalpoint);
							float closeStockVal = Float.parseFloat(closevalint + "." + closevalpoint);
							if (highStockVal < lowStockVal) {
								float temp = highStockVal;
								highStockVal = lowStockVal;
								lowStockVal = temp;
							}

							String csvRow = companySymbol + "," + start.toString() + ","
									+ start.toString()+" "+tformater.format(dateTime.plusSeconds(i).toDate()) + "," + openStockVal + ","
									+ highStockVal + "," + lowStockVal + "," + closeStockVal + "," + stockVolume + ","
									+ "0" + "," + "1" + "," + openStockVal + "," + highStockVal + "," + lowStockVal
									+ "," + closeStockVal + "," + stockVolume;
							writer.writeNext(csvRow.split(","));
							writer.flush();
						}

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					try {
						Date startTime = dformater.parse(start.toString() + " 06:00:00");
						Date endTime = dformater.parse(start.toString() + " 14:00:00");
						DateTime dateTime = new DateTime(startTime);
						int seconds = (int) ((endTime.getTime() - startTime.getTime()) / 1000);

						for (int i = 5; i <= seconds; i += 5) {

							int openvalint = df.getNumberBetween(openVal, closeVal);
							int openvalpoint = df.getNumberBetween(00, 99);

							int highvalint = df.getNumberBetween(openVal, closeVal);
							int highvalpoint = df.getNumberBetween(00, 99);

							int lowvalint = df.getNumberBetween(openVal, closeVal);
							int lowvalpoint = df.getNumberBetween(00, 99);

							int closevalint = df.getNumberBetween(openVal, closeVal);
							int closevalpoint = df.getNumberBetween(00, 99);

							int stockVolume = df.getNumberBetween(openStockVol, closeStockVol);

							float openStockVal = Float.parseFloat(openvalint + "." + openvalpoint);
							float highStockVal = Float.parseFloat(highvalint + "." + highvalpoint);
							float lowStockVal = Float.parseFloat(lowvalint + "." + lowvalpoint);
							float closeStockVal = Float.parseFloat(closevalint + "." + closevalpoint);
							if (highStockVal < lowStockVal) {
								float temp = highStockVal;
								highStockVal = lowStockVal;
								lowStockVal = temp;
							}

							String csvRow = companySymbol + "," + start.toString() + ","
									+ start.toString()+" "+tformater.format(dateTime.plusSeconds(i).toDate()) + "," + openStockVal + ","
									+ highStockVal + "," + lowStockVal + "," + closeStockVal + "," + stockVolume + ","
									+ "0" + "," + "1" + "," + openStockVal + "," + highStockVal + "," + lowStockVal
									+ "," + closeStockVal + "," + stockVolume;
							writer.writeNext(csvRow.split(","));
							writer.flush();
						}

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				start = start.plusDays(1);
			}
			writer.close();

		} else {
			System.out.println("Please Enter Saving Directory Path");
		}
		return filePath;

	}

	/**
	 * @param startDate
	 * @param endDate
	 * @param companySymbol
	 * @param openVal
	 * @param closeVal
	 * @param stockVol
	 * @throws IOException
	 */
	public void realtimeMockUpData(String saveDir, String companySymbol, int openVal, int closeVal, int openStockVol,
			int closeStockVol) throws IOException {
		DateFormat tformater = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		if (saveDir != "") {
			createDirectoryIfNeeded(saveDir);
			File file = new File("realtime" + unixTime + ".csv");
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fileWritter = new FileWriter(saveDir + "/" + file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);

			DataFactory df = new DataFactory();
			int openvalint = df.getNumberBetween(openVal, closeVal);
			int openvalpoint = df.getNumberBetween(00, 99);

			int highvalint = df.getNumberBetween(openVal, closeVal);
			int highvalpoint = df.getNumberBetween(00, 99);

			int lowvalint = df.getNumberBetween(openVal, closeVal);
			int lowvalpoint = df.getNumberBetween(00, 99);

			int closevalint = df.getNumberBetween(openVal, closeVal);
			int closevalpoint = df.getNumberBetween(00, 99);

			int stockVolume = df.getNumberBetween(openStockVol, closeStockVol);

			float openStockVal = Float.parseFloat(openvalint + "." + openvalpoint);
			float highStockVal = Float.parseFloat(highvalint + "." + highvalpoint);
			float lowStockVal = Float.parseFloat(lowvalint + "." + lowvalpoint);
			float closeStockVal = Float.parseFloat(closevalint + "." + closevalpoint);
			if (highStockVal < lowStockVal) {
				float temp = highStockVal;
				highStockVal = lowStockVal;
				lowStockVal = temp;
			}

			Date date = new Date();
			DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

			String csvRow = companySymbol + "," + dateformat.format(date) + "," + tformater.format(date) + ","
					+ openStockVal + "," + highStockVal + "," + lowStockVal + "," + closeStockVal + "," + stockVolume
					+ "," + "0" + "," + "1" + "," + openStockVal + "," + highStockVal + "," + lowStockVal + ","
					+ closeStockVal + "," + +stockVolume + "\n";

			System.out.println(csvRow);
			bufferWritter.write(csvRow);
			bufferWritter.close();
		}

	}

	/**
	 * @param startDate
	 * @param endDate
	 * @param companySymbol
	 * @param openVal
	 * @param closeVal
	 * @param stockVol
	 * @throws IOException
	 */
	public List<StockData> realtimeMockUpDataRedShift(String companySymbol, int openVal, int closeVal, int openStockVol,
			int closeStockVol) throws IOException {
		Date date = new Date();
		DateFormat tformater = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		DataFactory df = new DataFactory();
		int openvalint = df.getNumberBetween(openVal, closeVal);
		int openvalpoint = df.getNumberBetween(00, 99);

		int highvalint = df.getNumberBetween(openVal, closeVal);
		int highvalpoint = df.getNumberBetween(00, 99);

		int lowvalint = df.getNumberBetween(openVal, closeVal);
		int lowvalpoint = df.getNumberBetween(00, 99);

		int closevalint = df.getNumberBetween(openVal, closeVal);
		int closevalpoint = df.getNumberBetween(00, 99);

		int stockVolume = df.getNumberBetween(openStockVol, closeStockVol);

		float openStockVal = Float.parseFloat(openvalint + "." + openvalpoint);
		float highStockVal = Float.parseFloat(highvalint + "." + highvalpoint);
		float lowStockVal = Float.parseFloat(lowvalint + "." + lowvalpoint);
		float closeStockVal = Float.parseFloat(closevalint + "." + closevalpoint);
		if (highStockVal < lowStockVal) {
			float temp = highStockVal;
			highStockVal = lowStockVal;
			lowStockVal = temp;
		}

		List<StockData> stockrow = new ArrayList<StockData>();
		try {
			StockData stock = new StockData(companySymbol, dateformat.parse(dateformat.format(date)), date,
					openStockVal, highStockVal, lowStockVal, closeStockVal, stockVolume, 0, 1, openStockVal,
					highStockVal, lowStockVal, closeStockVal, stockVolume);
			stockrow.add(stock);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stockrow;

	}

	/**
	 * @param stationCode
	 * @param stationName
	 * @param lat
	 * @param lng
	 * @param tmaxOpen
	 * @param tmaxClose
	 * @param tminOpen
	 * @param tminClose
	 * @param avgWindSpeedOpen
	 * @param avgWindSpeedclose
	 * @param rainfallOpen
	 * @param rainfallClose
	 */
	public void realtimeWeatherData(String stationCode, String stationName, String lat, String lng, int tmaxOpen,
			int tmaxClose, int tminOpen, int tminClose, int avgWindSpeedOpen, int avgWindSpeedclose, int rainfallOpen,
			int rainfallClose) {

		DataFactory df = new DataFactory();

		int tmax = df.getNumberBetween(tmaxOpen, tmaxClose);
		int tmin = df.getNumberBetween(tminOpen, tminClose);

		int winSpeed = df.getNumberBetween(avgWindSpeedOpen, avgWindSpeedclose);
		int winSpeedpoint = df.getNumberBetween(00, 99);

		int rainfall = df.getNumberBetween(rainfallOpen, rainfallClose);
		int rainfallpoint = df.getNumberBetween(00, 99);

		float avgWindSpeed = Float.parseFloat(winSpeed + "." + winSpeedpoint);
		float avgRainFall = Float.parseFloat(rainfall + "." + rainfallpoint);

	}

	public List<WeatherData> incrementalWeatherData() throws IOException {
		
		//InputStream input = Thread.currentThread().getContextClassLoader()
		//		.getResourceAsStream("Incremental_weather_data.csv");
		
		
		String csvFile = awscredentials.getResourcePath()+"//Datasets//IncrementalWeatherData//Incremental_weather_data.csv";
		
		//InputStreamReader r = new InputStreamReader(input);
		//String csvFile = "Incremental_weather_data.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		Date currentDate = new Date();

		int rowcounter = 0;

		List<WeatherData> row = new ArrayList<WeatherData>();
		List<Integer> randomList = new ArrayList<Integer>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			//br = new BufferedReader(r);

			for (int i = 0; i < 7; i++) {
				Random random = new Random();
				int randomNumber = random.nextInt(365 - 1) + 1;
				randomList.add(randomNumber);
			}

			int count = 0;
			while ((line = br.readLine()) != null) {
				count++;

				if (randomList.contains(count)) {

					// use comma as separator
					String[] weatherData = line.split(cvsSplitBy);
					String stationCode = weatherData[0];
					String stationName = weatherData[1];
					float lat = Float.parseFloat(weatherData[2]);
					float lng = Float.parseFloat(weatherData[3]);
					int tmax = Integer.parseInt(weatherData[5]);
					int tmin = Integer.parseInt(weatherData[6]);
					float wind = Float.parseFloat(weatherData[7]);
					float rain = Float.parseFloat(weatherData[8]);
					int snowfall = Integer.parseInt(weatherData[9]);
					int storm = 0;
					if (wind >= 10 || rain >= 5) {
						storm = 1;
					}

					Calendar c = Calendar.getInstance();
					c.setTime(currentDate);
					c.add(Calendar.DATE, rowcounter);
					rowcounter++;

					WeatherData weather = new WeatherData(stationCode, stationName, lat, lng, c.getTime(), tmax, tmin,
							wind, rain, snowfall, storm);

					// date increment for 7 days
					row.add(weather);

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return row;

	}
	
	


	public List<CompanyProfile> createCompanyProfile() {

		DataFactory df = new DataFactory();

		String companyId = df.getNumberText(5);
		String ComapanyName = df.getBusinessName();
		String companySymbol = df.getRandomChars(4);
		String companyAddress = df.getAddress();
		Date company_foundedon = df.getBirthDate();
		String company_ceo = df.getFirstName() + " " + df.getLastName();
		String company_assets = df.getNumberText(8);
		String company_revenue = df.getNumberText(8);

		CompanyProfile cp = new CompanyProfile(companyId, ComapanyName, companySymbol, companyAddress,
				company_foundedon, company_ceo, company_assets, company_revenue);
		List<CompanyProfile> list = new ArrayList<CompanyProfile>();
		list.add(cp);
		return list;
	}

	public List<CompanyProducts> createCompanyProduct(String companyId, Date startDate, Date endDate) {

		DataFactory df = new DataFactory();
		List<CompanyProducts> list = new ArrayList<CompanyProducts>();
		for (int i = 0; i < 3; i++) {
			String productId = df.getNumberText(4);
			String companyID = companyId;
			String product_name = df.getRandomText(10);
			String product_description = df.getRandomText(100);
			String product_type = df.getRandomChars(10);
			Date product_initaldate = df.getDateBetween(startDate, endDate);
			int marketvol = df.getNumberBetween(10, 40);
			float lat = 37.6197f;
			float lng = -122.365f;
			String product_loc = "SFO";
			CompanyProducts cp = new CompanyProducts(productId, companyID, product_name, product_description,
					product_type, product_initaldate, marketvol, lat, lng, product_loc);
			list.add(cp);
		}
		return list;
	}

	public List<Companyannouncements> createCompanyannouncements(String companyId, Date startDate, Date endDate) {

		DataFactory df = new DataFactory();
		String[] announcementType = { "Product Lunch", "CEO Left", "Revenue Increased", "Aquisitation",
				"Product Lunch" };
		List<Companyannouncements> list = new ArrayList<Companyannouncements>();
		for (int i = 0; i < 5; i++) {
			String announcementId = df.getNumberText(5);
			String companyID = companyId;
			Date announcementDate = df.getDateBetween(startDate, endDate);
			String announcemnType = announcementType[i];
			String announcemnBy = df.getFirstName() + " " + df.getLastName();
			String announcemnFrom = "Press Release";
			String documentDoc = "";
			Companyannouncements ca = new Companyannouncements(announcementId, companyID, announcementDate,
					announcemnType, announcemnBy, announcemnFrom, documentDoc);
			list.add(ca);

		}
		return list;
	}

	public String getCompanySymbol() {
		return companySymbol;
	}

	public void setCompanySymbol(String companySymbol) {
		this.companySymbol = companySymbol;
	}

	public static void main(String args[]) throws IOException {
		MySQLConnection mysql = new MySQLConnection();
		AWSCredentials credentials = new BasicAWSCredentials(awscredentials.getAccessKey(), awscredentials.getSecretKey());
	    AmazonS3 s3client = new AmazonS3Client(credentials);
	    AwsRedshiftOperations redShift = new AwsRedshiftOperations();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		Calendar endcal = Calendar.getInstance();
		endcal.add(Calendar.DATE, -1);

		/* 3 Years Previous date */
		/*
		 * Calendar startcal = Calendar.getInstance();
		 * startcal.add(Calendar.YEAR, -3);
		 */

		Calendar startcal = Calendar.getInstance();
		startcal.add(Calendar.YEAR, -1);

		String startDate = df.format(startcal.getTime());
		String endDate = df.format(endcal.getTime());

		Date startDt = startcal.getTime();
		Date endDt = endcal.getTime();

		DataMockupGenerator mockup = new DataMockupGenerator();
		// Generate Company Profile
		List<CompanyProfile> cplist = mockup.createCompanyProfile();
		String companyID = cplist.get(0).getCompanyId();
		String companySymbol = cplist.get(0).getCompanySymbol();

		// Initialized Quartz
		StreamingMockupData streaming = new StreamingMockupData(companySymbol);

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

		// Mysql Connection
		//Connection conn = mysql.mysqlConnect();
		// Insert Data Into Mysql Table
		// mysql.insertDataCompanyMaster(conn, cplist);
	    // mysql.insertDataCompanyAnnouncement(conn, calist);
		// mysql.insertDataCompanyProduct(conn, cproList);
        // mysql.mysqlDisconnect(conn);
        //Generating Stock Mockup data
		//String filePath=mockup.minMockUpData("/home/abhinandan/TE/Datasets/Project/AWS/Datasets/Mockup/Stock", startDate, endDate,
			//	companySymbol, 35, 42, 35000, 37541, stockChanger);
		
		//String[] filePathSplit=filePath.split("/");
		//String fileName=filePathSplit[filePathSplit.length-1];
		//String s3folder="stockdata/"+fileName;
		//s3.S3Upload(s3client, bucketName, s3folder , filePath);
		
		//Connection redShiftConnect = redShift.redShiftConnect();
		//redShift.loadDatafromS3(redShiftConnect, "stock_data", bucketName+"/stockdata", fileName);
		//redShift.redShiftDisconnect(redShiftConnect);
		
		//s3.S3Upload(s3Client, existingBucketName, keyName, filePath);
		// mockup.realtimeMockUpData("/home/abhinandan/TE/Datasets/Project/AWS/Datasets/Mockup/Stock","TED",
		// 35, 38, 35000, 37541);
		streaming.startStreaming();

	}

}
