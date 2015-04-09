package com.gps.itunes.lib.tasks.progressinfo;

/**
 * Represents the Track Copy failure information.
 * <br/>
 * Created by leogps on 11/29/14.
 */
public class CopyTrackFailureInformation extends CopyTrackInformation {

    private final int failedCount;
    private final String failureMessage;
    private final Exception failureException;

    public CopyTrackFailureInformation(int progress, int trackCount, int currentTrackNo, String currentTrack, String toDest,
                                       String failureMessage, int failedCount, Exception failureException) {
        super(progress, trackCount, currentTrackNo, currentTrack, toDest);
        this.failedCount = failedCount;
        this.failureMessage = failureMessage;
        this.failureException = failureException;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public Exception getFailureException() {
        return failureException;
    }
}
