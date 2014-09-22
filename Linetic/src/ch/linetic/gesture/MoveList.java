package ch.linetic.gesture;

import ch.linetic.settings.Data;
import ch.linetic.settings.Default;

public class MoveList {

	private Move[] moves;
	
	public MoveList(Data data) {
		moves = new Move[Default.NBR_OF_MOVES];
		for (int i = 0; i < length(); i++)
			moves[i] = new Move(i, data);
	}

	public int length() {
		return moves.length;
	}

	public Move get(int i) {
		if (i < 0 && i >= length())
			return null;
		
		return moves[i];
	}
}
