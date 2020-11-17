package model;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * 
 * @author Alay Shah
 * @author Anshika Khare
 *
 */

public class Photo implements Serializable {
	
	final String dir = "dat";
	final String file = "users.dat";
	
	private static final long serialVersionUID = 1L;
	
	private String filePath;
	private String date;
	private Date d;
	private String caption;
	private ArrayList<Tag> tags;
//	private ImageView image; 
	
	
	
	/**
	 * Creates photo object from the given file path
	 * @param filePath
	 * 
	 */
	public Photo(String filePath) {
		this.filePath = filePath;
		File photo = new File(filePath);
//		System.out.println((photo.lastModified()));
		System.out.println(photo.getAbsolutePath());
		d = new Date(photo.lastModified());
		date = new SimpleDateFormat("MM/dd/yyy").format(d);
		try {
			d = new SimpleDateFormat("MM/dd/yyyy").parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tags = new ArrayList<>();
		caption = "";
		
		
	}
	/**
	 * Creates Photo from file and given date
	 * @param filePath
	 * @param date
	 */
	public Photo(String filePath, String date) {
		this.filePath = filePath;
		this.date = date;
		tags= new ArrayList<>();
		caption = "";
	}
	
	/**
	 * Checks if input equals current photo
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Photo)|| o==null) {
			return false;
		}else {
			Photo p = (Photo) o;
			return p.filePath == filePath && p.getCaption() ==caption;
		}
	}
	
	/**
	 * Checks if current tag is equal to tag t
	 * @param t
	 * @return
	 */
	public boolean hasTag(Tag t) {
		for(Tag ta: tags) {
			if(ta.equals(t)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	/**
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @return
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @return
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * @param caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * @return
	 */
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	/**
	 * Adds inputed tag
	 * @param t
	 */
	public void addTag(Tag t) {
		tags.add(t);
	}
	
	/**
	 * Returns Date Object
	 * @return
	 */
	public Date getDateObj() {
		return this.d;
	}
	
	/**
	 * Returns the ImageView of the photo
	 * @return
	 */
	public ImageView getImage() {
		File photo = new File(filePath);
		String path="";
		try {
			path = photo.toURI().toURL().toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(path);
		Image i = new Image(path);
		ImageView image = new ImageView(i);
		
		image.setFitHeight(100);
		image.setFitWidth(100);
		return image;
	}
	
	public void deleteTag(Tag t) {
		tags.remove(t);
	}
	
	
	
	
	
	

}
