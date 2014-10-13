package ch.linetic.camera;

import processing.core.PImage;
import ch.linetic.gesture.PoseInterface;
import ch.linetic.user.UserInterface;

public interface CameraInterface {
	
	public void update();
	public boolean isConnected();
	
	public PoseInterface capture(UserInterface user);
	public PImage getImage();
}
