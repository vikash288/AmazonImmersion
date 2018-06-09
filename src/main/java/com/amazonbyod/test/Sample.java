package com.amazonbyod.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

public class Sample {
	
	static String sample;
	
	public String getSample() {
		return sample;
	}

	
    
	public static void main(String args[]){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		DateFormat df2 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		//System.out.println(df.format(date));
		
		String sdate="2016-08-22 06:00:00";
		String edate = "2016-08-22 14:00:00";
		
		//String to date
		try {
			Date startTime=df.parse(sdate);
			Date enddate=df.parse(edate);
			
			int seconds = (int) ((enddate.getTime()-startTime.getTime())/1000);
			
			 // gets a calendar using the default time zone and locale.
			
			DateTime dateTime = new DateTime(startTime);
			for(int i=5;i<=seconds;i+=5){
				Calendar calendar = null;
				calendar.setTime(startTime);
				calendar.add(Calendar.SECOND, i);
				System.out.println(df1.format(dateTime.plusSeconds(i).toDate())+"---------->"+df2.format(dateTime.plusSeconds(i).toDate()));
				
			}
			
			
			Sample s =new Sample();
			sample="Hello";
			Test t =new Test();
			
			
			
			//System.out.println(seconds);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
