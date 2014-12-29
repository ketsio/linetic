package ch.linetic.gesture;

import processing.core.PVector;

/**
 * Implementation of the PoseInterface
 * @author ketsio
 *
 */
public class Pose implements PoseInterface {
	
	private final Joint[] joints;
	
	/**
	 * By default, all the joints of the pose have zero for their 3 coordinates
	 */
	public Pose() {
		this.joints = new Joint[JointType.values().length];
		for (int i = 0; i < joints.length; i++) {
			joints[i] = new Joint();
		}
	}

	@Override
	public Joint getJoint(JointType jointType) {
		return joints[jointType.index()];
	}

	@Override
	public void setJoint(JointType jointType, Joint joint) {
		joints[jointType.index()] = joint;
	}

	@Override
	public Joint getCenterOfMass() {
		PVector accumulator = new PVector(0, 0, 0);
		Joint joint;
		for (int i = 0; i < joints.length; i++) {
			joint = joints[i];
			accumulator.add(joint.x, joint.y, joint.z);
		}
		accumulator.div(joints.length);
		return new Joint(accumulator);
	}

}
