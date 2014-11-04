package com.gps.itunes.lib.types;

/**
 * LibraryObject of type KeyValue
 * 
 * @author leogps
 *
 */
public class KeyValue extends LibraryObject {

	private final Key key;

	/**
	 * Creates the {@link KeyValue} object with the corresponding Key, type and
	 * parent objects
	 * 
	 * @param key
	 * @param type
	 * @param parent
	 */
	public KeyValue(final Key key, final Type type, final LibraryObject parent) {
		super(type, parent);
		this.key = key;
	}

	/**
	 * Gets the key associated with this KeyValue object.
	 * 
	 * @return {@link Key}
	 */
	public Key getKey() {
		return key;
	}

}
