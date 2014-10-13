package ch.linetic.gesture;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.linetic.gesture.PoseInterface.JointType;
import processing.core.PVector;

public class PoseTest {
	
	PoseInterface pose;
	
	@Before
	public void createPose() {
		 pose = new Pose();
	}

	//-----------//
	//---Tests---//
	//-----------//

	@Test
	public void testDefaultJointValues() {
		for (JointType jointType : JointType.values()) {
			assertEq(pose.getJoint(jointType), 0, 0, 0);
		}
	}

	@Test
	public void testSettingJoints() {
		pose.setJoint(JointType.HAND_LEFT, new Joint(new PVector(1,2,3)));
		assertEq(pose.getJoint(JointType.HAND_LEFT), 1, 2, 3);
	}
	
	@Test
	public void testPoseDoNotContainsNullJoints() {
		for (JointType jointType : JointType.values()) {
			assertNotNull(pose.getJoint(jointType));
		}
	}
	
	@Test
	public void testCenterOfMass() {
		pose.setJoint(JointType.HAND_LEFT, new Joint(new PVector(1,2,3)));
		pose.setJoint(JointType.HAND_RIGHT, new Joint(new PVector(-1,0,-3)));
		
		double expectedY = 2d / (double) JointType.values().length;
		assertEq(pose.getCenterOfMass(), 0, expectedY, 0);
	}

	//-----------------------//
	//---Auxiliary-Methods---//
	//-----------------------//
	
	private void assertEq(Joint actual, double expectedX, double expectedY, double expectedZ) {
		assertEquals(expectedX, actual.x, 0.0000001);
		assertEquals(expectedY, actual.y, 0.0000001);
		assertEquals(expectedZ, actual.z, 0.0000001);
	}

}
