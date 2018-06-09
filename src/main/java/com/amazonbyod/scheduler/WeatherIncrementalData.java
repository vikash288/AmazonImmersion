package com.amazonbyod.scheduler;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.amazonbyod.listclass.WeatherData;
import com.amazonbyod.mockupdata.DataMockupGenerator;
import com.amazonbyod.redshift.AwsRedshiftOperations;

public class WeatherIncrementalData implements Job {
	DataMockupGenerator weather = new DataMockupGenerator();
	AwsRedshiftOperations redshift = new AwsRedshiftOperations();
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Connection conn = null;
		try {
			conn = redshift.redShiftConnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<WeatherData> row = null;
		try {
			row = weather.incrementalWeatherData();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		redshift.insertWeatherData(conn, row);
		try {
			redshift.redShiftConnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
