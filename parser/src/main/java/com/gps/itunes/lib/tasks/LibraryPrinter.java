package com.gps.itunes.lib.tasks;

import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.types.LibraryObject;
import com.gps.itunes.lib.types.TextValue;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Prints the entire Library. Find the code in here to traverse the library.
 *
 * @author leogps
 *
 */
public class LibraryPrinter {

	private final LibraryObject libObject;

	private static final Logger LOGGER = Logger
			.getLogger(LibraryPrinter.class.getName());

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
			LOGGER.log(Level.FINE, "Root - " + object.getType() + "[");
		}
		try {
			for (final LibraryObject obj : object.getChildren()) {
				LOGGER.log(Level.FINE, obj.getType().toString());
				if (obj.hasChildren()) {
					LOGGER.log(Level.FINE, "Children{");
					doPrintLibrary(obj);
					LOGGER.log(Level.FINE, "}");
				} else if (obj instanceof TextValue) {
					TextValue textVal = (TextValue) obj;
					LOGGER.log(Level.FINE, "#cData[" + textVal.getTextValue() + "]");
				}
			}
		} catch (NoChildrenException e) {
			LOGGER.log(Level.SEVERE, "(null)", e);
		}

	}
}
