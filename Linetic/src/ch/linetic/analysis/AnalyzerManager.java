package ch.linetic.analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.linetic.connexion.Server;
import ch.linetic.user.UserInterface;

public class AnalyzerManager implements AnalyzerManagerInterface {
	
	private Collection<AnalyzerInterface> analyzers;
	private Map<Integer, Map<AnalyzerInterface, Float>> results;
	private Server server;
	
	public AnalyzerManager(Server server) {
		this.analyzers = Analyzer.ALL;
		this.results = new HashMap<>();
		this.server = server;
	}

	@Override
	public void analyze(UserInterface user) {
		Map<AnalyzerInterface, Float> userResults = new HashMap<>();
		Float value;
		
		for (AnalyzerInterface analyzer : analyzers) {
			// Compute the analysis
			value = analyzer.analyze(user.getMovement());
			
			// Store the result
			userResults.put(analyzer, value);
			
			// Send the result
			trigger(analyzer, value);
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
	
	private void trigger(AnalyzerInterface analyzer, float value) {
		if (analyzer.doTrigger(value)) {
			server.send(analyzer.getSlug(), value);
		}
	}

}
