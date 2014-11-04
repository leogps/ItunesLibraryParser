package com.gps.itunes.lib.types;

import com.gps.itunes.lib.exceptions.NoChildrenException;

/**
 * LibraryObject of type String
 * 
 * @author leogps
 *
 */
public class LString extends LibraryObject {

	/**
	 * Creates {@link LString} object with the specified parent object
	 * 
	 * @param parent
	 */
	public LString(final LibraryObject parent) {
		super(Type.STRING, parent);
	}

	/**
	 * Converts and returns this value as String
	 * 
	 * @return {@link String}
	 * @throws com.gps.itunes.lib.exceptions.NoChildrenException
	 */
	public String getValue() throws NoChildrenException {
		return ((TextValue) getChildren()[0]).getTextValue();
	}
}
