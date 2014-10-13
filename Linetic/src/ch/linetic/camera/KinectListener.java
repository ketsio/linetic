package ch.linetic.camera;

import SimpleOpenNI.SimpleOpenNI;

public interface KinectListener {

	public void onNewUser(SimpleOpenNI kinect, int userId);
	public void onLostUser(SimpleOpenNI kinect, int userId);
}
