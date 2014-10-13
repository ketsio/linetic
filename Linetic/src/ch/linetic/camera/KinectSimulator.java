package ch.linetic.camera;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import ch.linetic.gesture.Joint;
import ch.linetic.gesture.Pose;
import ch.linetic.gesture.PoseInterface;
import ch.linetic.gesture.PoseInterface.JointType;
import ch.linetic.user.UserInterface;

public class KinectSimulator implements CameraInterface {

	private static final Random rand = new Random();
	private PGraphics pg;
	
	public KinectSimulator(PApplet parrent) {
		this.pg = parrent.createGraphics(640, 480);
		pg.beginDraw();
		pg.background(0);
		pg.textAlign(PApplet.CENTER, PApplet.CENTER);
		pg.color(255);
		pg.text("Kinect Simulator", pg.width / 2, pg.height / 2);
		pg.endDraw();
	}
	
	@Override
	public void update() {
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public PoseInterface capture(UserInterface user) {
		return randomPose();
	}

	@Override
	public PImage getImage() {
		return pg.get();
	}

	
	// RANDOM
	
	private PoseInterface randomPose() {
		PoseInterface pose = new Pose();
		for (JointType jointType : JointType.values()) {
			pose.setJoint(jointType, randomJoint());
		}
		return pose;
	}
	
	private Joint randomJoint() {
		return new Joint(rand.nextInt(300),rand.nextInt(300),rand.nextInt(300));
	}

}
