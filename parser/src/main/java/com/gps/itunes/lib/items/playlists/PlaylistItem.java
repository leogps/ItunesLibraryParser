package com.gps.itunes.lib.items.playlists;

/**
 * Itunes Track represented as a Playlist Item. 
 * 
 * @author leogps
 *
 */
public class PlaylistItem {

	private final long trackId;

	public PlaylistItem(final long trackId) {
		this.trackId = trackId;
	}

	/**
	 * Returns the unique trackId for this PlaylistItem
	 * 
	 * @return {@link long}
	 */
	public long getTrackId() {
		return trackId;
	}

	@Override
	public String toString() {
		return "TrackId: " + trackId;
	}

}
