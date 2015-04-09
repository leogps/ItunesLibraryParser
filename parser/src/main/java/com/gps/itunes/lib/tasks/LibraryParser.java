package com.gps.itunes.lib.tasks;

import com.gps.itunes.lib.exceptions.FileCopyException;
import com.gps.itunes.lib.exceptions.InvalidPlaylistException;
import com.gps.itunes.lib.exceptions.LibraryParseException;
import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.items.playlists.Playlist;
import com.gps.itunes.lib.items.tracks.Track;
import com.gps.itunes.lib.parser.ItunesLibraryParser;
import com.gps.itunes.lib.parser.utils.LogInitializer;
import com.gps.itunes.lib.parser.utils.PropertyManager;
import com.gps.itunes.lib.tasks.progressinfo.CopyTrackInformation;
import com.gps.itunes.lib.tasks.progressinfo.ProgressInformation;
import com.gps.itunes.lib.tasks.progressinfo.ProgressTracker;
import com.gps.itunes.lib.types.LibraryObject;
import com.gps.itunes.lib.xml.XMLParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Provides Solid implementation of {@link ItunesLibraryParser}
 *
 * @author leogps
 *
 */
public class LibraryParser implements ItunesLibraryParser {

	static {
		new LogInitializer();
	}

	private final LibraryObject root;
	private final Playlist[] allPlaylists;
	private final Track[] allTracks;
	private final Map<Long, Track[]> plistTrackMap;

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(LibraryParser.class);

	/**
	 * Parses the library with already constructed root element.
	 * 
	 * @param root
	 * @throws com.gps.itunes.lib.exceptions.NoChildrenException
	 */
	public LibraryParser(final LibraryObject root) throws NoChildrenException {
		this.root = root;
		this.allPlaylists = new PlaylistRetriever(root).retrievePlaylist();
		this.allTracks = new TracksRetriever(root).getTracks();
		this.plistTrackMap = new PlaylistTrackMapper(allPlaylists, allTracks)
				.getPlaylistTracks();

		log.debug("Successfully Parsed.");
	}
	
	/**
	 * Parses the iTunes library file located at the default location on the current running platform.
	 * <br>
	 * <p>The default library file location is specified in the config folder of the project directory. i.e., /config/{platform}-app.properties file</p>
	 * 
	 * @throws NoChildrenException
	 * @throws com.gps.itunes.lib.exceptions.LibraryParseException
	 */
	public LibraryParser() throws NoChildrenException, LibraryParseException {
		this.root = new XMLParser().parseXML(PropertyManager.getProperties()
				.getProperty("libraryFileLocation"));
		this.allPlaylists = new PlaylistRetriever(root).retrievePlaylist();
		this.allTracks = new TracksRetriever(root).getTracks();
		this.plistTrackMap = new PlaylistTrackMapper(allPlaylists, allTracks)
				.getPlaylistTracks();

		log.debug("Successfully Parsed.");
	}
	
	/**
	 * Parses the iTunes Library located at the specified location.
	 * 
	 * @param libFileLocation
	 * @throws LibraryParseException
	 * @throws NoChildrenException
	 */
	public LibraryParser(final String libFileLocation) throws LibraryParseException, NoChildrenException {
		this.root = new XMLParser().parseXML(libFileLocation);
		this.allPlaylists = new PlaylistRetriever(root).retrievePlaylist();
		this.allTracks = new TracksRetriever(root).getTracks();
		this.plistTrackMap = new PlaylistTrackMapper(allPlaylists, allTracks)
				.getPlaylistTracks();

		log.debug("Successfully Parsed.");
	}

	public LibraryObject getRoot() {
		return root;
	}

	public Playlist[] getAllPlaylists() {
		return allPlaylists;
	}

	public Track[] getAllTracks() {
		return allTracks;
	}

	public Track[] getPlaylistTracks(final Long playlistId) {
		return plistTrackMap.get(playlistId);
	}

	public Track[] getPlaylistTracks(final String playlistName)
			throws InvalidPlaylistException {
		for (final Playlist playlist : getAllPlaylists()) {
			if (playlist.getName().equals(playlistName)) {
				return plistTrackMap.get(playlist.getPlaylistId());
			}
		}

		throw new InvalidPlaylistException("Invalid playlist: " + playlistName);
	}

	public Playlist getPlaylist(final Long playlistId)
			throws InvalidPlaylistException {
		for (final Playlist playlist : getAllPlaylists()) {
			if (playlistId == playlist.getPlaylistId()) {
				return playlist;
			}
		}

		throw new InvalidPlaylistException("Invalid playlistId: " + playlistId);
	}

	public void copyPlaylists(final String playlistName,
			final String destination, boolean analyzeDuplicates) throws IOException, FileCopyException, InvalidPlaylistException {
		copyPlaylists(playlistName, destination, null, null, analyzeDuplicates);
	}

