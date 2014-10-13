package ch.linetic.gui.component;

import java.util.Observable;
import java.util.Observer;

import ch.linetic.gui.Color;
import ch.linetic.user.UserInterface;
import ch.linetic.user.UserMap;
import controlP5.ControlP5;
import controlP5.ListBox;
import controlP5.ListBoxItem;

public class UserListBox extends ListBox implements Observer {

	private UserMap users;
	private int x;
	private int y;
	private int width;
	private int height;
	

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
	
	public void reset() {
		cp5.papplet.fill(Color.BACKGROUNDCOLOR);
		cp5.papplet.noStroke();
		cp5.papplet.rect(x, y, width, height);
	}
	
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
