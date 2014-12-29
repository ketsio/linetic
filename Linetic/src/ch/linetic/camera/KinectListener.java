package ch.linetic.camera;

import SimpleOpenNI.SimpleOpenNI;

/**
 * a kinect listener is able to automatically call those functions when :
 * - a new user is detected
 * - an existing user is lost
 * @author ketsio
 *
 */
public interface KinectListener {

	/**
	 * Triggered when a new user is detected
	 * @param kinect the kinect that has detected the user
	 * @param userId the identifier of the user that has been detected
	 */
	public void onNewUser(SimpleOpenNI kinect, int userId);

	/**
	 * Triggered when an existing user is lost
	 * @param kinect the kinect that has lost the user
	 * @param userId the identifier of the user that has been lost
	 */
	public void onLostUser(SimpleOpenNI kinect, int userId);
}
