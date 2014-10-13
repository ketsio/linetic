package ch.linetic.gesture;

public interface PoseInterface {
	
	public enum JointType {
		NECK(0),
		SHOULDER_LEFT(1),
		SHOULDER_RIGHT(2),
		ELBOW_LEFT(3),
		ELBOW_RIGHT(4),
		HAND_LEFT(5),
		HAND_RIGHT(6);
		
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
