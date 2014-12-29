package ch.linetic.camera;

import processing.core.PImage;
import ch.linetic.gesture.PoseInterface;
import ch.linetic.user.UserInterface;

/**
 * The camera interface encapsulates the role of a camera by describing
 * what we need from that camera.
 * In our case, we need the camera to be able to update itself but
 * we also need it to capture the pose of a user
 * @author ketsio
 *
 */
public interface CameraInterface {
	
	/**
	 * Simply updates the camera (next image)
	 */
	public void update();
	
	/**
	 * Check if the camera is connected
	 * @return true if connected, false otherwise
	 */
	public boolean isConnected();
	
	/**
	 * Capture the pose (position of all parts of the body) given a user
	 * @param user the user being analyzed
	 * @return the actual pose of the user
	 */
	public PoseInterface capture(UserInterface user);
	
	/**
	 * Take a picture of the actual scene
	 * @return a PImage (image in Processing)
	 */
	public PImage getImage();
}
