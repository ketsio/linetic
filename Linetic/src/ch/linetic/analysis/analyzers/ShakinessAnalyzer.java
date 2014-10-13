package ch.linetic.analysis.analyzers;

import java.util.Collection;

import ch.linetic.analysis.Analyzer;
import ch.linetic.gesture.Joint;
import ch.linetic.gesture.MovementInterface;
import ch.linetic.gesture.PoseInterface.JointType;

public final class ShakinessAnalyzer extends Analyzer {

	public final static float MIN_VALUE = 0;
	public final static float MAX_VALUE = 180;

	public ShakinessAnalyzer(int index, String name) {
		super(index, name);
	}

	@Override
	protected float compute(MovementInterface movement) {
		if (movement.size() < 2) {
			return 0;
		}
		
		float accumulator = 0;
		for (Collection<Joint> joints : movement.getMovements()) {
			accumulator += anglePerFrameForJointAVG(joints);
		}
		accumulator /= JointType.values().length;
		return accumulator;
	}

	@Override
	protected float rescale(float x) {
		return rescale(x, MIN_VALUE, MAX_VALUE);
	}
	
	private float anglePerFrameForJointAVG(Collection<Joint> joints) {
		assert joints.size() > 1 : "Must have at least two joints to average the shakiness";
		
		float accumulator = 0;
		Joint previousJoint = null;
		
		for (Joint joint : joints) {
			if (previousJoint != null) {
				accumulator += previousJoint.angle(joint);
			}
			previousJoint = joint;
		}
		return accumulator / (joints.size() - 1);
	}

}
