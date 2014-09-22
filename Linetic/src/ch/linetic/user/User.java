package ch.linetic.user;

import ch.linetic.gesture.*;
import ch.linetic.settings.Util;
import processing.core.PApplet;
import processing.core.PVector;

public class User {
	public final int id;
	public String name;
	public int color;
	public RingBuffer rb;
	public int warning;

	public User(int userId, MoveList moves) {
	    this.id = userId;
	    String[] dwarfs = {
	      "Doc", "Grumpy", "Happy", "Sleepy", "Bashful", "Sneezy", "Dopey"
	    };
	    this.name = dwarfs[(int) Util.random(dwarfs.length)];
	    this.rb = new RingBuffer(moves);
	    this.color = color(Util.random(50, 255), Util.random(50, 255), Util.random(0, 100));
	    this.warning = -1;
	  }

	// TODO : TO CHANGE (temporary)
	private int color(int r, int v, int b) {
		PApplet pa = new PApplet();
		return pa.color(r, v, b);
	}

	public void hello() {
		System.out.println("Hello " + name + " !");
	}

	public void bye() {
		System.out.println("Bye " + name + ".");
	}

	public void fillBuffer(Pose pose) {
		rb.fillBuffer(pose);
	}

	public void record(Move move) {
		this.rb.record(move);
	}

	void stopRecording() {
		this.rb.stopRecording();
	}

	public void updateWarning(PVector jointNeck2D, PVector jointNeck3D) {
		warning = -1;

		if (jointNeck2D.x < 100)
			warning = 0;

		if (jointNeck2D.x > 540)
			warning = 4;

		if (jointNeck3D.z > 4000) {
			warning = 2;
			if (jointNeck2D.x < 100)
				warning = 1;
			if (jointNeck2D.x > 540)
				warning = 3;
		}

		if (jointNeck3D.z < 1500) {
			warning = 6;
			if (jointNeck2D.x < 100)
				warning = 7;
			if (jointNeck2D.x > 540)
				warning = 5;
		}
	}
	
}
