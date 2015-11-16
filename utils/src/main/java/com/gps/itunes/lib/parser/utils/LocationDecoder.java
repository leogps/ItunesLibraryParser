package com.gps.itunes.lib.parser.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by leogps on 11/15/15.
 */
public class LocationDecoder {

    private static final Logger log = Logger.getLogger(LocationDecoder.class.getName());

    public static String decodeLocation(String location) {
        if(location != null) {
            try {
                URL url = new URL(location);
                return URLDecoder.decode(url.getPath(), "UTF-8");
            } catch (MalformedURLException e) {
                log.log(Level.WARNING, "Could not decode location.", e);
            } catch (UnsupportedEncodingException e) {
                log.log(Level.WARNING, "Could not decode location.", e);
            }
        }
        return location;
    }

}
