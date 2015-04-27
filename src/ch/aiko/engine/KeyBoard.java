package ch.aiko.engine;

import java.util.HashMap;

public class KeyBoard {

	public static final boolean[] keys = new boolean[300000];
	private static HashMap<Integer, Integer> timesPressed = new HashMap<Integer, Integer>();
	
	public static void keyPressed(int key) {
		keys[key] = true;
		timesPressed.put(key, timesPressed.containsKey(key) ? timesPressed.get(key) + 1 : 1);
	}
	
	public static void keyReleased(int key) {
		keys[key] = false;
	}
	
	public static boolean isKeyPressed(int keyCode) {
		return keys[keyCode];
	}
	
	public static int getTimesPressed(int keyCode) {
		if(!timesPressed.containsKey(keyCode)) return 0;
		int i = timesPressed.get(keyCode);
		timesPressed.put(keyCode, i - 1 > 0 ? i - 1 : 0);
		return i;
	}
	
}
