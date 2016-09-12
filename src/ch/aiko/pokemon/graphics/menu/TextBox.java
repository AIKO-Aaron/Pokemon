package ch.aiko.pokemon.graphics.menu;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.settings.Settings;

public class TextBox extends MenuObject {

	public static final int THICKNESS = 5;

	private String __text;
	protected int x, y, w, h;
	protected ArrayList<String> lines;
	protected int index;
	protected MenuObjectAction action;
	protected boolean stopsUpdates;

	public TextBox(String text) {
		x = 0;
		this.__text = text;
	}

	public TextBox(String text, MenuObjectAction ac) {
		x = 0;
		this.__text = text;
		this.action = ac;
	}

	public TextBox(String text, MenuObjectAction ac, boolean su) {
		x = 0;
		this.__text = text;
		this.action = ac;
		this.stopsUpdates = su;
	}

	public TextBox setOnCloseAction(MenuObjectAction ac) {
		action = ac;
		return this;
	}

	@Override
	public void onOpen(Screen s) {
		super.onOpen(s);
		x = -s.getRenderer().getXOffset();
		w = s.getFrameWidth();
		h = 100;
		y = s.getFrameHeight() - h - s.getRenderer().getYOffset();
		lines = new ArrayList<String>();
		int maxWidth = s.getFrameWidth() - 20;
		if (__text == null) return;
		__text = __text.replace("\\n", "\n");
		for (String __lines : __text.split("\\n")) {
			String current = null;
			for (String __word : __lines.split(" ")) {
				if (getStringWidth(s, new Font(Settings.font, 0, h / 4), current == null ? __word : current + " " + __word) < maxWidth) {
					current = (current == null ? "" : current + " ") + __word;
				} else {
					lines.add(current);
					current = __word;
				}
			}
			lines.add(current);
			current = null;
		}
	}

	@Override
	public void update(Screen s, Layer l) {
		if (popKeyPressed(KeyEvent.VK_SPACE)) {
			if (index + 2 >= lines.size()) {
				closeMe();
				if (action != null) action.actionPerformed(this);
			}
			index++;
		}
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawRect(x, y, w, h, 0xFF000000, THICKNESS);
		renderer.fillRect(x + THICKNESS, y + THICKNESS, w - 2 * THICKNESS - 1, h - 2 * THICKNESS - 1, 0xFFFFFFFF);

		if(lines == null) return;
		
		String text1 = lines.size() > index ? lines.get(index) : "";
		String text2 = lines.size() > index + 1 ? lines.get(index + 1) : "";

		renderer.drawText(text1, Settings.font, h / 4, 0, x + 10, y + 10, 0xFF000000);
		renderer.drawText(text2, Settings.font, h / 4, 0, x + 10, y + 50, 0xFF000000);
	}

	@Override
	public String getName() {
		return "TextBox";
	}

	public boolean stopsUpdating() {
		return stopsUpdates;
	}

}
