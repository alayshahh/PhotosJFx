package controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.UserList;

/**
 * 
 * @author Alay Shah
 * @author Anshika Khare
 *
 */
public class Photos extends Application {
	
	
	public static Stage window;
	
	
	
	public static void main(String [] args) {
		launch(args); 
	}

	@Override
	public void start(Stage mainStage) throws Exception {
		// TODO Auto-generated method stub
		window = mainStage;

		FXMLLoader loader  = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/logInScreen.fxml"));
		
		Parent root  = loader.load();

		mainStage.setTitle("Photos");

		Scene logIn = new Scene (root);

		mainStage.setResizable(false);
		mainStage.setScene(logIn);
		mainStage.show();
		
		
		
	}
	
	

		
	
	

}
