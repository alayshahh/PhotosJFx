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
	//TODO Search by date
	
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
	 * @see Photo
	 */
	public Album(String name) {
		this.name = name;
	}
	
	/**
	 * Creates Album from list of Photos and given Title. Used for search results.
	 * @param name
	 * @param photos
	 * @see Photo
	 */
	public Album(String name, ArrayList<Photo>photos) {
		this.name = name;
		this.photos = photos;
	}
	
	/**
	 * Adds Photo me to Album
	 * @param me
	 * @see Photo
	 * 
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
	 * Returns list of Photos that have the searched tag 
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
	
	/**
	 * Returns List of Photos that have current tag. Use for AND search
	 * @param photos
	 * @param t2
	 * @return
	 * @see Photo
	 * @see Tag
	 */
	private ArrayList<Photo> searchTag(ArrayList<Photo> photos, Tag t2){
		for(Photo photo:photos) {
			if(!photo.hasTag(t2)) {
				photos.remove(photo);
			}
		}
		return photos;
	}
	
	/**
	 * Tag Search for AND and OR 
	 * @param t
	 * @param t2
	 * @param isAnd
	 * @return
	 */
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
	
	
	/**
	 * Returns true if the object is an Album and the name matches the current album name
	 */
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Album)||o==null){
			return false;
		}else {
			Album a = (Album) o;
			return this.name.equals(a.name);
		}
	}
}
