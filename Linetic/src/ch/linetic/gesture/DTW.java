package ch.linetic.gesture;

import processing.core.PApplet;

public class DTW {

	private int pointer = 0;
	private int w;
	private int h;
	private Move move;
	
	private float d[][];
	private float D[][];
	private int P[][];

	public DTW(Move m, int w, int h) {
		this.d = new float[w][h];
		this.D = new float[w][h];
		this.P = new int[w][h];
		
		this.w = w;
		this.h = h;
		this.move = m;
	}
	
	/**
	 * Updates the cost table d[][]
	 * @param costs : needs to be reversed (last pose first)
	 */
	public void updateInput(Pose pose) {
		if (move.empty)
			return;
		
		d[pointer] = move.costs(pose);
		pointer = pointer-- % w;
	}

	/**
	 * Computes the DTW algorithm
	 */
	public void compute() {

		D[0][0] = d[p(0)][0];
		P[0][0] = 0;

		for (int i = 1; i < w; i++) {
			D[i][0] = d[p(i)][0] + D[i - 1][0];
			P[i][0] = 1;
		}

		for (int j = 1; j < h; j++) {
			D[0][j] = d[p(0)][j] + D[0][j - 1];
			P[0][j] = -1;
		}

		for (int i = 1; i < w; i++) {
			for (int j = 1; j < h; j++) {
				D[i][j] = d[p(i)][j] + PApplet.min(D[i - 1][j - 1], D[i][j - 1], D[i - 1][j]);

				P[i][j] = 0;
				if (D[i][j - 1] < D[i - 1][j - 1])
					P[i][j] = -1;
				if (D[i - 1][j] < D[i - 1][j - 1]) {
					P[i][j] = 1;
					if (D[i][j - 1] < D[i - 1][j])
						P[i][j] = -1;
				}
			}
		}
	}

	/**
	 * Converts the indexes (according to pointer)
	 * @param i : index from (1) to (buffer.length)
	 * @return the index (pointer + buffer.length) to (pointer)
	 */
	private int p(int i) {
		return (i + pointer) % d.length;
	}

	public float getResult() {
		return getResult(D.length - 1, D.length - 1);
	}

	public float getResult(int w, int h) {
		return D[w][h];
	}

	public int[][] getPath() {
		return P;
	}

}
