package com.gps.itunes.lib.parser.utils;

import java.util.Calendar;

/**
 * Simple Utility class to check processing time.
 * 
 * @author leogps
 *
 */
public class ProcessesingTimeCheck {

	private Long startTime;
	private Long endTime;

	private static final int invSecs = 1000;
	private static final int invMins = 60 * invSecs;
	private static final int invHrs = 60 * invMins;

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ProcessesingTimeCheck.class);

	/**
	 * Set initial time
	 * 
	 */
	public void setANow() {
		startTime = Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * Set final time
	 */
	public void setBNow() {
		endTime = Calendar.getInstance().getTimeInMillis();
	}

	/** 
	 * Print time taken
	 */
	public void printTimeTaken() {
		if (startTime == null || endTime == null) {
			log.debug("Either start time or end time not set");
		} else {
			log.debug(parseTime(endTime - startTime));
		}
	}

	private String parseTime(long time) {
		final StringBuffer buffer = new StringBuffer();

		buffer.append("Elapsed Time: (HH:MM:SS.millis) ");
		buffer.append(time / invHrs);
		buffer.append(":" + time / invMins);
		buffer.append(":" + time / invSecs);
		buffer.append("." + time);

		return buffer.toString();
	}
}
