package com.gps.itunes.lib.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import com.gps.itunes.lib.exceptions.FileCopyException;
import com.gps.itunes.lib.tasks.progressinfo.CopyTrackFailureInformation;
import com.gps.itunes.lib.tasks.progressinfo.CopyTrackInformation;
import com.gps.itunes.lib.tasks.progressinfo.ProgressInformation;

import java.io.*;

/**
 * Utility class to copy the library files (files in general) to any
 * destination.
 * 
 * @author leogps
 * 
 */
public class FileFetcher implements Serializable{

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
	public static void copyFiles(final String[] srcArray, final String dest)
			throws FileCopyException {
		copyFiles(srcArray, dest, null, null);
	}

	/**
	 * Copies array of files represented by srcArray to destination and also
	 * informs the copy progress info i.e., {@link ProgressInformation} using
	 * the {@link ProgressInformer}
	 * 
	 * @param srcArray
	 * @param dest
	 * @param informer
	 * @param info
	 * @throws FileCopyException
	 */
	public static void copyFiles(
			final String[] srcArray,
			final String dest,
			final ProgressInformer<ProgressInformation<CopyTrackInformation>> informer,
			final ProgressInformation<CopyTrackInformation> info)
			throws FileCopyException {
		final boolean setProgressInfo = (informer != null && info != null);

		final File destFolder = new File(dest);
		if (!destFolder.exists()) {
			destFolder.mkdir();
		}

		int count = 0;
        int failedCount = 0;
        int total = srcArray.length;
        try {
            for (final String src : srcArray) {

                FileInputStream fis = null;
                FileOutputStream fos = null;

                try {
                    final URL srcUrl = new URL(src);

                    final File file = new File(URLDecoder.decode(srcUrl.getFile(),
                            "UTF-8"));

                    fis = new FileInputStream(file);

                    final File outputFile = new File(dest + File.separator
                            + file.getName());
                    fos = new FileOutputStream(outputFile);

                    if (setProgressInfo) {

                        final int progress = (int) ((count / (float) total) * 100);

                        final CopyTrackInformation copyTrackInfo = new CopyTrackInformation(
                                progress, total, count, file.getName(), outputFile.getAbsolutePath());

                        info.setInformation(copyTrackInfo);

                        informer.informProgress(info);
                    }


                    log.debug("Writing file: " + outputFile.getAbsolutePath());
                    outputFile.createNewFile();
                    final int len = 2048;
                    final byte[] b = new byte[len];
                    final int off = 0;

                    while (fis.read(b) != -1) {
                        fos.write(b, off, b.length);
                    }
                    log.debug("Done writing file: " + outputFile.getAbsolutePath());
                    ++count;
                } catch (IOException ioe) {
                    log.error("IOException occurred.", ioe);

                    failedCount++;
                    final int progress = (int) ((count / (float) total) * 100);
                    CopyTrackFailureInformation copyTrackFailureInformation =
                            new CopyTrackFailureInformation(
                                    progress, total, count, src, src,
                                    ioe.getLocalizedMessage(), failedCount, ioe);

                    info.setInformation(copyTrackFailureInformation);

                    informer.informProgress(info);

                } finally {
                    if(fis != null) {
                        fis.close();
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

	}
}
