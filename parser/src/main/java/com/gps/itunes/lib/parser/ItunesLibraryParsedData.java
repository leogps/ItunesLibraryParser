package com.gps.itunes.lib.parser;

import com.gps.itunes.lib.exceptions.InvalidPlaylistException;
import com.gps.itunes.lib.items.playlists.Playlist;
import com.gps.itunes.lib.items.tracks.Track;
import com.gps.itunes.lib.types.LibraryObject;

/**
 * Created by leogps on 11/8/15.
 */
public interface ItunesLibraryParsedData {

    /**
     * Gets the root element in the Itunes Library XML file.
     *
     * @return {@link com.gps.itunes.lib.types.LibraryObject}
     */
    LibraryObject getRoot();


    /**
     * Returns all the playlists in the Itunes library.
     *
     * @return array of {@link com.gps.itunes.lib.items.playlists.Playlist} objects
     */
    Playlist[] getAllPlaylists();


    /**
     * Retuens all the tracks in the Itunes Library.
     *
     * @return array of {@link com.gps.itunes.lib.items.tracks.Track} objects
     */
    Track[] getAllTracks();


    /**
     * Given a playlistId, this method returns all the tracks for that playlist.
     *
     * @param playlistId
     * @return array of {@link Track} objects
     */
    Track[] getPlaylistTracks(final Long playlistId);


    /**
     * Given a playlistName, this method returns all the tracks for that
     * playlist.<br />
     * Throws {@link com.gps.itunes.lib.exceptions.InvalidPlaylistException} if no such playlist exists.
     *
     * @param playlistName
     * @return array of {@link Track} objects
     * @throws com.gps.itunes.lib.exceptions.InvalidPlaylistException
     */
    Track[] getPlaylistTracks(final String playlistName)
            throws InvalidPlaylistException;


    /**
     * Returns a Playlist object for the given playlistId.
     *
     * @param playlistId
     * @return {@link Playlist}
     * @throws InvalidPlaylistException
     */
    Playlist getPlaylist(final Long playlistId)
            throws InvalidPlaylistException;

}
