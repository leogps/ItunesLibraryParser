package com.gps.itunes.lib.exceptions;

import com.gps.itunes.lib.types.LibraryObject;

/**
 * Thrown when a child element is accessed when the {@link LibraryObject} does
 * not contain a child.
 * 
 * @author leogps
 * 
 */
public class NoChildrenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoChildrenException(final String message) {
		super(message);
	}

}
