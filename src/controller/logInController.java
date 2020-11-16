package controller;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;
import model.UserList;

public class logInController implements Initializable {

	@FXML 
	Button LogInButton;
	@FXML 
	TextField usernameInput;
	
	
	public void initialize(URL url, ResourceBundle resourceBundle) {
		//read from file to import userList
		System.out.println("hello");
		
		try {
			UserList.getUserList().readApp();
		}catch(EOFException e){
			System.out.println("No users");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(UserList.getUserList().isEmpty()) {
			//set up stock user
			User stock = setStock();
			UserList.getUserList().addUser(stock);
			try {
				UserList.getUserList().writeApp();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public void logInPressed(ActionEvent event) throws IOException {
		String input = usernameInput.getText().trim();
		if(input.isEmpty()) {
			return;
		}
		
		if(input.equals("admin")){
			//run admin sequence
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
			Parent root = (Parent) loader.load();
			Scene admin  = new Scene(root);
			Photos.window.setScene(admin);
			
			
		}else {
			if(UserList.getUserList().contains(input)) {
				//open albums page
				
				
				
			}else {
				//give error (User doesnt exist/ wrong username)
				Alert invalid = new Alert(AlertType.ERROR);
				invalid.setContentText("Please enter a valid username");
				invalid.setHeaderText("Invalid Username Entered");
				invalid.showAndWait();
				usernameInput.clear();
				
			}
		}
	}
	
	public User setStock() {
		User stock = new User("stock");
		Album stockImg = new Album("stock img");
		stock.addAlbum(stockImg);
		String [] stockFilePaths = new String [] {"/docs/stock/Stock.jpg", "/docs/stock/stock2.jpg","/docs/stock/stock3.jpg", "/docs/stock/stock4.jpg", "/docs/stock/stock5.jpg"};
		String [] captions = new String [] {"omg", "mind = blown", "yes sir", "hacker", "coffee"};
		for(int i = 0; i<captions.length; i++) {
			Photo p = new Photo(stockFilePaths[i]);
			p.setCaption(captions[i]);
			stockImg.addPhoto(p);
		}
		return stock;
		
	}
	
	
	//at the beginning of the log in sequence we want to make sure that the admin and stock user is set up
}
