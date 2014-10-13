package ch.linetic.camera;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import processing.core.PApplet;

public class KinectTest {
	
	CameraInterface kinect;
	boolean isKinectConnected = true;
	  
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void init() {
		PApplet parrent = new PApplet();
		try {
			kinect = Kinect.getNextKinect(parrent);
		} catch (NoCameraConnectedException e) {
			isKinectConnected = false;
		}
	}

	@Test
	public void testGettingPoseOfANullUserShouldThrow() {
		if (isKinectConnected) {
			kinect.capture(null);
			exception.expect(IllegalArgumentException.class);
		}
	}
	
	@Test
	public void testNullKinectIffKinectIsNotConnected() {
		if (isKinectConnected) {
			assertNotNull(kinect);
		}
		else {
			assertNull(kinect);
		}
	}

}
