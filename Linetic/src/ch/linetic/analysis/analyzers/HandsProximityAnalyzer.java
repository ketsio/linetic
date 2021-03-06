package ch.linetic.analysis.analyzers;

import java.util.Iterator;

import ch.linetic.analysis.Analyzer;
import ch.linetic.gesture.Joint;
import ch.linetic.gesture.MovementInterface;
import ch.linetic.gesture.PoseInterface.JointType;

/**
 * Analyze the proximity of the hands
 * <code>MIN_VALUE</code> and <code>MAX_VALUE</code> can be adjust to modify the zone
 * where the hands are considered as close to each other
 * @author ketsio
 *
 */
public class HandsProximityAnalyzer extends Analyzer {

	public final static float MIN_VALUE = 300;
	public final static float MAX_VALUE = 30;
	public final static int NBR_LAST_POSES = 5;

	public HandsProximityAnalyzer(int index) {
		super(index, MIN_VALUE, MAX_VALUE);
	}

	@Override
	protected float compute(MovementInterface movement) {
		if (movement.size() < 1) {
			return 0;
		}
		
		float accumulator = 0;
		
		Iterator<Joint> leftH = movement.getJointMovement(JointType.HAND_LEFT, NBR_LAST_POSES).iterator();
		Iterator<Joint> rightH = movement.getJointMovement(JointType.HAND_RIGHT, NBR_LAST_POSES).iterator();
		
		while(leftH.hasNext() && rightH.hasNext()) {
			accumulator += leftH.next().dist(rightH.next());
		}
		accumulator /= NBR_LAST_POSES;
		return accumulator;
	}

	@Override
	public String name() {
		return "Hands Proximity";
	}

	@Override
	public String getSlug() {
		return "hands_proximity";
	}

	@Override
	public boolean doTrigger(float finalValue) {
		return finalValue >= 0;
	}
}
