package ch.linetic.user;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UserTest {
	
	UserInterface user;

	@Before
	public void init() {
		user = new User(0);
	}

	@Test
	public void testMovementIsNotNull() {
		assertNotNull(user.getMovement());
	}

}
