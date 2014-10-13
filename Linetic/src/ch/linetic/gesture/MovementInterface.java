package ch.linetic.gesture;

import java.util.Collection;

public interface MovementInterface {
	
	public static final int MAX_NBR_POSES = 25;
	
	public Collection<Joint> getJointMovement(PoseInterface.JointType jointType);
	public Collection<Collection<Joint>> getMovements();

	public void push(PoseInterface pose);
	public void push(Collection<PoseInterface> poses);
	
	public int size();
	
}
