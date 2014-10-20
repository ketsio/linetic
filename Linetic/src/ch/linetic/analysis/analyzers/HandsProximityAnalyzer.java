package ch.linetic.analysis.analyzers;

import java.util.Iterator;

import ch.linetic.analysis.Analyzer;
import ch.linetic.gesture.Joint;
import ch.linetic.gesture.MovementInterface;
import ch.linetic.gesture.PoseInterface.JointType;

public class HandsProximityAnalyzer extends Analyzer {

	public final static float MIN_VALUE = 200;
	public final static float MAX_VALUE = 0;

	public HandsProximityAnalyzer(int index, String name) {
		super(index, name);
	}

	@Override
	protected float compute(MovementInterface movement) {
		if (movement.size() < 1) {
			return 0;
		}
		
		float accumulator = 0;
		Iterator<Joint> leftH = movement.getJointMovement(JointType.HAND_LEFT).iterator();
		Iterator<Joint> rightH = movement.getJointMovement(JointType.HAND_RIGHT).iterator();
		
		while(leftH.hasNext() && rightH.hasNext()) {
			accumulator += leftH.next().dist(rightH.next());
		}
		accumulator /= movement.size();
		return accumulator;
	}

	@Override
	protected float rescale(float x) {
		return rescale(x, MIN_VALUE, MAX_VALUE);
	}

}
