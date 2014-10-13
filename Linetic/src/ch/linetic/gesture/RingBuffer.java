package ch.linetic.gesture;

import java.util.Collection;
import java.util.LinkedList;

import ch.linetic.gesture.PoseInterface.JointType;

public final class RingBuffer implements MovementInterface {
	
	private PoseInterface[] buffer;
	
	private int inputPointer = 0;
	private int outputPointer = 0;
	private int size = 0;
	
	public RingBuffer() {
		this.buffer = new PoseInterface[MAX_NBR_POSES];
	}

	@Override
	public Collection<Joint> getJointMovement(JointType jointType) {
		Collection<Joint> joints = new LinkedList<>();
		PoseInterface pose;
		int poseIndex;
		
		for (int i = 0; i < size; i++) {
			poseIndex = (i + outputPointer) % MAX_NBR_POSES;
			pose = buffer[poseIndex];
			joints.add(pose.getJoint(jointType));
		}
		return joints;
	}

	@Override
	public Collection<Collection<Joint>> getMovements() {
		Collection<Collection<Joint>> list = new LinkedList<>();
		for (JointType jointType : JointType.values()) {
			list.add(getJointMovement(jointType));
		}
		return list;
	}

	@Override
	public void push(PoseInterface pose) {
		buffer[inputPointer] = pose;
		next();
	}

	@Override
	public void push(Collection<PoseInterface> poses) {
		for (PoseInterface pose : poses) {
			push(pose);
		}
	}

	@Override
	public int size() {
		return size;
	}
	
	private void next() {
		inputPointer = (inputPointer + 1) % MAX_NBR_POSES;
		size = size < MAX_NBR_POSES ? size + 1 : MAX_NBR_POSES;
		outputPointer = size < MAX_NBR_POSES ? 0 : inputPointer;
	}
}
