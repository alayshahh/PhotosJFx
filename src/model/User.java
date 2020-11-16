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
	 * @param userName
	 */
	public User(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Returns the Albums that the user has
	 * @return
	 */
	public ArrayList<Album> getAlbums(){
		return this.albums;
	}
	
	/**
	 * Adds the Album to the user's album list
	 * 
	 * @param alb
	 */
	public void addAlbum(Album alb) {
		this.albums.add(alb);
	}
	
	/**
	 * Deletes the album from the user's album list
	 * 
	 * @param alb
	 */
	public void deleteAlbum(Album alb){
		this.albums.remove(alb);
	}
	
	
	/**
	 * Returns true if the user already has an album with the same name
	 * @param alb
	 * @return
	 */
	public boolean hasAlbum(Album alb) {
		return albums.contains(alb);
	}
	
	
	/**
	 * Returns the userName of the User
	 * @return
	 */
	public String getUserName() {
		return this.userName;
	}
	
	
	/**
	 *Returns true if Object is a User and username equals current username
	 */
	@Override 
	public boolean equals(Object o) {
		if(!(o instanceof User)|| o==null) {
			return false;
		}else {
			User u  = (User) o;
			return userName.equals(u.userName);
		}
	}
	
	
	
}
