package com.gps.itunes.lib.items.playlists;

/**
 * Object representing an Itunes Playlist.
 * 
 * @author leogps
 *
 */

public class Playlist {

	private final String name;
	private final long playlistId;
	private final String persistentId;
	private final boolean isMaster;
	private final boolean isAllItems;
	private final boolean isVisible;
	private final PlaylistItem[] playlistItems;

	public Playlist(final String name, final long playlistId,
			final String persistentId, final boolean isMaster,
			final boolean isAllItems, final boolean isVisible,
			final PlaylistItem... playlistItems) {
		this.name = name;
		this.playlistId = playlistId;
		this.persistentId = persistentId;
		this.isMaster = isMaster;
		this.isAllItems = isAllItems;
		this.isVisible = isVisible;
		this.playlistItems = playlistItems;
	}

	
	/**
	 * Returns array of trackIds this playlist contains as {@link PlaylistItem}
	 * objects
	 * 
	 * @return array of {@link PlaylistItem PlaylistItems}
	 */
	public PlaylistItem[] getPlaylistItems() {
		return playlistItems;
	}

	/**
	 * Returns the name of this playlist
	 * 
	 * @return {@link String}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the unique playlistId set by Itunes
	 * 
	 * @return {@link long}
	 */
	public long getPlaylistId() {
		return playlistId;
	}

	/**
	 * Returns master info set by Itunes
	 * 
	 * @return {@link boolean}
	 */
	public boolean isMaster() {
		return isMaster;
	}

	/**
	 * If this returns true that means this playlist contains all the items in
	 * the library.
	 * 
	 * @return {@link boolean}
	 */
	public boolean isAllItems() {
		return isAllItems;
	}
	
	/**
	 * Returns visibilty info set by Itunes
	 * 
	 * @return {@link boolean}
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Returns persistenceId set by Itunes
	 * 
	 * @return {@link String}
	 */
	public String getPersistentId() {
		return persistentId;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();

		buffer.append("Name: " + name);
		buffer.append("; playlistId: " + playlistId);
		buffer.append("; persistentId: " + persistentId);
		buffer.append("; isMaster: " + isMaster);
		buffer.append("; isAllItems: " + isAllItems);
		buffer.append("; isVisible: " + isVisible);
		buffer.append("; PlaylistItems [\n");
		for (final PlaylistItem item : playlistItems) {
			buffer.append("; playlistItem: " + item);
		}
		buffer.append("\n]");
		return buffer.toString();
	}
}
