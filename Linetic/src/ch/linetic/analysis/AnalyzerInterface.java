package ch.linetic.analysis;

import ch.linetic.gesture.MovementInterface;

public interface AnalyzerInterface {

	final static float RANGE_MIN = 0;
	final static float RANGE_MAX = 100;

	/**
	 * Equivalent to :
	 * <code>analyze(movement, true);</code>
	 */
	public float analyze(MovementInterface movement);
	
	/**
	 * Analyze the movement in its own way
	 * @param movement : the movement to be analyzed
	 * @param rescaled : if the value is rescaled to be between <code>RANGE_MIN</code> and <code>RANGE_MAX</code>
	 * @return the result of the analysis (raw or rescaled)
	 */
	public float analyze(MovementInterface movement, boolean rescaled);
	
	/**
	 * Getter
	 * @return the name of the analyzer
	 */
	public String name();

	/**
	 * Getter
	 * @return
	 */
	public String getSlug();

	/**
	 * Getter
	 * @return
	 */
	public boolean doTrigger(float finalValue);
}
