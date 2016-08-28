package ch.aiko.pokemon.entity.player;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.graphics.menu.Button;
import ch.aiko.pokemon.graphics.menu.ButtonGroupMenu;
import ch.aiko.pokemon.graphics.menu.ButtonMenuTest;
import ch.aiko.pokemon.graphics.menu.RaveMenu;
import ch.aiko.pokemon.settings.SettingsPanel;

public class PlayerMenu extends ButtonGroupMenu {

	// private String[] texts = new String[] { "Pokedex", "Pokemon", "Beutel", "what?", "rave!" };

	public final int WIDTH = 350;
	private int openOffset;
	private int time = 10;

	public PlayerMenu(Screen parent) {
		super(parent);
	}

	@Override
	public String getName() {
		return "MainMenu";
	}

	@Override
	public void renderMenu(Renderer r) {}

	@Override
	public void updateMenu(Screen s, Layer l) {
		if (openOffset > 0) openOffset-=time;

		for (Layer b : buttons) {
			if (b instanceof Button) {
				Button m = (Button) b;
				m.setX(parent.getWidth() - WIDTH + openOffset);
			}
		}
	}

	@Override
	public void onOpen() {
		openOffset = WIDTH;

		addButton(new Button(parent.getWidth(), 0, WIDTH, 0, "Pokedex", (b) -> level.openMenu(new ButtonMenuTest(parent))));
		addButton(new Button(parent.getWidth(), 0, WIDTH, 0, "Pokemon", (b) -> {}));
		addButton(new Button(parent.getWidth(), 0, WIDTH, 0, "Bag", (b) -> {}));
		addButton(new Button(parent.getWidth(), 0, WIDTH, 0, "Settings", (b) -> level.openMenu(new SettingsPanel(parent))));
		addButton(new Button(parent.getWidth(), 0, WIDTH, 0, "Rave", (b) -> level.openMenu(new RaveMenu(parent))));
	}

	@Override
	public void onClose() {
		removeAllButtons();
	}

	public Screen getScreen() {
		return parent;
	}

}
