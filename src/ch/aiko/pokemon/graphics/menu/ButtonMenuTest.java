package ch.aiko.pokemon.graphics.menu;

import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;

public class ButtonMenuTest extends Menu {

	private int count = 20;

	public ButtonMenuTest(Screen parent) {
		super(parent);

		float bs = parent.getFrameHeight() / count;
		for (float i = 0; i < count; i++)
			addButton(new Button(parent.getFrameWidth() - 350 - 350, (int) (bs * i), 350, (int) bs, "Index: " + (i + 1)), 1, (int) i);
	}

	public void onOpen() {}

	public void onClose() {}

	public void renderMenu(Renderer r) {}

	public void updateMenu(Screen s) {}
	
	public boolean stopsUpdating() {
		return false;
	}

	public String getName() {
		return "TestMenuForButtons";
	}

}
