package com.gps.itunes.lib.types;

import com.gps.itunes.lib.exceptions.NoChildrenException;

/**
 * LibraryObject of type 'True'
 * 
 * @author leogps
 *
 */
public class True extends LibraryObject {

	/**
	 * Creates the {@link True} object with the specified parent object.
	 * 
	 * @param parent
	 */
	public True(LibraryObject parent) {
		super(Type.TRUE, parent);
	}
	
	/**
	 * Converts into boolean and returns it.
	 * 
	 * @return boolean
	 * @throws com.gps.itunes.lib.exceptions.NoChildrenException
	 */
	public boolean getValue() throws NoChildrenException {
		return true;
	}

}
