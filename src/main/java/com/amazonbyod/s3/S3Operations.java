package com.amazonbyod.s3;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpRequestBase;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

/**
 * @author abhinandan
 *
 */
public class S3Operations {
	
	/**
	 * @param s3client
	 * @param bucketName
	 */
	public void createBucket(AmazonS3 s3client, String bucketName) {
		s3client.setRegion(Region.getRegion(Regions.US_WEST_2));
		if (!s3client.doesBucketExist(bucketName)) {
			s3client.createBucket(bucketName);
		}
	}
	
	/**
	 * @param s3client
	 * @return
	 */
	public List<Bucket> listofBuckets(AmazonS3 s3client){
		List<Bucket> buckets = s3client.listBuckets();
		return buckets;
	}
	
	
    /**
     * @param bucketName
     * @param folderName
     * @param client
     */
    public void createFolder(String bucketName, String folderName, AmazonS3 s3client) {
    	// create meta-data for your folder and set content-length to 0
    	ObjectMetadata metadata = new ObjectMetadata();
    	metadata.setContentLength(0);
    	// create empty content
    	InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
    	// create a PutObjectRequest passing the folder name suffixed by /
    	PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
    				folderName + "/", emptyContent, metadata);
    	// send request to S3 to create folder
    	s3client.putObject(putObjectRequest);
    }
    
    
    /**
     * @param s3client
     * @param bucketName
     */
    public void deleteBucket(AmazonS3 s3client,String bucketName){
        s3client.deleteBucket(bucketName);
    }
    
    
    /**
     * @param s3client
     * @param bucketName
     */
	public void listofObjects(AmazonS3 s3client, String bucketName) {
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix("");
		ObjectListing objectListing;

		do {
			objectListing = s3client.listObjects(listObjectsRequest);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
			}
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		} while (objectListing.isTruncated());
	}
	
	
	
	
	
	/**
	 * @param existingBucketName
	 * @param keyName
	 * @param filePath
	 * @return
	 */
	public String S3Upload(AmazonS3 s3Client,String existingBucketName, String keyName, String filePath) {

		String eTag = "";

		// Create a list of UploadPartResponse objects. You get one of these
		// for each part upload.
		List<PartETag> partETags = new ArrayList<PartETag>();

		// Step 1: Initialize.
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(existingBucketName, keyName);
		InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);

		File file = new File(filePath);
		long contentLength = file.length();
		long partSize = 5242880; // Set part size to 5 MB.

		try {
			// Step 2: Upload parts.
			long filePosition = 0;
			for (int i = 1; filePosition < contentLength; i++) {
				// Last part can be less than 5 MB. Adjust part size.
				partSize = Math.min(partSize, (contentLength - filePosition));

				// Create request to upload a part.
				UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(existingBucketName)
						.withKey(keyName).withUploadId(initResponse.getUploadId()).withPartNumber(i)
						.withFileOffset(filePosition).withFile(file).withPartSize(partSize);

				// Upload part and add response to our list.
				partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());

				filePosition += partSize;
			}

			// Step 3: Complete.
			CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(existingBucketName, keyName,
					initResponse.getUploadId(), partETags);

			CompleteMultipartUploadResult result = s3Client.completeMultipartUpload(compRequest);
			eTag = result.getETag();
			//logger.info("File upload successfully in Amazon S3 with a bucket name:" + existingBucketName
			//		+ " with a file name:" + keyName + " Etag value:" + eTag);
		} catch (Exception e) {
			//logger.error("Runtime exception occured:" + e.toString());
			s3Client.abortMultipartUpload(
					new AbortMultipartUploadRequest(existingBucketName, keyName, initResponse.getUploadId()));
		}

		return eTag;
	}
	
	
	
	public void readFromS3(AmazonS3 s3Client,String bucketName, String key) throws IOException {
	    S3Object s3object = s3Client.getObject(new GetObjectRequest(
	            bucketName, key));
	    System.out.println(s3object.getObjectMetadata().getContentType());
	    System.out.println(s3object.getObjectMetadata().getContentLength());

	    BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
	    String line;
	    while((line = reader.readLine()) != null) {
	      // can copy the content locally as well
	      // using a buffered writer
	      System.out.println(line);
	    }
	  }
	
	
	
	public void writeinS3(AmazonS3 s3Client,String bucketName, String key ,InputStream objectContent) throws IOException {
	    S3Object s3object = s3Client.getObject(new GetObjectRequest(
	            bucketName, key));
	    String source = "This is the source of my input stream";
	    s3object.setObjectContent(new S3ObjectInputStream(new ByteArrayInputStream(source.getBytes()), null));
	  }

}
