package ch.aiko.pokemon.level;

import ch.aiko.engine.Menu;
import ch.aiko.engine.Mouse;

public class LevelEditor extends Menu {

	private Level currentLevel;

	public LevelEditor(Level levelToEdit) {
		this.currentLevel = levelToEdit;
	}

	public Level getModifiedLevel() {
		return currentLevel;
	}

	public void update(double d) {
		int mouseX = Mouse.MouseX;
		int mouseY = Mouse.MouseY;

		System.out.println(Mouse.isMousePressed(0) + ": " + mouseX + "|" + mouseY);
	}

	public String name() {
		return "LevelEditor";
	}

	public void onOpen() {
		
	}

	public void onClose() {
		
	}

	public void draw(double d) {
		
	}
}
