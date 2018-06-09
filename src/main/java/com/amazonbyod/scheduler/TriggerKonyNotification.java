package com.amazonbyod.scheduler;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.amazonbyod.kony.KonyMobilePushNotification;

public class TriggerKonyNotification {
	
	public void startNotification(){
		// First we must get a reference to a scheduler
        
        try {
        	SchedulerFactory sf = new StdSchedulerFactory();
        	System.out.println("Kony App Start");
			Scheduler sched = sf.getScheduler();
			JobDetail job3 = JobBuilder.newJob(KonyNotification.class)
					.withIdentity("Kony Mobile Notification", "group5")
					.build();

					// get a "nice round" time a few seconds in the future....
					Date startTime = DateBuilder.nextGivenSecondDate(null, 10);

					//This trigger will run every 10 sec for 4 times
					CronTrigger trigger3 = TriggerBuilder.newTrigger()
				            .withIdentity("everydayTrigger", "group5")
				            .withSchedule( CronScheduleBuilder.cronSchedule( "0 0/3 * 1/1 * ? *"))
				            .build();
					sched.scheduleJob(job3, trigger3);
			
			       sched.start();
			
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
