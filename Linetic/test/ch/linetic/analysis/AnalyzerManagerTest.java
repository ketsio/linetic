package ch.linetic.analysis;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.linetic.user.User;
import ch.linetic.user.UserInterface;

public class AnalyzerManagerTest {

	AnalyzerManagerInterface am;
	
	@Before
	public void init() {
		am = new AnalyzerManager();
	}

	@Test(expected=UserNotFoundException.class)
	public void testUserNotAnalyzedShouldThrow() throws UserNotFoundException {
		am.getResult(new User(0));
	}

	@Test(expected=UserNotFoundException.class)
	public void testNullUserShouldThrow() throws UserNotFoundException {
		am.getResult(null);
	}

	@Test
	public void testUserThatHaveBeenAnalyzedShouldReturnSomethingNotNull() throws UserNotFoundException {
		UserInterface user = new User(0);
		am.analyze(user);
		assertNotNull(am.getResult(user));
	}

}
