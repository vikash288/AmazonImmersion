package com.amazonbyod.redshift;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.amazonbyod.awsprop.AWSProjectProperties;
import com.amazonbyod.listclass.StockData;
import com.amazonbyod.listclass.WeatherData;

public class AwsRedshiftOperations {

	static AWSProjectProperties awscredentials = new AWSProjectProperties();
	
	

	public Connection redShiftConnect() throws IOException {
		
		 String dbURL = "jdbc:redshift://"+awscredentials.getRedshifturl()+":"+awscredentials.getRedshiftport()+"/"+awscredentials.getRedshift_dbname();
		 String MasterUsername = awscredentials.getMaster_username();
		 String MasterUserPassword = awscredentials.getMaster_password();
		 
		Connection connect = null;
		try {
			Class.forName("com.amazon.redshift.jdbc4.Driver");
			Properties props = new Properties();
			props.setProperty("user", MasterUsername);
			props.setProperty("password", MasterUserPassword);
			connect = DriverManager.getConnection(dbURL, props);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;

	}

	public int redShiftDisconnect(Connection connect) {
		int flag = 0;
		if (connect != null) {
			try {
				connect.close();
				flag = 1;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			flag = 0;
		}
		return flag;

	}
	
	public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
	    return new java.sql.Date(date.getTime());
	}
	
	public java.sql.Timestamp convertJavaDateToSqlTimeStamp(java.util.Date date) {
		Date date1 = new Date();
	    return new java.sql.Timestamp(date1.getTime());
	}


	public void insertStockData(Connection conn, List<StockData> row) {
		String insertTableSQL = "INSERT INTO stock_data (stock_symbol,stock_date,stock_time ,stock_open ,stock_high,stock_low ,stock_close ,stock_vol ,stock_div ,stock_split ,stock_adjopen ,stock_adjhigh ,stock_adjlow ,stock_adjclose ,stock_adjvol )"
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, row.get(0).getCompanySymbol());
			preparedStatement.setDate(2, convertJavaDateToSqlDate(row.get(0).getStockDate()));
			preparedStatement.setTimestamp(3, convertJavaDateToSqlTimeStamp(row.get(0).getStockTime()));
			preparedStatement.setFloat(4, row.get(0).getStockopen());
			preparedStatement.setFloat(5, row.get(0).getStockhigh());
			preparedStatement.setFloat(6, row.get(0).getStockadjlow());
			preparedStatement.setFloat(7, row.get(0).getStockclose());
			preparedStatement.setInt(8, row.get(0).getStockvol());
			preparedStatement.setFloat(9, row.get(0).getStockdiv());
			preparedStatement.setInt(10, row.get(0).getStocksplit());
			preparedStatement.setFloat(11, row.get(0).getStockadjopen());
			preparedStatement.setFloat(12, row.get(0).getStockadjhigh());
			preparedStatement.setFloat(13, row.get(0).getStockadjlow());
			preparedStatement.setFloat(14, row.get(0).getStockadjclose());
			preparedStatement.setInt(15, row.get(0).getStockadjvol());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertWeatherData(Connection conn, List<WeatherData> row) {
		
		String insertTableSQL = "INSERT INTO weather_data_incremental (station_code,station_name,lat ,lng ,wdate,tmax ,tmin ,windspeed ,rainfall ,snowfall ,storm,flag)"
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		//Truncate Table
		truncateTable(conn,"weather_data_incremental");
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
			int flag=0;
			System.out.println(row.size());
			for(int i=0;i<row.size();i++){
			preparedStatement.setString(1, row.get(i).getStationCode());
			preparedStatement.setString(2, row.get(i).getStationName());
			preparedStatement.setFloat(3, row.get(i).getLat());
			preparedStatement.setFloat(4, row.get(i).getLng());
			preparedStatement.setDate(5, convertJavaDateToSqlDate(row.get(i).getDate()));
			preparedStatement.setInt(6, row.get(i).getTmax());
			preparedStatement.setInt(7, row.get(i).getTmin());
			preparedStatement.setFloat(8, row.get(i).getWind());
			preparedStatement.setFloat(9, row.get(i).getRain());
			preparedStatement.setInt(10, row.get(i).getSnowfall());
			//preparedStatement.setInt(11, row.get(i).getStorm());
			preparedStatement.setInt(11, 0);
			preparedStatement.setInt(12, flag);
			flag=1;
			preparedStatement.executeUpdate();
			}
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void truncateTable(Connection conn,String tableName){
		String SQLQuery="truncate "+tableName;
		
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(SQLQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startCreateTable(Connection conn) {
		
		String stockData="create table if not exists stock_data(stock_symbol varchar(255),stock_date date,stock_time TIMESTAMP,stock_open float,stock_high float,stock_low float,stock_close float,stock_vol int,stock_div float,stock_split int,stock_adjopen float,stock_adjhigh float,stock_adjlow float,stock_adjclose float,stock_adjvol int)";
		String weather_storm_data="create table if not exists weather_storm_data (station_code varchar(255),station_name varchar(255),lat float,lng float,wdate date,tmax int,tmin int,windspeed float,rainfall float,snowfall int,storm int)";
		String weather_data_incremental="create table if not exists weather_data_incremental (station_code varchar(255),station_name varchar(255),lat float,lng float,wdate date,tmax int,tmin int,windspeed float,rainfall float,snowfall int,storm int,flag int)";
		String weather_prediction="create table if not exists weather_prediction (station_code varchar(255),station_name varchar(255),lat float,lng float,wdate date,tmax int,tmin int,windspeed float,rainfall float,snowfall int,storm int,flag int,pre int)";
		try {
			/*Statement stmt = conn.createStatement();
			stmt.executeUpdate(stockData);
			stmt.executeUpdate(weather_storm_data);
			stmt.executeUpdate(weather_data_incremental);
			stmt.executeUpdate(weather_prediction);*/
			
				Statement stmt = conn.createStatement();
			   stmt.executeUpdate(stockData);
			   Statement stmt1 = conn.createStatement();
			   stmt1.executeUpdate(weather_storm_data);
			   Statement stmt2 = conn.createStatement();
			   stmt2.executeUpdate(weather_data_incremental);
			   Statement stmt3 = conn.createStatement();
			   stmt3.executeUpdate(weather_prediction);
			   
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public void loadDatafromS3(Connection conn,String tablename,String bucketStructure,String key) throws IOException{
		truncateTable(conn,tablename);
		String loadSQL = "copy "+tablename+" from 's3://"+bucketStructure+"/"+key+"' "
				+ "credentials 'aws_access_key_id="+awscredentials.getAccessKey()+";aws_secret_access_key="+awscredentials.getSecretKey()+"'"
				+ "delimiter ',' region 'us-west-2'";
		System.out.println(loadSQL);
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(loadSQL);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
