package com.gps.itunes.lib.types;

import com.gps.itunes.lib.exceptions.NoChildrenException;

/**
 * LibraryObject of type 'KEY'
 * 
 * @author leogps
 *
 */
public class Key extends LibraryObject {

	private LibraryObject keyValue;

	/**
	 * Creates {@link Key} object with the specified parent
	 * 
	 * @param parent
	 */
	public Key(final LibraryObject parent) {
		super(Type.KEY, parent);
	}

	@Override
	public boolean isKey() {
		return true;
	}

	/**
	 * Gets the value of this key
	 * 
	 * @return {@link LibraryObject}
	 */
	public LibraryObject getKeyValue() {
		return keyValue;
	}

	/**
	 * Sets the value of this key
	 * 
	 * @param keyValue
	 */
	public void setKeyValue(LibraryObject keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * Gets the name of the Key
	 * 
	 * @return
	 * @throws com.gps.itunes.lib.exceptions.NoChildrenException
	 */
	public String getKeyName() throws NoChildrenException {
		return ((TextValue) getChildren()[0]).getTextValue();
	}

}
