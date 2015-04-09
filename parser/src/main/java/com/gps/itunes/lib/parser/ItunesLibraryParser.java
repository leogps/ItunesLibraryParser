package com.gps.itunes.lib.parser;

import java.io.IOException;
import java.util.List;

import com.gps.itunes.lib.exceptions.FileCopyException;
import com.gps.itunes.lib.exceptions.InvalidPlaylistException;
import com.gps.itunes.lib.items.playlists.Playlist;
import com.gps.itunes.lib.items.tracks.Track;
import com.gps.itunes.lib.tasks.ProgressInformer;
import com.gps.itunes.lib.tasks.progressinfo.CopyTrackInformation;
import com.gps.itunes.lib.tasks.progressinfo.ProgressInformation;
import com.gps.itunes.lib.tasks.progressinfo.ProgressTracker;
import com.gps.itunes.lib.types.LibraryObject;

/**
 * <strong>Itunes Library Parser API</strong> <br/>
 * Parses the itunes library, constructs and fetches all the Library Info as
 * objects.<br/>
 * Also provides some utility methods to perform tasks on the library.
 * 
 * 
 * @author leogps
 * 
 */
public interface ItunesLibraryParser {
	
	/**
	 * Gets the root element in the Itunes Library XML file.
	 * 
	 * @return {@link com.gps.itunes.lib.types.LibraryObject}
	 */
	public LibraryObject getRoot();
	
	
	/**
	 * Returns all the playlists in the Itunes library.
	 * 
	 * @return array of {@link com.gps.itunes.lib.items.playlists.Playlist} objects
	 */
	public Playlist[] getAllPlaylists();
	
	
	/**
	 * Retuens all the tracks in the Itunes Library.
	 * 
	 * @return array of {@link com.gps.itunes.lib.items.tracks.Track} objects
	 */
	public Track[] getAllTracks();
	
	
	/**
	 * Given a playlistId, this method returns all the tracks for that playlist.
	 * 
	 * @param playlistId
	 * @return array of {@link Track} objects
	 */
	public Track[] getPlaylistTracks(final Long playlistId);
	
	
	/**
	 * Given a playlistName, this method returns all the tracks for that
	 * playlist.<br />
	 * Throws {@link com.gps.itunes.lib.exceptions.InvalidPlaylistException} if no such playlist exists.
	 * 
	 * @param playlistName
	 * @return array of {@link Track} objects
	 * @throws com.gps.itunes.lib.exceptions.InvalidPlaylistException
	 */
	public Track[] getPlaylistTracks(final String playlistName)
			throws InvalidPlaylistException;
	
	
	/**
	 * Returns a Playlist object for the given playlistId.
	 * 
	 * @param playlistId
	 * @return {@link Playlist}
	 * @throws InvalidPlaylistException
	 */
	public Playlist getPlaylist(final Long playlistId)
			throws InvalidPlaylistException;

	/**
	 * Copy the specified playlist to destination specified.
	 * 
	 * @param playlistName
	 * @param destination
	 * @param analyzeDuplicates
	 * @throws IOException
	 */
	public void copyPlaylists(final String playlistName,
			final String destination, boolean analyzeDuplicates) throws IOException, FileCopyException, InvalidPlaylistException;
	
	
	/**
	 * Copy the specified playlist to destination specified. <br />
	 * Also informs the {@link com.gps.itunes.lib.tasks.progressinfo.ProgressInformation copy progress information}
	 * using the {@link com.gps.itunes.lib.tasks.ProgressInformer}
	 * 
	 * @param playlistName
	 * @param destination
	 * @param informer
	 * @param info
	 * @param analyzeDuplicates
	 * @throws IOException
	 */
	public void copyPlaylists(final String playlistName, final String destination,
			final ProgressInformer<ProgressInformation<CopyTrackInformation>> informer, final ProgressInformation<CopyTrackInformation> info,
							  boolean analyzeDuplicates) throws IOException, FileCopyException, InvalidPlaylistException;

	/**
	 * Copy the specified playlist to destination specified. <br />
	 * Also informs the {@link com.gps.itunes.lib.tasks.progressinfo.ProgressInformation copy progress information}
	 * using the {@link com.gps.itunes.lib.tasks.ProgressInformer}
	 *
	 * @param playlistName
	 * @param destination
	 * @param progressTrackerList
	 * @param analyzeDuplicates
	 * @throws IOException
	 * @throws FileCopyException
	 */
	public void copyPlaylists(final String playlistName, final String destination,
							  final List<ProgressTracker> progressTrackerList, boolean analyzeDuplicates) throws IOException, FileCopyException, InvalidPlaylistException;
	
	
	/**
	 * Copy the specified playlist to destination specified.
	 * 
	 * @param playlistId
	 * @param destination
	 * @param analyzeDuplicates
	 * @throws IOException
	 */
	public void copyPlaylists(final Long playlistId, final String destination, boolean analyzeDuplicates)
			throws IOException, FileCopyException, InvalidPlaylistException;

	/**
	 * Copy the specified playlist to destination specified. <br />
	 * Also informs the list of {@link ProgressInformation copy progress information}
	 * using the {@link ProgressInformer}s
	 * @param playlistId
	 * @param destination
	 * @param progressTrackerList
	 * @param analyzeDuplicates
	 * @throws IOException
	 * @throws FileCopyException
	 */
	public void copyPlaylists(final Long playlistId, final String destination,
							  final List<ProgressTracker> progressTrackerList, boolean analyzeDuplicates) throws IOException, FileCopyException, InvalidPlaylistException;
	
	/**
	 * Copy the specified playlist to destination specified. <br />
	 * Also informs the {@link ProgressInformation copy progress information}
	 * using the {@link ProgressInformer}
	 * 
	 * @param playlistId
	 * @param destination
	 * @param informer
	 * @param info
	 * @param analyzeDuplicates
	 * @throws IOException
	 */
	public void copyPlaylists(final Long playlistId, final String destination,
			final ProgressInformer<ProgressInformation<CopyTrackInformation>> informer, final ProgressInformation<CopyTrackInformation> info,
							  boolean analyzeDuplicates) throws IOException, FileCopyException, InvalidPlaylistException;
			
}
