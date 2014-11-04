package com.gps.itunes.lib.types;

/**
 * LibraryObject of type Array
 * 
 * @author leogps
 *
 */
public class Array extends LibraryObject {
	
	/**
	 * Creates {@link Array} object with the specified parent object
	 * 
	 * @param parent
	 */
	public Array(LibraryObject parent){
		super(Type.ARRAY, parent);
	}

}
