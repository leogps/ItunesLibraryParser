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
    public static final String VERSION = "2.0.0";
    private static boolean inited = false;
	private static final String APP_INFO = "\n" +
		   "  ________      __              _________    " 		+ "\n"  +
           "    ! !        !  !            !  !       \\ " 	    + "\n"  +  
           "    ! !        !  !            !  !        | "      + "\n"  +   
           "    ! !        !  !            !  !````````  "      + "\n"  +     
           "    ! !        !  !            !  !          "      + "\n"  +               
           "    ! !        !  !            !  !          "    	+ "\n"  +  
           " `````````      ```````````    ````          "      + "\n"  +       
           "           ItunesLibraryParser v" + VERSION;

    private static LogInitializer instance;
    private static final Object lock = new Object();

    public static LogInitializer getInstance() {
        if(instance == null) {
            synchronized (lock) {
                if(instance == null) {
                    instance = new LogInitializer();
                }
            }
        }
        return instance;
    }

    private LogInitializer() {
        init();
    }

	static {

		BasicConfigurator.configure();

		String log4jPath = new File("").getAbsolutePath() + File.separator + "log4j.properties";
		if(FileUtils.checkFileExistence(log4jPath)) {
			PropertyConfigurator.configure(log4jPath);
		}

		log = Logger.getLogger(LogInitializer.class);
	}

	private static void init() {
        if(!inited) {
            log.debug("Configured logging utility.");
            log.debug(APP_INFO);
            inited = true;
        }
	}

}
