package com.amazonbyod.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonbyod.awsprop.AWSProjectProperties;
import com.amazonbyod.scheduler.TriggerKonyNotification;

public class Test {
	
	
/*	public static void main(String args[]) throws IOException {
		 AWSProjectProperties awscredentials = new AWSProjectProperties();
		AWSCredentials credentials = new BasicAWSCredentials(awscredentials.getAccessKey(), awscredentials.getSecretKey());
		// Each instance of TransferManager maintains its own thread pool
		// where transfers are processed, so share an instance when possible
		TransferManager tx = new TransferManager(credentials);
		 
		// The upload and download methods return immediately, while
		// TransferManager processes the transfer in the background thread pool
		File file= new File("Incremental_weather_data.csv");
		Upload upload = tx.upload("", "sample.csv", file);
		
		 
		// While the transfer is processing, you can work with the transfer object
		while (upload.isDone() == false) {
		    System.out.println(upload.getProgress().getPercentTransferred() + "%");
		}	
	
	}*/
	
	
	public static void testPython() throws IOException{
		 
			
		Runtime runtime = Runtime.getRuntime();
	    Process processs = runtime.exec("python C://Users//Administrator//Scheduler//Scheduler.py");
		OutputStream output = processs.getOutputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(processs.getInputStream()));		
		String processString = "";		
		while((processString = br.readLine()) != null) {			
			
			processString =processString+"\t"+ br.readLine();		
			System.out.println(processString);
		}
	}
	
	
	public static void main(String args[]) throws IOException{
		
		TriggerKonyNotification t = new TriggerKonyNotification();
		t.startNotification();
		//File file= new File("Incremental_weather_data.csv");
		/*String csvFile = "Incremental_weather_data.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);

                System.out.println("Country [code= " + country[0] + " , name=" + country[1] + "]");

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
	}
}