	public void copyPlaylists(final String playlistName, final String destination,
			final ProgressInformer<ProgressInformation<CopyTrackInformation>> informer, final ProgressInformation<CopyTrackInformation> info,
							  boolean analyzeDuplicates) throws IOException, FileCopyException, InvalidPlaylistException {
		for (final Playlist playlist : getAllPlaylists()) {
			if (playlist.getName().equalsIgnoreCase(playlistName)) {

				if(informer != null && info != null){
					copyPlaylists(playlist.getPlaylistId(),
							destination, informer, info, analyzeDuplicates);
				} else {
					copyPlaylists(playlist.getPlaylistId(),
							destination, analyzeDuplicates);
				}

				break;
			}
		}
	}

	public void copyPlaylists(final String playlistName, final String destination,
							  final List<ProgressTracker> progressTrackerList, boolean analyzeDuplicates)
			throws IOException, FileCopyException, InvalidPlaylistException {
		for(ProgressTracker progressTracker : progressTrackerList) {
			copyPlaylists(playlistName, destination, progressTracker.getProgressInformer(), progressTracker.getProgressInformation(), analyzeDuplicates);
		}

	}

	public void copyPlaylists(final Long playlistId, final String destination, boolean analyzeDuplicates)
			throws IOException, FileCopyException, InvalidPlaylistException {
		copyPlaylists(playlistId, destination, null, null, analyzeDuplicates);
		
	}

	public void copyPlaylists(final Long playlistId, final String destination,
							  final List<ProgressTracker> progressTrackerList,
							  boolean analyzeDuplicates) throws IOException, FileCopyException, InvalidPlaylistException {
		final Track[] plistTracks = plistTrackMap.get(playlistId);

		final File playlistFolder = new File(destination
				+ File.separator + getPlaylist(playlistId).getName() + "-" + playlistId);

		if (!playlistFolder.exists()) {
			playlistFolder.mkdir();
		}

		if (playlistFolder.exists()) {

			final String[] srcArray = TrackLocationRetriever
					.getTrackLocations(plistTracks);

			if(analyzeDuplicates) {

				final List<String> srcArrayNoDuplicates = new ArrayList<String>();
				final List<String> existingFileList = new ArrayList<String>();
				final File destinationDir = new File(destination);
				for (String source : srcArray) {
					URL url = new URL(source);
					File existingFile = FileFetcher.searchFile(FileFetcher.getFile(source), destinationDir);
					if (existingFile != null) {
						existingFileList.add(existingFile.getAbsolutePath());
					} else {
						srcArrayNoDuplicates.add(source);
					}
				}

				final List<File> copiedFiles;
				if (progressTrackerList != null) {
					copiedFiles = FileFetcher.copyFiles(srcArrayNoDuplicates.toArray(new String[srcArrayNoDuplicates.size()]),
							playlistFolder.getAbsolutePath(), progressTrackerList);
				} else {
					copiedFiles = FileFetcher.copyFiles(srcArrayNoDuplicates.toArray(new String[srcArrayNoDuplicates.size()]),
							playlistFolder.getAbsolutePath());
				}

				M3uCreator.createM3u(playlistFolder, copiedFiles, existingFileList);

			} else {

				if (progressTrackerList != null) {
					FileFetcher.copyFiles(srcArray,
							playlistFolder.getAbsolutePath(), progressTrackerList);
				} else {
					FileFetcher.copyFiles(srcArray,
							playlistFolder.getAbsolutePath());
				}
				M3uCreator.createM3u(playlistFolder.getAbsolutePath());
			}

		} else {
			log.error("Playlist folder could not be created.");
		}
	}

	public void copyPlaylists(final Long playlistId, final String destination,
			final ProgressInformer<ProgressInformation<CopyTrackInformation>> informer, final ProgressInformation<CopyTrackInformation> info,
							  boolean analyzeDuplicates) throws IOException, FileCopyException, InvalidPlaylistException {
		ProgressTracker progressTracker = new ProgressTracker(informer, info);
		List<ProgressTracker> progressTrackerList = new ArrayList<ProgressTracker>();
		progressTrackerList.add(progressTracker);

		copyPlaylists(playlistId, destination, progressTrackerList, analyzeDuplicates);
	}

	/**
	 * Copies the playlist to the default destination specified in the propeties
	 * file. <br />
	 * {@code
	 * property=copyDestination in the properties file.
	 * }
	 * 
	 * @param playlistId
	 * @throws IOException
	 */
	public void copyPlaylists(final Long playlistId) throws IOException, FileCopyException, InvalidPlaylistException {

		copyPlaylists(playlistId, fetchDefaultDestination(playlistId), false);

	}

	private String fetchDefaultDestination(final Long playlistId) {
		return PropertyManager.getProperties().getProperty("copyDestination")
				+ File.separator + playlistId;
	}

	/**
	 * Copies all the playlists to the default destination specified in the
	 * propeties file. <br />
	 * {@code
	 * property=copyDestination in the properties file.
	 * }
	 * 
	 * @param destination
	 * @throws IOException
	 */
	public void copyAllPlaylists(final String destination, boolean analyzeDuplicates) throws IOException, FileCopyException, InvalidPlaylistException {
		Set<Long> plistKeys = plistTrackMap.keySet();

		for (final Iterator<Long> it = plistKeys.iterator(); it.hasNext();) {
			final Long playlistId = it.next();
			copyPlaylists(playlistId,
					destination == null ? fetchDefaultDestination(playlistId)
							: destination, analyzeDuplicates);
		}
	}

}
