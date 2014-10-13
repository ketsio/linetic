package ch.linetic.gui.component;

import ch.linetic.gui.Linetic;
import processing.core.PApplet;

public class GridComponent extends Component {

	final int padding;
	Drawable[][] components;
	int rows;
	int cols;
	
	private int currentPos = 0;

	public GridComponent(
			PApplet parrent, 
			int x, 
			int y, 
			int width, 
			int height, 
			int length)
	{
		super(parrent, x, y, width, height);
		updateRowsAndCols(length);
		this.padding = Linetic.PADDING;
	}

	@Override
	public void draw() {
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				if (components[r][c] != null)
					components[r][c].draw();
	}

	public void put(Component c, int row, int col) {
		int compWidth = (width - (cols - 1) * padding) / cols;
		int compHeight = (height - (rows - 1) * padding) / rows;
		int compX = x + (compWidth + padding) * col;
		int compY = y + (compHeight + padding) * row;
		
		components[row][col] = c;
		c.setup(compX, compY, compWidth, compHeight);
	}
	
	public void put(Component c) {
		int row = currentPos / cols;
		int col = currentPos % cols;
		
		put(c, row, col);
		currentPos = (currentPos + 1) % (rows * cols);
	}
	
	public void reset() {
		currentPos = 0;
	}
	
	void updateRowsAndCols(int length) {
	    int c = 4;
	    int r = PApplet.ceil((float) length / (float) c);
	    c = r*c - length >= r ? PApplet.ceil((float)length / (float)r) : c;
		this.rows = r;
		this.cols = c;
		this.components = new Drawable[rows][cols];
	}

}
