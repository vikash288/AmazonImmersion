package com.amazonbyod.scheduler;

import java.sql.Connection;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.amazonbyod.listclass.StockData;
import com.amazonbyod.mockupdata.DataMockupGenerator;
import com.amazonbyod.redshift.AwsRedshiftOperations;

public class StockIncrementalData implements Job{
	DataMockupGenerator stockMockup = new DataMockupGenerator();
	AwsRedshiftOperations redshift = new AwsRedshiftOperations();
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap data = context.getJobDetail().getJobDataMap();
			String stockSymbol = data.getString("CompanySymbol"); 
			Connection conn = redshift.redShiftConnect();
			List<StockData> row=stockMockup.realtimeMockUpDataRedShift(stockSymbol,35, 55, 35000, 37541);
			redshift.insertStockData(conn, row);
			redshift.redShiftDisconnect(conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
