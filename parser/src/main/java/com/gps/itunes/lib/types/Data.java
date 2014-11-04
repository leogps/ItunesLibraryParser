package com.gps.itunes.lib.types;

/**
 * LibraryObject of TYPE.DATA
 * 
 * @author leogps
 *
 */
public class Data extends LibraryObject {

	/**
	 * Creates {@link Data} object with the specified parent object 
	 * 
	 * @param parent
	 */
	public Data(LibraryObject parent){
		super(Type.DATA, parent);
	}
}
