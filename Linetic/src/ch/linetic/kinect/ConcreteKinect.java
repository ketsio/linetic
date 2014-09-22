package ch.linetic.kinect;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;
import ch.linetic.gesture.Pose;
import ch.linetic.settings.Default;
import ch.linetic.user.User;
import ch.linetic.user.UserMap;

public class ConcreteKinect extends Kinect {
	
	private boolean mirrored = false;

	ConcreteKinect(SimpleOpenNI kinect, PApplet parrent, UserMap users) {
		super(parrent, users);
		this.kinect = kinect;
	}
	
	
	public Pose capturePose(User user) {

	    Pose pose = new Pose();

	    PVector jointNeck3D = new PVector();
	    PVector jointLeftShoulder3D = new PVector();
	    PVector jointLeftElbow3D = new PVector();
	    PVector jointLeftHand3D = new PVector();
	    PVector jointRightShoulder3D = new PVector();
	    PVector jointRightElbow3D = new PVector();
	    PVector jointRightHand3D = new PVector();

	    PVector jointNeck2D = new PVector();  
	    PVector jointLeftShoulder2D = new PVector();
	    PVector jointLeftElbow2D = new PVector();
	    PVector jointLeftHand2D = new PVector();
	    PVector jointRightShoulder2D = new PVector();
	    PVector jointRightElbow2D = new PVector();
	    PVector jointRightHand2D = new PVector();

	    // Get the joint positions
	    kinect.getJointPositionSkeleton(user.id, SimpleOpenNI.SKEL_NECK, jointNeck3D);  
	    kinect.getJointPositionSkeleton(user.id, SimpleOpenNI.SKEL_LEFT_SHOULDER, jointLeftShoulder3D);
	    kinect.getJointPositionSkeleton(user.id, SimpleOpenNI.SKEL_LEFT_ELBOW, jointLeftElbow3D);
	    kinect.getJointPositionSkeleton(user.id, SimpleOpenNI.SKEL_LEFT_HAND, jointLeftHand3D);
	    kinect.getJointPositionSkeleton(user.id, SimpleOpenNI.SKEL_RIGHT_SHOULDER, jointRightShoulder3D);
	    kinect.getJointPositionSkeleton(user.id, SimpleOpenNI.SKEL_RIGHT_ELBOW, jointRightElbow3D);
	    kinect.getJointPositionSkeleton(user.id, SimpleOpenNI.SKEL_RIGHT_HAND, jointRightHand3D);

	    kinect.convertRealWorldToProjective(jointNeck3D, jointNeck2D);
	    kinect.convertRealWorldToProjective(jointLeftShoulder3D, jointLeftShoulder2D);
	    kinect.convertRealWorldToProjective(jointLeftElbow3D, jointLeftElbow2D);
	    kinect.convertRealWorldToProjective(jointLeftHand3D, jointLeftHand2D);
	    kinect.convertRealWorldToProjective(jointRightShoulder3D, jointRightShoulder2D);
	    kinect.convertRealWorldToProjective(jointRightElbow3D, jointRightElbow2D);
	    kinect.convertRealWorldToProjective(jointRightHand3D, jointRightHand2D);

	    // Calculate relative position  
	    pose.jointLeftShoulderRelative = PVector.sub(jointLeftShoulder3D, jointNeck3D);
	    pose.jointLeftElbowRelative = PVector.sub(jointLeftElbow3D, jointNeck3D);
	    pose.jointLeftHandRelative = PVector.sub(jointLeftHand3D, jointNeck3D);
	    pose.jointRightShoulderRelative = PVector.sub(jointRightShoulder3D, jointNeck3D);
	    pose.jointRightElbowRelative = PVector.sub(jointRightElbow3D, jointNeck3D);
	    pose.jointRightHandRelative = PVector.sub(jointRightHand3D, jointNeck3D);

	    // Draw
	    pg.stroke(user.color);
	    pg.strokeWeight(5);
	    
	    pg.line(jointNeck2D.x, jointNeck2D.y, jointLeftShoulder2D.x, jointLeftShoulder2D.y);
	    pg.line(jointLeftShoulder2D.x, jointLeftShoulder2D.y, jointLeftElbow2D.x, jointLeftElbow2D.y);
	    pg.line(jointLeftElbow2D.x, jointLeftElbow2D.y, jointLeftHand2D.x, jointLeftHand2D.y);  
	    pg.line(jointNeck2D.x, jointNeck2D.y, jointRightShoulder2D.x, jointRightShoulder2D.y);
	    pg.line(jointRightShoulder2D.x, jointRightShoulder2D.y, jointRightElbow2D.x, jointRightElbow2D.y);
	    pg.line(jointRightElbow2D.x, jointRightElbow2D.y, jointRightHand2D.x, jointRightHand2D.y);

	    // Warnings
	    user.updateWarning(jointNeck2D, jointNeck3D);

	    return pose;
	}

	@Override
	public void update() {
		
		// Update the camera
		kinect.update();

		// Draw depthImageMap
		pg.beginDraw();
		pg.image(kinect.depthImage(), 0, 0);
		for (User u : users.values())
			if (kinect.isTrackingSkeleton(u.id))
				evaluateSkeleton(u);
		pg.endDraw();

	}

	private void evaluateSkeleton(User u) {
		// capture and draw
		Pose pose = capturePose(u);
		if (Default.NORM_SIZE)
			pose.normalizeSize();

		// add to the buffer
		u.fillBuffer(pose);

	}

	@Override
	public PImage screenshot() {
		return pg.get();
	}

	@Override
	public PImage screenshot(String filepath) {
	    pg.save(filepath); 
		return parrent.loadImage(filepath);
	}
	
	@Override
	public int width() {
		return kinect.depthWidth();
	}
	
	@Override
	public int height() {
		return kinect.depthHeight();
	}

	@Override
	public boolean isMirrored() {
		return mirrored;
	}

	@Override
	public void mirror() {
		mirrored = !mirrored;
	}

}
