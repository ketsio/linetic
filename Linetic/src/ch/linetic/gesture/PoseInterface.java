package ch.linetic.gesture;

/**
 * A pose is composed of several Joint (one of each JointType).
 * This interface describes what a pose can offer.
 * @author ketsio
 *
 */
public interface PoseInterface {
	
	/**
	 * The enum JointType enumerates all the types of joint present in the body
	 * that the Kinect is able to recognize.
	 * @author ketsio
	 *
	 */
	public enum JointType {
		HEAD(0),
		NECK(1),
		SHOULDER_LEFT(2),
		SHOULDER_RIGHT(3),
		ELBOW_LEFT(4),
		ELBOW_RIGHT(5),
		HAND_LEFT(6),
		HAND_RIGHT(7),
		TORSO(8),
		HIP_LEFT(9),
		HIP_RIGHT(10),
		KNEE_LEFT(11),
		KNEE_RIGHT(12),
		FOOT_LEFT(13),
		FOOT_RIGHT(14);
		
		int index = 0;
		
		/**
		 * Create a JointType given an index
		 * @param i the index
		 */
		JointType(int i) {
			this.index = i;
		}
		
		/**
		 * Get the index of a JoinType
		 * Useful for implementing an array of JointType
		 * @return the index
		 */
		public int index() {
			return index;
		}
	}
	
	/**
	 * Get a Joint in a particular JointType
	 * @param jointType the type of joint (part of the body)
	 * @return a Joint
	 */
	public Joint getJoint(JointType jointType);
	
	/**
	 * Set a Joint in a particular JointType
	 * @param jointType the type of joint (part of the body)
	 * @param joint the new value for the Joint
	 */
	public void setJoint(JointType jointType, Joint joint);
	
	/**
	 * Get the center of mass of the pose
	 * @return a Joint that is the center of mass
	 */
	public Joint getCenterOfMass();
}
