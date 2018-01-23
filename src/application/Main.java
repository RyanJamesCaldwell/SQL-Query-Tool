package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * This class creates the JavaFX GUI for the querying MySQL databases.
 * 
 * @author Ryan Caldwell
 * @version Version 1.0
 */
public class Main extends Application {
	
	/**
	 * Primary window for the UI
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load( getClass().getResource("FXMLDocument.fxml") );
			Scene scene = new Scene(root, 712, 703);
			primaryStage.setTitle("Database Querying - GUI");
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/9JK9Gzs.jpg")));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launches JavaFX application.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}