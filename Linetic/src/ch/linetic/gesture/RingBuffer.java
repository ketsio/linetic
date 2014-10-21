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
		return getMovements(size);
	}

	@Override
	public Collection<Joint> getJointMovement(JointType jointType, int sizeFromEnd) {
		sizeFromEnd = sizeFromEnd > size ? size : sizeFromEnd;
		sizeFromEnd = sizeFromEnd < 0 ? 0 : sizeFromEnd;
		
		Collection<Joint> joints = new LinkedList<>();
		PoseInterface pose;
		int poseIndex;
		
		for (int i = 0; i < sizeFromEnd; i++) {
			poseIndex = (inputPointer - sizeFromEnd + i + MAX_NBR_POSES) % MAX_NBR_POSES;
			pose = buffer[poseIndex];
			joints.add(pose.getJoint(jointType));
		}
		return joints;
	}

	@Override
	public Collection<Collection<Joint>> getMovements(int sizeFromEnd) {
		Collection<Collection<Joint>> list = new LinkedList<>();
		for (JointType jointType : JointType.values()) {
			list.add(getJointMovement(jointType, sizeFromEnd));
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
