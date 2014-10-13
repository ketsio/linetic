package ch.linetic.user;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UserMapTest {
	
	private UserMap users;

	@Before
	public void init() {
		users = new UserMap();
	}

	@Test
	public void testInitialSizeShouldBeZero() {
		assertEquals(0,users.size(),0);
	}

	@Test
	public void testPushingAndPopingTheSameIntShouldCancelEachOther() {
		users.push(0);
		users.pop(0);
		assertEquals(0,users.size(),0);
	}

	@Test
	public void testContainsAParticularUser() {
		users.push(0);
		assertTrue(users.containsValue(users.get(0)));
	}

	@Test
	public void testContainsAParticularKey() {
		users.push(0);
		assertTrue(users.containsKey(0));
	}

}
