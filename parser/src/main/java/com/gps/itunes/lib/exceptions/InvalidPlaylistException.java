
package com.gps.itunes.lib.exceptions;

/**
 * Wrapper class that represents Invalid Playlist situation. 
 *
 * @author leogps
 */
public class InvalidPlaylistException extends Exception {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPlaylistException(final String message){
        super(message);
    }
    
}
