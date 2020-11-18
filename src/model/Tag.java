package model;

import java.io.Serializable;



/**
 * @author Alay Shah
 * @author Anshika Khare
 *
 */
public class Tag implements Serializable{
	static final long serialVersionUID = 1L;
	
	private String key;
	private String value;
	
	/**
	 * Creates tag with a  key/value
	 * @param key Tag Arrtibute
	 * @param value Tag value
	 */
	public Tag(String key, String value) {
		this.key=key.trim().toUpperCase();
		this.value=value.trim().toUpperCase();
	}
	
	/**
	 * Returns true if o is a Tag and is equal to current tag
	 * 
	 * @param o Object to be checked for equality
	 * @return
	 * 
	 */
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Tag )||o ==null) {
			return false;
		}else {
			Tag t = (Tag) o;
			return (this.value.equals(t.value))&& (this.key.equals(t.key));
		}
	}
	
	public String toString() {
		return this.key +"="+ this.value;
	}
	
	/**
	 * Checks if a certain key,value pair is equal to a tag object
	 * @param key Tag Attribute
	 * @param value Tag Value
	 * @return true if key and value match
	 */
	public boolean equals(String key, String value) {
		return(this.key.equals(key.trim().toUpperCase()))&&(this.value.equals(value.trim().toUpperCase()));
	}
	/**
	 * Returns tag value
	 * @return value
	 */
	public String getValue() {
		return this.value;
	}
	/**
	 * Returns Tag key
	 * @return key
	 */
	public String getKey() {
		return this.key;
	}
	
	
	
	
}
