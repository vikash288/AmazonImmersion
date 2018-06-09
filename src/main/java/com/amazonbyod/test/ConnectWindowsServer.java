package com.amazonbyod.test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ConnectWindowsServer {
	
	public static void main(String args[]) throws JSchException {

		JSch jsch=new JSch();
		
		String user = "Administrator";
	    String host = "54.244.39.78";
	    int port = 22;
	    String privateKey = "/home/abhinandan/aws/ArnabKeyPair.pem";
	    
	    //jsch.addIdentity(privateKey);
	    jsch.addIdentity(privateKey);
	    
	    
	      System.out.println("identity added ");

	      Session session = jsch.getSession(user, host, port);
	      session.setPassword("Thirdeye@2016");  //for Windows server
	      System.out.println("session created.");

	      // disabling StrictHostKeyChecking may help to make connection but makes it insecure
	      // see http://stackoverflow.com/questions/30178936/jsch-sftp-security-with-session-setconfigstricthostkeychecking-no
	      // 
	         java.util.Properties config = new java.util.Properties();
	         config.put("StrictHostKeyChecking", "no");
	         session.setConfig(config);

	      session.connect();

	      Channel channel=session.openChannel("exec");
	      ((ChannelExec)channel).setCommand("ipconfig");

	      // X Forwarding
	      // channel.setXForwarding(true);

	      //channel.setInputStream(System.in);
	      channel.setInputStream(null);

	      //channel.setOutputStream(System.out);

	      //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
	      //((ChannelExec)channel).setErrStream(fos);
	      ((ChannelExec)channel).setErrStream(System.err);

	     

	      

	      channel.setOutputStream(System.out);

	      /*
	      // Choose the pty-type "vt102".
	      ((ChannelShell)channel).setPtyType("vt102");
	      */

	      /*
	      // Set environment variable "LANG" as "ja_JP.eucJP".
	      ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
	      */

	      //channel.connect();
	      channel.connect(3*1000);
	    }

}
