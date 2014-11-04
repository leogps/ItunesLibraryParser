package com.gps.itunes.lib.tasks.progressinfo;

/**
 * Progress Information
 * 
 * @author leogps
 *
 * @param <T>
 */
public interface ProgressInformation<T> {
	
	/**
	 * Sets the progress information.
	 * 
	 * @param info
	 */
	public void setInformation(final T info);
	
	/**
	 * Gets the progress information.
	 * 
	 * @return
	 */
	public T getInformation();

}
