package databaseConnectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is to be used as a connector between the user and a MySQL database. This class requires one instantiation per 
 * connected database. <br />
 * <b>Note:</b> Once instantiated, the database name cannot change.
 * 
 * @author Ryan Caldwell
 * @version Version 1.0
 */
public final class MySQLConnector implements SQLConnector {
	
	private String url;					// The URL to connect to the database
	private String username;				// The username that is used for database authentication
	private String password;				// The correlating password that is used for database authentication
	private final String databaseName;	// The name of the database that will be used
	private Connection connect;			// The connection made to the MySQL database
	
	/**
	 * Establishes the connection between the user and the MySQL database.
	 * 
	 * @param URL The URL to the database. Ex: "localhost:3306" for local, default MySQL database
	 * @param username The username that is used for database authentication
	 * @param password The correlating password that is used for database authentication
	 * @param databaseName The name of the database that will be used
	 * @throws SQLException If compiler cannot find com.mysql.jdbc.Driver
	 * @throws ClassNotFoundException If compiler cannot establish connection.
	 */
	public MySQLConnector(String URL, String username, String password, String databaseName) {
		connect = null;
		this.url = "jdbc:mysql://" + URL + "/";
		this.username = username;
		this.password = password;
		this.databaseName = databaseName;
	}
	
	// Establishes the connection between the user and the MySQL database.
	// Autoreconnect is automatically set to true, and useSSL is automatically set to false.
	public void establishConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection(this.url + this.databaseName + "?autoReconnect=true&useSSL=false&" + 
				"user=" + this.username + "&password=" + this.password);
	}
	
	/**
	 * Closes the connection with the connected MySQL database.
	 * @throws SQLException 
	 */
	@Override
	public void closeConnection() throws SQLException  {
		this.connect.close();
	}
	
	/**
	 * Returns the name of the connector.
	 * @return Returns the name of the connector.
	 */
	@Override
	public String toString() {
		return "MySQLConnector: URL - " + new String(this.url) + ", Database Name - " + new String(this.databaseName);
	}
	
	/**
	 * Returns the current connection.
	 * 
	 * @return Returns the connection, null if unsuccessfully connected.
	 */
	@Override
	public Connection getConnection() {
		return this.connect;
	}
	
	/**
	 * Returns the username currently being used for logging into the MySQL database.
	 * 
	 * @return Username currently being used for logging into the MySQL database.
	 */
	public String getUsername() {
		return new String(this.username);
	}
	
	/**
	 * Sets the username being used to connect to the database to the new username.
	 * 
	 * @param newUsername The new username to be used when connecting to the database.
	 */
	public void setUsername(String newUsername) {
		if(newUsername != null) {
			this.username = newUsername;
		}
		else {
			System.err.println("Username cannot be null.");
		}
	}
	
	/**
	 * Returns the URL currently being used for connecting to the MySQL database.
	 * 
	 * @return (String) URL currently being used for connecting to the MySQL database.
	 */
	public String getURL() {
		return new String(this.url);
	}
	
	/**
	 * Sets the URL being used to connect to the database to the new URL.
	 * 
	 * @param newURL The new URL to be used when connecting to the database.
	 */
	public void setURL(String newURL) {
		if(newURL != null) {
			this.url = newURL;
		}
		else {
			System.err.println("URL cannot be null.");
		}
	}
	
	/**
	 * Returns the name of the database that is being connected to.
	 * 
	 * @return The name of the database that is being connected to.
	 */
	public String getDatabaseName() {
		return new String(this.databaseName);
	}
}