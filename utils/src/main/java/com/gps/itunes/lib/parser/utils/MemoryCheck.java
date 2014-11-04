package com.gps.itunes.lib.parser.utils;

/**
 * Memory Checker
 * 
 * @author leogps
 *
 */
public class MemoryCheck {
	private static final int mb = 1024 * 1024;
	private static final int kb = 1024;

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(MemoryCheck.class);

	/**
	 * Prints used memory information. 
	 * 
	 */
	public static void printUsedMemoryInfo() {
		final Runtime runtime = Runtime.getRuntime();
		log.debug("Used memory (in MB): "
				+ (runtime.totalMemory() - runtime.freeMemory()) / mb);

		log.debug("Used memory (in KB): "
				+ (runtime.totalMemory() - runtime.freeMemory()) / kb);
	}
}
