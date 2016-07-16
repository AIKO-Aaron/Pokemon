package ch.aiko.pokemon.graphics.menu;

import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;

public class MainMenu extends Menu {

	final int width = 250;

	public MainMenu(Screen parent) {
		super(parent);
	}

	public boolean stopsRendering() {
		return false;
	}

	public boolean stopsUpdating() {
		return true;
	}

	public String getName() {
		return "MainMenu";
	}

	public void render(Renderer r) {
		r.drawRect(r.getWidth() - width, 0, width, r.getHeight(), 0xFF000000, 5);
		r.fillRect(r.getWidth() - width + 5, 5, width - 10, r.getHeight() - 10, 0xFFFFFFFF);
	}

	public void onOpen() {}

	public void onClose() {}

}
