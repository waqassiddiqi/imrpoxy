package im.proxy.db;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class DatabaseHeartbeat implements Runnable {

	private Logger log = Logger.getLogger(getClass().getName());
	private long heartbeatInterval = 2;
	
	public DatabaseHeartbeat() {
		ResourceBundle myResources = ResourceBundle.getBundle("improxy");
	
		try {
			
			heartbeatInterval = Long.parseLong(myResources.getString("db.heartbeat_interval"));
			
		} catch (Exception e) {
			log.warn("Resource key not found", e);
		}
		
	}
	
	@Override
	public void run() {
		log.info("DatabaseHeartbeat running ...");
		
		for (;;) {
			log.info("Query database for heartbeat");
			
			Statement stmt = null;
			try {
				
				stmt = DatabaseConnection.getInstance().getConnection().createStatement();
				stmt.execute("SELECT code, msg FROM sms_notification WHERE language=1");
				
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				if(stmt != null)
					try { stmt.close(); } catch (SQLException e) { log.error("Error in closing resource", e); }
			}
			
			try {
				TimeUnit.MINUTES.sleep(heartbeatInterval);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

}
