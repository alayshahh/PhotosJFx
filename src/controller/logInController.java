package controller;

import java.io.EOFException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.io.File;

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

/**
 * @author Alay Shah
 * @author Anshika Khare
 *
 */
public class logInController implements Initializable {

	@FXML 
	Button LogInButton;
	@FXML 
	TextField usernameInput;
	
	
	/**
	 * Initializes the app set up. Makes sure that there is at least a stock user. Reads from file if there is data.
	 * @see UserList
	 * 
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		//read from file to import userList
		//System.out.println("hello");
		
		try {
			UserList.getUserList().readApp();
		}catch(EOFException e){
			//System.out.println("No users");
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
//			System.out.println("Resouce Gotten");
			Parent root = (Parent) loader.load();
//			System.out.println("Resouce Loaded");
			Scene admin  = new Scene(root);
			AdminController next = loader.getController();
			next.start();
//			System.out.println("Scene Gotten");
			Photos.window.setScene(admin);
			
//			System.out.println("Scene Set");
			
			
		}else {
			if(UserList.getUserList().contains(input)) {
				//open albums page
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumListView.fxml"));
				Parent root = (Parent) loader.load();
				Scene user  = new Scene(root);
				UserController next = loader.getController();
				next.start(UserList.getUserList().getUser(input));
				Photos.window.setScene(user);
				
				
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
	
	/**
	 * Sets up stock image user if this is the first time the user opens app.
	 * @return
	 */
	public User setStock() {
		String currentPath = System.getProperty("user.dir");
		User stock = new User("stock");
		Album stockImg = new Album("stock img");
		stock.addAlbum(stockImg);
		char x = File.separatorChar;
		String relativePath = x+"data"+x+"stock"+x;
		String [] stockFilePaths = new String [] {"Stock.jpg", "stock2.jpg","stock3.jpg", "stock4.jpg", "stock5.jpg"};
		String [] captions = new String [] {"omg", "mind = blown", "yes sir", "hacker", "coffee"};
		for(int i = 0; i<captions.length; i++) {
			Photo p = new Photo(currentPath+relativePath+stockFilePaths[i]);
			p.setCaption(captions[i]);
			stockImg.addPhoto(p);
			//System.out.println(p.getDate());
		}
		//System.out.println(stockImg);
		return stock;
		
	}
	
	
	//at the beginning of the log in sequence we want to make sure that the admin and stock user is set up
}
