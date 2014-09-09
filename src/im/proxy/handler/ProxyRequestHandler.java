package im.proxy.handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

public class ProxyRequestHandler implements Runnable {
	private Logger log = Logger.getLogger(getClass().getName());
	private Socket mClientSocket;
	private boolean mClientConnected = false;
	
	public ProxyRequestHandler(Socket clientSocket) {
        this.mClientSocket = clientSocket;
        this.mClientConnected = true;
    }
	
	public void run() {
        log.info("New incoming request");
        
		try {
			while (this.mClientConnected) {
				if (this.mClientSocket.isOutputShutdown()) {
					this.mClientConnected = false;
				} else {
					BufferedReader inFromClient = new BufferedReader(new InputStreamReader(this.mClientSocket.getInputStream()));
					DataOutputStream outToClient = new DataOutputStream(this.mClientSocket.getOutputStream());

					String xml = inFromClient.readLine();

					this.log.info("<< incoming request: " + xml);
					
					String response = processRequest(xml);
					
					outToClient.writeBytes(response);
					
					this.log.info(">> outgoing response: " + response);
						
					this.mClientConnected = false;
					this.mClientSocket.close();
				}
			}
		} catch (Exception e) {
			this.log.error(e.getMessage(), e);
		} finally {
			try {
				if(this.mClientSocket != null && this.mClientSocket.isClosed() == false)
					this.mClientSocket.close();
			} catch (IOException e) {
				log.error("Failed to free socket resource", e);
			}
		}
	}
	
	private String processRequest(String xml) throws XPathExpressionException {
		XPathFactory xpathFactory = XPathFactory.newInstance();
	    XPath xpath = xpathFactory.newXPath();
	    String aMsisdn = "";
	    String bMsisdn = "";
	    
		InputSource source = new InputSource(new StringReader(xml));
		
		String function = xpath.evaluate("/methodCall/function", source);
		
		CommandHandler cmdHandler = null;
		
		Map<String, String> commandParams = new HashMap<String, String>();
		
		source = new InputSource(new StringReader(xml));
		aMsisdn = xpath.evaluate("/methodCall/aMsisdn", source);
		commandParams.put("msisdn", aMsisdn);
		
		source = new InputSource(new StringReader(xml));
		bMsisdn = xpath.evaluate("/methodCall/bMsisdn", source);
		commandParams.put("channel", bMsisdn);
		
		cmdHandler = new SendIntroMessageCommandHandler(commandParams);
		
		String commandResponse = "";
		if(cmdHandler != null) {
			try {
				commandResponse = cmdHandler.execute();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				
				commandResponse = "Error";
			}
		}
		
		return commandResponse;
	}
}
