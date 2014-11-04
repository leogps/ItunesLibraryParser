package com.gps.itunes.lib.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

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
	 * @throws IOException
	 */
	public static void copyFiles(final String[] srcArray, final String dest)
			throws IOException {
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
	 * @throws IOException
	 */
	public static void copyFiles(
			final String[] srcArray,
			final String dest,
			final ProgressInformer<ProgressInformation<CopyTrackInformation>> informer,
			final ProgressInformation<CopyTrackInformation> info)
			throws IOException {
		final boolean setProgressInfo = (informer != null && info != null);

		final File destFolder = new File(dest);
		if (!destFolder.exists()) {
			destFolder.mkdir();
		}

		int count = 0;
		for (final String src : srcArray) {

			final URL srcUrl = new URL(src);

			final File file = new File(URLDecoder.decode(srcUrl.getFile(),
					"UTF-8"));

			final FileInputStream fis = new FileInputStream(file);

			final File outputFile = new File(dest + File.separator
					+ file.getName());
			final FileOutputStream fos = new FileOutputStream(outputFile);

			if (setProgressInfo) {

				final int progress = (int) ((++count / (float) srcArray.length) * 100);

				final CopyTrackInformation copyTrackInfo = new CopyTrackInformation(
						progress, srcArray.length, count, file.getName(), outputFile.getAbsolutePath());

				info.setInformation(copyTrackInfo);

				informer.informProgress(info);
			}

			try {
				log.debug("Writing file: " + outputFile.getAbsolutePath());
				outputFile.createNewFile();
				final int len = 2048;
				final byte[] b = new byte[len];
				final int off = 0;

				while (fis.read(b) != -1) {
					fos.write(b, off, b.length);
				}
				log.debug("Done writing file: " + outputFile.getAbsolutePath());
			} catch (IOException ioe) {
				log.error("IOException occurred.", ioe);
			} finally {
				fis.close();
				fos.flush();
				fos.close();
			}
		}

	}
}
