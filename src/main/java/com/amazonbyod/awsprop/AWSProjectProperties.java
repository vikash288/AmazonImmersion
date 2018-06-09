package com.amazonbyod.awsprop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author abhinandan
 *
 */

public class AWSProjectProperties {

	
	private String accessKey = "accessKey";
	private String secretKey = "secretKey";
	private String bucketName = "bucketName";
	private String mysql_dbname = "mysql_dbname";
	private String mysql_username = "mysql_username";
	private String mysql_password = "mysql_password";
	private String mysql_JDBC_DRIVER = "mysql_JDBC_DRIVER";
	private String mysql_DB_URL = "mysql_DB_URL";
	private String redshift_jdbc_url = "redshift_jdbc_url";
	private String master_username = "master_username";
	private String master_password = "master_password";
	private String stockDatapath = "stockDatapath";
	private String cloudbeam_slave_url = "cloudbeam_slave_url";
	private String cloudbeam_taskname = "cloudbeam_taskname";
	private String kony_url = "kony_url";
	private String weatherDatapath = "weatherDatapath";
	private String prediction_path = "prediction_path";
	private String resourcePath = "resourcePath";
	private String redshift_dbname = "redshift_dbname";
	private String redshifturl = "redshifturl";
	private String kony_appid = "kony_appid";
	private String redshiftport = "redshiftport";


	

	

	static Properties prop = new Properties();
	static InputStream input = Thread.currentThread().getContextClassLoader()
			.getResourceAsStream("ProjectConf.properties");
	 
	//static FileOutputStream output = new FileOutputStream("ProjectConf.properties");
	
	
	
	
	

	/**
	 * @return the accessKey
	 * @throws IOException
	 */
	public String getAccessKey() throws IOException {
		prop.load(input);
		return prop.getProperty(accessKey);
	}

