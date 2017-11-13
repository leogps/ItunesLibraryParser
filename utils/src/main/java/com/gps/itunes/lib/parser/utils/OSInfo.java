package com.gps.itunes.lib.parser.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class to retrieve Operating system info.
 * 
 * @author leogps
 *
 */
public class OSInfo {
	
	private final static String OS_NAME = "os.name";
        private final static String OS_ARCH = "os.arch";
	
    /**
     * Checks if the current machine is a Mac
     * 
     * @return boolean
     */
	public static boolean isOSMac() {
		return StringUtils.containsIgnoreCase(System.getProperty(OS_NAME), "Mac");
	}

	/**
	 * Checks if the current machine is a Windows machine.
	 * 
	 * @return
	 */
	public static boolean isOSWin() {
		return StringUtils.containsIgnoreCase(System.getProperty(OS_NAME), "Windows");
	}
        
	/**
	 * Checks if the System is a 64 bit machine.
	 * 
	 * @return boolean
	 */
	public static boolean isArch64() {
		return StringUtils.containsIgnoreCase(System.getProperty(OS_ARCH), "x86_64" ) ||
				StringUtils.containsIgnoreCase(System.getProperty(OS_ARCH), "amd64" );
	}

	/**
	 * Checks if the System is a 32 bit machine.
	 * 
	 * @return boolean
	 */
	public static boolean isArch32() {
		return !isArch64();
	}

	public static String getOsPrefix() {
		return isOSMac() ? "mac" : "win";
	}
        
}
