package com.gps.itunes.lib.exceptions;

import java.io.FileNotFoundException;

/**
 * Wrapper class that represents Exception in Library Parsing.
 *  
 * @author leogps
 */
public class LibraryParseException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static boolean isLibraryFileNotFound;
	private static final String preErrorMsg = "Is the error because the library file could not be found: ";

	public LibraryParseException(final Exception ex) {
		super(
				preErrorMsg
						+ (LibraryParseException.isLibraryFileNotFound = ex instanceof FileNotFoundException),
				ex);

	}

	public LibraryParseException(final String msg, final Exception ex) {
		super(
				msg
						+ "; "
						+ preErrorMsg
						+ (LibraryParseException.isLibraryFileNotFound = ex instanceof FileNotFoundException),
				ex);
	}

	public boolean isLibraryFileNotFound() {
		return isLibraryFileNotFound;
	}

}
