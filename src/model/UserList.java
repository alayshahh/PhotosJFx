package model;

import java.io.*;
import java.util.ArrayList;
/**
 * 
 * @author Alay Shah
 * @author Anshika Khare
 *
 */

public class UserList implements Serializable {

	
	static final long serialVersionUID = 1L;
	public static final String storDir = "dat";
	public static final String storeFile = "users.dat";
	
	
	ArrayList<User> allUsers = new ArrayList<>();
	
	public void addUser(User me) {
		allUsers.add(me);
	}
	
	public void removeUser(User me) {
		allUsers.remove(me);
	}
	
	public boolean contains(User me) {
		return allUsers.contains(me);
	}
	

}

