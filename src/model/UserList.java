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
	
	static UserList userList = new UserList();
	
	static final long serialVersionUID = 1L;
	public static final String storDir = "dat";
	public static final String storeFile = "users.dat";
	
	
	ArrayList<User> allUsers = new ArrayList<>();
	
	/**
	 * Takes in User and add it to the list
	 * @param me
	 */
	public void addUser(User me) {
		allUsers.add(me);
	}
	
	/**
	 * Removes given user from the list
	 * @param me
	 */
	public void removeUser(User me) {
		allUsers.remove(me);
	}
	
	/**
	 * Tells if the current user is in the list
	 * @param me
	 * @return
	 */
	public boolean contains(User me) {
		return allUsers.contains(me);
	}
	
	/**
	 * Tells if a user with the current username is in the list
	 * @param userName
	 * @return
	 */
	public boolean contains(String userName) {
		for(User u: allUsers) {
			if(userName.equals(u.getUserName())) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Tells if userList is empty
	 * @return
	 */
	public boolean isEmpty() {
		return allUsers.isEmpty();
	}
	
	/**
	 * returns the user based on the given Username
	 * @param userName
	 * @return
	 */
	public User getUser(String userName) {
		for(User u: allUsers) {
			if(u.getUserName().equals(userName)) {
				return u;
			}
		}
		return null;
	}
	
	
	public void writeApp() throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storDir + File.separator + storeFile));
		oos.writeObject(userList);
		oos.close();
	}
	
	public void readApp() throws IOException, ClassNotFoundException{
		ObjectInputStream  ois = new ObjectInputStream( new FileInputStream(storDir+File.separator+storeFile));
		userList = (UserList) ois.readObject();
		ois.close();
		
	}
	public static UserList getUserList() {
		return userList;
	}
}

