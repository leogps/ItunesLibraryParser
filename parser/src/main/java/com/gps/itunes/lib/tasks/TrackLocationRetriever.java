package com.gps.itunes.lib.tasks;

import java.util.ArrayList;
import java.util.List;

import com.gps.itunes.lib.items.tracks.Track;

/**
 * Retrieves the track locations. 
 * 
 * @author leogps
 *
 */
public class TrackLocationRetriever {

	/**
	 * Returns the location array of the specified tracks.
	 * 
	 * @param tracks
	 * @return array of {@link String}
	 */
	public static String[] getTrackLocations(final Track[] tracks) {

		final List<String> locations = new ArrayList<String>();

		for (final Track track : tracks) {
			locations.add(track.getLocation());
		}

		return locations.toArray(new String[locations.size()]);
	}
}
