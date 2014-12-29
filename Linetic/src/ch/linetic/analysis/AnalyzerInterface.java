package ch.linetic.analysis;

import ch.linetic.gesture.MovementInterface;

/**
 * The analyzer is the building block of the project.
 * Each analyzers analyzes the movement of the user in its own way.
 * The behavior of the analyzer is located in the <code>analyze()</code> function.
 * @author ketsio
 *
 */
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
	 * @return the unique slug of the analyzer (no two analyzers should have the same slug)
	 */
	public String getSlug();

	/**
	 * Check if we can trigger the result to all the clients connected
	 * given the result between <code>RANGE_MIN</code> and <code>RANGE_MAX</code>
	 * @param finalValue the final result of the analyzer
	 * @return true if we should trigger, false otherwise
	 */
	public boolean doTrigger(float finalValue);
}
