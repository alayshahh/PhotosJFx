package controller;

import java.io.File;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import model.Album;
import model.Photo;
import model.User;
import model.UserList;
/**
 * 
 * @author Alay Shah
 * @author Anshika Khare
 *
 */
public class AlbumController {
	
	@FXML Button LogOut;
	@FXML Button back;
	@FXML Button create;
	@FXML Button view;
	@FXML Button add;
	@FXML Button delete;
	@FXML TableView<Photo> table; 
	@FXML TableColumn<Photo, ImageView> photoCol;
	@FXML TableColumn<Photo,String> capCol;
	//@FXML ListView<ImageView> list;
	
	
	Album album;
	User me;
	boolean search;
	
	/**
	 * Initializes the view. Sets up photos and the captions in table view.
	 * @param alb
	 * @param u
	 * @param isSearch
	 */
	public void start(Album alb, User u, boolean isSearch) {
		me = u;
		album = alb;
		search = isSearch;
		if(isSearch) {
			view.setVisible(false);
			add.setVisible(false);
			delete.setVisible(false);
		}else {
			create.setVisible(false);
		}
		ObservableList<Photo> ps = FXCollections.observableArrayList();
		for(Photo p: alb.getPhotos()) {
			ps.add(p);	
		}
		//list.setItems(imgs);
		photoCol.setCellValueFactory(new PropertyValueFactory<Photo, ImageView>("image"));
		capCol.setCellValueFactory(new PropertyValueFactory<Photo, String>("caption"));
		table.setItems(ps);
		table.autosize();
		capCol.setResizable(false);
		capCol.setSortable(false);
		photoCol.setResizable(false);
		photoCol.setSortable(false);
		table.requestFocus();
		
	}
	
	
	/**
	 * Opens file explorer and addes chosen file to the album
	 * @param e
	 * @throws IOException
	 */
	public void add(ActionEvent e) throws IOException {
		System.out.println("Add pressed");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File f = fileChooser.showOpenDialog(Photos.window);
		if(f==null) {
			return;
		}
		//System.out.println(f.getAbsolutePath());
		Photo p = new Photo(f.getAbsolutePath());
		album.addPhoto(p);
		UserList.getUserList().writeApp();
		start(album,me,false);
		table.getSelectionModel().select(p);
		view(e);
	}
	
	
	public void view(ActionEvent e) throws IOException {
		if(table.getSelectionModel().getSelectedItem()==null){
			return;
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoView.fxml"));
		Parent root = (Parent) loader.load();
		Scene user  = new Scene(root);
		PhotoController next = loader.getController();
		next.start(me,album, table.getSelectionModel().getSelectedItem());
		Photos.window.setScene(user);
	}

	/**
	 * Deletes the selected photo from the album.
	 * @param e
	 */
	public void delete(ActionEvent e) {
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setHeaderText("Are you sure you want to delete this picure?");
		Optional<ButtonType> pressed = a.showAndWait();
		pressed.ifPresent(b->{
			if(b==ButtonType.OK) {
				album.removePhoto(table.getSelectionModel().getSelectedItem());
				start(album,me,false);
				try {
					UserList.getUserList().writeApp();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		return;
		
		
	}
	
	/**
	 * Creates album from the searchResult
	 * @param e
	 */
	public void createAlbum(ActionEvent e) {
		System.out.println("create pushed");
		Dialog<String> getNewAlbum = new Dialog<>();
		getNewAlbum.setTitle("Create Album");
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
			album.rename(albumName);
			me.addAlbum(album);
			try {
				UserList.getUserList().writeApp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		});
		try {
			back(e);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	/**
	 * Goes back to previous Screen
	 * @param e
	 * @throws IOException
	 */
	public void back(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumListView.fxml"));
		Parent root = (Parent) loader.load();
		Scene user  = new Scene(root);
		UserController next = loader.getController();
		next.start(me);
		Photos.window.setScene(user);
	}
	
	/**
	 * Saves and logs User out
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
