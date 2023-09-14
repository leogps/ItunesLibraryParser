package com.gps.itunes.lib.parser.utils;

import java.util.logging.Logger;

/**
 * Memory Checker
 *
 * @author leogps
 *
 */
public class MemoryCheck {
	private static final int mb = 1024 * 1024;
	private static final int kb = 1024;

	private static final Logger LOGGER = Logger.getLogger(MemoryCheck.class.getName());

	/**
	 * Prints used memory information.
	 *
	 */
	public static void printUsedMemoryInfo() {
		final Runtime runtime = Runtime.getRuntime();
		LOGGER.info("Used memory (in MB): "
				+ (runtime.totalMemory() - runtime.freeMemory()) / mb);

		LOGGER.info("Used memory (in KB): "
				+ (runtime.totalMemory() - runtime.freeMemory()) / kb);
	}
}
