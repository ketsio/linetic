package ch.linetic.gui.component;

import ch.linetic.gui.Drawable;
import processing.core.PApplet;

public abstract class Component implements Drawable {

	PApplet parrent;
	
	int x = 0;
	int y = 0;
	int width = 0;
	int height = 0;
	
	public Component(PApplet parrent, int x, int y, int width, int height) {
		this.parrent = parrent;
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
