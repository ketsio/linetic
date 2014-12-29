package ch.linetic.gesture;

import java.util.Collection;

/**
 * A movement is composed of several poses (the number is defined by the variable MAX_NBR_POSES).
 * This interface describes what a movement can offer.
 * @author ketsio
 *
 */
public interface MovementInterface {
	
	/**
	 * The maximum number of poses simultaneously analyzed
	 * That is, at each moment, we can evaluates the last X poses of the user
	 * where the maximum of X is the value assigned to MAX_NBR_POSES
	 */
	public static final int MAX_NBR_POSES = 25;
	
	/**
	 * Get a Collection of Joints according to the part of the body given
	 * Ordered by time : Least recent to more recent
	 * e.g. getJointMovement(JointType.HAND_LEFT) returns all the Positions (Joints)
	 * of the left hand during the last X poses where X is the number of poses
	 * that have been recorded (bounded by MAX_NBR_POSES)
	 *   0. Position - Oldest pose
	 *   1. Position
	 * 	 2. Position
	 * 	 ...
	 * 	 X. Position - Newest pose
	 * @param jointType The part of the body
	 * @return a Collection of Joint
	 */
	public Collection<Joint> getJointMovement(PoseInterface.JointType jointType);
	
	/**
	 * Get a collection of joint movements for each part of the body
	 * Ordered by time : Least recent to more recent
	 * That is, a collection of collections of joints
	 * e.g. :
	 * 0. HAND_LEFT :
	 * 		0. Position - Oldest pose
	 * 		1. Position
	 * 		2. Position
	 * 		...
	 * 		X. Position - Newest pose
	 * 1. HAND_RIGHT :
	 * 		0. Position - Oldest pose
	 * 		1. Position
	 * 		2. Position
	 * 		...
	 * 		X. Position - Newest pose
	 * 2. HEAD :
	 * 		...
	 * @return a Collection of Collection of Joints
	 */
	public Collection<Collection<Joint>> getMovements();

	

	/**
	 * Get a Collection of the last `sizeFromEnd` Joints according to the part of the body given
	 * Ordered by time : Least recent to more recent
	 * e.g. getJointMovement(JointType.HAND_LEFT) returns all the Positions (Joints)
	 * of the left hand during the last `sizeFromEnd` poses
	 *   0. Position - Oldest pose
	 *   1. Position
	 * 	 2. Position
	 * 	 ...
	 * 	 sizeFromEnd. Position - Newest pose
	 * @param jointType The part of the body
	 * @param sizeFromEnd The number of pose you want to get starting from the newest pose
	 * @return a Collection of Joint
	 */
	public Collection<Joint> getJointMovement(PoseInterface.JointType jointType, int sizeFromEnd);

	/**
	 * Get a collection of the last `sizeFromEnd` joint movements 
	 * for each part of the body
	 * Ordered by time : Least recent to more recent
	 * That is, a collection of collections of joints
	 * e.g. :
	 * 0. HAND_LEFT :
	 * 		0. Position - Oldest pose
	 * 		1. Position
	 * 		2. Position
	 * 		...
	 * 		sizeFromEnd. Position - Newest pose
	 * 1. HAND_RIGHT :
	 * 		0. Position - Oldest pose
	 * 		1. Position
	 * 		2. Position
	 * 		...
	 * 		sizeFromEnd. Position - Newest pose
	 * 2. HEAD :
	 * 		...
	 * @param sizeFromEnd The number of pose you want to get starting from the newest pose
	 * @return a Collection of Collection of Joints
	 */
	public Collection<Collection<Joint>> getMovements(int sizeFromEnd);

	
	/**
	 * Push a pose to the movement
	 * @param pose The pose to add to the movement
	 */
	public void push(PoseInterface pose);
	
	/**
	 * Push a whole collection of pose to the movement
	 * @param poses The collection of poses to add to the movement
	 */
	public void push(Collection<PoseInterface> poses);
	
	/**
	 * Get the size of the movement
	 * i.e. The number of poses that as been captured so far
	 * bounded by MAX_NBR_POSES
	 */
	public int size();
	
}
