package com.gps.itunes.lib.tasks;

import com.gps.itunes.lib.exceptions.InvalidPlaylistException;
import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.items.playlists.Playlist;
import com.gps.itunes.lib.items.tracks.Track;
import com.gps.itunes.lib.parser.ItunesLibraryParsedData;
import com.gps.itunes.lib.parser.utils.LogInitializer;
import com.gps.itunes.lib.types.LibraryObject;

import java.util.Map;

/**
 * Created by leogps on 11/8/15.
 */
public class ItunesLibraryParsedDataImpl implements ItunesLibraryParsedData {

    static {
        LogInitializer.getInstance();
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
    public ItunesLibraryParsedDataImpl(final LibraryObject root, final Playlist[] allPlaylists,
                                       final Track[] allTracks, final Map<Long, Track[]> plistTrackMap) throws NoChildrenException {
        this.root = root;
        this.allPlaylists = allPlaylists;
        this.allTracks = allTracks;
        this.plistTrackMap = plistTrackMap;
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

}
