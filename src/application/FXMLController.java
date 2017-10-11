package application;

import java.sql.ResultSet;

import databaseConnectors.MySQLConnector;
import databaseQuerying.QuerySubmission;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXMLController contains the underlying UI functionality for the "Database Querying - GUI" program.
 * 
 * @author Ryan Caldwell
 * @version Version 1.0
 */
public class FXMLController {
	
	@FXML
	private TextField usernameField;			// TextField for the user to enter their username in
	@FXML
	private PasswordField passwordField;		// PasswordField for the user to enter their password in
	@FXML
	private TextField databaseURLField;		// TextField for the user to enter the database URL in
	@FXML
	private TextField databaseNameField;		// TextField for the user to enter the database name in
	@FXML
	private TextArea queryResult;			// TextArea for displaying the result of the user's query
	@FXML
	private TextArea tablesInDatabase;		// TextArea for displaying the tables in the connected database
	@FXML
	private TextField queryField;			// TextField for the user to enter their query in
	
	private MySQLConnector connector = null;	// Connector for connecting to the MySQL database
	private QuerySubmission querySubmitter;	// QuerySubmission for submitting queries to the MySQL database
	private ResultSet queryResultSet;		// ResultSet containing the server's reply to user queries

	/**
	 * Initializes the connection between the user and the database. Once connected, retrieves
	 * the tables from the connected database and displays them in the "Tables in Database" TextField.
	 */
	@FXML
	public void establishConnection(ActionEvent event) {
		this.connector = new MySQLConnector(this.databaseURLField.getText(), this.usernameField.getText(), this.passwordField.getText(), this.databaseNameField.getText());
		this.querySubmitter = new QuerySubmission(connector);
		
		this.queryResultSet = this.querySubmitter.showTables();
		this.tablesInDatabase.setText(this.querySubmitter.getResultSetString(this.queryResultSet));
	}
	
	/**
	 * Closes the connection to the database and exits the program.
	 */
	@FXML
	public void handleCloseAction(ActionEvent event) {
		if(this.connector != null) {
			this.connector.closeConnection();
		}
		Platform.exit();
	}
	
	/**
	 * Queries the database with the user-supplied query, then populates the TextField below with the result.
	 * <b>Note:</b> Query text field cannot be empty.
	 */
	@FXML
	public void runQuery(ActionEvent event) {
		if(this.queryField.getText() != "" && this.queryField.getText() != null && this.queryField.getText() != " ") {
			String newQuery = this.queryField.getText().toLowerCase();
			
			if(newQuery.contains("insert") || newQuery.contains("modify") || newQuery.contains("delete") || newQuery.contains("update")) {
				this.querySubmitter.submitWriteQuery(newQuery);
			}
			else {
				this.queryResultSet = this.querySubmitter.submitReadQuery(this.queryField.getText());
				
				if(this.queryResultSet != null) {
					queryResult.setText(this.querySubmitter.getResultSetString(this.queryResultSet));
				}
				else {
					queryResult.setText("Query cannot be null.");
				}
			}
		}
		else {
			System.err.println("Error: User attempted to submit empty query.");
		}
	}
	
	/**
	 * Gives the user information about the program.
	 */
	@FXML
	public void displayAbout(ActionEvent event) {
		new Alert(Alert.AlertType.INFORMATION, "This program is used to query MySQL databases.\nDeveloper: Ryan Caldwell\nLast Revised Date: 11-OCT-2017").showAndWait();
	}
}