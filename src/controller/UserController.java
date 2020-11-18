package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.UserList;



/**
 * 
 * @author Alay Shah
 * @author Anshika Khare
 *
 */

public class UserController {
	
	@FXML ChoiceBox<String> choiceBox;
	
	//@FXML ListView<Album> listView;
	@FXML TextField searchBar;
	@FXML TableView<Album> table;
	@FXML TableColumn<Album, String> nameCol;
	@FXML TableColumn<Album, String> numCol;
	@FXML TableColumn<Album, String> dateCol;
	
	
	ObservableList<Album> albums;
	
	User me; 
	
	
	
	/**
	 * Shows all the albums that the current user has. Populates the ListView with Albums.
	 * 
	 * @param user
	 * @see User
	 * @see Album
	 */
	public void start(User user) {
		me = user;
		choiceBox.setItems(FXCollections.observableArrayList("Tag", "Date"));
		choiceBox.setValue("Tag");
		albums = FXCollections.observableArrayList();
//		System.out.println(me+" num Albums "+ me.getAlbums().size());
//		System.out.println(me.getAlbums().get(0));
		albums.addAll(me.getAlbums());
		nameCol.setCellValueFactory(new PropertyValueFactory<Album, String>("name"));
		numCol.setCellValueFactory(new PropertyValueFactory<Album,String>("numPhotos"));
		dateCol.setCellValueFactory(new PropertyValueFactory<Album,String>("dateRange"));
		table.autosize();
		nameCol.setResizable(false);
		numCol.setReorderable(false);
		dateCol.setResizable(false);
		table.setItems(albums);
	
	}
	
	/**
	 * Adds new album to the User's library. Makes sure the user doesn't have another album with the same name and that name is not blank.
	 * @param e
	 * @see Album
	 * @see User
	 */
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
	
