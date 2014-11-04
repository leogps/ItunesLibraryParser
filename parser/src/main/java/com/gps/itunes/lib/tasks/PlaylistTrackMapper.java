package com.gps.itunes.lib.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gps.itunes.lib.items.playlists.Playlist;
import com.gps.itunes.lib.items.playlists.PlaylistItem;
import com.gps.itunes.lib.items.tracks.Track;

/**
 * Class to establish various relationships between playlist(s) and track(s).
 * Maps Playlists vs Tracks
 * 
 * @author leogps
 * 
 */
public class PlaylistTrackMapper {

	private final Playlist[] playlists;
	private final Track[] allTracks;

	public Playlist[] getPlaylists() {
		return playlists;
	}

	public Track[] getAllTracks() {
		return allTracks;
	}

	/**
	 * Creates {@link PlaylistTrackMapper} object. For proper functioning of
	 * this class's methods, pass all the playlists and track objects created
	 * from a Library.
	 * 
	 * @param playlists
	 * @param allTracks
	 */
	public PlaylistTrackMapper(final Playlist[] playlists,
			final Track[] allTracks) {
		this.playlists = playlists;
		this.allTracks = allTracks;
	}

	/**
	 * Returns a Map with playlistIds as keys and the corresponding tracks[] as values.
	 * 
	 * @return {@link Map}
	 */
	public Map<Long, Track[]> getPlaylistTracks() {

		final Map<Long, Track[]> playlistTrackMap = new HashMap<Long, Track[]>();

		final List<Track> allTrackList = getTrackList(allTracks);
		final Map<Long, Track> trackMap = getTrackMap(allTrackList);

		for (final Playlist plist : playlists) {

			final List<Track> trackList = new ArrayList<Track>();

			for (final PlaylistItem item : plist.getPlaylistItems()) {
				trackList.add(trackMap.get(item.getTrackId()));
			}

			playlistTrackMap.put(plist.getPlaylistId(),
					trackList.toArray(new Track[trackList.size()]));
		}

		return playlistTrackMap;
	}

	/**
	 * 
	 * For the list of {@link Track Tracks} passed, a Map is returned with
	 * trackId as key and corresponding {@link Track} object as value.
	 * 
	 * @param allTrackList
	 * @return {@link Map}
	 */
	public static Map<Long, Track> getTrackMap(final List<Track> allTrackList) {
		final Map<Long, Track> trackMap = new HashMap<Long, Track>();
		for (final Track track : allTrackList) {
			trackMap.put(track.getTrackId(), track);
		}
		return trackMap;
	}

	/**
	 * Converts Array of Tracks to List of Tracks.
	 * 
	 * @param tracks
	 * @return {@link List}
	 */
	public static List<Track> getTrackList(final Track[] tracks) {
		final List<Track> allTrackList = new ArrayList<Track>();
		for (final Track track : tracks) {
			allTrackList.add(track);
		}
		return allTrackList;
	}

}
