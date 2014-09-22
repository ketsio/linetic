package ch.linetic.gui.page;

import processing.core.PApplet;

public class MovePage extends Page {
	
	public int moveId = 0;

	public MovePage(PApplet parrent) {
		super(parrent);
	}

	@Override
	public void drawFrame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawUpdate() {
		// TODO Auto-generated method stub

	}
	
	public void changeMove(int moveId) {
		this.moveId = moveId;
	}

}
