package ch.aiko.engine;

import java.awt.AWTException;
import java.awt.Robot;

public class Mouse {

	private static boolean[] buttons = new boolean[5];

	public static int MouseX, MouseY, MouseWheel;

	public static void mousePressed(int button) {
		buttons[button] = true;
	}

	public static void mouseReleased(int button) {
		buttons[button] = false;
	}

	public static boolean isMousePressed(int button) {
		return buttons[button];
	}

	public static void updateMouse(int x, int y) {
		MouseX = x;
		MouseY = y;
	}

	public static void mouseWheel(int amount) {
		MouseWheel += amount;
	}

	private static Robot r;

	public static void moveMouse(int x, int y) {
		if (r == null) try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		r.mouseMove(x, y);
	}

}
