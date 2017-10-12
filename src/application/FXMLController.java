package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import databaseConnectors.MySQLConnector;
import databaseQuerying.QuerySubmission;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

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
		try {
			this.connector = new MySQLConnector(this.databaseURLField.getText(), this.usernameField.getText(), this.passwordField.getText(), this.databaseNameField.getText());
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Could not connect to MySQL database: " + this.databaseNameField.getText());
			this.tablesInDatabase.setText("Could not connect to the database.");
		}
		
		if(this.connector != null) {
			this.querySubmitter = new QuerySubmission(connector);
			try {
				this.queryResultSet = this.querySubmitter.showTables();
			} catch (SQLException e) {
				System.err.println("Error submitting showTables();");
			}
			this.tablesInDatabase.setText(this.querySubmitter.getResultSetString(this.queryResultSet));
		}
		
	}
	
	/**
	 * Closes the connection to the database and exits the program.
	 */
	@FXML
	public void handleCloseAction(ActionEvent event) {
		if(this.connector != null) {
			try {
				this.connector.closeConnection();
			} catch (SQLException e) {
				System.err.println("Error closing connection.");
			}
		}
		Platform.exit();
	}
	
	/**
	 * Queries the database with the user-supplied query, then populates the queryResult TextField below with the result.
	 * <b>Note:</b> Query text field cannot be empty.
	 */
	@FXML
	public void runQuery(ActionEvent event) {
		if(this.queryField.getText() != null && this.queryField.getText().length() > 1) {
			String newQuery = this.queryField.getText().toLowerCase();
			
			if(newQuery.contains("insert") || newQuery.contains("modify") || newQuery.contains("delete") || newQuery.contains("update")) {
				try {
					this.querySubmitter.submitWriteQuery(newQuery);
				} catch (SQLException e) {
					System.err.println("Error submitting write query: " + newQuery);
				}
			}
			else {
				try {
					this.queryResultSet = this.querySubmitter.submitReadQuery(newQuery);
				} catch (SQLException e) {
					this.queryResult.setText("Error submitting read query: " + newQuery);
				}
				
				if(this.queryResultSet != null) {
					this.queryResult.setText(this.querySubmitter.getResultSetString(this.queryResultSet));
				}
				else {
					this.queryResult.setText("Query cannot be null.");
				}
			}
		}
		else {
			this.queryResult.setText("Error: User attempted to submit empty query.");
		}
	}
	
	/**
	 * Gives the user information about the program in a pop-up window.
	 */
	@FXML
	public void displayAbout(ActionEvent event) {
		new Alert(Alert.AlertType.INFORMATION, "This program is used to query MySQL databases.\nDeveloper: Ryan Caldwell\nLast Revised Date: 11-OCT-2017").showAndWait();
	}
	
	
	/**
	 * Saves the user's most recent query result in a .txt file
	 */
	@FXML
	public void exportQueryResults() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File saveFile;
		File selectedDirectory;
		String savePath;
		
		if(this.queryResult.getText().length() > 1) {
			directoryChooser.setTitle("Save Query Results");
			File defaultDirectory = new File("/Users/ryancaldwell/Desktop/");
			directoryChooser.setInitialDirectory(defaultDirectory);
			selectedDirectory = directoryChooser.showDialog(null);
			savePath = selectedDirectory.toString() + "/" + System.currentTimeMillis() + "-newQueryResult.txt";
			
			saveFile = new File(savePath);
			
			try {
				FileWriter fw = new FileWriter(saveFile);
				fw.write(this.queryResult.getText());
				fw.close();
			} catch (IOException e) {
				System.err.println("Error exporting file.");
			}
			
			try {
				saveFile.createNewFile();
			} catch (IOException e) {
				System.err.println("Error creating new file.");
			}
			
			new Alert(Alert.AlertType.INFORMATION, "File " + savePath + " saved.").showAndWait();
		}
		else {
			new Alert(Alert.AlertType.ERROR, "Cannot save query since there is no output.").showAndWait();
		}
	}
}