package ch.linetic.gui.component;

import processing.core.PApplet;

/**
 * A component if an entity of the GUI
 * It is drawable (therefore implements {@link Drawable}) and has a position and a size
 * @author ketsio
 *
 */
abstract public class Component implements Drawable {

	/** the PApplet of the application */
	PApplet parrent;
	
	/** x coordinate of the position */
	int x = 0;

	/** y coordinate of the position */
	int y = 0;

	/** width of the component */
	int width = 0;

	/** height of the component */
	int height = 0;

	/**
	 * Creates a component with default position and size
	 * Useful when we do not know those parameters statically
	 * @param parrent the PApplet of the application
	 */
	public Component(PApplet parrent) {
		this.parrent = parrent;
	}
	
	/**
	 * Creates a component with given position and size
	 * @param parrent the PApplet of the application
	 * @param x the x coordinate of the position
	 * @param y the y coordinate of the position
	 * @param width the width of the component
	 * @param height the height of the component
	 */
	public Component(PApplet parrent, int x, int y, int width, int height) {
		this.parrent = parrent;
		setup(x, y, width, height);
	}
	
	/**
	 * Set the position and the size of the component
	 * @param x the x coordinate of the position
	 * @param y the y coordinate of the position
	 * @param width the width of the component
	 * @param height the height of the component
	 */
	public void setup(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
