package com.amazonbyod.scheduler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class StreamingMockupData {
	
	String companySymbol;

	public StreamingMockupData(String companySymbol){
		this.companySymbol=companySymbol;
	}
	
	public String getCompanySymbol() {
		return companySymbol;
	}
	
	private final static Logger logger = Logger.getLogger(StreamingMockupData.class);
	
	public void startStreaming() {
		try {
			
			// First we must get a reference to a scheduler
	        SchedulerFactory sf = new StdSchedulerFactory();
	        Scheduler sched = sf.getScheduler();

	        /**
	         * Job 1 using Trigger
	         */
			JobDetail job1 = JobBuilder.newJob(StockIncrementalData.class)
					.withIdentity("Streaming-Stock-Data", "group1")
					.build();

			//This trigger will run every five second in infinite loop
			Trigger trigger1 = TriggerBuilder.newTrigger()
					.withIdentity("every5SecondTrigger", "group1")
					.startAt(new Date(System.currentTimeMillis()))
					.withSchedule( CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
					.build();
			
			job1.getJobDataMap().put("CompanySymbol", getCompanySymbol());
			
			sched.scheduleJob(job1, trigger1);
			

			/**
			 * Job 2 using SimpleTrigger
			 */
			JobDetail job2 = JobBuilder.newJob(WeatherIncrementalData.class)
			.withIdentity("Streaming-Weather-Data", "group1")
			.build();

			// get a "nice round" time a few seconds in the future....
			Date startTime = DateBuilder.nextGivenSecondDate(null, 10);

			//This trigger will run every 10 sec for 4 times
			CronTrigger trigger2 = TriggerBuilder.newTrigger()
		            .withIdentity("everydayTrigger", "group2")
		            .withSchedule( CronScheduleBuilder.cronSchedule("0/20 * * * * ?"))
		            .build();
			sched.scheduleJob(job2, trigger2);
	
	       sched.start();


		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	


}