	/**
	 * Renames the selected album.
	 * @param e
	 */
	public void renameAlbum(ActionEvent e) {
		Album alb =  table.getSelectionModel().getSelectedItem();
		Dialog<String> rename = new Dialog<>();
		rename.setTitle("Rename Album");
		ButtonType add = new ButtonType("Rename", ButtonData.OK_DONE);
		rename.getDialogPane().getButtonTypes().addAll(add, ButtonType.CANCEL);
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField userInput = new TextField();
	    userInput.setPromptText("Enter Album Name");
	    gridPane.add(userInput, 1, 0);
		rename.getDialogPane().setContent(gridPane);
		rename.setResultConverter(dialogButton->{
			if(dialogButton==add) {
				return userInput.getText();
			}
			return null;
		});
		Optional<String> result = rename.showAndWait();
		result.ifPresent(name -> {
			name = name.trim();
			if(me.hasAlbum(new Album(name))){
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Album Already Exists");
				a.showAndWait();
				return;
			}
			if(name.isBlank()) {
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Enter Valid Album Name");
				a.showAndWait();
				return;
			}
			alb.rename(name);
			try {
				UserList.getUserList().writeApp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		});
		start(me);
		
		
	}
	
	/**
	 * Deletes selected album
	 * @param e
	 */
	public void deleteAlbum(ActionEvent e) {
		Album alb = table.getSelectionModel().getSelectedItem();
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
	
	/**
	 * Opens selected Album in a new View.
	 * @param e
	 * @throws IOException 
	 */
	public void openAlbum(ActionEvent e) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotosListView.fxml"));
		Parent root = (Parent) loader.load();
		Scene user  = new Scene(root);
		AlbumController next = loader.getController();
		next.start(table.getSelectionModel().getSelectedItem(),me, false);
		Photos.window.setScene(user);
	}
	
	/**
	 * Opens view with all photos that are part of the search hit.
	 * @param e
	 * @throws IOException 
	 */
	public void searchHit(ActionEvent e) throws IOException {
		ArrayList<Photo> searchResult = new ArrayList<>();
		String query = searchBar.getText().trim();
		String [] queries = query.split(" ");
		if(choiceBox.getValue().equals("Tag")) {
			
			String [] q1;
			String []q2;
			
			boolean isAnd;
			if(queries.length!=1&&queries.length!=3) {
				Alert a  = new Alert(AlertType.ERROR);
				a.setContentText("Searches should be of format: Tag=value (AND/OR Tag=value)");
				a.showAndWait();
				return;
			}
			if(queries.length==1) {
				q1 = queries[0].split("=");
				if(q1.length!=2) {
					Alert a  = new Alert(AlertType.ERROR);
					a.setContentText("Searches should be of format: Tag=value (AND/OR Tag=value)");
					a.showAndWait();
					return;
				}else {
					//search
					Tag t = new Tag(q1[0],q1[1]);
					for(Album alb: me.getAlbums()) {
						for(Photo p: alb.searchTag(t)) {
							if(!searchResult.contains(p)) {
								searchResult.add(p);
							}
						}
					}
				}
			} else if(queries[1].toUpperCase().equals("AND")|| queries[1].toUpperCase().equals("OR")) {
				if(queries[1].length()==3) {
					isAnd=true;
				}else isAnd = false;
				
				q1 = queries[0].split("=");
				q2 = queries[2].split("=");
				
				if(q1.length==2 && q2.length==2) {
					Tag t1 = new Tag(q1[0], q1[1]);
					Tag t2 = new Tag(q2[0], q2[1]);
					for(Album alb: me.getAlbums()) {
						for(Photo p: alb.searchTag(t1, t2, isAnd)) {
							if(!searchResult.contains(p)) {
								searchResult.add(p);
							}
						}
					}
				}else {
					Alert a  = new Alert(AlertType.ERROR);
					a.setContentText("Searches should be of format: Tag=value (AND/OR Tag=value)");
					a.showAndWait();
					return;
				}
			}else {
				Alert a  = new Alert(AlertType.ERROR);
				a.setContentText("Searches should be of format: Tag=value (AND/OR Tag=value)");
				a.showAndWait();
				return;
			}
			
		}else {
			//search by date
			if(queries.length!=3||!(queries[1].equals("to"))) {
				Alert a  = new Alert(AlertType.ERROR);
				a.setContentText("Searches should be of format: mm/dd/yyy to mm/dd/yyyy");
				a.showAndWait();
				return;
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Date min = new Date();
			Date max = new Date();
			try {
				min = sdf.parse(queries[0]);
				max = sdf.parse(queries[2]);
			}catch(ParseException e1) {
				//alert
				Alert a  = new Alert(AlertType.ERROR);
				a.setContentText("Searches should be of format: mm/dd/yyy to mm/dd/yyyy");
				a.showAndWait();
				return;
				
			}
			
			if(! sdf.format(min).equals(queries[0])|| ! sdf.format(max).equals(queries[2])) {
				//alert
				Alert a  = new Alert(AlertType.ERROR);
				a.setContentText("Searches should be of format: mm/dd/yyy to mm/dd/yyyy");
				a.showAndWait();
				return;
			}else {
				//search
				for(Album album: me.getAlbums()) {
					for(Photo p: album.searchDate(min, max)) {
						if(!searchResult.contains(p)) {
							searchResult.add(p);
						}
					}
				}
			}
			
		}
		if (searchResult.size()==0) {
			//alert no matches
			Alert a  = new Alert(AlertType.ERROR);
			a.setContentText("No matches");
			a.showAndWait();
			return;
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotosListView.fxml"));
		Parent root = (Parent) loader.load();
		Scene user  = new Scene(root);
		AlbumController next = loader.getController();
		next.start(new Album("temp",searchResult),me, true);
		Photos.window.setScene(user);
		
		
		
		
		
	}
	
	
	
	/**
	 * Returns to logINscreen
	 * @param e
	 */
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
