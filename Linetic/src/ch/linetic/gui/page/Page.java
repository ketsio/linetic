package ch.linetic.gui.page;

import ch.linetic.settings.Default;
import processing.core.PApplet;

public abstract class Page {
	
	int padding = Default.PADDING;
	PApplet parrent;
	
	// settings
	String title = "Untitled";
	
	// GUI
	int backgroundColor;
	int width = Default.WIDTH;
	int height = Default.HEIGHT;
	
	// states
	boolean updateDisplay = false;
	
	public Page(PApplet parrent) {
		this.parrent = parrent;
		this.backgroundColor = parrent.color(255);
	}

	public void start() {
		parrent.size(width, height, PApplet.JAVA2D);
		parrent.background(backgroundColor);
		drawFrame();
	}
	
	public void reset() {
		start();
	}
	
	public void draw() {
		drawFrame();
		
		if (!updateDisplay)
			return;
		
		updateDisplay = false;
		drawUpdate();
	}

	abstract public void drawFrame();
	abstract public void drawUpdate();
}
