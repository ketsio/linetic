package ch.linetic.analysis.analyzers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.linetic.analysis.Analyzer;
import ch.linetic.gesture.Joint;
import ch.linetic.gesture.MovementInterface;
import ch.linetic.gesture.PoseInterface.JointType;

/**
 * Analyze the multiplicity of the movement
 * i.e. The more part of your body you move the bigger the result will be
 * You can keep this analyzer close to 0% if you move step by step, member by member
 * without being brutal in your moves
 * @author ketsio
 *
 */
public final class MultiplicityAnalyzer extends Analyzer {

	public final static float MIN_VALUE = 5;
	public final static float MAX_VALUE = 25;
	public final static int NBR_LAST_POSES = 15;

	public MultiplicityAnalyzer(int index) {
		super(index, MIN_VALUE, MAX_VALUE);
	}

	@Override
	protected float compute(MovementInterface movement) {
		if (movement.size() < 2) {
			return 0;
		}
		Map<JointType, Float> activeJoints = new HashMap<>();
		
		for (JointType jointType : JointType.values()) {
			Collection<Joint> joints = movement.getJointMovement(jointType, NBR_LAST_POSES);
			activeJoints.put(jointType, operationPerFrameAVG(joints));
		}

		if (activeJoints.containsKey(JointType.HAND_LEFT)
				&& activeJoints.containsKey(JointType.ELBOW_LEFT)) {
			activeJoints.put(JointType.HAND_LEFT,
					activeJoints.get(JointType.HAND_LEFT).floatValue()
							+ activeJoints.get(JointType.ELBOW_LEFT)
									.floatValue());
			activeJoints.remove(JointType.ELBOW_LEFT);
		}

		if (activeJoints.containsKey(JointType.HAND_RIGHT)
				&& activeJoints.containsKey(JointType.ELBOW_RIGHT)) {
			activeJoints.put(JointType.HAND_RIGHT,
					activeJoints.get(JointType.HAND_RIGHT).floatValue()
							+ activeJoints.get(JointType.ELBOW_RIGHT)
									.floatValue());
			activeJoints.remove(JointType.ELBOW_RIGHT);
		}

		if (activeJoints.containsKey(JointType.FOOT_LEFT)
				&& activeJoints.containsKey(JointType.KNEE_LEFT)) {
			activeJoints.put(JointType.FOOT_LEFT,
					activeJoints.get(JointType.FOOT_LEFT).floatValue()
							+ activeJoints.get(JointType.KNEE_LEFT)
									.floatValue());
			activeJoints.remove(JointType.KNEE_LEFT);
		}
		
		if (activeJoints.containsKey(JointType.FOOT_RIGHT)
				&& activeJoints.containsKey(JointType.KNEE_RIGHT)) {
			activeJoints.put(JointType.FOOT_RIGHT,
					activeJoints.get(JointType.FOOT_RIGHT).floatValue()
							+ activeJoints.get(JointType.KNEE_RIGHT)
									.floatValue());
			activeJoints.remove(JointType.KNEE_RIGHT);
		}
		
		float accumulator = 0f;
		float maxFloat = 0f;
		
		for (Map.Entry<JointType, Float> entry : activeJoints.entrySet()) {
			if (entry.getValue().floatValue() >= maxFloat) {
				maxFloat = entry.getValue().floatValue();
			}
			accumulator += entry.getValue().floatValue();
		}
		
		accumulator -= maxFloat;
		
		return accumulator / JointType.values().length;
	}
	
	@Override
	protected float operationPerFrame(Joint a, Joint b) {
		return a.dist(b);
	}

	@Override
	public String name() {
		return "Multiplicity";
	}

	@Override
	public String getSlug() {
		return "multiplicity";
	}

	@Override
	public boolean doTrigger(float finalValue) {
		return finalValue >= 0;
	}
}
