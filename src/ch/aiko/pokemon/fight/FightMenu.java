package ch.aiko.pokemon.fight;

import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.graphics.menu.Button;
import ch.aiko.pokemon.graphics.menu.Menu;

public class FightMenu extends Menu {

	public FightMenu(Screen parent) {
		super(parent);

		addButton(new Button(0, 0, 350, 100, "Fight", (b) -> buttonPressed(b)), 1, 0);
		addButton(new Button(0, 100, 350, 100, "Pokemon", (b) -> buttonPressed(b)), 1, 1);
		addButton(new Button(0, 200, 350, 100, "Item", (b) -> buttonPressed(b)), 1, 2);
		addButton(new Button(0, 300, 350, 100, "Run", (b) -> buttonPressed(b)), 1, 3);
	}

	public void buttonPressed(Button sender) {
		removeAllButtons();
		addButton(new Button(0, 0, 350, 100, sender.getText(), (b) -> buttonPressed(b)), 1, 0);
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
