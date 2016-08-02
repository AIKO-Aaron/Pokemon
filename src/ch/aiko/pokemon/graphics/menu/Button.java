package ch.aiko.pokemon.graphics.menu;

import java.awt.Font;
import java.awt.event.MouseEvent;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.settings.Settings;

public class Button extends MenuObject {

	private static final int THICKNESS = 3;

	protected int layer;
	protected int x, y, w, h;
	protected String text;
	protected boolean selected = false;
	protected MenuObjectAction r = (MenuObject b) -> {};

	public Button() {
		x = y = w = h = 0;
		text = "";
	}

	public Button(int x, int y, int w, int h, String text) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
	}

	public Button(int x, int y, int w, int h, String text, MenuObjectAction r) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
		this.r = r;
	}

	public Button setX(int x) {
		this.x = x;
		return this;
	}

	public Button setY(int y) {
		this.y = y;
		return this;
	}

	public Button setWidth(int w) {
		this.w = w;
		return this;
	}

	public Button setHeight(int h) {
		this.h = h;
		return this;
	}

	public Button setText(String text) {
		this.text = text;
		return this;
	}

	public Button setSelected(boolean sel) {
		selected = sel;
		return this;
	}

	public Button setAction(MenuObjectAction r) {
		this.r = r;
		return this;
	}

	public boolean isInside(int mx, int my) {
		return mx >= x && mx <= x + w && my >= y && my <= y + h;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public String getText() {
		return text;
	}

	public boolean isSelected() {
		return selected;
	}

	public MenuObjectAction getAction() {
		return r;
	}

	public void update(Screen screen, Layer l) {
		int xx = getMouseXInFrame(screen);
		int yy = getMouseYInFrame(screen);
		if (xx > x && xx < x + w && yy > y && yy < y + h) {
			selected = true;
			if (input.popMouseKey(MouseEvent.BUTTON1)) {
				buttonPressed();
			}
		} else selected = false;
	}

	public void render(Renderer renderer) {
		renderer.drawRect(x, y, w, h, selected ? 0xFFFF00FF : 0xFF000000, THICKNESS);
		renderer.fillRect(x + THICKNESS, y + THICKNESS, w - 2 * THICKNESS - 1, h - 2 * THICKNESS - 1, 0xFFFFFFFF);

		int xstart = x + (w - getStringWidth(renderer.getScreen(), new Font(Settings.font, 0, h / 2), text)) / 2;
		int ystart = y + (h - getStringHeight(renderer.getScreen(), new Font(Settings.font, 0, h / 2))) / 2;

		renderer.drawText(text, Settings.font, h / 2, 0, xstart, ystart, 0xFF000000);
	}

	public void buttonPressed() {
		r.actionPerformed(this);
	}

	public Layer setLayer(int layer) {
		this.layer = layer;
		return this;
	}

}
