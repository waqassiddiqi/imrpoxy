package im.proxy.provgw;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import RequestResult.RequestResult;

public class Client {
	public static String provGwIP = "127.0.0.1";
	public static int provGwPort = 9091;
	private TcpClient client;
	
	private Logger log = Logger.getLogger(getClass().getName());	
	
	static {
		ResourceBundle myResources = ResourceBundle.getBundle("improxy");
		
		provGwIP = myResources.getString("provgw.ip");
		provGwPort = Integer.parseInt(myResources.getString("provgw.port"));
	}
	
	public Client() {
		client = new TcpClient();
	}
	
	public void sendRequest(String request, String refId) {
		
		RequestResult r = client.sendRequest(provGwIP, provGwPort, 30, 30, request, refId);		
		log.debug("ProvGw response: " + r.responseString);
		
	}
}
