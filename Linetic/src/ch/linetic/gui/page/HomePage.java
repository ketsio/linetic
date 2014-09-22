package ch.linetic.gui.page;

import ch.linetic.gui.component.KinectCameraComponent;
import ch.linetic.kinect.Kinect;
import processing.core.PApplet;

public class HomePage extends Page {
	
	KinectCameraComponent kinectCameraComponent;

	public HomePage(PApplet parrent, Kinect kinect) {
		super(parrent);
		kinectCameraComponent = new KinectCameraComponent(parrent, padding, padding, kinect);
	}

	@Override
	public void drawFrame() {
		kinectCameraComponent.draw();
	}

	@Override
	public void drawUpdate() {
		// ...
	}

}
