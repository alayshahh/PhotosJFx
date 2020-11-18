package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import model.User;
import model.UserList;

/**
 * @author Alay Shah
 * @author Anshika Khare
 *
 */
public class AdminController{
	
	@FXML Button deleteUser;
	@FXML Button createUser;
	@FXML Button logOut;
	@FXML ListView<User> listUsers;
	
	private ObservableList<User> users;
	
	
	
	/**
	 * Starts the admin screen and populates the list with User instances.
	 */
	public void start() {
		UserList list = UserList.getUserList();
		users = FXCollections.observableArrayList();
		users.addAll(list.getAll());
		listUsers.setItems(users);
	}

	
	/**
	 * Creates a new user with the inputted username.
	 * @param e action event
	 * @see User
	 */
	public void createUserPressed(ActionEvent e) {
		Dialog<String> getNewUser = new Dialog<>();
		getNewUser.setTitle("Create New User");
		ButtonType add = new ButtonType("Create", ButtonData.OK_DONE);
		getNewUser.getDialogPane().getButtonTypes().addAll(add, ButtonType.CANCEL);
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField userInput = new TextField();
	    userInput.setPromptText("Enter Username");
	    gridPane.add(userInput, 1, 0);
		getNewUser.getDialogPane().setContent(gridPane);
		getNewUser.setResultConverter(dialogButton -> {
			if(dialogButton==add) {
				return userInput.getText();
			}
			return null;
		});
		Optional<String> result = getNewUser.showAndWait();
		result.ifPresent(userName ->{
			userName = userName.trim();
			if(UserList.getUserList().contains(userName)) {
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("User Already Exists");
				a.showAndWait();
				return;
			}
			if(userName.isBlank()) {
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Enter Valid Username");
				a.showAndWait();
				return;
			}
			if(userName.toLowerCase().equals("admin")) {
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Enter Valid Username");
				a.showAndWait();
				return;
			}
			UserList.getUserList().addUser(new User(userName));
			try {
				UserList.getUserList().writeApp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		});
		start();
	}
	
	/**
	 * Deletes the selected user instance from UserList
	 * 
	 * @param e action event
	 * @see User
	 * @see UserList
	 */
	public void deleteUserPressed(ActionEvent e) {
		User u = listUsers.getSelectionModel().getSelectedItem();
//		if(u.getUserName().equals("stock")) {
//			Alert warning = new Alert(AlertType.WARNING);
//			warning.setHeaderText("Cannot delete Stock User");
//			
//		}
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setContentText("Are you sure you want to delete "+ u +"?");
		Optional<ButtonType> selected = confirm.showAndWait();
		selected.ifPresent(b ->{
			if(b == ButtonType.OK) {
				UserList.getUserList().removeUser(u);
				try {
					UserList.getUserList().writeApp();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				start();
			}
		});
		
	}
	
	/**
	 * Safely logs admin out and returns them back to the login screen 
	 * 
	 * @param e action event
	 * @throws IOException if write fails
	 */
	public void logOutPressed(ActionEvent e) throws IOException {
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setContentText("Are you sure you want to log out?");
		Optional<ButtonType> selected = confirm.showAndWait();
		selected.ifPresent(b ->{
			try {
				UserList.getUserList().writeApp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			FXMLLoader loader  = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/logInScreen.fxml"));
			
			Parent root;
			try {
				root = (Parent) loader.load();
				Scene logIn = new Scene(root);
				Photos.window.setScene(logIn);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		});
	}

	
}
