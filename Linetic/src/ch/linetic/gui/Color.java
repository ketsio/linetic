package ch.linetic.gui;

import processing.core.PApplet;

/**
 * Helper class storing the colors needed in the project.
 * Contains the color chart of the project.
 * @author ketsio
 *
 */
public class Color {
	
	public static final PApplet parrent = new PApplet();
	public static final int BACKGROUNDCOLOR = color(255);
	public static final int SHADE_A = color(61,189,255);
	public static final int SHADE_A_DARK = color(54,168,227);
	public static final int SHADE_B = color(211,241,105);
	public static final int SHADE_B_DARK = color(185, 212, 92);
	public static final int BLACK = color(0);
	public static final int GREY_LIGHT = color(230);

	public static int color(int r, int g, int b) {
		return parrent.color(r, g, b);
	}
	
	public static int color(int grey) {
		return parrent.color(grey);
	}
}
