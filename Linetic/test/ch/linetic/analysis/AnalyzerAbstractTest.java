package ch.linetic.analysis;

import java.util.List;
import java.util.Random;

import org.junit.Before;

import ch.linetic.gesture.Joint;
import ch.linetic.gesture.MovementInterface;
import ch.linetic.gesture.Pose;
import ch.linetic.gesture.PoseInterface;
import ch.linetic.gesture.RingBuffer;
import ch.linetic.gesture.PoseInterface.JointType;

public abstract class AnalyzerAbstractTest {
	
	protected Random rand;
	
	protected List<AnalyzerInterface> analyzers = Analyzer.ALL;
	protected MovementInterface emptyMovement;
	
	@Before
	public void createMovements() {
		emptyMovement = new RingBuffer();
		rand = new Random();
	}
	
	//-----------------------//
	//---Auxiliary-Methods---//
	//-----------------------//
	
	// PREDEFINED
	
	protected MovementInterface blankMovement() {
		MovementInterface movement = new RingBuffer();
		for (int i = 0; i < MovementInterface.MAX_NBR_POSES; i++) {
			movement.push(new Pose());
		}
		return movement;
	}
	
	protected MovementInterface movementA() {
		MovementInterface movement = new RingBuffer();
		for (int i = 0; i < MovementInterface.MAX_NBR_POSES; i++) {
			movement.push(pointPose(i));
		}
		return movement;
	}
	
	protected PoseInterface pointPose(int x) {
		PoseInterface pose = new Pose();
		for (JointType jointType : JointType.values()) {
			pose.setJoint(jointType, new Joint(x,x,x));
		}
		return pose;
	}
	
	// RANDOM
	
	protected int randomValidSize() {
		return rand.nextInt(MovementInterface.MAX_NBR_POSES - 2) + 2;
	}
	
	protected MovementInterface randomValidMovement() {
		return randomMovement(randomValidSize());
	}

	protected MovementInterface randomMovement() {
		return randomMovement(rand.nextInt(MovementInterface.MAX_NBR_POSES));
	}

	protected MovementInterface randomMovement(int size) {
		MovementInterface movement = new RingBuffer();
		for (int i = 0; i < size; i++) {
			movement.push(randomPose());
		}
		return movement;
	}
	
	protected PoseInterface randomPose() {
		PoseInterface pose = new Pose();
		for (JointType jointType : JointType.values()) {
			pose.setJoint(jointType, randomJoint());
		}
		return pose;
	}
	
	protected Joint randomJoint() {
		return new Joint(rand.nextInt(300),rand.nextInt(300),rand.nextInt(300));
	}
}
