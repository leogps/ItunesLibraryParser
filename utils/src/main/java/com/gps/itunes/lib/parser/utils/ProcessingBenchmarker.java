package com.gps.itunes.lib.parser.utils;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple Utility class to check processing time.
 *
 * @author leogps
 *
 */
public class ProcessingBenchmarker {

	private Long startTime;
	private Long endTime;

	private static final int invSecs = 1000;
	private static final int invMins = 60 * invSecs;
	private static final int invHrs = 60 * invMins;

	private static final Logger LOGGER = Logger
			.getLogger(ProcessingBenchmarker.class.getName());

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
			LOGGER.log(Level.FINE, "Either start time or end time not set");
		} else {
			LOGGER.log(Level.FINE, parseTime(endTime - startTime));
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
