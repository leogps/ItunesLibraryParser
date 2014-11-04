package com.gps.itunes.lib.items.tracks;

/**
 * Object Representation of Itunes Track.
 * 
 * @author leogps
 *
 */
public class Track {

	private final long trackId;
	private final String trackName;
	private final String location;
	private final AdditionalTrackInfo additionalTrackInfo;

	public Track(final long trackId, final String trackName,
			final String location, final AdditionalTrackInfo additionalTrackInfo) {
		this.trackId = trackId;
		this.trackName = trackName;
		this.location = location;
		this.additionalTrackInfo = additionalTrackInfo;
	}

	/**
	 * Returns the unique trackId set by Itunes
	 * 
	 * @return {@link long}
	 */
	public long getTrackId() {
		return trackId;
	}

	/**
	 * Returns the track name.
	 * 
	 * @return {@link String}
	 */
	public String getTrackName() {
		return trackName;
	}

	/**
	 * Returns the location of this track.
	 * 
	 * @return {@link String}
	 */
	public String getLocation() {
		return location;
	}

    public boolean isMovie() {
        String isMovie = getAdditionalTrackInfo().getAdditionalInfo("Movie");
        return isMovie != null && String.valueOf(Boolean.TRUE).equalsIgnoreCase(isMovie);
    }

    public boolean hasVideo() {
        String hasVideo = getAdditionalTrackInfo().getAdditionalInfo("Has Video");
        return hasVideo != null && String.valueOf(Boolean.TRUE).equalsIgnoreCase(hasVideo);
    }

	/**
	 * Returns the Additional Track Info associated with this Track.
	 * 
	 * @return {@link AdditionalTrackInfo}
	 */
	public AdditionalTrackInfo getAdditionalTrackInfo() {
		return additionalTrackInfo;
	}

	@Override
	public String toString() {
		return "TrackId: " + trackId + "; trackName: " + trackName
				+ "; location: " + location + "; " + additionalTrackInfo;
	}
}