package com.gps.itunes.lib.parser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Utility class for reading and accessing app properties. 
 * 
 * @author leogps
 *
 */
public class PropertyManager {

	private final static String macProperties = "config" + File.separator
			+ "mac-app.properties";

	private final static String winProperties = "config" + File.separator
			+ "win-app.properties";

	private final static String userHomeDir = System.getProperty("user.home");

	private static final Properties properties = PropertyManager
			.readProperties();

	static {
		properties.put(
				"libraryFileLocation",
				(userHomeDir != null ? userHomeDir : "")
						+ properties.getProperty("inputXMLFile"));

	}

	/**
	 * Returns app specific properties
	 * 
	 * @return {@link java.util.Properties}
	 */
	public static Properties getProperties() {
		return properties;
	}

	private static Properties readProperties() {
		final String propertiesFilePath;

		if (OSInfo.isOSWin()) {
			propertiesFilePath = winProperties;
		} else {
			propertiesFilePath = macProperties;
		}

		final Properties props = new Properties();
		final String separator = File.separator;
		final String rootPath = new File("").getAbsolutePath();
		try {
			props.load(new FileInputStream(new File(rootPath + separator
					+ propertiesFilePath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return props;
	}

	/**
	 * Checks if the properties are set in the web context.
	 * 
	 * @return
	 */
	public static boolean isWebContext() {
		final String webContext = System.getProperty("ILP_WEB_CONTEXT");
		return webContext != null && webContext.equalsIgnoreCase("true");
	}

	/**
	 * Reads the properties off the web context {URL}.
	 * 
	 * @return {@link java.util.Properties}
	 */
	public static Properties readWebProperties() {

		final Properties props = new Properties();
		try {
			if (OSInfo.isOSWin()) {
				final URL winURL = new URL(
						System.getProperty("ILP_WEB_WIN_LINK"));
				props.load(winURL.openStream());
			} else {
				final URL macURL = new URL(
						System.getProperty("ILP_WEB_MAC_LINK"));
				props.load(macURL.openConnection().getInputStream());
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return props;
	}

}
