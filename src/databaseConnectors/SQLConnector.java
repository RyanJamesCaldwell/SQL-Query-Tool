package databaseConnectors;

import java.sql.Connection;

/**
 * Connector interface for various database types to implement.
 * 
 * @author Ryan Caldwell
 * @version Version 1.0
 */
public interface SQLConnector {
	public Connection getConnection();
	public void closeConnection();
	public String toString();
}
