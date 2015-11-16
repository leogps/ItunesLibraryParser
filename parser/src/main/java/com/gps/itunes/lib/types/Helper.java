package com.gps.itunes.lib.types;

/**
 * Library objects' helper class.
 * 
 * @author leogps
 *
 */
public class Helper {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(Helper.class);


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
		log.debug("Playlist type could not be found for: " + name);
		return null;
	}

}
