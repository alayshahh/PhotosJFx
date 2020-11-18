package model;

import java.io.Serializable;
import java.util.*;
/**
 * 
 * @author Alay Shah
 * @author Anshika Khare
 *
 */

public class User implements Serializable{
	
	static final long serialVersionUID = 1L;
	ArrayList<Album> albums = new ArrayList<>();
	private String userName;
	
	/**
	 * Constructor for User, takes in userName.
	 * @param userName Username for user
	 */
	public User(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Returns the Albums that the user has
	 * @return Albums of User
	 */
	public ArrayList<Album> getAlbums(){
		return this.albums;
	}
	
	/**
	 * Adds the Album to the user's album list
	 * 
	 * @param alb Album to be added
	 */
	public void addAlbum(Album alb) {
		this.albums.add(alb);
	}
	
	/**
	 * Deletes the album from the user's album list
	 * 
	 * @param alb Album to be deleted
	 */
	public void deleteAlbum(Album alb){
		this.albums.remove(alb);
	}
	
	/**
	 * Gets Album from list.
	 * @param alb Album to be obtained
	 * @return Album that was wanted
	 */
	public Album getThisAlbum(Album alb) {
		for(Album a: albums) {
			if(a.equals(alb)) {
				return a;
			}
		}
		return null;
	}
	
	/**
	 * Returns true if the user already has an album with the same name
	 * @param alb Album to be checked
	 * @return true if Album is owned by User
	 */
	public boolean hasAlbum(Album alb) {
		return albums.contains(alb);
	}
	
	
	/**
	 * Returns the userName of the User
	 * @return UserName
	 */
	public String getUserName() {
		return this.userName;
	}
	
	
	
 
	public boolean equals(Object o) {
		if(!(o instanceof User)|| o==null) {
			return false;
		}else {
			User u  = (User) o;
			return userName.equals(u.userName);
		}
	}
	

	public String toString () {
		return userName;
	}
	
	
}
