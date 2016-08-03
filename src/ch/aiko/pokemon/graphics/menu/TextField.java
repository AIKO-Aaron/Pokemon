package ch.aiko.pokemon.graphics.menu;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.input.Input;
import ch.aiko.pokemon.settings.Settings;

public class TextField extends MenuObject implements KeyListener {

	private static final int THICKNESS = 3;

	public static final ArrayList<Integer> blackList = new ArrayList<Integer>(Arrays.asList(new Integer[] { KeyEvent.VK_ENTER, KeyEvent.VK_BACK_SPACE, KeyEvent.VK_SHIFT, KeyEvent.VK_ALT, KeyEvent.VK_META, KeyEvent.VK_CONTROL, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_ESCAPE, KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_PAGE_UP, KeyEvent.VK_END, KeyEvent.VK_BEGIN, KeyEvent.VK_DELETE, KeyEvent.VK_FINAL, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT }));

	protected int x, y, w, h, size;
	protected String text = "";
	protected boolean selected = false;
	protected MenuObjectAction onEnter;
	protected boolean defaultText = true;

	public TextField(int x, int y, int w, int h, int size) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.size = size;
	}

	public TextField(int x, int y, int w, int h, int size, String text) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.size = size;
		this.text = text;
	}

	public TextField(int x, int y, int w, int h, int size, String text, MenuObjectAction r) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.size = size;
		this.text = text;
		onEnter = r;
	}

	public void update(Screen screen, Layer l) {
		if (input.popMouseKey(MouseEvent.BUTTON1)) {
			int xx = getMouseXInFrame(screen);
			int yy = getMouseYInFrame(screen);
			if (xx > x && xx < x + w && yy > y && yy < y + h) {
				selected = true;
				if (defaultText) text = "";
			} else {
				selected = false;
			}
		}

		if (selected) {
			if (input.popKeyPressed(KeyEvent.VK_ENTER)) entered();
			else if (input.popKeyPressed(KeyEvent.VK_BACK_SPACE)) text = text.substring(0, text.length() - 1);
		}
	}

	public void setInput(Input input) {
		this.input = input;
		input.screen.addKeyListener(this);
	}

	public void entered() {
		if (onEnter != null) onEnter.actionPerformed(this);
	}

	public void setAction(MenuObjectAction r) {
		onEnter = r;
	}
	
	public void onClose() {
		input.screen.removeKeyListener(this);
	}

	public void render(Renderer renderer) {
		renderer.drawRect(x, y, w, h, selected ? 0xFFFF00FF : 0xFF000000, THICKNESS);
		renderer.fillRect(x + THICKNESS, y + THICKNESS, w - 2 * THICKNESS - 1, h - 2 * THICKNESS - 1, 0xFFFFFFFF);

		if (text == null || text.replace(" ", "").equalsIgnoreCase("")) return;

		int xstart = x + (w - getStringWidth(renderer.getScreen(), new Font(Settings.font, 0, h / 2), text)) / 2;
		int ystart = y + (h - getStringHeight(renderer.getScreen(), new Font(Settings.font, 0, h / 2))) / 2;

		renderer.drawText(text, Settings.font, h / 2, 0, xstart, ystart, 0xFF000000);
	}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (!blackList.contains(keyCode)) text += e.getKeyChar();
	}

	public void keyReleased(KeyEvent e) {}
	
	public String getText() {
		return text;
	}

}