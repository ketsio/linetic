package ch.linetic.kinect;

import ch.linetic.settings.Default;
import ch.linetic.user.User;
import ch.linetic.user.UserMap;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import SimpleOpenNI.SimpleOpenNI;

public abstract class Kinect {

	SimpleOpenNI kinect = null;
	PApplet parrent;
	UserMap users;
	PGraphics pg = null;
	
	public Kinect(PApplet parrent, UserMap users) {
		this.parrent = parrent;
		this.users = users;
		this.pg = parrent.createGraphics(width(), height());
	}

	public static Kinect create(PApplet parrent, UserMap users) {
		
		SimpleOpenNI kinect;
		if (Default.MULTI_THREADING_KINECT)
			kinect = new SimpleOpenNI(parrent, SimpleOpenNI.RUN_MODE_MULTI_THREADED);
		else
			kinect = new SimpleOpenNI(parrent);

		if (kinect.isInit() == false) {
			System.out.println("Can't init SimpleOpenNI, maybe the camera is not connected!");
			return new NullKinect(parrent, users);
		}

		// Enable depthMap generation & skeleton for particular joints
		kinect.enableDepth();
		kinect.enableUser();

		return new ConcreteKinect(kinect, parrent, users);
	}
	

	public PGraphics camera() {
		return pg;
	}

	public abstract void update();
	//public abstract Pose capturePose(User user);
	public abstract PImage screenshot();
	public abstract PImage screenshot(String filepath);

	public abstract int width();
	public abstract int height();
	public abstract boolean isMirrored();
	public abstract void mirror();

	void onNewUser(SimpleOpenNI kinect, int userId) {

		User user = users.push(userId);
		kinect.startTrackingSkeleton(userId);
		user.hello();
	}

	void onLostUser(SimpleOpenNI kinect, int userId) {

		User user = users.pop(userId);
		kinect.stopTrackingSkeleton(userId);
		user.bye();
	}

}