	/**
	 * @return the secretKey
	 * @throws IOException
	 */
	public String getSecretKey() throws IOException {
		prop.load(input);
		return prop.getProperty(secretKey);
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public String getMysql_dbname() throws IOException {
		prop.load(input);
		return prop.getProperty(mysql_dbname);
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public String getMysql_username() throws IOException {
		prop.load(input);
		return prop.getProperty(mysql_username);
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public String getMysql_password() throws IOException {
		prop.load(input);
		return prop.getProperty(mysql_password);
	}
	
	/**
	 * @return
	 * @throws IOException 
	 */
	public String getMysql_JDBC_DRIVER() throws IOException {
		prop.load(input);
		return prop.getProperty(mysql_JDBC_DRIVER);
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public String getMysql_DB_URL() throws IOException {
		prop.load(input);
		return prop.getProperty(mysql_DB_URL);
	}
	

	/**
	 * @return
	 * @throws IOException
	 */
	public String getRedshift_jdbc_url() throws IOException {
		prop.load(input);
		return prop.getProperty(redshift_jdbc_url);
	}

	
	/**
	 * @return
	 * @throws IOException
	 */
	public String getMaster_username() throws IOException {
		prop.load(input);
		return prop.getProperty(master_username);
	}
	
	/**
	 * @return
	 * @throws IOException
	 */	
	public String getMaster_password() throws IOException {
		prop.load(input);
		return prop.getProperty(master_password);
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	public String getStockDatapath() throws IOException {
		prop.load(input);
		return prop.getProperty(stockDatapath);
	}
    
	/**
	 * @return
	 * @throws IOException
	 */
	public String getCloudbeam_slave_url() throws IOException {
		prop.load(input);
		return prop.getProperty(cloudbeam_slave_url);
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	public String getBucketName() throws IOException {
		prop.load(input);
		return prop.getProperty(bucketName);
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	public String getCloudbeam_taskname() throws IOException {
		prop.load(input);
		return prop.getProperty(cloudbeam_taskname);
	}
    
	public String getKony_url() throws IOException {
		prop.load(input);
		return prop.getProperty(kony_url);
	}
	
	public String getWeatherDatapath() throws IOException {
		prop.load(input);
		return prop.getProperty(weatherDatapath);
	}
	
	public String getPrediction_path() throws IOException {
		prop.load(input);
		return prop.getProperty(prediction_path);
		
	}
	
	public String getResourcePath() throws IOException {
		prop.load(input);
		return prop.getProperty(resourcePath);
	}
    
	public String getRedshift_dbname() throws IOException {
		prop.load(input);
		return prop.getProperty(redshift_dbname);
	}

	public String getRedshifturl() throws IOException {
		prop.load(input);
		return prop.getProperty(redshifturl);
	}

	public String getKony_appid() throws IOException {
		prop.load(input);
		return prop.getProperty(kony_appid);
	}

	public String getRedshiftport() throws IOException {
		prop.load(input);
		return prop.getProperty(redshiftport);
	}

  
	/*
	 * Setter Part Starts Here
	 * 
	 */
	
	OutputStream out = null;
	
	public void setRedshiftport(String redshiftport) {
		this.redshiftport = redshiftport;
	}

	
	public void setRedshift_dbname(String redshift_dbname) {
		this.redshift_dbname = redshift_dbname;
	}

	public void setRedshifturl(String redshifturl) {
		this.redshifturl = redshifturl;
	}

	public void setKony_appid(String kony_appid) {
		this.kony_appid = kony_appid;
	}
	
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	public void setPrediction_path(String prediction_path) throws IOException {
		File f = new File("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("prediction_path",prediction_path);
		out = new FileOutputStream( f );
		prop.store(out, "This is an optional header comment string");
		System.out.print(getPrediction_path());
		
	}
	
	public void setWeatherDatapath(String weatherDatapath) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("weatherDatapath",weatherDatapath);
		prop.store(outfile, null);
		outfile.close();
		System.out.print(getWeatherDatapath());
	}
	
	public void setKony_url(String kony_url) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("kony_url",kony_url);
		prop.store(outfile, null);
		outfile.close();
	}
	
	public void setAccessKey(String accessKey) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("accessKey", accessKey);
		prop.store(outfile, null);
		outfile.close();
	}
	public void setSecretKey(String secretKey) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("secretKey", secretKey);
		prop.store(outfile, null);
		outfile.close();
	}
	public void setBucketName(String bucketName) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("bucketName", bucketName);
		prop.store(outfile, null);
		outfile.close();
	}
	public void setMysql_dbname(String mysql_dbname) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("mysql_dbname", mysql_dbname);
		prop.store(outfile, null);
		outfile.close();
	}
	public void setMysql_username(String mysql_username) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("mysql_username", mysql_username);
		prop.store(outfile, null);
		outfile.close();
	}
	public void setMysql_password(String mysql_password) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("mysql_password", mysql_password);
		prop.store(outfile, null);
		outfile.close();
	}
	public void setMysql_JDBC_DRIVER(String mysql_JDBC_DRIVER) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("mysql_JDBC_DRIVER", mysql_JDBC_DRIVER);
		prop.store(outfile, null);
		outfile.close();
	}
	public void setMysql_DB_URL(String mysql_DB_URL) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("mysql_DB_URL", mysql_DB_URL);
		prop.store(outfile, null);
		outfile.close();
	}
	public void setRedshift_jdbc_url(String redshift_jdbc_url) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("redshift_jdbc_url", redshift_jdbc_url);
		prop.store(outfile, null);
		outfile.close();
	}
	public void setMaster_username(String master_username) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("master_username", master_username);
		prop.store(outfile, null);
		outfile.close();
	}
	public void setMaster_password(String master_password) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("master_password", master_password);
		prop.store(outfile, null);
		outfile.close();
	}
	public void setStockDatapath(String stockDatapath) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("stockDatapath", stockDatapath);
		prop.store(outfile, null);
		outfile.close();
	}
	public void setCloudbeam_slave_url(String cloudbeam_slave_url) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("cloudbeam_slave_url", cloudbeam_slave_url);
		prop.store(outfile, null);
		outfile.close();
	}
	
	public void setCloudbeam_taskname(String cloudbeam_taskname) throws IOException {
		FileOutputStream outfile = new FileOutputStream("ProjectConf.properties");
		prop.load(input);
		prop.setProperty("cloudbeam_taskname", cloudbeam_taskname);
		prop.store(outfile, null);
		outfile.close();
	}

	
	  public static void main(String args[]) throws IOException{
	  
		  AWSProjectProperties aws = new AWSProjectProperties();
	System.out.println(aws.getStockDatapath());
	 
	  }
	 
}
