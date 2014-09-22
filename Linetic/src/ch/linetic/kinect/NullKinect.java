package ch.linetic.kinect;

import processing.core.PApplet;
import processing.core.PImage;
import ch.linetic.user.UserMap;

public class NullKinect extends Kinect {

	NullKinect(PApplet parrent, UserMap users) {
		super(parrent, users);
		pg.beginDraw();
		pg.background(0);
		pg.textAlign(PApplet.CENTER, PApplet.CENTER);
		pg.color(255);
		pg.text("No kinect connected", pg.width / 2, pg.height / 2);
		pg.endDraw();
	}

	@Override
	public void update() {
	}

	@Override
	public PImage screenshot() {
		return null;
	}

	@Override
	public PImage screenshot(String filepath) {
		return null;
	}

	@Override
	public int width() {
		return 640;
	}

	@Override
	public int height() {
		return 480;
	}

	@Override
	public boolean isMirrored() {
		return false;
	}

	@Override
	public void mirror() {
	}

}
