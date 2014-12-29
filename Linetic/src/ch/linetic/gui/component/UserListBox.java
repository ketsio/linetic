package ch.linetic.gui.component;

import java.util.Observable;
import java.util.Observer;

import ch.linetic.gui.Color;
import ch.linetic.user.UserInterface;
import ch.linetic.user.UserMap;
import controlP5.ControlP5;
import controlP5.ListBox;
import controlP5.ListBoxItem;

/**
 * Extension of the class {@link ListBox} from the library "controlP5"
 * used for displaying a real time list of all user recognized by the kinect at any moment
 * @author ketsio
 *
 */
public class UserListBox extends ListBox implements Observer {

	private UserMap users;
	private int x;
	private int y;
	private int width;
	private int height;
	

	/**
	 * Creates a UserListBox given its size, position and the map of users of the application
	 * @param cp5 the {@link ControlP5} instance of the application
	 * @param users the map of users of the application
	 * @param x the x coordinate of the position
	 * @param y the y coordinate of the position
	 * @param width the width of the component
	 * @param height the height of the component
	 */
	public UserListBox(ControlP5 cp5, UserMap users, int x, int y, int width, int height) {
		super(cp5, "UserList");
		this.users = users;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		int barHeight = 20;
		setPosition(x, y + barHeight);
		setSize(width, height);
		setBarHeight(barHeight);
		setItemHeight(barHeight);
        setColorBackground(Color.SHADE_A);
        setColorForeground(Color.SHADE_A_DARK);
        setColorActive(Color.SHADE_B);
        setColorLabel(Color.BLACK);

		getCaptionLabel()
			.toUpperCase(true)
			.set("User List")
			.setColor(Color.BLACK);
		getCaptionLabel().getStyle().marginTop = 5;
		getValueLabel().getStyle().marginTop = 3;
	}

	@Override
	public void update(Observable o, Object arg) {
		clear();
		reset();
		
		for (UserInterface user : users) 
			addUser(user);
		if (users.values().size() == 0)
			addItem("No User Identified", 0);
		
	}
	
	/**
	 * Stop displaying the component by covering it with a rectangle that has
	 * the same color as the background color
	 */
	public void reset() {
		cp5.papplet.fill(Color.BACKGROUNDCOLOR);
		cp5.papplet.noStroke();
		cp5.papplet.rect(x, y, width, height);
	}
	
	/**
	 * Add a user to the UserListBox
	 * Define the style of each list item
	 * @param user the user to be added
	 */
	public void addUser(UserInterface user) {
		ListBoxItem l = addItem(user.getName(), user.getId());
		l.setColorBackground(Color.SHADE_B);
		l.setColorForeground(Color.SHADE_B_DARK);
        l.setColorActive(Color.SHADE_A);
        if (user == users.highlightedUser) {
        	l.setColorBackground(Color.SHADE_A);
        	l.setColorForeground(Color.SHADE_A_DARK);
        }
	}

}
