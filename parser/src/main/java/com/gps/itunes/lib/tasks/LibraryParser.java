package com.gps.itunes.lib.tasks;

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
import com.gps.itunes.lib.types.LibraryObject;
import com.gps.itunes.lib.xml.XMLParser;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

	@Override
	public LibraryObject getRoot() {
		return root;
	}

	@Override
	public Playlist[] getAllPlaylists() {
		return allPlaylists;
	}

	@Override
	public Track[] getAllTracks() {
		return allTracks;
	}

	@Override
	public Track[] getPlaylistTracks(final Long playlistId) {
		return plistTrackMap.get(playlistId);
	}

	@Override
	public Track[] getPlaylistTracks(final String playlistName)
			throws InvalidPlaylistException {
		for (final Playlist playlist : getAllPlaylists()) {
			if (playlist.getName().equals(playlistName)) {
				return plistTrackMap.get(playlist.getPlaylistId());
			}
		}

		throw new InvalidPlaylistException("Invalid playlist: " + playlistName);
	}
	
	@Override
	public Playlist getPlaylist(final Long playlistId)
			throws InvalidPlaylistException {
		for (final Playlist playlist : getAllPlaylists()) {
			if (playlistId == playlist.getPlaylistId()) {
				return playlist;
			}
		}

		throw new InvalidPlaylistException("Invalid playlistId: " + playlistId);
	}

	@Override
	public void copyPlaylists(final String playlistName,
			final String destination) throws IOException {
		copyPlaylists(playlistName, destination, null, null);
	}
	
	@Override
	public void copyPlaylists(final String playlistName, final String destination,
			final ProgressInformer<ProgressInformation<CopyTrackInformation>> informer, final ProgressInformation<CopyTrackInformation> info) throws IOException {
		for (final Playlist playlist : getAllPlaylists()) {
			if (playlist.getName().equalsIgnoreCase(playlistName)) {
				
				final File playlistFolder = new File(destination
						+ File.separator + playlistName);
				
				if (!playlistFolder.exists()) {
					playlistFolder.mkdir();
				}

				if (playlistFolder.exists()) {
					if(informer != null && info != null){
						copyPlaylists(playlist.getPlaylistId(),
								playlistFolder.getAbsolutePath(), informer, info);
					} else {
						copyPlaylists(playlist.getPlaylistId(),
								playlistFolder.getAbsolutePath());
					}
				} else {
					log.error("Playlist folder could not be created.");
				}
				break;
			}
		}
	}

	@Override
	public void copyPlaylists(final Long playlistId, final String destination)
			throws IOException {
		copyPlaylists(playlistId, destination, null, null);
		
	}
	
	@Override
	public void copyPlaylists(final Long playlistId, final String destination,
			final ProgressInformer<ProgressInformation<CopyTrackInformation>> informer, final ProgressInformation<CopyTrackInformation> info) throws IOException {
		final Track[] plistTracks = plistTrackMap.get(playlistId);

		final String[] srcArray = TrackLocationRetriever
				.getTrackLocations(plistTracks);
		
		if(informer != null && info != null){
			FileFetcher.copyFiles(srcArray, destination, informer, info);
		} else {
			FileFetcher.copyFiles(srcArray, destination);
		}
		
		M3uCreator.createM3u(destination);
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
	public void copyPlaylists(final Long playlistId) throws IOException {

		copyPlaylists(playlistId, fetchDefaultDestination(playlistId));

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
	public void copyAllPlaylists(final String destination) throws IOException {
		Set<Long> plistKeys = plistTrackMap.keySet();

		for (final Iterator<Long> it = plistKeys.iterator(); it.hasNext();) {
			final Long playlistId = it.next();
			copyPlaylists(playlistId,
					destination == null ? fetchDefaultDestination(playlistId)
							: destination);
		}
	}

}
