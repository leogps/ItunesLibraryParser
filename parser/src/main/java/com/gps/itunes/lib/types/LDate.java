package com.gps.itunes.lib.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gps.itunes.lib.exceptions.NoChildrenException;

/**
 * LibraryObject of type Date
 * 
 * @author leogps
 *
 */
public class LDate extends LibraryObject {

	final static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

	/**
	 * Creates {@link LDate} object with the specified parent object.
	 * 
	 * @param parent
	 */
	public LDate(final LibraryObject parent) {
		super(Type.DATE, parent);
	}

	/**
	 * Formats and returns the value of the Date.
	 * 
	 * @return
	 * @throws com.gps.itunes.lib.exceptions.NoChildrenException
	 */
	public Date getValue() throws NoChildrenException {
		final String textVal = ((TextValue) getChildren()[0]).getTextValue();
		Date date = null;
		try {
			date = formatter.parse(textVal);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
