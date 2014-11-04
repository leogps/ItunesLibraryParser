package com.gps.itunes.lib.items.tracks;

/**
 * Track's common Info holder.
 * 
 * @author leogps
 * 
 */
public enum TrackData {
	TRACK_ID("Track ID"), NAME("Name"), LOCATION("location");

	private final String name;

	TrackData(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}