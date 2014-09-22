package ch.linetic.user;

import org.apache.commons.collections15.buffer.CircularFifoBuffer;

import processing.core.PApplet;
import ch.linetic.gesture.*;

public class RingBuffer {

	private int frames;
	private CircularFifoBuffer<Pose> buffer;
	private DTW[] dtw;

	private float[] cost;
	private float[] costLast;

	private boolean recording;
	private int recCounter;
	private Move recMove;

	public RingBuffer(MoveList moves) {
		this.frames = 50;
		this.buffer = new CircularFifoBuffer<>(frames);

		this.cost = new float[moves.length()];
		this.costLast = new float[moves.length()];
		
		this.dtw = new DTW[moves.length()];
		for (int i = 0; i < dtw.length; i++)
			dtw[i] = new DTW(moves.get(i), frames, moves.get(i).frames);
	}

	void fillBuffer(Pose pose) {
		buffer.add(pose);
		for (int i = 0; i < dtw.length; i++)
			dtw[i].updateInput(pose);
	}

	void record(Move move) {
		if (recording)
			return;

		recCounter = 0;
		recording = true;
		recMove = move;
	}

	void stopRecording() {
		if (!recording)
			return;

		recording = false;
		copyBuffer(recMove, recCounter);
		recMove.endRecord();
	}

	void copyBuffer(Move move, int framesToCopy) {
		framesToCopy = PApplet.min(framesToCopy, frames);
		Pose[] ba = buffer.toArray(new Pose[0]);

		if (framesToCopy > ba.length)
			return;

		for (int i = ba.length - framesToCopy, j = 0; i < ba.length; i++, j++)
			move.get(j).copyFrom(ba[i]);
	}

	void updateCost(int moveId) {
		costLast[moveId] = cost[moveId];
		cost[moveId] = cost(moveId);
	}

	float getPercent(int moveId) {
		// TODO : implement
		return 0;
	}

	float costToPercent(float cost) {
		// TODO : implement
		return 0;
	}

	/**
	 * Calculates the cost of a move using DTW
	 * 
	 * @param moveId
	 * @return
	 */
	float cost(int moveId) {
		DTW dtw = this.dtw[moveId];
		dtw.compute();
		return dtw.getResult();
	}

}
