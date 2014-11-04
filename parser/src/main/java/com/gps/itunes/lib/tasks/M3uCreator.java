package com.gps.itunes.lib.tasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class to create M3u file from all the tracks in a particular folder.
 * 
 * @author leogps
 *
 */
public class M3uCreator {

	private static final String fileExtension = ".m3u";

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(M3uCreator.class);

	/**
	 * Creates the M3u file in the playlist folder specified
	 * 
	 * @param playlistFolder
	 * @throws IOException
	 */
	public static void createM3u(final String playlistFolder)
			throws IOException {
		final File playlistDir = new File(playlistFolder);

		log.debug("Creating playlist for directory: " + playlistDir);

		if (playlistDir.isDirectory()) {
			final File playlistFile = new File(playlistDir.getAbsolutePath()
					+ File.separator + playlistDir.getName() + fileExtension);

			final BufferedWriter bw = new BufferedWriter(new FileWriter(
					playlistFile));
			try {

				if ((!playlistFile.exists() && playlistFile.createNewFile())
						|| playlistFile.exists()) {
					for (final String fileName : playlistDir.list()) {
						bw.write(fileName);
						bw.newLine();
					}
					log.debug("Playlist file created successfully. "
							+ playlistFile);
				} else {
					log.error("Error occurred when creating playlist file.");
				}
			} finally {
				bw.flush();
				bw.close();
			}
		}

	}

}
