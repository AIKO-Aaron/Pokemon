package ch.aiko.pokemon.fight;

import java.awt.event.KeyEvent;

import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.graphics.menu.Button;
import ch.aiko.pokemon.graphics.menu.Menu;

public class FightMenu extends Menu {

	final int width = 300;

	public FightMenu(Screen parent) {
		super(parent);
		
		x_for_close = false;

		int xc = parent.getFrameWidth() - width;
		int yc = parent.getFrameHeight() - 100;

		addButton(new Button(xc - width, yc - 100, width, 100, "Fight", (b) -> buttonPressed(b)), 1, 0);
		addButton(new Button(xc, yc - 100, width, 100, "Pokemon", (b) -> buttonPressed(b)), 1, 2);
		addButton(new Button(xc - width, yc, width, 100, "Item", (b) -> buttonPressed(b)), 1, 1);
		addButton(new Button(xc, yc, width, 100, "Run", (b) -> buttonPressed(b)), 1, 3);
	}

	public void buttonPressed(Button sender) {
		removeAllButtons();
		addButton(new Button(0, 0, 350, 100, sender.getText(), (b) -> buttonPressed(b)), 1, 0);
	}

	public void onOpen() {
		xOffset = 2 * width;
	}

	public void onClose() {}

	public void renderMenu(Renderer r) {

	}

	public void updateMenu(Screen s) {
		if (s.popKeyPressed(KeyEvent.VK_RIGHT) || s.popKeyPressed(KeyEvent.VK_LEFT)) index = (index + 2) % buttons.size();

		if (xOffset > 0) xOffset -= 10;
	}

	public String getName() {
		return "FightMenu";
	}

}
