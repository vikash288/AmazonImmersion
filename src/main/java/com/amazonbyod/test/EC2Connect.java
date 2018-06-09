package com.amazonbyod.test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class EC2Connect {

	public static void main(String args[]) throws JSchException {

		JSch jsch=new JSch();
		
		String user = "ubuntu";
	    String host = "54.149.51.248";
	    int port = 22;
	    String privateKey = "/home/abhinandan/aws/ArnabKeyPair.pem";
	    
	    jsch.addIdentity(privateKey);
	    
	      System.out.println("identity added ");

	      Session session = jsch.getSession(user, host, port);
	      //session.setPassword("");  //for Windows server
	      System.out.println("session created.");

	      // disabling StrictHostKeyChecking may help to make connection but makes it insecure
	      // see http://stackoverflow.com/questions/30178936/jsch-sftp-security-with-session-setconfigstricthostkeychecking-no
	      // 
	         java.util.Properties config = new java.util.Properties();
	         config.put("StrictHostKeyChecking", "no");
	         session.setConfig(config);

	      session.connect();

	      Channel channel=session.openChannel("shell");

	      // Enable agent-forwarding.
	      //((ChannelShell)channel).setAgentForwarding(true);

	      channel.setInputStream(System.in);
	      /*
	      // a hack for MS-DOS prompt on Windows.
	      channel.setInputStream(new FilterInputStream(System.in){
	          public int read(byte[] b, int off, int len)throws IOException{
	            return in.read(b, off, (len>1024?1024:len));
	          }
	        });
	       */

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
