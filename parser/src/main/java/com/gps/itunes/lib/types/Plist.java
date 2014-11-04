package com.gps.itunes.lib.types;

/**
 * LibraryObject of type Plist. <br />
 * This is typically the root element.
 * 
 * @author leogps
 * 
 */
public class Plist extends LibraryObject {
	
	/**
	 * Creates a Plist object. Plist does not have a parent as it is the root
	 * element
	 */
	public Plist(){
		super(Type.PLIST, null);
	}
}
