package ch.aiko.pokemon.fight;

import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.graphics.menu.Menu;

public class FightMenu extends Menu {

	private static int index = 0;
	private String[] texts = new String[] { "Fight", "Bag", "Pokemon", "Run" };

	public FightMenu(Screen parent) {
		super(parent);
	}

	public void onOpen() {}

	public void onClose() {}

	public void renderMenu(Renderer r) {
		
	}

	public void updateMenu(Screen s) {
		
	}

	public String getName() {
		return "FightMenu";
	}

}
