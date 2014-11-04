package com.gps.itunes.lib.tasks.progressinfo;

/**
 * Copy Information of Tracks
 * 
 * @author leogps
 *
 */
public class CopyTrackInformation {

	private final int progress;
	private final int trackCount;
	private final int currentTrackNo;
	private final String currentTrack;
	private final String toDest;

	public CopyTrackInformation(final int progress, final int trackCount,
			final int currentTrackNo, final String currentTrack,
			final String toDest) {
		this.progress = progress;
		this.trackCount = trackCount;
		this.currentTrackNo = currentTrackNo;
		this.currentTrack = currentTrack;
		this.toDest = toDest;
	}

	/**
	 * Gets the progress
	 * 
	 * @return int
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * Gets the total number of tracks that are being copied.
	 * 
	 * @return int
	 */
	public int getTrackCount() {
		return trackCount;
	}

	/**
	 * Gets the current track number that is being copied.
	 * 
	 * @return int
	 */
	public int getCurrentTrackNo() {
		return currentTrackNo;
	}

	/**
	 * Gets the current Track name.
	 * 
	 * @return {@link String}
	 */
	public String getCurrentTrack() {
		return currentTrack;
	}

	/**
	 * Gets the current track copy destination.
	 * 
	 * @return {@link String}
	 */
	public String getToDest() {
		return toDest;
	}

}
