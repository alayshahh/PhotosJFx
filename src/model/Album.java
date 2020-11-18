package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	Date minDate = new Date(Long.MAX_VALUE);
	Date maxDate = new Date(0);
	
	/**
	 * Creates new Album of with the title name
	 * @param name name of album
	 * @see Photo
	 */
	public Album(String name) {
		this.name = name;
	}
	
	/**
	 * Creates Album from list of Photos and given Title. Used for search results.
	 * @param name name of album
	 * @param photos List of photos that belong in album
	 * @see Photo
	 */
	public Album(String name, ArrayList<Photo>photos) {
		this.name = name;
		this.photos = photos;
		updateDateRange();
	}
	
	/**
	 * Gets list of all photos.
	 * @return list of photos in album
	 */
	public ArrayList<Photo> getPhotos(){
		return photos;
	}
	
	/**
	 * Adds Photo me to Album
	 * @param me Photo to be added
	 * @see Photo
	 * 
	 */
	public void addPhoto(Photo me) {
		if(me.getDateObj().before(minDate)) {
			minDate=me.getDateObj();
		}
		if(me.getDateObj().after(maxDate)) {
			maxDate = me.getDateObj();
		}
		photos.add(me);
	}
	
	/**
	 * Renames Album to name
	 * @param name New name of Album
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
		updateDateRange();
	}
	
	/**
	 * Returns true if the album contains Photo me
	 * @param me Photo
	 * @return true if photo is in album
	 * @see Photo
	 */
	public boolean contains(Photo me) {
		return photos.contains(me);
	}
	
	/**
	 * Returns list of Photos that have the searched tag 
	 * @param t Tag to be searched for
	 * @return list of photos with tag t
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
		ArrayList<Photo> toRemove = new ArrayList<>();
		for(Photo photo:photos) {
			if(!photo.hasTag(t2)) {
				toRemove.add(photo);
			}
		}
		return toRemove;
	}
	
	/**
	 * Tag Search for AND and OR 
	 * @param t Tag one
	 * @param t2 Tag two
	 * @param isAnd Conjunction or Disjunction
	 * @return List of photos that fit criteria
	 */
	public ArrayList<Photo> searchTag(Tag t, Tag t2, boolean isAnd){
		ArrayList<Photo> result = searchTag(t);
		if(!isAnd) {
			result.addAll(searchTag(t2));
			return result;
		}else {
			ArrayList<Photo> toRemove = searchTag(result, t2);
			result.removeAll(toRemove);
			return result;
		}
	}
	
	/**
	 * Returns arrayList of photos that are in the given range
	 * @param min Minimum Date
	 * @param max Max Date
	 * @return List of photos in date range
	 */
	public ArrayList<Photo> searchDate(Date min, Date max){
		ArrayList<Photo> res = new ArrayList<>();
		if(min.after(maxDate) || max.before(minDate)) {
			return res;
		}
		for (Photo p: photos) {
			if(p.getDateObj().equals(min)|| p.getDateObj().equals(max)) {
				res.add(p);
			}else if(p.getDateObj().after(min) && p.getDateObj().before(max)) {
				res.add(p);
			}
		}
		return res;
	}
	
	
	/**
	 * Returns a string that has the maxDate and minDate of the album
	 * @return String that has the date Range
	 */
	public String getDateRange() {
		if(photos.size()==0) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		return sdf.format(minDate)+" to "+sdf.format(maxDate);
	}
	
	/**
	 * Returns number of photos in album.
	 * @return number of photos in album
	 */
	public String getNumPhotos() {
		return ""+photos.size();
	}
	
	/**
	 * Returns album name
	 * @return Album name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Updates date range of album after photo is deleted
	 */
	private void updateDateRange() {
		maxDate = new Date(0);
		minDate = new Date(Long.MAX_VALUE);
		for(Photo p: photos) {
			if(p.getDateObj().after(maxDate)) {
				maxDate = p.getDateObj();
			}
			if(p.getDateObj().before(minDate)) {
				minDate = p.getDateObj();
			}
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
	
	/**
	 * Gives next image in order. Used for slide show purposes.
	 * @param p current photo
	 * @return Photo that comes after current
	 */
	public Photo getNext(Photo p) {
		int i = photos.indexOf(p);
		if(i==photos.size()-1) {
			i=0;
		}else i++;
		return photos.get(i);
		
	}
	/**
	 * Gives previous image in order. Used for slideshow purposes.
	 * @param p current photo
	 * @return image before current
	 */
	public Photo getPrev(Photo p) {
		int i = photos.indexOf(p);
		if(i==0) {
			i=photos.size()-1;
		}else i--;
		return photos.get(i);
		
	}

	/**
	 * @return string in format of Album
	 * 
	 */
	@Override
	public String toString() {
		if(photos.size()==0) {
			return name+" * 0 * *";
		}else {
			return name+ " * "+ photos.size()+ " * " + getDateRange() + " * ";
		}
	}
	
}
