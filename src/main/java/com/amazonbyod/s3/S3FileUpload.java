package com.amazonbyod.s3;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.StringUtils;
import com.amazonbyod.awsprop.AWSProjectProperties;

public class S3FileUpload {
	
	 static AWSProjectProperties awscredentials = new AWSProjectProperties();
	 static S3Operations s3 = new S3Operations();
	 static final String bucketName = "amazon-aws-immersion-project";
	public static void main(String args[]) throws IOException{
		
	    AWSCredentials credentials = new BasicAWSCredentials(awscredentials.getAccessKey(), awscredentials.getSecretKey());
	    AmazonS3 s3client = new AmazonS3Client(credentials);
	    s3client.setRegion(Region.getRegion(Regions.US_WEST_2));
	    //Step -1 Create Bucket
	    s3.createBucket(s3client, bucketName);
	    //Step -2 List of Buckets
	    List<Bucket> buckets = s3.listofBuckets(s3client);
	    for (Bucket bucket : buckets) {
	            System.out.println(bucket.getName() + "\t ----------------->" +
	                    StringUtils.fromDate(bucket.getCreationDate())+ "\t ----------------->" +bucket.getOwner());
	    }
	    //Step- 3 Create Folder inside Bucket
	    s3.createFolder(bucketName, "stockdata", s3client);
	    s3.createFolder(bucketName, "weatherdata", s3client);
	    /*s3client.putObject(new PutObjectRequest(bucketName, "stockdata/WIKI-AAPL1.csv", 
	    		new File("/home/abhinandan/TE/Datasets/Project/AWS/Datasets/Mockup/Stock/WIKI-AAPL.csv")));*/
	    s3.S3Upload(s3client, bucketName, "stockdata/WIKI-AAPL1.csv", "/home/abhinandan/TE/Datasets/Project/AWS/Datasets/Mockup/Stock/data.csv");
	    
	   // ObjectMetadata metaData = new ObjectMetadata();
	   // ByteArrayInputStream input = new ByteArrayInputStream("Hello World!112".getBytes());
	    
	   // s3client.putObject(bucketName, "hello.txt", input, metaData);
	    
	    
	    
	    //Step -4 List of objects
	    s3.listofObjects(s3client, bucketName);
	    
	    //Step -5 Read From S3
	    //s3.readFromS3(s3client, bucketName, "hello.txt");
	    
	   /* String source = "This is the source of my input stream";
	    InputStream in = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
	    
	    //Step -6 Write in S3
	    s3.writeinS3(s3client, bucketName, "hello.txt", in);
	    
	  //Step -7 Read From S3
	    s3.readFromS3(s3client, bucketName, "hello.txt");*/
	    

	}

}
