package ch.linetic.settings;

import java.util.Random;

public class Util {
	
	private static Random r = new Random();

	public static int random(int size) {
		return r.nextInt(size);
	}
	
	public static int random(int min, int max) {
		return r.nextInt(max - min) + min;
	}
}
