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
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
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
public class PhotoController {

	Album album;
	User me;
	Photo photo;
	
	@FXML ImageView img;
	@FXML TextField date;
	@FXML TextField caption;
	@FXML ListView<Tag> tagsList;
	
	
	
	
	
	
	/**
	 * Initializes the view for the given photo, filling in table, image view, date, caption and the tagList.
	 * @param u
	 * @param alb
	 * @param p
	 */
	public void start(User u, Album alb, Photo p) {
		me = u;
		album = alb; 
		photo = p;
//		System.out.println(p);
//		System.out.println(photo);
		
		ObservableList<Tag> tags = FXCollections.observableArrayList();
		tags.addAll(photo.getTags());
		tagsList.setItems(tags);
		
		img.setImage(photo.getImage().getImage());
		img.autosize();
		img.setStyle("-fx-alignment: center ;");
		date.setEditable(false);
		date.setText(photo.getDate());
		caption.setEditable(false);
		caption.setText(photo.getCaption());
		
		
	}
	
	
	/**
	 * Opens next photo
	 * @param e
	 */
	public void nextHit(ActionEvent e ) {
		photo = album.getNext(photo);
		start(me, album, photo);
		
	}
	
	/**
	 * Opens previous photo
	 * @param e
	 */
	public void prevHit(ActionEvent e) {
		photo = album.getPrev(photo);
		start(me, album, photo);
		
	}
	
	/**
	 * Goes back to previous screen
	 * @param e
	 * @throws IOException
	 */
	public void backHit(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/PhotosListView.fxml"));
		Parent root = (Parent) loader.load();
		Scene user  = new Scene(root);
		AlbumController next = loader.getController();
		next.start(album,me,false);
		Photos.window.setScene(user);
	}
	
	/**
	 * Changes caption of photo
	 * @param e
	 */
	public void editCaptionHit(ActionEvent e) {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Edit Caption");
		ButtonType add = new ButtonType("Save", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(add, ButtonType.CANCEL);
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField newCap = new TextField();
	    newCap.setPromptText("Caption");
	    if(!photo.getCaption().isBlank()) {
	    	newCap.setText(photo.getCaption());
	    }
	    gridPane.add(newCap, 1, 0);
	    dialog.getDialogPane().setContent(gridPane);
	    dialog.setResultConverter(dialogButton -> {
	        if (dialogButton == add) {
	            return newCap.getText();
	        }
	        return null;
	    });
	    Optional<String> cap = dialog.showAndWait();
	    cap.ifPresent(set->{
	    	photo.setCaption(set);
	    	try {
				UserList.getUserList().writeApp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	start(me,album,photo);
	    });
	    
		
	}
	
	
	/**
	 * Adds new Tag to photo if not already there
	 * @param e
	 */
	public void addTag(ActionEvent e) {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Add New Tag");
	
		ButtonType add = new ButtonType("Add Tag", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(add, ButtonType.CANCEL);
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField tName = new TextField();
	    tName.setPromptText("Tag Type");
	    TextField tVal = new TextField();
	    tVal.setPromptText("Tag Value");
	    gridPane.add(tName, 1, 0);
	    gridPane.add(tVal, 1, 1);
	    dialog.getDialogPane().setContent(gridPane);
	    dialog.setResultConverter(dialogButton -> {
	        if (dialogButton == add) {
	            return new Pair<>(tName.getText().trim(), tVal.getText().trim());
	        }
	        return null;
	    });
	    Optional<Pair<String, String>> result = dialog.showAndWait();
	    
	    result.ifPresent(pair-> {
	    	Pair<String,String> info = pair;
	    	Tag t  = new Tag(info.getKey(), info.getValue());
	    	System.out.println(t);
	    	if(photo.hasTag(t)) {
	    		//alert
	    		Alert alert = new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Duplicate Item");
	    		alert.setHeaderText("You already have this tag,");
	    		alert.showAndWait();
	    	}else {
	    		photo.addTag(t);
	    		System.out.println(photo.getTags());
	    		try {
					UserList.getUserList().writeApp();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		start(me,album,photo);
	    	}
	 
	    	
	    });
	    
		
	}
	
	/**
	 * Removes selected tag from the photo
	 * @param e
	 */
	public void deleteTag(ActionEvent e) {
		
		Alert a  =  new Alert(AlertType.CONFIRMATION);
		a.setHeaderText("Are you sure you want to delete this tag?");
		Optional<ButtonType> press = a.showAndWait();
		press.ifPresent(b->{
			if(b == ButtonType.OK) {
				Tag t = tagsList.getSelectionModel().getSelectedItem();
				photo.deleteTag(t);
				try {
					UserList.getUserList().writeApp();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				start(me,album,photo);
				
			}
		});
		
	}
	
	/**
	 * Moves selected photo to given album. Deletes from current album. Returns to album overview.
	 * @param e
	 */
	public void moveTo(ActionEvent e) {
		
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Move To");
		ButtonType add = new ButtonType("Move", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(add, ButtonType.CANCEL);
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField tName = new TextField();
	    tName.setPromptText("Album Name");
	    gridPane.add(tName, 1, 0);
	    dialog.getDialogPane().setContent(gridPane);
	    dialog.setResultConverter(dialogButton -> {
	        if (dialogButton == add) {
	            return tName.getText();
	        }
	        return null;
	    });
	    Optional<String> moveLoc = dialog.showAndWait();
	    moveLoc.ifPresent(s->{
	    	if(me.hasAlbum(new Album(s))) {
	    		Album mover = me.getThisAlbum(new Album(s));
	    		if(mover.contains(photo)) {
	    			Alert a = new Alert(AlertType.INFORMATION);
	    			a.setHeaderText("Album already has this photo.");
	    			a.showAndWait();
	    			return;
	    		}else {
	    			mover.addPhoto(photo);
	    			album.removePhoto(photo);
	    			try {
						UserList.getUserList().writeApp();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    			try {
						backHit(e);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    		}
	    		
	    	}else {
	    		Alert a = new Alert(AlertType.ERROR);
	    		a.setHeaderText("Invalid Name");
	    		a.showAndWait();
	    		return;
	    	}
	    });
	    
	    
	    
		
	}
	
	/**
	 * Copies selected Album to given album.
	 * @param e
	 */
	public void copyTo(ActionEvent e) {
		
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Move To");
		ButtonType add = new ButtonType("Move", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(add, ButtonType.CANCEL);
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField tName = new TextField();
	    tName.setPromptText("Album Name");
	    gridPane.add(tName, 1, 0);
	    dialog.getDialogPane().setContent(gridPane);
	    dialog.setResultConverter(dialogButton -> {
	        if (dialogButton == add) {
	            return tName.getText();
	        }
	        return null;
	    });
	    Optional<String> moveLoc = dialog.showAndWait();
	    moveLoc.ifPresent(s->{
	    	if(me.hasAlbum(new Album(s))) {
	    		Album mover = me.getThisAlbum(new Album(s));
	    		if(mover.contains(photo)) {
	    			Alert a = new Alert(AlertType.INFORMATION);
	    			a.setHeaderText("Album already has this photo.");
	    			a.showAndWait();
	    			return;
	    		}else {
	    			mover.addPhoto(photo);
	    			try {
						UserList.getUserList().writeApp();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    		}
	    		
	    	}else {
	    		Alert a = new Alert(AlertType.ERROR);
	    		a.setHeaderText("Invalid Name");
	    		a.showAndWait();
	    		return;
	    	}
	    });
		
	}
	
	/**
	 * Logs out user.
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
	

