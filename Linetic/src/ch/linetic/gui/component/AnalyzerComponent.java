package ch.linetic.gui.component;

import ch.linetic.analysis.AnalyzerInterface;
import ch.linetic.gui.Color;
import processing.core.PApplet;

/**
 * Component of the GUI that display the result of an Analyzer
 * @author ketsio
 *
 */
public class AnalyzerComponent extends Component {

	private final static int MAX_WIDTH = 2000;
	private final static int MAX_HEIGHT = 500;

	private AnalyzerInterface analyzer;
	private Float result;

	public AnalyzerComponent(PApplet parrent, AnalyzerInterface analyzer, Float result) {
		super(parrent);
		this.analyzer = analyzer;
		this.result = result;
	}

	@Override
	public void draw() {
		int barWidth = width > MAX_WIDTH ? MAX_WIDTH : width;
		int barHeight = height > MAX_HEIGHT ? MAX_HEIGHT : height;
		int barX = x + (width - barWidth) / 2;
		int barY = y + (height - barHeight) / 2;
		
		parrent.noStroke();
		parrent.fill(210);
		parrent.rect(barX, barY, barWidth, barHeight);

		parrent.fill(Color.SHADE_B);
		parrent.rect(barX, barY, barWidth * result / 100, barHeight);
		
		parrent.fill(0);
		parrent.textAlign(PApplet.CENTER, PApplet.CENTER);
		parrent.text(analyzer.name(), x + width / 2, y + height / 2);
	}

}
