package ch.linetic.gui.component;

import ch.linetic.kinect.Kinect;
import processing.core.PApplet;
import processing.core.PGraphics;

public class KinectCameraComponent extends Component {
	
	private Kinect kinect;

	public KinectCameraComponent(PApplet parrent, int x, int y, Kinect kinect) {
		super(parrent, x, y, kinect.width(), kinect.height());
		this.kinect = kinect;
	}

	@Override
	public void draw() {
		PGraphics pg = kinect.camera();
		if (!kinect.isMirrored()) {
			parrent.image(pg, x, y);
		}
		else {
			parrent.pushMatrix();
			parrent.translate(x, y);
			parrent.scale(-1, 1);
			parrent.image(pg, -pg.width, 0);
			parrent.popMatrix();
		}
		
	}


}
