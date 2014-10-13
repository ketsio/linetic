package ch.linetic.analysis;

import java.util.Arrays;
import java.util.List;

import ch.linetic.analysis.analyzers.*;
import ch.linetic.gesture.MovementInterface;

public abstract class Analyzer implements AnalyzerInterface {

	private final int index;
	private final String name;
	
	//---------------//
	//---Analyzers---//
	//---------------//
	
	public final static AnalyzerInterface SPEED = new SpeedAnalyzer(0,"Speed");
	public final static AnalyzerInterface SHAKINESS = new ShakinessAnalyzer(1,"Shakiness");
	
	public final static List<AnalyzerInterface> ALL = Arrays.asList(SPEED, SHAKINESS);

	
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
		x -= (min - RANGE_MIN);
		x *= (RANGE_MAX - RANGE_MIN) / (max - min);
		x = x < RANGE_MIN ? RANGE_MIN : x;
		x = x > RANGE_MAX ? RANGE_MAX : x;
		return x;
	}
	
}
