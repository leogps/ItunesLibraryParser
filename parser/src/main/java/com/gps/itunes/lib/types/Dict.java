package com.gps.itunes.lib.types;

import java.util.HashMap;
import java.util.Map;

/**
 * LibraryObject of Type Dict
 * 
 * @author leogps
 *
 */
public class Dict extends LibraryObject {

	private Map<Key, LibraryObject> dictMap = new HashMap<Key, LibraryObject>();

	/**
	 * Creates {@link Dict} object with the specified parent object
	 * 
	 * @param parent
	 */
	public Dict(final LibraryObject parent) {
		super(Type.DICT, parent);
	}

	/**
	 * Puts the LibraryObject into the Library Dictionary. <br />
	 * Puts the value into a {@link Map} with key as {@link Key} and value as
	 * {@link LibraryObject} <br />
	 * Returns the value returned by {@link Map#put(Key, LibraryObject)} method.
	 * 
	 * @param key
	 * @param value
	 * @return {@link LibraryObject}
	 */
	public LibraryObject put(final Key key, final LibraryObject value) {
		return dictMap.put(key, value);
	}

}
