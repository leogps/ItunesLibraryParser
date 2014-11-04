package com.gps.itunes.lib.types;

import com.gps.itunes.lib.exceptions.NoChildrenException;

/**
 * LibraryObject of type TextValue. <br />
 * This object does not have any children. Like CDATA in XML.
 * 
 * @author leogps
 * 
 */
public class TextValue extends LibraryObject {
	
	private String textValue;

	/**
	 * Creates the {@link TextValue} object with the specified parent and the
	 * value of text.
	 * 
	 * @param parent
	 * @param textValue
	 */
	public TextValue(LibraryObject parent, String textValue) {
		super(Type.TEXTVALUE, parent);
		this.textValue = textValue;
		setChildren((LibraryObject[]) null);
	}
	
	/**
	 * Returns the text value
	 * 
	 * @return {@link String}
	 */
	public String getTextValue(){
		return textValue;
	}

	@Override
	public LibraryObject[] getChildren() throws NoChildrenException {
		throw new NoChildrenException("Cannot have children to the plain text.");
	}
	
	@Override
	public boolean hasChildren(){
		return false;
	}

}
