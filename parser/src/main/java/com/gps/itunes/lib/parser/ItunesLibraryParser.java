package com.gps.itunes.lib.parser;

import com.gps.itunes.lib.exceptions.FileCopyException;
import com.gps.itunes.lib.exceptions.InvalidPlaylistException;
import com.gps.itunes.lib.exceptions.LibraryParseException;
import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.tasks.ProgressInformer;
import com.gps.itunes.lib.tasks.progressinfo.CopyTrackInformation;
import com.gps.itunes.lib.tasks.progressinfo.ProgressInformation;
import com.gps.itunes.lib.tasks.progressinfo.ProgressTracker;

import java.io.IOException;
import java.util.List;

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
     * Add Parse configuration information.
     *
     * @param configurationKey
     * @param configurationValue
     */
    void addParseConfiguration(String configurationKey, String configurationValue);

	/**
	 * Reads the library parse configuration and returns the {@link ItunesLibraryParsedData ParsedData}
	 *
	 * @return
	 */
	ItunesLibraryParsedData parse() throws LibraryParseException, NoChildrenException;


    /**
     * Copy the specified playlist to destination specified.
     *
     * @param playlistName
     * @param destination
     * @param analyzeDuplicates
     * @throws IOException
     */
    void copyPlaylists(final String playlistName,
                       final String destination, boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData)
            throws IOException, FileCopyException, InvalidPlaylistException;


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
    void copyPlaylists(final String playlistName, final String destination,
                       final ProgressInformer<ProgressInformation<CopyTrackInformation>> informer, final ProgressInformation<CopyTrackInformation> info,
                       boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData) throws IOException, FileCopyException, InvalidPlaylistException;

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
    void copyPlaylists(final String playlistName, final String destination,
                       final List<ProgressTracker> progressTrackerList, boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData)
            throws IOException, FileCopyException, InvalidPlaylistException;


    /**
     * Copy the specified playlist to destination specified.
     *
     * @param playlistId
     * @param destination
     * @param analyzeDuplicates
     * @throws IOException
     */
    void copyPlaylists(final Long playlistId, final String destination, boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData)
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
    void copyPlaylists(final Long playlistId, final String destination,
                       final List<ProgressTracker> progressTrackerList, boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData)
            throws IOException, FileCopyException, InvalidPlaylistException;

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
    void copyPlaylists(final Long playlistId, final String destination,
                       final ProgressInformer<ProgressInformation<CopyTrackInformation>> informer, final ProgressInformation<CopyTrackInformation> info,
                       boolean analyzeDuplicates, ItunesLibraryParsedData itunesLibraryParsedData) throws IOException, FileCopyException, InvalidPlaylistException;

}
