package ch.linetic.analysis;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ch.linetic.analysis.analyzers.*;
import ch.linetic.gesture.Joint;
import ch.linetic.gesture.MovementInterface;

public abstract class Analyzer implements AnalyzerInterface {

	private final int index;
	private final String name;
	
	//---------------//
	//---Analyzers---//
	//---------------//
	
	public final static AnalyzerInterface SPEED = new SpeedAnalyzer(0,"Speed");
	public final static AnalyzerInterface SHAKINESS = new ShakinessAnalyzer(1,"Shakiness");
	public final static AnalyzerInterface HAND_SHAKINESS = new HandShakinessAnalyzer(2, "Hand Shakiness");
	public final static AnalyzerInterface HANDS_PROXIMITY = new HandsProximityAnalyzer(3, "Hands Proximity");
	
	public final static List<AnalyzerInterface> ALL = Arrays.asList(
			SPEED, SHAKINESS, HAND_SHAKINESS, HANDS_PROXIMITY);

	
	//-------------//
	//-------------//
	//-------------//
	
	protected Analyzer(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	@Override
	public float analyze(MovementInterface movement) {
		return analyze(movement, true);
	}
	
	@Override
	public float analyze(MovementInterface movement, boolean rescaled) {
		return rescaled ? rescale(compute(movement)) : compute(movement);
	}
	
	protected abstract float compute(MovementInterface movement);
	protected abstract float rescale(float x);

	//-------------//
	//---Getters---//
	//-------------//
	
	@Override
	public String name() { return this.name; }
	
	/** @return (int) : The index of the analyzer */
	public int index() { return this.index; }


	//-------------//
	//---Helpers---//
	//-------------//
	
	protected float rescale(float x, float min, float max) {
		x -= min;
		x *= (RANGE_MAX - RANGE_MIN) / (max - min);
		x += RANGE_MIN;
		x = x < RANGE_MIN ? RANGE_MIN : x;
		x = x > RANGE_MAX ? RANGE_MAX : x;
		return x;
	}
	
	protected float operationPerFrame(Joint a, Joint b) {
		return a.dist(b); // Distance by default
	}
	
	protected float operationPerFrameAVG(Collection<Joint> joints) {
		assert joints.size() > 1 : "Must have at least two joints to average";
		
		float accumulator = 0;
		Joint previousJoint = null;
		
		for (Joint joint : joints) {
			if (previousJoint != null) {
				accumulator += operationPerFrame(previousJoint, joint);
			}
			previousJoint = joint;
		}
		return accumulator / (joints.size() - 1);
	}
}







