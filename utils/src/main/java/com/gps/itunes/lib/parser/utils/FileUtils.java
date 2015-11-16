package com.gps.itunes.lib.parser.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by leogps on 10/3/15.
 */
public class FileUtils {

    public static boolean checkFileExistence(String filePath) {
        try {
            checkFileExistenceThrowable(filePath);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void checkFileExistenceThrowable(String filePath) throws FileNotFoundException {
        FileReader fr = null;
        try {
            if(filePath == null) {
                throw new RuntimeException("Properties file path is null");
            }

            String prefix = "";
            if(!filePath.startsWith(File.separator)) {
                prefix = new File("").getAbsolutePath();
            }
            fr = new FileReader(prefix + File.separator + filePath);
        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            if(fr != null) {
                IOUtils.closeQuietly(fr);
            }
        }
    }
}
