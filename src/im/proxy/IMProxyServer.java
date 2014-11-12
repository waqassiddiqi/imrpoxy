package im.proxy;

import im.proxy.db.DatabaseHeartbeat;
import im.proxy.handler.ProxyRequestHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class IMProxyServer {
	private Logger log = Logger.getLogger(getClass().getName());
	private int mPort = 8081;
	private int mMaxClients = 10;
	private ServerSocket mListenSocket;
	ExecutorService mRequestPool;
	
	public IMProxyServer() {
		readConfig();
	}

	public void readConfig() {
		this.log.info("Initializing IMProxyServer...");

		try {
			
			ResourceBundle myResources = ResourceBundle.getBundle("improxy");
			this.mPort = Integer.parseInt(myResources.getString("im.proxy.server.port"));
			this.mMaxClients = Integer.parseInt(myResources.getString("im.proxy.server.client.max"));
			
			this.mRequestPool = Executors.newFixedThreadPool(this.mMaxClients);

		} catch (Exception ex) {
			log.error("Unable to initialize: " + ex.getMessage(), ex);
			System.exit(-1);
		}

		this.log.info("IMProxyServer initialized");
	}
	
	public void startServer() {
		try {
			this.mListenSocket = new ServerSocket(this.mPort);
			log.info("IMProxyServer started at port: " + this.mPort);
			while (true) {
				Socket clientSocket = this.mListenSocket.accept();
				ProxyRequestHandler req = new ProxyRequestHandler(clientSocket);
				this.mRequestPool.execute(req);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			log.info("Shutting down IMProxyServer...");
		}
	}

	public static void main(String[] args) {
		new IMProxyServer().startServer();		
		new Thread(new DatabaseHeartbeat()).start();
	}
}
