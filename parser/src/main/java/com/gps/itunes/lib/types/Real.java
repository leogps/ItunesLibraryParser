package com.gps.itunes.lib.types;

/**
 * LibraryObject of type Real
 * 
 * @author leogps
 *
 */
public class Real extends LibraryObject {

	/**
	 * Creates {@link Real} object with the specified parent obect.
	 * 
	 * @param parent
	 */
	public Real(LibraryObject parent) {
		super(Type.REAL, parent);
	}
}
