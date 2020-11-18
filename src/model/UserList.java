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
	public static final String storDir = "data";
	public static final String storeFile = "users.dat";
	
	
	ArrayList<User> allUsers = new ArrayList<>();
	
	/**
	 * Takes in User and add it to the list
	 * @param me User to be added
	 */
	public void addUser(User me) {
		allUsers.add(me);
	}
	
	/**
	 * Removes given user from the list
	 * @param me User to be removed
	 */
	public void removeUser(User me) {
		allUsers.remove(me);
	}
	
	/**
	 * Tells if the current user is in the list
	 * @param me User to be checked
	 * @return true if is in UserList
	 */
	public boolean contains(User me) {
		return allUsers.contains(me);
	}
	
	/**
	 * Tells if a user with the current username is in the list
	 * @param userName userName of User to be checked
	 * @return true if is contained
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
	 * @return true if UserList is empty
	 */
	public boolean isEmpty() {
		return allUsers.isEmpty();
	}
	
	/**
	 * returns the user based on the given Username
	 * @param userName UserName of User to be obtained
	 * @return  User that has the same username
	 */
	public User getUser(String userName) {
		for(User u: allUsers) {
			if(u.getUserName().equals(userName)) {
				return u;
			}
		}
		return null;
	}
	
	
	 
	/**
	 * Writes to file
	 * @throws IOException if file does not exist
	 */
	public void writeApp() throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storDir + File.separator + storeFile));
		oos.writeObject(userList);
		oos.close();
	}
	

	/**
	 * Reads from file
	 * @throws IOException if file not found
	 * @throws ClassNotFoundException if class is not found
	 */
	public void readApp() throws IOException, ClassNotFoundException{
		ObjectInputStream  ois = new ObjectInputStream( new FileInputStream(storDir+File.separator+storeFile));
		userList = (UserList) ois.readObject();
		ois.close();
		
	}
	/**
	 * Gives current UserList
	 * @return Current UserList
	 */
	public static UserList getUserList() {
		return userList;
	}
	
	/**
	 * Gives the full list of current users
	 * @return list of Users in UserList
	 */
	public ArrayList<User> getAll(){
		return allUsers;
	}
	
	
}

