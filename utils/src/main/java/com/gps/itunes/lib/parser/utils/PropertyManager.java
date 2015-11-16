package com.gps.itunes.lib.parser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Utility class for reading and accessing app properties.
 *
 * Attempts to read properties file from
 * <strong>${currentDirectory}/config/mac-app.properties</strong> for OSX and
 * <strong>${currentDirectory}/config/win-app.properties</strong> for Windows.
 *
 * <br/>
 *
 * If a property is found in the System.Properties, it will take higher precedence over the property read from the properties file.
 *
 * @author leogps
 *
 */
public class PropertyManager {

    private static final Logger log = Logger.getLogger(PropertyManager.class.getName());

    private static final Map<String, String> CONFIGURATION_MAP = new HashMap<String, String>();
    static {
        readPropertiesFromFile(CONFIGURATION_MAP);
    }

    public static Map<String, String> getConfigurationMap() {
        return CONFIGURATION_MAP;
    }

    public enum Property {

        LIBRARY_FILE_LOCATION_PROPERTY("itunes.xml.file", "~/Music/iTunes/iTunes Music Library.xml"),

        DEFAULT_LIBRARY_FILE_HOME_DIR_SUFFIX("user.home.itunes.xml.file", "Music/iTunes/iTunes Music Library.xml"),

        /**
         * local.dtd.file -->  Path to the dtd file stored locally.
         * <br/>
         * If this property is not found, the dtd url described in the XML file is used to parse the XML. The URL is typically
         * http://www.apple.com/DTDs/PropertyList-1.0.dtd and in order to parse the XML without having the local dtd file or the
         * property set for it, the application needs have valid access to the link http://www.apple.com/DTDs/PropertyList-1.0.dtd.
         *
         */
        ITUNES_LOCAL_DTD_FILE_PROPERTY("local.dtd.file", "/config/PropertyList-1.0.dtd"),

        ITUNES_DEFAULT_XML_DTD_URL("itunes.xml.dtdUrl", "http://www.apple.com/DTDs/PropertyList-1.0.dtd"),

        MAC_PROPERTIES_FILE_PROPERTY("ilp.mac.properties.file", "/config/mac-app.properties"),

        WIN_PROPERTIES_FILE_PROPERTY("ilp.win.properties.file", "/config/win-app.properties");

        private final String key;
        private final String defaultValue;

        Property(String key, String defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public String getKey() {
            return key;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        @Override
        public String toString() {
            return name();
        }
    }

	public static void readPropertiesFromFile(Map<String, String> configurationMap) {
        String propertiesFilePath = null;

		if (OSInfo.isOSWin() && configurationMap.containsKey(Property.WIN_PROPERTIES_FILE_PROPERTY)) {
            propertiesFilePath = configurationMap.get(Property.WIN_PROPERTIES_FILE_PROPERTY);
		} else if(OSInfo.isOSMac() && configurationMap.containsKey(Property.MAC_PROPERTIES_FILE_PROPERTY)){
            propertiesFilePath = configurationMap.get(Property.MAC_PROPERTIES_FILE_PROPERTY);
		}

        if(propertiesFilePath == null) {
            propertiesFilePath = "config" + File.separator + OSInfo.getOsPrefix() + "-app.properties";
        }
        if(!propertiesFilePath.startsWith(File.separator)) {
            propertiesFilePath = new File("").getAbsolutePath() + File.separator + propertiesFilePath;
        }

        if(FileUtils.checkFileExistence(propertiesFilePath)) {
            log.info("Properties file found: " + propertiesFilePath);

            try {

                Properties propertyFileProperties = new Properties();
                propertyFileProperties.load(new FileInputStream(propertiesFilePath));

                for(Object keyObj : propertyFileProperties.keySet()) {
                    String key = (String) keyObj;
                    if(!configurationMap.containsKey(key)) {
                        configurationMap.put(key, propertyFileProperties.getProperty(key));
                        log.info(String.format("Using Property from the properties file: {%s : %s}", key, propertyFileProperties.getProperty(key)));
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            log.info("Properties file not found: " + propertiesFilePath);
            log.info("Properties file needs to be placed at ${currentDirectory}/config/mac-app.properties for OSX and " +
                    "${currentDirectory}/config/win-app.properties for Windows.");
            log.info("${currentDirectory} represents the invocation directory.");

            log.info("Asserting the information is passed through System.Properties.");
        }

        // If library file location is not set, use default location
        if(!configurationMap.containsKey(Property.LIBRARY_FILE_LOCATION_PROPERTY.getKey())) {
            String defaultLocation = getDefaultLibraryLocation();
            configurationMap.put(Property.LIBRARY_FILE_LOCATION_PROPERTY.getKey(), defaultLocation);
        }

        if(!configurationMap.containsKey(Property.ITUNES_DEFAULT_XML_DTD_URL.getKey())) {
            configurationMap.put(Property.ITUNES_DEFAULT_XML_DTD_URL.getKey(),
                    Property.ITUNES_DEFAULT_XML_DTD_URL.defaultValue);
        }
	}

    private static String getDefaultLibraryLocation() {
        return System.getProperty("user.home") + File.separator +
                Property.DEFAULT_LIBRARY_FILE_HOME_DIR_SUFFIX.defaultValue;
    }

    private static String getUserHomeDirOrBlank() {
        String userHomeDir = System.getProperty("user.home");
        if(userHomeDir == null) {
            return "";
        }
        return userHomeDir;

    }
}
