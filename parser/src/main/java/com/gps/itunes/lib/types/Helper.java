package com.gps.itunes.lib.types;

/**
 * Library objects' helper class.
 * 
 * @author leogps
 *
 */
public class Helper {
	

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
		System.out.println(name);
		return null;
	}

}
