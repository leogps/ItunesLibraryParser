package com.gps.itunes.lib.tasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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

		createM3u(playlistDir);

	}

	private static void createM3u(File playlistDir) throws IOException {
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

	public static void createM3u(File playlistDir, List<File> copiedFiles, List<String> existingFileList) throws IOException {
		if(playlistDir.isDirectory()) {
			final File playlistFile = new File(playlistDir.getAbsolutePath()
					+ File.separator + playlistDir.getName() + fileExtension);

			final BufferedWriter bw = new BufferedWriter(new FileWriter(
					playlistFile));
			try {


				if ((!playlistFile.exists() && playlistFile.createNewFile())
						|| playlistFile.exists()) {
					for (final File copiedFile : copiedFiles) {
						bw.write(getRelativePath(playlistDir, copiedFile));
						bw.newLine();
					}
					for(final String existingFile : existingFileList) {
						bw.write(getRelativePath(playlistDir, new File(existingFile)));
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

	/**
	 * Gets the relative path of the @param relativeToFile from @param absoluteDir.
	 * <br/>
	 *
	 * Reference: http://mrpmorris.blogspot.com/2007/05/convert-absolute-path-to-relative-path.html
	 * by Peter Morris.
	 *
	 * @Correction Consider file and if it is, add it at the end.
	 *
	 * <br/>
	 *
	 * @param absoluteDir
	 * @param relativeToFile
	 * @return
	 */
	public static String getRelativePath(File absoluteDir, File relativeToFile) {
		if(absoluteDir == null || relativeToFile == null) {
			return null;
		}

		final String absolutePath = absoluteDir.getAbsolutePath();
		final String relativeTo;
		if(!relativeToFile.isDirectory()) {
			relativeTo = relativeToFile.getParent();
		} else {
			relativeTo = relativeToFile.getAbsolutePath();
		}

		String[] absoluteDirectories = absolutePath.split(File.separator);
		String[] relativeDirectories = relativeTo.split(File.separator);

		//Get the shortest of the two paths
		int length = absoluteDirectories.length < relativeDirectories.length ? absoluteDirectories.length : relativeDirectories.length;

		//Use to determine where in the loop we exited
		int lastCommonRoot = -1;
		int index;

		//Find common root
		for (index = 0; index < length; index++) {
			if (absoluteDirectories[index].equals(relativeDirectories[index])) {
				lastCommonRoot = index;
			}
			else {
				break;
			}
		}

		//If we didn't find a common prefix then throw
		if (lastCommonRoot == -1) {
			return new File(relativeTo).toURI().relativize(new File(absolutePath).toURI()).getPath();
		}

		//Build up the relative path
		StringBuilder relativePath = new StringBuilder();

		// Check if both represent the same directory.
		if(!(absoluteDirectories.length == relativeDirectories.length && lastCommonRoot + 1 == absoluteDirectories.length)) {

			//Add on the ../
			for (index = lastCommonRoot + 1; index < absoluteDirectories.length; index++) {
				if (absoluteDirectories[index].length() > 0) {
					relativePath.append(".." + File.separator);
				}
			}

			//Add on the folders
			for (index = lastCommonRoot + 1; index < relativeDirectories.length - 1; index++) {
				relativePath.append(relativeDirectories[index] + File.separator);
			}
			relativePath.append(relativeDirectories[relativeDirectories.length - 1]);

			if(!relativeToFile.isDirectory()) {
				relativePath.append(File.separator + relativeToFile.getName());
			}
		} else if(!relativeToFile.isDirectory()){
			relativePath.append(relativeToFile.getName());
		}


		return relativePath.toString();
	}
}
