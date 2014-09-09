package im.proxy.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

public class DatabaseConnection {
	private BasicDataSource ds;
	private static DatabaseConnection instance = null;
	private Logger log;
	private String dbUsername;
	private String dbPassword;
	private String dbUrl;
	private String dbPort;
	private String dbName;
	
	private DatabaseConnection() {
		log = Logger.getLogger(getClass().getName());
		
		readConfig();
		
		log.info("Initializing DB connection pool...");
		
		ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername(dbUsername);
		ds.setPassword(dbPassword);
		ds.setUrl("jdbc:mysql://" + this.dbUrl + ":" + this.dbPort + "/" + this.dbName);
		
		ds.setMinIdle(5);
        ds.setMaxIdle(20);
        ds.setMaxOpenPreparedStatements(180);
	}
	
	private void readConfig() {
		ResourceBundle myResources = ResourceBundle.getBundle("improxy");
		dbUsername = myResources.getString("db.user");
		dbPassword = myResources.getString("db.password");
		dbUrl = myResources.getString("db.url");
		dbPort = myResources.getString("db.port");
		dbName = myResources.getString("db.name");
	}
	
	public synchronized static DatabaseConnection getInstance() {
		if(instance == null) {
			instance = new DatabaseConnection();
		}
		return instance;
	}
	
	public Connection getConnection() throws SQLException {
		return this.ds.getConnection();
	}
}