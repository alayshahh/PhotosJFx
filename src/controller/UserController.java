package controller;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import model.Album;
import model.User;
import model.UserList;





public class UserController {
	
	@FXML ChoiceBox<String> choiceBox;
	
	@FXML ListView<Album> listView;
	
	
	ObservableList<Album> albums;
	
	User me; 
	
	
	
	public void start(User user) {
		me = user;
		choiceBox.setItems(FXCollections.observableArrayList("Tag", "Date"));
		choiceBox.setValue("Tag");
		albums = FXCollections.observableArrayList();
		System.out.println(me+" num Albums"+ me.getAlbums().size());
		System.out.println(me.getAlbums().get(0));
		albums.addAll(me.getAlbums());
		listView.setItems(albums);
	
	}
	
	public void newAlbum(ActionEvent e) {
		Dialog<String> getNewAlbum = new Dialog<>();
		getNewAlbum.setTitle("Create New Album");
		ButtonType add = new ButtonType("Create", ButtonData.OK_DONE);
		getNewAlbum.getDialogPane().getButtonTypes().addAll(add, ButtonType.CANCEL);
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField userInput = new TextField();
	    userInput.setPromptText("Enter Album Name");
	    gridPane.add(userInput, 1, 0);
		getNewAlbum.getDialogPane().setContent(gridPane);
		getNewAlbum.setResultConverter(dialogButton -> {
			if(dialogButton==add) {
				return userInput.getText();
			}
			return null;
		});
		Optional<String> result = getNewAlbum.showAndWait();
		result.ifPresent(albumName ->{
			albumName = albumName.trim();
			if(me.hasAlbum(new Album(albumName))) {
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Album Already Exists");
				a.showAndWait();
				return;
			}
			if(albumName.isBlank()) {
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Enter Valid Album Name");
				a.showAndWait();
				return;
			}
			me.addAlbum(new Album(albumName));
			try {
				UserList.getUserList().writeApp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		});
		start(me);
		
	}
	
	public void renameAlbum(ActionEvent e) {
		
	}
	
	public void deleteAlbum(ActionEvent e) {
		Album alb = listView.getSelectionModel().getSelectedItem();
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setHeaderText("Are you sure you want to delete this album?");
		Optional<ButtonType> pressed = a.showAndWait();
		pressed.ifPresent(b->{
			if(b== ButtonType.OK) {
				me.deleteAlbum(alb);
				try {
					UserList.getUserList().writeApp();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		start(me);
		
	}
	
	public void openAlbum(ActionEvent e) {
		
	}
	
	public void searchHit(ActionEvent e) {
		
	}
	public void logOut(ActionEvent e) {
		
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
