package ch.linetic.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;

public class UserMap extends Observable implements Iterable<UserInterface> {
	
	private static int MAX_NBR_USERS = 6;
	private static Random r = new Random();
	
	private Map<Integer, UserInterface> users;
	
	/**
	 * The highlightedUser is the user that is analyzed by the different Analyzers
	 */
	public UserInterface highlightedUser = null;
	
	public UserMap() {
		this.users = new HashMap<Integer, UserInterface>();
	}
	
	/**
	 * Return a User given its identifier
	 * @param userId the identifier of the user
	 * @return the user
	 */
	public UserInterface get(int userId) {
		return users.get(userId);
	}
	
	/**
	 * @return all the Users as a Collection
	 */
	public Collection<UserInterface> values() {
		return users.values();
	}
	
	/**
	 * @return the number of users
	 */
	public int size() {
		return users.values().size();
	}
	
	/**
	 * Check if the user with identifier `key` exists
	 * @param key the user id
	 * @return true if the user exists, false otherwise
	 */
	public boolean containsKey(Integer key) {
		return users.keySet().contains(key);
	}

	/**
	 * Check if the user `user` exists
	 * @param user
	 * @return true if the user exists, false otherwise
	 */
	public boolean containsValue(UserInterface user) {
		return users.values().contains(user);
	}
	
	/**
	 * Change the current highlightedUser to a given user
	 * @param user the new highlightedUser
	 */
	public void setHighlightedUser(UserInterface user) {
		highlightedUser = user;
		System.out.println(user.getName() + " is the new hightligtedUser");
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Add a User to the map given its identifier only
	 * The User itself will be created here
	 * @param userId the user identifier
	 * @return the user that has been created
	 */
	public UserInterface push(int userId) {

		if (users.size() >= MAX_NBR_USERS) {
			System.out.println(">> Error : Maximum number of person ("+MAX_NBR_USERS+") reached : "
					+ users.size());
			return null;
		}

		User user = new User(userId);
		if (users.isEmpty())
			highlightedUser = user;

		users.put(userId, user);
		setChanged();
		notifyObservers();
		return user;
	}

	/**
	 * Remove a User to the map given its identifier
	 * @param userId the user identifier
	 * @return the user that has been removed
	 */
	public UserInterface pop(int userId) {

		UserInterface user = users.get(userId);
		users.remove(userId);
		if (users.isEmpty())
			highlightedUser = null;
		else if (highlightedUser.getId() == userId)
			highlightedUser = getRandomUser();
		
		setChanged();
		notifyObservers();
		return user;
	}

	@Override
	public Iterator<UserInterface> iterator() {
		return users.values().iterator();
	}

	private UserInterface getRandomUser() {
		if (users.isEmpty())
			return null;
		List<UserInterface> currentUsers = new ArrayList<UserInterface>(users.values());
		return currentUsers.get(random(currentUsers.size()));
	}

	private static int random(int size) {
		return r.nextInt(size);
	}
}
