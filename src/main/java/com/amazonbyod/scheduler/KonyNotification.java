package com.amazonbyod.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.amazonbyod.kony.KonyMobilePushNotification;

public class KonyNotification implements Job{
 
	KonyMobilePushNotification konyn = new KonyMobilePushNotification();
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		 System.out.println("Inside Scheduler Kony");
		 //konyn.getStorm();
		 try {
			 konyn.getStorm();
			 //konyn.sendNotification("2016*11-23");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
