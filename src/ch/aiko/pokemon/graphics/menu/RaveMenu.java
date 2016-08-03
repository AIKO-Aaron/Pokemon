package ch.aiko.pokemon.graphics.menu;

import java.util.Random;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;

public class RaveMenu extends Menu {

	private Random rand = new Random();
	
	public RaveMenu(Screen parent) {
		super(parent);
	}

	public void onOpen() {}

	public void onClose() {}

	public boolean stopsRendering() {
		return true;
	}
	
	public void renderMenu(Renderer r) {
		r.fillRect(0, 0, r.getWidth(), r.getHeight(), rand.nextInt(0xFFFFFF) + 0xFF000000);
	}

	public void updateMenu(Screen s, Layer l) {}

	public String getName() {
		return "RaveMenu";
	}

}
