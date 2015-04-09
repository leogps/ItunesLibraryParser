package com.gps.itunes.lib.tasks;

import com.gps.itunes.lib.exceptions.FileCopyException;
import com.gps.itunes.lib.tasks.progressinfo.CopyTrackFailureInformation;
import com.gps.itunes.lib.tasks.progressinfo.CopyTrackInformation;
import com.gps.itunes.lib.tasks.progressinfo.ProgressInformation;
import com.gps.itunes.lib.tasks.progressinfo.ProgressTracker;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to copy the library files (files in general) to any
 * destination.
 * 
 * @author leogps
 * 
 */
public class FileFetcher implements Serializable {

    public static final String UTF_8 = "UTF-8";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(FileFetcher.class);

	/**
	 * Copies array of files represented by srcArray to destination.
	 * 
	 * @param srcArray
	 * @param dest
	 * @throws FileCopyException
	 */
	public static List<File> copyFiles(final String[] srcArray, final String dest)
			throws FileCopyException {
		return copyFiles(srcArray, dest, null);
	}

	/**
	 * Copies array of files represented by srcArray to destination and also
	 * informs the copy progress info i.e., {@link ProgressInformation} using
	 * the {@link ProgressInformer}
	 * 
	 * @param srcArray
	 * @param dest
	 * @throws FileCopyException
	 */
	public static List<File> copyFiles(
			final String[] srcArray,
			final String dest,
			final List<ProgressTracker> progressTrackerList)
			throws FileCopyException {
		final boolean setProgressInfo = (progressTrackerList != null && !progressTrackerList.isEmpty());
        final List<File> copiedFiles = new ArrayList<File>();

		final File destFolder = new File(dest);
		if (!destFolder.exists()) {
			destFolder.mkdir();
		}

		int count = 0;
        int failedCount = 0;
        int total = srcArray.length;
        try {
            for (final String src : srcArray) {

                InputStream is = null;
                FileOutputStream fos = null;

                try {
                    URL url = new URL(src);
                    URLConnection urlConnection = url.openConnection();
                    is = urlConnection.getInputStream();
                    String fileName = getFile(src).getName();

                    final File outputFile = new File(dest + File.separator
                            + fileName);
                    fos = new FileOutputStream(outputFile);

                    if (setProgressInfo) {

                        final int progress = (int) ((count / (float) total) * 100);

                        final CopyTrackInformation copyTrackInfo = new CopyTrackInformation(
                                progress, total, count, fileName, outputFile.getAbsolutePath());

                        for(ProgressTracker progressTracker : progressTrackerList) {
                            progressTracker.getProgressInformation().setInformation(copyTrackInfo);
                            progressTracker.getProgressInformer().informProgress(progressTracker.getProgressInformation());
                        }
                    }


                    log.debug("Writing file: " + outputFile.getAbsolutePath());
                    outputFile.createNewFile();
                    final int len = 8192;
                    final byte[] b = new byte[len];
                    final int off = 0;

                    while (is.read(b) != -1) {
                        fos.write(b, off, b.length);
                    }
                    log.debug("Done writing file: " + outputFile.getAbsolutePath());
                    copiedFiles.add(outputFile);
                    ++count;
                } catch (IOException ioe) {
                    log.error("IOException occurred.", ioe);

                    failedCount++;
                    final int progress = (int) ((count / (float) total) * 100);
                    CopyTrackFailureInformation copyTrackFailureInformation =
                            new CopyTrackFailureInformation(
                                    progress, total, count, src, src,
                                    ioe.getLocalizedMessage(), failedCount, ioe);

                    if(setProgressInfo) {
                        for(ProgressTracker progressTracker : progressTrackerList) {
                            progressTracker.getProgressInformation().setInformation(copyTrackFailureInformation);
                            progressTracker.getProgressInformer().informProgress(progressTracker.getProgressInformation());
                        }
                    }

                } finally {
                    if(is != null) {
                        is.close();
                    }
                    if(fos != null) {
                        fos.flush();
                        fos.close();
                    }
                }
            }
        } catch (IOException ioe) {
            log.error("IOException occurred.", ioe);
            throw new FileCopyException(ioe);
        }
        return copiedFiles;
	}

    /**
     *  If there is a '+' sign in the src which is fine in a URL, the return file.exists() returns false.
     *  <br/>
     *  Only use to fetch the file name and not NIO operations.
     *
     * @param src
     * @return
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public static File getFile(String src) throws MalformedURLException, UnsupportedEncodingException {
        final URL srcUrl = new URL(src);

        final File file = new File(URLDecoder.decode(srcUrl.getFile(),
                UTF_8));

        return file;

    }

    /**
     *
     *
     * @param srcFile
     * @param destination
     * @return
     */
    public static File searchFile(File srcFile, File destination) {
        if(destination.isDirectory()) {

            for(File file : destination.listFiles()) {
                if(file.isDirectory()) {
                    File returnFile = searchFile(srcFile, file);
                    if(returnFile != null) {
                        return returnFile;
                    }
                } else if(file.getName().equals(srcFile.getName())) {
                    return file;
                }
            }

        } else {
            log.error("files can only be searched in directories. Found non-directory.");
        }

        return null;
    }
}
