package ch.linetic.gui;

import java.util.Observer;

import oscP5.OscMessage;
import processing.core.PApplet;
import processing.core.PGraphics;
import SimpleOpenNI.SimpleOpenNI;
import ch.linetic.analysis.AnalyzerManager;
import ch.linetic.analysis.AnalyzerManagerInterface;
import ch.linetic.connexion.Server;
import ch.linetic.camera.CameraInterface;
import ch.linetic.camera.Kinect;
import ch.linetic.camera.KinectListener;
import ch.linetic.camera.KinectSimulator;
import ch.linetic.camera.NoCameraConnectedException;
import ch.linetic.gesture.PoseInterface;
import ch.linetic.gui.component.AnalyzerGridComponent;
import ch.linetic.gui.component.UserListBox;
import ch.linetic.user.UserInterface;
import ch.linetic.user.UserMap;
import controlP5.ControlEvent;
import controlP5.ControlP5;

@SuppressWarnings("serial")
public class Linetic extends PApplet implements KinectListener {

	private static final int WIDTH = 1200;
	private static final int HEIGHT = 480 + 15 * 2;
	public static final int PADDING = 15;

	private boolean isCameraConnected = true;
	private CameraInterface camera;
	private UserMap users;
	private AnalyzerManagerInterface am;

	public ControlP5 cp5;
	public UserListBox userListBox;
	public AnalyzerGridComponent analyzerGrid;

	public int userIdSimulation = 0;
	public Server server;

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "LineticSender" });
	}

	public void setup() {

		size(WIDTH, HEIGHT, JAVA2D);
		background(Color.BACKGROUNDCOLOR);

		try {
			camera = Kinect.getNextKinect(this);
		} catch (NoCameraConnectedException e) {
			// isCameraConnected = false;
			camera = new KinectSimulator(this);
		}
		
		server = new Server(this);
		cp5 = new ControlP5(this);

		users = new UserMap();
		am = new AnalyzerManager(server);

		int cameraWidth = 640;
		int listWidth = 100;
		int gridWidth = WIDTH - cameraWidth - listWidth - 15 * 4;

		analyzerGrid = new AnalyzerGridComponent(this, am, users, cameraWidth
				+ 2 * PADDING, PADDING, gridWidth, 480);
		userListBox = new UserListBox(cp5, users, cameraWidth + gridWidth + 3
				* PADDING, PADDING, listWidth, 480);
		users.addObserver((Observer) userListBox);
	}

	public void draw() {
		if (!isCameraConnected) {
			drawCameraNotConnected();
			return;
		}

		// Update the camera
		camera.update();

		// Capture the new frame for each user
		PoseInterface pose;
		for (UserInterface user : users.values()) {
			pose = camera.capture(user);
			user.getMovement().push(pose);
		}

		// Analyze the users
		am.analyze(users.values());

		// Draw the camera
		image(camera.getImage(), 15, 15);

		// Draw the data
		analyzerGrid.draw();

	}

	private void drawCameraNotConnected() {
		PGraphics pg = createGraphics(640, 480);
		pg.beginDraw();
		pg.background(0);
		pg.textAlign(PApplet.CENTER, PApplet.CENTER);
		pg.color(255);
		pg.text("Camera not connected", pg.width / 2, pg.height / 2);
		pg.endDraw();
		image(pg.get(), 15, 15);
	}

	public void keyPressed() {

		UserInterface user;
		switch (key) {
		case '+':
			user = users.push(userIdSimulation++);
			if (user != null) {
				user.hello();
			}
			break;

		case '-':
			user = users.pop(--userIdSimulation);
			if (user != null) {
				user.bye();
			}
			break;
		}
	}

	// ---------------//
	// ---Listeners---//
	// ---------------//

	@Override
	public void onNewUser(SimpleOpenNI kinect, int userId) {
		UserInterface user = users.push(userId);
		kinect.startTrackingSkeleton(userId);
		System.out.println("isTrackingSkeleton(" + userId + ") = "
				+ kinect.isTrackingSkeleton(userId));
		user.hello();
	}

	@Override
	public void onLostUser(SimpleOpenNI kinect, int userId) {
		UserInterface user = users.pop(userId);
		kinect.stopTrackingSkeleton(userId);
		user.bye();
	}

	public void controlEvent(ControlEvent theEvent) {
		if (theEvent.isGroup() && theEvent.getName().equals("UserList")) {
			int userId = (int) theEvent.getGroup().getValue();
			assert users.containsKey(userId) : userId;
			UserInterface user = users.get(userId);
			users.setHighlightedUser(user);
		}
	}

	void oscEvent(OscMessage m) {
		server.read(m);
	}
}
