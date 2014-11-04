package com.gps.itunes.lib.types;

import com.gps.itunes.lib.exceptions.NoChildrenException;

/**
 * LibraryObject of type False.
 * 
 * @author leogps
 *
 */
public class False extends LibraryObject {

	/**
	 * Creates {@link False} object with the specified parent object
	 * 
	 * @param parent
	 */
	public False(LibraryObject parent) {
		super(Type.FALSE, parent);
	}

	/**
	 * Converts this object into boolean and returns it.
	 * 
	 * @return boolean
	 * @throws com.gps.itunes.lib.exceptions.NoChildrenException
	 */
	public boolean getValue() throws NoChildrenException {
		return false;
	}
}
