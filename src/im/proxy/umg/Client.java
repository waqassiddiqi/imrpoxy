package im.proxy.umg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ResourceBundle;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;


public class Client {
	private static String umgIP = "127.0.0.1";
	private static String umgUsername;
	private static String umgPassword;
	private static String umgFlashSmsCode = "16";
	private static int umgPort = 8081;
	
	private Logger log = Logger.getLogger(getClass().getName());
	
	static {
		ResourceBundle myResources = ResourceBundle.getBundle("improxy");
		try {
			umgPort = Integer.parseInt(myResources.getString("umg.port"));
			umgIP = myResources.getString("umg.ip");
			umgUsername = myResources.getString("umg.username");
			umgPassword = myResources.getString("umg.password");
			umgFlashSmsCode = myResources.getString("umg.flash_sms_code");
			
		} catch (Exception e) { }
	}
	
	public String sendRequest(String refId, String aMsisdn, String bMsisdn, String textMessage) {
		Socket clientSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			
			String umgRequest = constructRequest(refId, aMsisdn, bMsisdn, textMessage); 			
			log.info("Sending UMG request: " + umgRequest);
			
			clientSocket = new Socket(umgIP, umgPort);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			out.println(umgRequest);
			
			StringBuilder sb = new StringBuilder();
		    String line;
		    while ((line = in.readLine()) != null)
		        sb.append(line).append("\n");
		    
		    return sb.toString();
			
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {						
			try {
				if(out != null)
					out.close();
				
				if(in != null)
					in.close();
				
				if(clientSocket != null)
					clientSocket.close();
			} catch (IOException e) {
				log.error("sendRequest(): failed to clear resources", e);
			}			
		}
		
		return null;
	}
	
	private String constructRequest(String refId, String aMsisdn, String bMsisdn, String textMessage) {
		String rawRequest = "%s,%s,,%s,%s,%s,%s,%s,%s";
		
		byte[] encodedBytes = Base64.encodeBase64(textMessage.getBytes());
		
		return String.format(rawRequest, refId, refId, umgUsername, umgPassword, 
				new String(encodedBytes), aMsisdn, bMsisdn, umgFlashSmsCode);
	}
}
