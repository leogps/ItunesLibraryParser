package com.gps.itunes.lib.tasks;

import com.gps.itunes.lib.tasks.progressinfo.ProgressInformation;

/**
 * The way the progress is defined to be informed.
 * 
 * @author leogps
 *
 * @param <T>
 */
public interface ProgressInformer<T extends ProgressInformation> {
	
	/**
	 * Informs the progress.
	 * 
	 * @param t
	 */
	public void informProgress(final T t);
	

}
