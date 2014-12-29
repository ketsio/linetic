package ch.linetic.gui.component;

import ch.linetic.gui.Linetic;
import processing.core.PApplet;

/**
 * A GridComponent is a component that act as a grid for other components
 * i.e. We can add as many component as we want with the <code>put(Component c)</code> function
 * and it automatically allocate the write amount of space for each component in order
 * to display everything as a grid.
 * It is important that the components of the grid can be setup dynamically
 * (using the function <code>setup(x,y,width,height)</code>) without causing any problems
 * as it's the grid itself that setup the size and position of all the components.
 * @author ketsio
 *
 */
public class GridComponent extends Component {

	final int padding;
	Drawable[][] components;
	int rows;
	int cols;
	
	private int currentPos = 0;

	/**
	 * Creates a GridComponent given its size, position and
	 * the number of component to be added to the grid
	 * @param parrent the PApplet of the application
	 * @param x the x coordinate of the position
	 * @param y the y coordinate of the position
	 * @param width the width of the grid
	 * @param height the height of the grid
	 * @param length the number of components
	 */
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

	/**
	 * Add a Component to the grid given its position in the grid
	 * @param c the component to be added
	 * @param row the position in the rows
	 * @param col the position in the columns
	 */
	public void put(Component c, int row, int col) {
		int compWidth = (width - (cols - 1) * padding) / cols;
		int compHeight = (height - (rows - 1) * padding) / rows;
		int compX = x + (compWidth + padding) * col;
		int compY = y + (compHeight + padding) * row;
		
		components[row][col] = c;
		c.setup(compX, compY, compWidth, compHeight);
	}
	
	/**
	 * Add a Component to the grid without taking care of its
	 * exact position in the grid
	 * @param c the component to be added
	 */
	public void put(Component c) {
		int row = currentPos / cols;
		int col = currentPos % cols;
		
		put(c, row, col);
		currentPos = (currentPos + 1) % (rows * cols);
	}
	
	/**
	 * Reset the pointer that decide where the next component is going to be placed
	 * May therefore erase the previous components that where added before
	 */
	public void reset() {
		currentPos = 0;
	}
	
	/**
	 * Update the number of row and columns according to the number of components
	 * to be added to the grid
	 * @param length the number of components to be added
	 */
	void updateRowsAndCols(int length) {
	    int c = 4;
	    int r = PApplet.ceil((float) length / (float) c);
	    c = r*c - length >= r ? PApplet.ceil((float)length / (float)r) : c;
		this.rows = r;
		this.cols = c;
		this.components = new Drawable[rows][cols];
	}

}
