package com.gps.itunes.lib.tasks;

import com.gps.itunes.lib.exceptions.FileCopyException;
import com.gps.itunes.lib.exceptions.InvalidPlaylistException;
import com.gps.itunes.lib.exceptions.LibraryParseException;
import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.items.playlists.Playlist;
import com.gps.itunes.lib.items.tracks.Track;
import com.gps.itunes.lib.parser.ItunesLibraryParsedData;
import com.gps.itunes.lib.parser.ItunesLibraryParser;
import com.gps.itunes.lib.parser.utils.PropertyManager;
import com.gps.itunes.lib.tasks.progressinfo.CopyTrackInformation;
import com.gps.itunes.lib.tasks.progressinfo.ProgressInformation;
import com.gps.itunes.lib.tasks.progressinfo.ProgressTracker;
import com.gps.itunes.lib.types.LibraryObject;
import com.gps.itunes.lib.xml.XMLParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides Solid implementation of {@link ItunesLibraryParser}
 *
 * @author leogps
 *
 */
public class LibraryParser implements ItunesLibraryParser {

	private final XMLParser xmlParser = new XMLParser();

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
            .getLogger(LibraryParser.class);

    public void addParseConfiguration(String configurationKey, String configurationValue) {
		PropertyManager.getConfigurationMap().put(configurationKey, configurationValue);
    }

    public ItunesLibraryParsedData parse() throws LibraryParseException, NoChildrenException {
        String libraryFileLocation = PropertyManager.getConfigurationMap()
				.get(PropertyManager.Property.LIBRARY_FILE_LOCATION_PROPERTY.getKey());

        LibraryObject root = xmlParser.parseXML(libraryFileLocation);
        Playlist[] allPlaylists = new PlaylistRetriever(root).retrievePlaylist();
        Track[] allTracks = new TracksRetriever(root).getTracks();
        Map<Long, Track[]> playlistTrackMap = new PlaylistTrackMapper(allPlaylists, allTracks)
                .getPlaylistTracks();

        log.debug("Successfully Parsed.");
        return new ItunesLibraryParsedDataImpl(root, allPlaylists, allTracks, playlistTrackMap);
	}

	public void copyPlaylists(final String playlistName,
							  final String destination, boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData)
			throws IOException, FileCopyException, InvalidPlaylistException {
		copyPlaylists(playlistName, destination, null, null, analyzeDuplicates, itunesLibraryParsedData);
	}

	public void copyPlaylists(final String playlistName, final String destination,
							  final ProgressInformer<ProgressInformation<CopyTrackInformation>> informer, final ProgressInformation<CopyTrackInformation> info,
							  boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData)
			throws IOException, FileCopyException, InvalidPlaylistException {
		for (final Playlist playlist : itunesLibraryParsedData.getAllPlaylists()) {
			if (playlist.getName().equalsIgnoreCase(playlistName)) {

				if(informer != null && info != null){
					copyPlaylists(playlist.getPlaylistId(),
							destination, informer, info, analyzeDuplicates, itunesLibraryParsedData);
				} else {
					copyPlaylists(playlist.getPlaylistId(),
							destination, analyzeDuplicates, itunesLibraryParsedData);
				}

				break;
			}
		}
	}

	public void copyPlaylists(final String playlistName, final String destination,
							  final List<ProgressTracker> progressTrackerList, boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData)
			throws IOException, FileCopyException, InvalidPlaylistException {
		for(ProgressTracker progressTracker : progressTrackerList) {
			copyPlaylists(playlistName, destination, progressTracker.getProgressInformer(), progressTracker.getProgressInformation(), analyzeDuplicates, itunesLibraryParsedData);
		}

	}

	public void copyPlaylists(final Long playlistId, final String destination, boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData)
			throws IOException, FileCopyException, InvalidPlaylistException {
		copyPlaylists(playlistId, destination, null, null, analyzeDuplicates, itunesLibraryParsedData);

	}

	public void copyPlaylists(final Long playlistId, final String destination,
							  final List<ProgressTracker> progressTrackerList,
							  boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData)
			throws IOException, FileCopyException, InvalidPlaylistException {
		final Track[] plistTracks = itunesLibraryParsedData.getPlaylistTracks(playlistId);

		Playlist playlist = itunesLibraryParsedData.getPlaylist(playlistId);

		final File playlistFolder = new File(destination
				+ File.separator + playlist.getName() + "-" + playlistId);

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
							  boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData)
            throws IOException, FileCopyException, InvalidPlaylistException {
		ProgressTracker progressTracker = new ProgressTracker(informer, info);
		List<ProgressTracker> progressTrackerList = new ArrayList<ProgressTracker>();
		progressTrackerList.add(progressTracker);

		copyPlaylists(playlistId, destination, progressTrackerList, analyzeDuplicates, itunesLibraryParsedData);
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
	public void copyPlaylists(final Long playlistId, ItunesLibraryParsedData itunesLibraryParsedData)
            throws IOException, FileCopyException, InvalidPlaylistException {

		copyPlaylists(playlistId, fetchDefaultDestination(playlistId), false, itunesLibraryParsedData);

	}

	private String fetchDefaultDestination(final Long playlistId) {
		return PropertyManager.getConfigurationMap()
                .get("playist.copy.destination")
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
	public void copyAllPlaylists(final String destination, boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData)
            throws IOException, FileCopyException, InvalidPlaylistException {
		Playlist[] playlists = itunesLibraryParsedData.getAllPlaylists();
		for (Playlist playlist : playlists) {
			final Long playlistId = playlist.getPlaylistId();
			copyPlaylists(playlistId,
					destination == null ? fetchDefaultDestination(playlistId)
							: destination, analyzeDuplicates, itunesLibraryParsedData);
		}
	}
}
