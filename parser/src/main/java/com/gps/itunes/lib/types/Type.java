package com.gps.itunes.lib.types;

/**
 * Enum representing all the different types of elements in the Itunes Library
 * XML specified in the DTD file.
 * 
 * @author leogps
 * 
 */
public enum Type {

	PLIST("plist"), DICT("dict"), KEY("key"), STRING("string"), INTEGER(
			"integer"), DATE("date"), TRUE("true"), FALSE("false"), ARRAY(
			"array"), DATA("data"), REAL("real"), TEXTVALUE("(text)");

	Type(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private final String name;
}
