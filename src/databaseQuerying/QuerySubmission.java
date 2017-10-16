package databaseQuerying;

import databaseConnectors.MySQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * A class that provides functionality for querying a database.
 * 
 * @author Ryan Caldwell
 * @version Version 1.0
 */
public class QuerySubmission {
	
	private MySQLConnector connector; // Connector used for connecting to the MySQL database
	private Connection connection;	  // Connection used by the MySQLConnector
	
	/**
	 * Receives a connected MySQLConnector, sets up for querying.
	 * 
	 * @param newConnector A connected MySQLConnector
	 */
	public QuerySubmission(MySQLConnector newConnector) {
		connector = newConnector;
		connection = newConnector.getConnection();
	}
	
	/**
	 * Receives a String query to be sent to the currently connected database.
	 * 
	 * <p><b>Ex:</b> select * from databaseName.tableName;</p>
	 * 
	 * @param query A String query to be sent to the database.
	 * @return Returns the ResultSet received from the currently connected database, null if an error occurred.
	 * @throws SQLException 
	 */
	public ResultSet submitReadQuery(String query) throws SQLException {
		PreparedStatement statement;
		ResultSet resultSet = null;
		
		if(query != null && query.length() > 1){
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery(query);
		}
		else {
			System.err.println("Query cannot be null.");
		}
		
		return resultSet;
	}
	
	/**
	 * Receives a String update query to be sent to the currently connected database.
	 * 
	 * @param query A String update query to be sent to the database (INSERT, DELETE, MODIFY, UPDATE, etc.).
	 * @throws SQLException 
	 */
	public void submitWriteQuery(String query) throws SQLException {
		PreparedStatement statement;
		
		if(query != null) {
			statement = connection.prepareStatement(query);
			statement.executeUpdate(query);
		}
		
		else {
			System.err.println("Query cannot be null.");
		}
	}
	
	/**
	 * Queries the testing.test table "select * from testing.test" and returns the entire ResultSet.
	 * 
	 * @return Returns populated ResultSet if successful, null otherwise.
	 * @throws SQLException 
	 */
	public ResultSet returnTable(String tableName) throws SQLException {
		String query = "select * from " + tableName;
		return submitReadQuery(query);
	}
	
	/**
	 * Returns the MySQLConnector being used.
	 * 
	 * @return Returns the MySQLConnector being used.
	 */
	public MySQLConnector getConnector() {
		return this.connector;
	}
	
	/**
	 * Prints the ResultSet in a format: "column1_name: column1_value, ... , columnN_name: columnN_value"
	 * 
	 * @param result The ResultSet to be printed
	 */
	public void printResultSet(ResultSet result) {
		try {
			ResultSetMetaData rsmd = result.getMetaData();
			int numColumns = rsmd.getColumnCount();
			String columnValue;
			
			while(result.next()) {
				for(int i = 1; i <= numColumns; i++) {
					if (i > 1) {
						System.out.print(", ");
					}
					columnValue = result.getString(i);
					System.out.print(rsmd.getColumnLabel(i) + ": " + columnValue);
				}
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints the ResultSet in a format: "column1_name: column1_value, ... , columnN_name: columnN_value"
	 * 
	 * @param result The ResultSet to be printed
	 */
	public String getResultSetString(ResultSet result) {
		StringBuilder sb = null;
		try {
			sb = new StringBuilder("");
			ResultSetMetaData rsmd = result.getMetaData();
			int numColumns = rsmd.getColumnCount();
			String columnValue;
			
			while(result.next()) {
				for(int i = 1; i <= numColumns; i++) {
					if (i > 1) {
						sb.append("\n");
					}
					columnValue = result.getString(i);
					sb.append(rsmd.getColumnLabel(i) + ": " + columnValue);
				}
				sb.append("\n\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	/**
	 * Returns a ResultSet containing the different databases that are on the MySQL Server.
	 * 
	 * @return ResultSet containing the different databases that are on the MySQL Server.
	 * @throws SQLException 
	 */
	public ResultSet showDatabases() throws SQLException {
		return this.submitReadQuery("show databases;");
	}
	
	/**
	 * Returns a ResultSet containing the different tables that are in the MySQL database.
	 * 
	 * @return ResultSet containing the different tables that are in the MySQL database.
	 * @throws SQLException 
	 */
	public ResultSet showTables() throws SQLException {
		return this.submitReadQuery("show tables;");
	}
}