package ch.linetic.analysis;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ch.linetic.analysis.analyzers.*;
import ch.linetic.gesture.Joint;
import ch.linetic.gesture.MovementInterface;

public abstract class Analyzer implements AnalyzerInterface {

	private static final long FRAMES_TO_WAIT_BEFORE_ADJUSTING = 20;

	protected final int index;
	protected double minValue;
	protected double maxValue;

	protected double minBoundary;
	protected double maxBoundary;
	protected final boolean isReversed;

	protected boolean doAdjust;
	protected Statistics stats;

	// ---------------//
	// ---Analyzers---//
	// ---------------//

	public final static AnalyzerInterface SPEED = new SpeedAnalyzer(0);
	public final static AnalyzerInterface SHAKINESS = new ShakinessAnalyzer(1);
	public final static AnalyzerInterface HAND_SHAKINESS = new HandShakinessAnalyzer(2);
	public final static AnalyzerInterface HANDS_PROXIMITY = new HandsProximityAnalyzer(3);
	public final static AnalyzerInterface REVERSE = new OrientationAnalyzer(4);
	public final static AnalyzerInterface CLAP = new ClapAnalyzer(5);

	public final static List<AnalyzerInterface> ALL = Arrays.asList(SPEED,
			SHAKINESS, HAND_SHAKINESS, HANDS_PROXIMITY, REVERSE, CLAP);

	// -------------//
	// -------------//
	// -------------//

	protected Analyzer(int index, float minValue, float maxValue, boolean doAdjust) {
		this.index = index;

		if (minValue <= maxValue) {
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.minBoundary = minValue;
			this.maxBoundary = maxValue;
			isReversed = false;
		} else {
			this.minValue = maxValue;
			this.maxValue = minValue;
			this.minBoundary = maxValue;
			this.maxBoundary = minValue;
			isReversed = true;
		}

		this.stats = new Statistics();
		this.doAdjust = doAdjust;
	}

	protected Analyzer(int index, float minValue, float maxValue) {
		this(index, minValue, maxValue, true);
	}

	@Override
	public float analyze(MovementInterface movement) {
		return analyze(movement, true);
	}

	@Override
	public float analyze(MovementInterface movement, boolean rescaled) {
		if (movement.size() < 1) {
			return 0;
		}
		float rawValue = compute(movement);
		if (doAdjust) {
			stats.addData(rawValue);
			adjust(rawValue);
		}
		return rescaled ? rescale(rawValue) : rawValue;
	}

	protected abstract float compute(MovementInterface movement);

	// -------------//
	// ---Getters---//
	// -------------//

	/** @return (int) : The index of the analyzer */
	public int index() {
		return this.index;
	}

	// ---------------//
	// ---Adjusting---//
	// ---------------//

	protected float rescale(float x) {
		double minValue = isReversed ? this.maxValue : this.minValue;
		double maxValue = isReversed ? this.minValue : this.maxValue;

		x -= minValue;
		x *= (RANGE_MAX - RANGE_MIN) / (maxValue - minValue);
		x += RANGE_MIN;
		x = x < RANGE_MIN ? RANGE_MIN : x;
		x = x > RANGE_MAX ? RANGE_MAX : x;
		return x;
	}

	protected void adjust(float raw) {
		if (stats.getSize() > FRAMES_TO_WAIT_BEFORE_ADJUSTING) {
			this.minValue = adjustMinValue();
			this.maxValue = adjustMaxValue();
			/*System.out.println(">> " + name() + ": "
					+ (isReversed ? "(reversed) " : " ") + minValue + " / "
					+ maxValue);*/
		}
	}

	protected double adjustMinValue() {
		double newMinValue;
		newMinValue = stats.getMinOfConfidenceInterval();
		newMinValue = Math.max(newMinValue, minBoundary);
		newMinValue -= Math.abs(newMinValue - minBoundary) / 2.0;
		return newMinValue;
	}

	protected double adjustMaxValue() {
		double newMaxValue;
		newMaxValue = stats.getMaxOfConfidenceInterval();
		newMaxValue = Math.min(newMaxValue, maxBoundary);
		newMaxValue += Math.abs(newMaxValue - maxBoundary) / 2.0;
		return newMaxValue;
	}

	// -------------//
	// ---Helpers---//
	// -------------//

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
