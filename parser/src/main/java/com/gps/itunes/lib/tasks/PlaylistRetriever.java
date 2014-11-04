package com.gps.itunes.lib.tasks;

import java.util.ArrayList;
import java.util.List;

import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.items.playlists.Playlist;
import com.gps.itunes.lib.items.playlists.PlaylistItem;
import com.gps.itunes.lib.types.Array;
import com.gps.itunes.lib.types.Dict;
import com.gps.itunes.lib.types.Key;
import com.gps.itunes.lib.types.LInteger;
import com.gps.itunes.lib.types.LString;
import com.gps.itunes.lib.types.LibraryObject;
import com.gps.itunes.lib.types.Type;

/**
 * Playlist Retriever class
 * 
 * @author leogps
 *
 */
public class PlaylistRetriever {

	private final LibraryObject libObject;
	private static final String PLAYLISTS = "Playlists";
	private static final String NAME = "Name";
	private static final String PLAYLIST_ID = "Playlist ID";
	private static final String PLAYLIST_PERSISTENT_ID = "Playlist Persistent ID";
	private static final String VISIBLE = "Visible";
	private static final String ALL_ITEMS = "All Items";
	private static final String MASTER = "Master";

	/**
	 *  Creates {@link PlaylistRetriever} object with the {@link LibraryObject} passed.
	 * @param libObject
	 */
	public PlaylistRetriever(final LibraryObject libObject) {
		this.libObject = libObject;
	}

	/**
	 * Retrieves all the playlists from the {@link LibraryObject} that is used to initialize this object.
	 * 
	 * @return
	 * @throws NoChildrenException
	 */
	public Playlist[] retrievePlaylist() throws NoChildrenException {

		final List<Playlist> playlist = new ArrayList<Playlist>();

		doRetrievePlaylist(libObject, playlist);

		return playlist.toArray(new Playlist[playlist.size()]);
	}

	private void doRetrievePlaylist(final LibraryObject library,
			final List<Playlist> playlist) throws NoChildrenException {

		for (final LibraryObject child : library.getChildren()) {

			final Key dictKey;

			if (child.isKey()
					&& (dictKey = (Key) child).getKeyName().equalsIgnoreCase(
							PLAYLISTS)) {

				final Array array = (Array) dictKey.getKeyValue();

				if (array.hasChildren()) {
					for (final LibraryObject playlistDict : array.getChildren()) {

						final Dict dict = (Dict) playlistDict;
						String name = null;
						long playlistId = 0;
						String persistentId = null;
						boolean isMaster = false;
						boolean isVisible = false;
						boolean isAllitems = false;
						PlaylistItem[] playlistItems = new PlaylistItem[0];

						if (dict.hasChildren()) {
							for (final LibraryObject dictChild : dict
									.getChildren()) {

								if (dictChild.isKey()) {

									final Key key = (Key) dictChild;

									if (key.getKeyName().equalsIgnoreCase(NAME)) {

										name = ((LString) key.getKeyValue())
												.getValue();

									} else if (key.getKeyName()
											.equalsIgnoreCase(PLAYLIST_ID)) {

										playlistId = ((LInteger) key
												.getKeyValue()).getValue();

									} else if (key.getKeyName()
											.equalsIgnoreCase(
													PLAYLIST_PERSISTENT_ID)) {

										persistentId = ((LString) key
												.getKeyValue()).getValue();

									} else if (key.getKeyName()
											.equalsIgnoreCase(VISIBLE)) {

										isVisible = (key.getType() == Type.TRUE);

									} else if (key.getKeyName()
											.equalsIgnoreCase(ALL_ITEMS)) {

										isAllitems = (key.getType() == Type.TRUE);

									} else if (key.getKeyName()
											.equalsIgnoreCase(MASTER)) {

										isMaster = (key.getType() == Type.TRUE);

									}

								} else if (dictChild.getType() == Type.ARRAY) {
									playlistItems = retrievePlaylistItems((Array) dictChild);
								}

							}
						}

						playlist.add(new Playlist(name, playlistId,
								persistentId, isMaster, isAllitems, isVisible,
								playlistItems));
						/**
						 * if (playlistItems != null && !isAllitems) {
						 * 
						 * playlist.add(new Playlist(name, playlistId,
						 * persistentId, isMaster, isAllitems, isVisible,
						 * playlistItems));
						 * 
						 * } else {
						 * 
						 * final PlaylistItem[] allItems = new TracksParser(
						 * this.libObject).getAllItems();
						 * 
						 * playlist.add(new Playlist(name, playlistId,
						 * persistentId, isMaster, isAllitems, isVisible,
						 * allItems)); }
						 **/

					}

				}

			}

			if (child.hasChildren()) {
				doRetrievePlaylist(child, playlist);
			}

		}
	}

	private PlaylistItem[] retrievePlaylistItems(final Array playlistArray)
			throws NoChildrenException {
		final List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();

		if (playlistArray.hasChildren()) {
			for (final LibraryObject child : playlistArray.getChildren()) {

				if (child.getType() == Type.DICT) {
					final Dict playlistItemDict = (Dict) child;
					final Key key = (Key) playlistItemDict.getChildren()[0];

					final long trackId = ((LInteger) key.getKeyValue())
							.getValue();
					playlistItemList.add(new PlaylistItem(trackId));

				}

			}
		}

		return playlistItemList.toArray(new PlaylistItem[playlistItemList
				.size()]);
	}
}
