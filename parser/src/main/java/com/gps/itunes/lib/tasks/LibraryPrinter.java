package com.gps.itunes.lib.tasks;

import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.types.LibraryObject;
import com.gps.itunes.lib.types.TextValue;

/**
 * Prints the entire Library. Find the code in here to traverse the library.
 * 
 * @author leogps
 * 
 */
public class LibraryPrinter {

	private final LibraryObject libObject;
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(LibraryPrinter.class);

	/**
	 * Creates the {@link LibraryPrinter} object for the {@link LibraryObject} passed.
	 * 
	 * @param libObject
	 */
	public LibraryPrinter(final LibraryObject libObject) {
		this.libObject = libObject;
	}

	/**
	 * Prints the library.
	 */
	public void printLibrary() {
		doPrintLibrary(libObject);
	}

	private void doPrintLibrary(final LibraryObject object) {
		if (object.getParent() == null) {
			log.debug("Root - " + object.getType() + "[");
		}
		try {
			for (final LibraryObject obj : object.getChildren()) {
				log.debug(obj.getType());
				if (obj.hasChildren()) {
					log.debug("Children{");
					doPrintLibrary(obj);
					log.debug("}");
				} else if (obj instanceof TextValue) {
					TextValue textVal = (TextValue) obj;
					log.debug("#cData[" + textVal.getTextValue() + "]");
				}
			}
		} catch (NoChildrenException e) {
			log.error("(null)", e);
		}

	}
}
