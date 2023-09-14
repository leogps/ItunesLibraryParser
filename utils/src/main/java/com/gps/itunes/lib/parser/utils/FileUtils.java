package com.gps.itunes.lib.parser.utils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by leogps on 10/3/15.
 */
public class FileUtils {

    private static final String WINDOWS_DRIVE_SEPARATOR = ":";

    public static boolean checkFileExistence(String filePath) {
        try {
            checkFileExistenceThrowable(filePath);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void checkFileExistenceThrowable(String filePath) throws FileNotFoundException {
        if(filePath == null) {
            throw new RuntimeException("Properties file path is null");
        }
        String prefix = "";
        if(checkToAddPrefix(filePath)) {
            prefix = new File("").getAbsolutePath();
        }
        String fullFilePath = String.format("%s%s%s", prefix, File.separator, filePath);
        File file = new File(fullFilePath);
        if (!file.exists()) {
            throw new FileNotFoundException(fullFilePath);
        }
    }

    private static boolean checkToAddPrefix(String filePath) {
        return
                (!OSInfo.isOSWin() && !filePath.startsWith(File.separator)) ||
                        (OSInfo.isOSWin() && !filePath.contains(WINDOWS_DRIVE_SEPARATOR));
    }
}
