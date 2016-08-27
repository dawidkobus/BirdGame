package birdgame.util;

public class Timer {
	private static int timeCounter = 0;

	public static boolean timePassed(float time, int delta) {
		timeCounter += delta;
		if (timeCounter > time * 1000) {
			timeCounter = 0;
			return true;
		} else {
			return false;
		}
	}
}
