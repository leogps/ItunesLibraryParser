package com.gps.itunes.lib.parser.utils;

import java.util.logging.Logger;

/**
 * Logging Utility initializer
 *
 * @author leogps
 *
 */
public class BannerPrinter {

    private static final Logger LOGGER = Logger.getLogger(BannerPrinter.class.getName());
    public static final String VERSION = "2.0.1";
	private static final String APP_INFO = "\n" +
		   "  ________      __              _________    " 		+ "\n"  +
           "    ! !        !  !            !  !       \\ " 	    + "\n"  +
           "    ! !        !  !            !  !        | "      + "\n"  +
           "    ! !        !  !            !  !````````  "      + "\n"  +
           "    ! !        !  !            !  !          "      + "\n"  +
           "    ! !        !  !            !  !          "    	+ "\n"  +
           " `````````      ```````````    ````          "      + "\n"  +
           "           ItunesLibraryParser v" + VERSION;

	public static void print() {
        LOGGER.info(APP_INFO);
	}
}
