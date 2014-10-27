package ch.linetic.analysis.analyzers;

import java.util.Iterator;

import ch.linetic.analysis.Analyzer;
import ch.linetic.gesture.Joint;
import ch.linetic.gesture.MovementInterface;
import ch.linetic.gesture.PoseInterface.JointType;

public final class ClapAnalyzer extends Analyzer {

	public final static float MIN_VALUE = 0;
	public final static float MAX_VALUE = 3;
	private static final int NBR_LAST_POSES = 15;
	private static final float MIN_DISTANCE_CLAP = 60;
	private static final float MIN_DISTANCE_TO_ACTIVATE_CLAP = 60;
	
	public ClapAnalyzer(int index) {
		super(index, MIN_VALUE, MAX_VALUE, false);
	}

	@Override
	protected float compute(MovementInterface movement) {
		if (movement.size() < 1) {
			return 0;
		}
		
		int state = 0;
		
		Iterator<Joint> leftH = movement.getJointMovement(JointType.HAND_LEFT, NBR_LAST_POSES).iterator();
		Iterator<Joint> rightH = movement.getJointMovement(JointType.HAND_RIGHT, NBR_LAST_POSES).iterator();
		
		float previousDistance = 0, currentDistance = 0;
		float previousDistPoint = 0;
		boolean firstIteration = true;
		
		while(leftH.hasNext() && rightH.hasNext()) {
			if (firstIteration) {
				previousDistance = leftH.next().dist(rightH.next());
				previousDistPoint = previousDistance;
				firstIteration = false;
				continue;
			}
			
			currentDistance = leftH.next().dist(rightH.next());
			switch (state) {
			case 0:
				if (currentDistance <= MIN_DISTANCE_TO_ACTIVATE_CLAP) {
					state = 1;
					previousDistPoint = currentDistance;
				}
				break;
			case 1:
				if (currentDistance < previousDistPoint) {
					previousDistPoint = currentDistance;
				}
				if (currentDistance - previousDistPoint >= MIN_DISTANCE_CLAP) {
					state = 2;
					previousDistPoint = currentDistance;
				}
				break;
			case 2:
				if (currentDistance > previousDistPoint) {
					previousDistPoint = currentDistance;
				}
				if (previousDistPoint - currentDistance >= MIN_DISTANCE_CLAP) {
					state = 3;
				}
				break;

			default:
				break;
			}
			
			previousDistance = currentDistance;
		}
		return state;
	}

	@Override
	public String name() {
		return "Clap";
	}

}
