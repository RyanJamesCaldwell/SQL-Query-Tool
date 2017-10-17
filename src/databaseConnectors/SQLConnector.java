package databaseConnectors;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connector interface for various database types to implement.
 * 
 * @author Ryan Caldwell
 * @version Version 1.0
 */
public interface SQLConnector {
	public Connection getConnection();
	public void closeConnection() throws SQLException;
	@Override
	public String toString();
}
