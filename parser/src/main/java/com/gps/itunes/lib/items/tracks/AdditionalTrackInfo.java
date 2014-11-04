package com.gps.itunes.lib.items.tracks;

import java.util.HashMap;
import java.util.Map;

import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.types.*;

/**
 * All the info about an Itunes Track that is not available in the {@link Track}
 * object is made available here.
 * 
 * @author leogps
 * 
 */
public class AdditionalTrackInfo {

	private final Map<String, String> trackInfoMap = new HashMap<String, String>();
	private final long trackId;

	public AdditionalTrackInfo(final long trackId, final Key[] keyObjects)
			throws NoChildrenException {
		this.trackId = trackId;
		if (keyObjects.length > 0) {
			parseAdditionalInfo(keyObjects);
		}
	}

	private void parseAdditionalInfo(final Key[] keyObjects)
			throws NoChildrenException {

		for (final Key key : keyObjects) {

			switch (key.getKeyValue().getType()) {
			case STRING:
				trackInfoMap.put(key.getKeyName(),
						((LString) key.getKeyValue()).getValue());
				break;

			case INTEGER:
				trackInfoMap.put(key.getKeyName(), String
						.valueOf(((LInteger) key.getKeyValue()).getValue()));
				break;

			case DATE:
				trackInfoMap.put(key.getKeyName(),
						String.valueOf(((LDate) key.getKeyValue()).getValue()));
				break;

            case TRUE:
                trackInfoMap.put(key.getKeyName(),
                        String.valueOf(((True) key.getKeyValue()).getValue()));
                break;
			}
		}

	}

	/**
	 * Returns all the additional info available for this Track.
	 * 
	 * @return {@link String}
	 */
	public String getAllAdditionalInfo() {
		return this.toString();
	}

	/**
	 * Takes a particular information as the parameter and returns the value.
	 * 
	 * @param whichInfo
	 * @return {@link String}
	 */
	public String getAdditionalInfo(final String whichInfo) {
		return trackInfoMap.get(whichInfo);
	}

	@Override
	public String toString() {
		return "trackId = " + trackId + "; AdditionalInfo: "
				+ trackInfoMap.toString();
	}

}
