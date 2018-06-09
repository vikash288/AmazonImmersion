package com.amazonbyod.kony;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.amazonbyod.awsprop.AWSProjectProperties;
import com.amazonbyod.redshift.AwsRedshiftOperations;

public class KonyMobilePushNotification {
	
	//KonyMobilePushNotification kony = new KonyMobilePushNotification();
	static AwsRedshiftOperations redshift = new AwsRedshiftOperations();
	
	public static void getStorm(){
		System.out.println("Inside GetStorm");
		
		try {
			Connection conn = redshift.redShiftConnect();
			Statement stmt = conn.createStatement();
			
			Date date = new Date();
			DateFormat dformater = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			String sql = "select pre,wdate from weather_prediction;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString("wdate"));
				if (rs.getInt(1) == 1) {
					
					System.out.println("Storm on "+rs.getString("wdate"));
					System.out.println(sendNotification(rs.getString("wdate")));
					//System.out.println(rs.getString("wdate"));
				}
			}
			rs.close();
			conn.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String sendNotification(String wdate) throws IOException{
		AWSProjectProperties prop =new AWSProjectProperties();
		//String url = "https://vikashsharma.messaging.konycloud.com:443/message";
		String url = prop.getKony_url();
		String kony_appid=prop.getKony_appid();
		String responseData = "";
		try {
			StringEntity input = new StringEntity("{ \"messageRequest\" : { \"appId\" :\""+kony_appid+"\",\"global\" : { },\"messages\" : {\"message\" : {\"content\" : {\"priorityService\" : \"true\",\"data\" : \"Weather Alert - Storm going to happen on "+wdate+" \",\"mimeType\" : \"text/plain\"},\"overrideMessageId\" : 0,\"startTimestamp\" : \"0\",\"expiryTimestamp\" : \"0\",\"subscribers\" : {\"subscriber\" :  {\"allActive\" : true } },\"platformSpecificProps\" : {\"title\" : \"AWS-BYOD\", \"android\" : {\"title\" : \"AWS-BYOD\" , \"priority\" : \"HIGH\" },  \"wns\" : {\"notificationType\" : \"TOAST\",\"text1\" : \"AWS-BYOD\",\"text2\" : \"Weather Alert - Storm going to happen on factory area !.\",\"params\" : { },\"image\" : { },\"text\" : { }}},\"type\" : \"PUSH\"} } } }");
            System.out.println("{ \"messageRequest\" : { \"appId\" :"+kony_appid+",\"global\" : { },\"messages\" : {\"message\" : {\"content\" : {\"priorityService\" : \"true\",\"data\" : \"Weather Alert - Storm going to happen on "+wdate+" \",\"mimeType\" : \"text/plain\"},\"overrideMessageId\" : 0,\"startTimestamp\" : \"0\",\"expiryTimestamp\" : \"0\",\"subscribers\" : {\"subscriber\" :  {\"allActive\" : true } },\"platformSpecificProps\" : {\"title\" : \"AWS-BYOD\", \"android\" : {\"title\" : \"AWS-BYOD\" , \"priority\" : \"HIGH\" },  \"wns\" : {\"notificationType\" : \"TOAST\",\"text1\" : \"AWS-BYOD\",\"text2\" : \"Weather Alert - Storm going to happen on factory area !.\",\"params\" : { },\"image\" : { },\"text\" : { }}},\"type\" : \"PUSH\"} } } }");
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			post.setHeader("content-type", "application/json");
			post.setEntity(input);

			HttpResponse response = client.execute(post);
			responseData = EntityUtils.toString(response.getEntity());
			System.out.println("Kony Response data "+responseData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseData;
		
	}
	
	
	
   public static void main(String args[]) throws IOException{
	   KonyMobilePushNotification konyn = new KonyMobilePushNotification();
	   konyn.getStorm();
	   
   }



}
