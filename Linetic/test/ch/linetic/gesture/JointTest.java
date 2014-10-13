package ch.linetic.gesture;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import processing.core.PVector;

public class JointTest {
	
	Joint joint;
	
	@Before
	public void createJoint() {
		joint = new Joint();
	}

	//-----------//
	//---Tests---//
	//-----------//

	@Test
	public void testDefaultJointIsOrigin() {
		assertEquals(0, joint.x, 0);
		assertEquals(0, joint.y, 0);
		assertEquals(0, joint.z, 0);
	}
	
	@Test
	public void testJointInitializedWithPVector() {
		PVector vector = new PVector(1,2,3);
		Joint joint = new Joint(vector);
		
		assertEquals(1, joint.x, 0);
		assertEquals(2, joint.y, 0);
		assertEquals(3, joint.z, 0);
	}
	
	@Test
	public void testAngleMethod() {
		Joint joint1 = new Joint(10,-2,3);
		Joint joint2 = new Joint(-10,2,-3);
		assertEquals(180.0, joint1.angle(joint2), 0.1);

		joint1 = new Joint(87,0,0);
		joint2 = new Joint(0,0,34);
		assertEquals(90.0, joint1.angle(joint2), 0.1);
	}
	
	@Test
	public void testAngleMethodWithNoMagnitudeReturnsZero() {
		Joint that = new Joint(1,0,1);
		assertEquals(0, joint.angle(that), 0.1);
	}

}
