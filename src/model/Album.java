package model;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 
 * @author Alay Shah
 * @author Anshika Khare
 *
 */
public class Album implements Serializable{
	
	static final long serialVersionUID = 1L;
	
	/**
	 * Album Name
	 */
	String name;
	
	/**
	 * Photos the Album Contains
	 */
	ArrayList<Photo> photos= new ArrayList<>(); 
	
	/**
	 * Creates new Album of with the title name
	 * @param name
	 */
	public Album(String name) {
		this.name = name;
	}
	
	/**
	 * Adds Photo me to Album
	 * @param me
	 * @see Photo
	 */
	public void addPhoto(Photo me) {
		photos.add(me);
	}
	
	/**
	 * Renames Album to name
	 * @param name
	 * @see Photo 
	 */
	public void rename(String name) {
		this.name = name;
	}
	
	/**
	 * Deletes the photo from the album
	 * @param me
	 * @see Photo
	 */
	public void removePhoto(Photo me) {
		photos.remove(me);
	}
	
	/**
	 * Returns true if the album contains Photo me
	 * @param me
	 * @return
	 * @see Photo
	 */
	public boolean contains(Photo me) {
		return photos.contains(me);
	}
	/**
	 * Returns list of Photos that are in 
	 * @param t
	 * @return
	 */
	public ArrayList<Photo> searchTag(Tag t){
		ArrayList<Photo> result = new ArrayList<>();
		for(Photo photo : photos) {
			if(photo.hasTag(t)) {
				result.add(photo);
			}
		}
		return result;
	}
	
	private ArrayList<Photo> searchTag(ArrayList<Photo> photos, Tag t2){
		for(Photo photo:photos) {
			if(!photo.hasTag(t2)) {
				photos.remove(photo);
			}
		}
		return photos;
	}
	
	public ArrayList<Photo> searchTag(Tag t, Tag t2, boolean isAnd){
		ArrayList<Photo> result = searchTag(t);
		if(!isAnd) {
			result.addAll(searchTag(t2));
			return result;
		}else {
			result = searchTag(result, t2);
			return result;
		}
	}
}
