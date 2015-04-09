package com.gps.itunes.lib.tasks.progressinfo;

import com.gps.itunes.lib.tasks.ProgressInformer;

/**
 * Created by leogps on 3/27/15.
 */
public class ProgressTracker {

    private final ProgressInformer progressInformer;

    private final ProgressInformation progressInformation;

    public ProgressTracker(ProgressInformer progressInformer, ProgressInformation progressInformation) {
        this.progressInformer = progressInformer;
        this.progressInformation = progressInformation;
    }

    public ProgressInformer getProgressInformer() {
        return progressInformer;
    }

    public ProgressInformation getProgressInformation() {
        return progressInformation;
    }
}
