package com.gps.itunes.lib.tasks;

import java.util.ArrayList;
import java.util.List;

import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.items.playlists.PlaylistItem;
import com.gps.itunes.lib.items.tracks.Track;
import com.gps.itunes.lib.types.LibraryObject;

/**
 * 
 * Parses the Tracks
 * 
 * @author leogps
 *
 */
public class TracksParser {

	private final LibraryObject libObject;

	/**
	 * Creates the {@link TracksParser} object for the {@link LibraryObject}
	 * specified.
	 * 
	 * @param libObject
	 */
	public TracksParser(final LibraryObject libObject) {
		this.libObject = libObject;
	}

	/**
	 * Parses all the tracks in the Library as an array of {@link PlaylistItem}s
	 * 
	 * @return array of {@link PlaylistItem}s
	 * @throws com.gps.itunes.lib.exceptions.NoChildrenException
	 */
	public PlaylistItem[] getAllItems() throws NoChildrenException {

		final List<PlaylistItem> allItemlist = new ArrayList<PlaylistItem>();

		final Track[] tracks = new TracksRetriever(libObject).getTracks();

		for (final Track track : tracks) {
			allItemlist.add(new PlaylistItem(track.getTrackId()));
		}

		return allItemlist.toArray(new PlaylistItem[allItemlist.size()]);
	}
}
