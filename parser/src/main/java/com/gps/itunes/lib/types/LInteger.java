package com.gps.itunes.lib.types;

import com.gps.itunes.lib.exceptions.NoChildrenException;

/**
 * LibraryObject of type Integer
 * 
 * @author leogps
 *
 */
public class LInteger extends LibraryObject {

	/**
	 * Creates {@link LInteger} object with the specified parent object
	 * 
	 * @param parent
	 */
	public LInteger(final LibraryObject parent) {
		super(Type.INTEGER, parent);
	}

	/**
	 * Converts and returns this value.
	 * 
	 * @return long
	 * @throws com.gps.itunes.lib.exceptions.NoChildrenException
	 */
	public long getValue() throws NoChildrenException {
		return Long.valueOf(((TextValue) getChildren()[0]).getTextValue());
	}

}
