package com.gps.itunes.lib.types;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Library objects' helper class.
 *
 * @author leogps
 *
 */
public class Helper {

	private static final Logger LOGGER = Logger
			.getLogger(Helper.class.getName());


	/**
	 * Returns the Type of playlist
	 *
	 * @param name
	 * @return {@link Type}
	 */
	public static Type getPlaylistType(final String name) {
		for (final Type type : LibraryObject.TYPE_ARRAY) {
			if (name.equalsIgnoreCase(type.getName())) {
				return type;
			}
		}
		LOGGER.log(Level.FINE, "Playlist type could not be found for: " + name);
		return null;
	}

}
