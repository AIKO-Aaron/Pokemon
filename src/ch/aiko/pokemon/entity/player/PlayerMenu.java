package ch.aiko.pokemon.entity.player;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.graphics.menu.ButtonMenuTest;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.pokemon.graphics.menu.RaveMenu;
import ch.aiko.pokemon.settings.Settings;

public class PlayerMenu extends Menu {

	private final int background = 0xFFFF00FF;
	private final int lines = 0xFF0000FF;
	private final int selected = 0xFF00FF00;
	private final int width = 350;

	private static int index = 0;
	private String[] texts = new String[] { "Pokedex", "Pokemon", "Beutel", "what?", "rave!" };

	public PlayerMenu(Screen parent) {
		super(parent);
	}

	@Override
	public String getName() {
		return "MainMenu";
	}

	@Override
	public void renderMenu(Renderer r) {
		r.drawRect(r.getWidth() - width, 0, width, r.getHeight(), lines, 5);
		r.fillRect(r.getWidth() - width + 5, 5, width - 10, r.getHeight() - 10, background);
		float fieldSize = (float) r.getHeight() / (float) texts.length;
		for (int i = 0; i < texts.length; i++) {
			if (i == index) r.drawRect(r.getWidth() - width, (int) (fieldSize * i), width, (int) fieldSize, selected, 5);
			else r.drawLine(r.getWidth() - width + 5, (int) (fieldSize * (i + 1)), r.getWidth() - 5, (int) (fieldSize * (i + 1)), lines, 5);
			r.drawText(texts[i], new Font(Settings.font, 0, (int) fieldSize / 2), r.getWidth() - width + 10, (int) (i * fieldSize), lines);
		}
	}

	@Override
	public void updateMenu(Screen s, Layer l) {
		if (getMouseXInFrame(s) > s.getFrameWidth() - width && getMouseXInFrame(s) < s.getFrameWidth()) {
			float fieldSize = (float) s.getFrameHeight() / (float) texts.length;
			int ypos = getMouseYInFrame(s);
			index = (int) (ypos / fieldSize);
			
			if(popMouseKey(MouseEvent.BUTTON1)) performAction();
		}

		if (input.popKeyPressed(KeyEvent.VK_DOWN)) index = (index + 1) % texts.length;
		if (input.popKeyPressed(KeyEvent.VK_UP)) index = index > 0 ? (index - 1) : texts.length - 1;

		if (input.popKeyPressed(KeyEvent.VK_X)) closeMe();

		if (input.popKeyPressed(KeyEvent.VK_SPACE)) performAction();
	}

	public void performAction() {
		switch (index) {
			case 0:
				level.openMenu(new ButtonMenuTest(parent));
				break;
			case 4:
				level.openMenu(new RaveMenu(parent));
				break;
			default:
				break;
		}
	}

	@Override
	public void onOpen() {}

	@Override
	public void onClose() {}

}
