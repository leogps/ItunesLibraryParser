package com.gps.itunes.lib.tasks;

import java.util.ArrayList;
import java.util.List;

import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.items.tracks.AdditionalTrackInfo;
import com.gps.itunes.lib.items.tracks.Track;
import com.gps.itunes.lib.items.tracks.TrackData;
import com.gps.itunes.lib.types.Dict;
import com.gps.itunes.lib.types.Key;
import com.gps.itunes.lib.types.LInteger;
import com.gps.itunes.lib.types.LString;
import com.gps.itunes.lib.types.LibraryObject;
import com.gps.itunes.lib.types.Type;

/**
 * Retrieves the Tracks from the Library
 * 
 * @author leogps
 *
 */
public class TracksRetriever {

	private final LibraryObject libObject;

	/**
	 * Creates the {@link TracksRetriever} object for the specified {@link LibraryObject}
	 * 
	 * @param libObject
	 */
	public TracksRetriever(final LibraryObject libObject) {
		this.libObject = libObject;
	}

	/**
	 * Retrives all the Tracks in the Library 
	 * 
	 * @return array of {@link com.gps.itunes.lib.items.tracks.Track}s
	 * @throws com.gps.itunes.lib.exceptions.NoChildrenException
	 */
	public Track[] getTracks() throws NoChildrenException {
		final List<Track> trackList = new ArrayList<Track>();
		doGetTracks(libObject, trackList);
		return trackList.toArray(new Track[trackList.size()]);
	}

	private void doGetTracks(final LibraryObject libraryObject,
			final List<Track> trackList) throws NoChildrenException {

		for (final LibraryObject child : libraryObject.getChildren()) {

			final Key key;
			if (child.getType() == Type.KEY
					&& (key = (Key) child).getKeyName().equalsIgnoreCase(
							"Tracks")) {
				final Dict dict = (Dict) key.getKeyValue();

				if (dict.hasChildren()) {

					for (final LibraryObject trackInfo : dict.getChildren()) {
						if (trackInfo.getType() == Type.KEY) {
							final Key trackKey = (Key) trackInfo;
							final Dict trackDict = (Dict) trackKey
									.getKeyValue();

							trackList.add(populateTrackInfo(trackDict));
						}
					}

				}
			}
			if (child.hasChildren()) {
				doGetTracks(child, trackList);
			}
		}

	}

	private Track populateTrackInfo(Dict trackDict) throws NoChildrenException {
		long trackId = 0;
		String trackName = null;
		String location = null;
		final List<Key> additionalInfoObjects = new ArrayList<Key>();

		for (final LibraryObject track : trackDict.getChildren()) {
			if (track.getType() == Type.KEY) {
				final Key key = (Key) track;

				if (key.getKeyName().equalsIgnoreCase(
						TrackData.TRACK_ID.getName())) {
					if (key.getKeyValue().getType() == Type.INTEGER) {
						trackId = ((LInteger) key.getKeyValue()).getValue();
					}
				} else if (key.getKeyName().equalsIgnoreCase(
						TrackData.NAME.getName())) {
					if (key.getKeyValue().getType() == Type.STRING) {
						trackName = ((LString) key.getKeyValue()).getValue();
					}
				} else if (key.getKeyName().equalsIgnoreCase(
						TrackData.LOCATION.getName())) {
					if (key.getKeyValue().getType() == Type.STRING) {
						location = ((LString) key.getKeyValue()).getValue();
					}
				} else {
					additionalInfoObjects.add(key);
				}
			}
		}
		final AdditionalTrackInfo additionalTrackInfo = new AdditionalTrackInfo(
				trackId,
				additionalInfoObjects.toArray(new Key[additionalInfoObjects
						.size()]));

		return new Track(trackId, trackName, location, additionalTrackInfo);
	}
}
