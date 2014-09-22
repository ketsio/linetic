package ch.linetic.gui;

import ch.linetic.gesture.Move;
import ch.linetic.gesture.MoveList;
import ch.linetic.gui.page.HomePage;
import ch.linetic.gui.page.Page;
import ch.linetic.kinect.Kinect;
import ch.linetic.settings.Data;
import ch.linetic.user.UserMap;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class LineticSender extends PApplet {
	
	// settings
	boolean useFullscreen = false;
	int nbrOfMoves = 11;
	int nbrOfPerson = 2;
	boolean autoPoseDetection = false;
	boolean useMultiThreading = true;
	
	// variables
	private Kinect kinect;
	private UserMap users;
	private MoveList moves;
	private Data data;
	
	// GUI
	private Page currentPage;

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "LineticSender" });
	}
	
	public void setup() {
		
		// Setting up the system
		data = new Data(this);
		moves = new MoveList(data);
		users = new UserMap(moves);
		kinect = Kinect.create(this, users);
		
		// Setting up the GUI
		currentPage = new HomePage(this, kinect);
		currentPage.start();
		// ...
	}
	
	public void draw() {
		
		// Update the system
		kinect.update();
		
		// Update the GUI
		currentPage.draw();
	}

	public void keyPressed() {
		if ((key >= '0') && (key <= '9') && (users.highlightedUser != null)) {
			int keyIndex = key - '0';
			
			Move move = moves.get(keyIndex);
			move.beginRecord(users.highlightedUser, kinect);

			println("POSE " + keyIndex + " SAVED");
		}

		switch (key) {
		case 'c':
			kinect.screenshot("capture.png");
			break;

		case 'd':
			kinect.mirror();
			break;
		}
	}

}
