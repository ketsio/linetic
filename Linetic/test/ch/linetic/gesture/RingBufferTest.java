package ch.linetic.gesture;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.linetic.gesture.PoseInterface.JointType;

public class RingBufferTest {
	
	MovementInterface ringBuffer;
	int expectedIndexSize = 0;
	
	@Before
	public void createBuffer() {
		ringBuffer = new RingBuffer();
	}

	//-----------//
	//---Tests---//
	//-----------//

	@Test
	public void testInitialSizeIsZero() {
		assertEquals(0, ringBuffer.size(), 0);
	}
	
	@Test
	public void testJointMovementsAreNotNull() {
		for (JointType jointType : PoseInterface.JointType.values()) {
			Collection<Joint> joints = ringBuffer.getJointMovement(jointType);
			assertNotNull(joints);
		}
	}

	@Test
	public void testSizeIncreasesAfterPushingAndStops() {
		ringBuffer.push(new Pose());
		increaseIndexSize(1);
		assertEquals(expectedIndexSize, ringBuffer.size(), 0);
		
		ringBuffer.push(new Pose());
		increaseIndexSize(1);
		assertEquals(expectedIndexSize, ringBuffer.size(), 0);

		List<PoseInterface> list = smallBlankMovement();
		ringBuffer.push(list);
		increaseIndexSize(list.size());
		assertEquals(expectedIndexSize, ringBuffer.size(), 0);
		
		List<PoseInterface> list2 = blankMovement();
		ringBuffer.push(list2);
		increaseIndexSize(list2.size());
		assertEquals(expectedIndexSize, ringBuffer.size(), 0);
	}

	@Test
	public void testCircleBufferPoseStayAndDisappear() {
		ringBuffer.push(poseA());
		assertPos(JointType.HAND_LEFT, 0, 1, 2, 3);
		
		// PoseA stays at position 0 at every push
		for (int i = 0; i < MovementInterface.MAX_NBR_POSES - 1; i++) {
			ringBuffer.push(new Pose());
			assertPos(JointType.HAND_LEFT, 0, 1, 2, 3);
		}
		
		// PoseA is no longer in the buffer
		ringBuffer.push(new Pose());
		for (Joint actual : ringBuffer.getJointMovement(JointType.HAND_LEFT)) {
			assertEquals(0, actual.x, 0.0000001);
			assertEquals(0, actual.y, 0.0000001);
			assertEquals(0, actual.z, 0.0000001);
		}
	}
	
	@Test
	public void testCircleBufferPoseMoveWhenBufferIsFull() {
		int expectedIndex = MovementInterface.MAX_NBR_POSES - 1;
		
		ringBuffer.push(blankMovement());
		ringBuffer.push(poseA());
		assertPos(JointType.HAND_LEFT, expectedIndex, 1, 2, 3);
		
		// PoseA moves at every push (from MAX-1 to 0)
		for (int i = 0; i < MovementInterface.MAX_NBR_POSES - 1; i++) {
			ringBuffer.push(new Pose());
			--expectedIndex;
			assertPos(JointType.HAND_LEFT, expectedIndex, 1, 2, 3);
		}
		
		// PoseA is no longer in the buffer
		ringBuffer.push(new Pose());
		for (Joint actual : ringBuffer.getJointMovement(JointType.HAND_LEFT)) {
			assertEquals(0, actual.x, 0.0000001);
			assertEquals(0, actual.y, 0.0000001);
			assertEquals(0, actual.z, 0.0000001);
		}
	}

	//-----------------------//
	//---Auxiliary-Methods---//
	//-----------------------//
	
	private void increaseIndexSize(int i) {
		if (expectedIndexSize + i < MovementInterface.MAX_NBR_POSES)
			expectedIndexSize += i;
		else
			expectedIndexSize = MovementInterface.MAX_NBR_POSES;
	}
	
	private void assertPos(JointType jointType, int pos, int expectedX, int expectedY, int expectedZ) {
		Collection<Joint> list = ringBuffer.getJointMovement(jointType);
		Joint[] joints = list.toArray(new Joint[JointType.values().length]);
		Joint actual = joints[pos];
		
		assertEquals(expectedX, actual.x, 0.0000001);
		assertEquals(expectedY, actual.y, 0.0000001);
		assertEquals(expectedZ, actual.z, 0.0000001);
	}
	
	private static PoseInterface poseA() {
		Pose pose = new Pose();
		for (JointType jointType : PoseInterface.JointType.values()) {
			pose.setJoint(jointType, new Joint(1,2,3));
		}
		return pose;
	}

	private static List<PoseInterface> smallBlankMovement() {
		List<PoseInterface> list = new ArrayList<>(MovementInterface.MAX_NBR_POSES);
		for (int i = 0; i < 5; i++) {
			list.add(new Pose());
		}
		return list;
	}
	
	private static List<PoseInterface> blankMovement() {
		List<PoseInterface> list = new ArrayList<>(MovementInterface.MAX_NBR_POSES);
		for (int i = 0; i < MovementInterface.MAX_NBR_POSES; i++) {
			list.add(new Pose());
		}
		return list;
	}

}
