package ch.linetic.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.linetic.gesture.MoveList;
import ch.linetic.settings.Default;
import ch.linetic.settings.Util;

public class UserMap {
	
	private Map<Integer, User> users;
	public User highlightedUser = null;
	
	private MoveList moves;
	private int nbrOfPerson = Default.NBR_OF_PERSONS;
	
	public UserMap(MoveList moves) {
		this.users = new HashMap<Integer, User>();
		this.moves = moves;
	}
	
	public User get(int userId) {
		return users.get(userId);
	}
	
	public Collection<User> values() {
		return users.values();
	}
	
	public User push(int userId) {

		if (users.size() > nbrOfPerson) {
			System.out.println(">> Error : Maximum number of person reached : "
					+ nbrOfPerson);
			return null;
		}

		User user = new User(userId, moves);
		if (users.isEmpty())
			highlightedUser = user;

		users.put(userId, user);
		return user;
	}

	public User pop(int userId) {

		User user = users.get(userId);
		users.remove(userId);
		if (users.isEmpty())
			highlightedUser = null;
		else if (highlightedUser.id == userId)
			highlightedUser = getRandomUser();
		
		return user;
	}

	private User getRandomUser() {
		if (users.isEmpty())
			return null;
		List<User> currentUsers = new ArrayList<User>(users.values());
		return currentUsers.get(Util.random(currentUsers.size()));
	}
}
