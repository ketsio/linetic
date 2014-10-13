package ch.linetic.analysis;

import java.util.Collection;
import java.util.Map;

import ch.linetic.user.UserInterface;

public interface AnalyzerManagerInterface {
	
	/**
	 * Analyze the user with all the analyzers given when instantiated
	 * @param user : the user to be analyzed
	 */
	public void analyze(UserInterface user);
	
	/**
	 * Apply <code>analyze(user)</code> for each user of the collection
	 * @param users : the collection of users to be analyzed
	 */
	public void analyze(Collection<UserInterface> users);
	
	/**
	 * Return the result of the analysis of a certain user for all the analyzers
	 * @param user : the user that has been analyzed
	 * @return an Analyzer->Result Map
	 * @throws UserNotFoundException : if the user has never been analyzed before
	 */
	public Map<AnalyzerInterface, Float> getResult(UserInterface user) throws UserNotFoundException;
}
