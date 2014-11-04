package com.gps.itunes.lib.parser.utils;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Logging Utility initializer
 * 
 * @author leogps
 *
 */
public class LogInitializer {

	private static final Logger log;
	private static final String APP_INFO = "\n" +
		   "  ________      __              _________    " 		+ "\n"  +
           "    ! !        !  !            !  !       \\ " 	    + "\n"  +  
           "    ! !        !  !            !  !        | "      + "\n"  +   
           "    ! !        !  !            !  !````````  "      + "\n"  +     
           "    ! !        !  !            !  !          "      + "\n"  +               
           "    ! !        !  !            !  !          "    	+ "\n"  +  
           " `````````      ```````````    ````          "      + "\n"  +       
           "           ItunesLibraryParser v1.0";

	static {

		BasicConfigurator.configure();
		PropertyConfigurator.configure(new File("").getAbsolutePath()
				+ File.separator + "log4j.properties");

		log = Logger.getLogger(LogInitializer.class);
	}
	
	public LogInitializer(){
		init();
	}

	private void init() {
		log.debug("Configured logging utility.");
		log.debug(APP_INFO);
	}

}
