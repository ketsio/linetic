package ch.linetic.analysis;

import java.util.Collection;
import java.util.Map;

import ch.linetic.user.UserInterface;

/**
 * interface that manages the analyzers.
 * All the analyzers that this interface manage must be given when instantiated or 
 * accessible through a static variable. (e.g. <code>Analyzer.ALL</code>)
 * 
 * Manage the analyzers by computing all the result for a given user and
 * returning the results as a Map.
 * It should also manage sending the result to the connected clients.
 * @author ketsio
 *
 */
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
