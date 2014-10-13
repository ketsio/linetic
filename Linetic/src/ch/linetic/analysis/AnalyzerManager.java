package ch.linetic.analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.linetic.user.UserInterface;

public class AnalyzerManager implements AnalyzerManagerInterface {
	
	private Collection<AnalyzerInterface> analyzers;
	private Map<Integer, Map<AnalyzerInterface, Float>> results;
	
	public AnalyzerManager() {
		analyzers = Analyzer.ALL;
		results = new HashMap<>();
	}

	@Override
	public void analyze(UserInterface user) {
		Map<AnalyzerInterface, Float> userResults = new HashMap<>();
		Float value;
		
		for (AnalyzerInterface analyzer : analyzers) {
			value = analyzer.analyze(user.getMovement());
			userResults.put(analyzer, value);
		}
		
		results.put(user.getId(), userResults);
	}

	@Override
	public void analyze(Collection<UserInterface> users) {
		for (UserInterface user : users) {
			analyze(user);
		}
	}

	@Override
	public Map<AnalyzerInterface, Float> getResult(UserInterface user)
			throws UserNotFoundException {
		
		if ( user == null || !results.containsKey(user.getId()) )
			throw new UserNotFoundException();
		
		return results.get(user.getId());
	}

}
