package im.proxy.db;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class SubscriberDAO {
	private Logger log = Logger.getLogger(getClass().getName());
	
	protected DatabaseConnection db;
	
	public SubscriberDAO() {
		db = DatabaseConnection.getInstance();
	}
	
	public String getIntroductionMessage(String aMsisdn, String bMsisdn) {
		CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = this.db.getConnection().prepareCall("{ call getStatusMessage(?, ?) }");
			stmt.setString(1, aMsisdn);
			stmt.setString(2, bMsisdn);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			log.error("getStatusMessage failed: " + e.getMessage(), e);
		} finally {
			try {
				if(rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException ex) {
				log.error("failed to close db resources: " + ex.getMessage(), ex);
			}
		}
		
		return null;
	}
}