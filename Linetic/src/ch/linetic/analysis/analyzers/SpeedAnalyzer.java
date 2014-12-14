package ch.linetic.analysis.analyzers;

import java.util.Collection;

import ch.linetic.analysis.Analyzer;
import ch.linetic.gesture.Joint;
import ch.linetic.gesture.MovementInterface;
import ch.linetic.gesture.PoseInterface.JointType;

public final class SpeedAnalyzer extends Analyzer {

	public final static float MIN_VALUE = 0;
	public final static float MAX_VALUE = 30;
	public final static int NBR_LAST_POSES = 10;

	public SpeedAnalyzer(int index) {
		super(index, MIN_VALUE, MAX_VALUE);
	}

	@Override
	protected float compute(MovementInterface movement) {
		if (movement.size() < 2) {
			return 0;
		}
		
		float accumulator = 0;
		for (Collection<Joint> joints : movement.getMovements(NBR_LAST_POSES)) {
			accumulator += operationPerFrameAVG(joints);
		}
		accumulator /= JointType.values().length;
		return accumulator;
	}
	
	@Override
	protected float operationPerFrame(Joint a, Joint b) {
		return a.dist(b);
	}

	@Override
	public String name() {
		return "Speed";
	}

	@Override
	public String getSlug() {
		return "speed";
	}

	@Override
	public boolean doTrigger(float finalValue) {
		return finalValue >= 0;
	}
}
