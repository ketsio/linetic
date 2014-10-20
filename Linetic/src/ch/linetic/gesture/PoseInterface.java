package ch.linetic.gesture;

public interface PoseInterface {
	
	public enum JointType {
		HEAD(0),
		NECK(1),
		SHOULDER_LEFT(2),
		SHOULDER_RIGHT(3),
		ELBOW_LEFT(4),
		ELBOW_RIGHT(5),
		HAND_LEFT(6),
		HAND_RIGHT(7),
		TORSO(8);
		
		int index = 0;
		
		JointType(int i) {
			this.index = i;
		}
		
		public int index() {
			return index;
		}
	}
	
	
	public Joint getJoint(JointType joint);
	public void setJoint(JointType handLeft, Joint joint);
	public Joint getCenterOfMass();
}
