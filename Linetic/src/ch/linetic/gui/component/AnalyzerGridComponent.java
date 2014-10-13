package ch.linetic.gui.component;

import java.util.Map;

import ch.linetic.analysis.AnalyzerInterface;
import ch.linetic.analysis.AnalyzerManagerInterface;
import ch.linetic.analysis.UserNotFoundException;
import ch.linetic.gui.Color;
import ch.linetic.user.UserMap;
import processing.core.PApplet;

public class AnalyzerGridComponent extends GridComponent {

	private UserMap users;
	private AnalyzerManagerInterface am;

	public AnalyzerGridComponent(
			PApplet parrent, 
			AnalyzerManagerInterface am,
			UserMap users,
			int x, 
			int y, 
			int width,
			int height)	
	{
		super(parrent, x, y, width, height, 0);
		this.users = users;
		this.am = am;
	}
	
	@Override
	public void draw() {
		parrent.noStroke();
		parrent.fill(Color.BACKGROUNDCOLOR);
		parrent.rect(x, y, width, height);
		
		try {
			Map<AnalyzerInterface, Float> results = am.getResult(users.highlightedUser);
			updateRowsAndCols(results.size());
			reset();
			for (AnalyzerInterface analyzer : results.keySet()) {
				put(new AnalyzerComponent(parrent, analyzer, results.get(analyzer)));
			}
			super.draw();
			
		} catch (UserNotFoundException e) {
			parrent.fill(0);
			parrent.textAlign(PApplet.CENTER, PApplet.CENTER);
			parrent.text("No User Found", x + width / 2, y + height / 2);
		}
	}
	
	@Override
	void updateRowsAndCols(int length) {
		int c, r;
		if (length < 5) {
			r = length;
			c = 1;
		}
		else {
		    r = 5;
		    c = PApplet.ceil((float) length / (float) r);
		    r = c*r - length >= c && length > 5 ? PApplet.ceil((float) length / (float) c) : r;
		}
		this.rows = r;
		this.cols = c;
		this.components = new Drawable[rows][cols];
	}

}
