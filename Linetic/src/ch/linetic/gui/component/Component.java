package ch.linetic.gui.component;

import processing.core.PApplet;


abstract public class Component implements Drawable {

	PApplet parrent;
	
	int x = 0;
	int y = 0;
	int width = 0;
	int height = 0;

	public Component(PApplet parrent) {
		this.parrent = parrent;
	}
	
	public Component(PApplet parrent, int x, int y, int width, int height) {
		this.parrent = parrent;
		setup(x, y, width, height);
	}
	
	public void setup(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
