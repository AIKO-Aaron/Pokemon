package ch.aiko.pokemon.graphics;

import java.awt.event.KeyEvent;

import ch.aiko.engine.KeyBoard;
import ch.aiko.engine.Window;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.level.Level;

public class Frame extends Window {
	private static final long serialVersionUID = 8804880656221396300L;

	public static final int WIDTH = 960;
	public static final int HEIGHT = 540;

	private static Level level;

	long lastTime = System.currentTimeMillis();

	public Frame(Level level) {
		Frame.level = level;
	}

	public void setup() {
		Window.create(title, WIDTH, HEIGHT);
	}

	public static Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		Frame.level = level;
	}

	public void quit() {
		System.out.println("Saving and exiting game");
		save();

		System.exit(0);
	}

	public void save() {
		Pokemon.save();
	}

	public void draw() {		
		if (level == null) return;
		level.paint();
		level.paint(getGraphics(), this);
	}

	public void update(double delta) {
		if (KeyBoard.getTimesPressed(KeyEvent.VK_ESCAPE) > 0 && !level.getPlayer().isInFight()) if (isMenuOpened() && menu.canClose()) closeMenu();
		else quit();

		if (level == null) return;
		level.update(Frame.this);
	}

	public boolean isKeyPressed(int integerValue) {
		return KeyBoard.isKeyPressed(integerValue);
	}
}
