package graphing;

import maths.PhoneData;

public interface TrackChangeListener {
	public abstract String previousTrackName();
	public abstract String nextTrackName();
	public abstract PhoneData[] setTrack();
}
