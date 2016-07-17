package ch.aiko.pokemon.graphics.menu;

import java.awt.Font;
import java.awt.FontMetrics;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.LayerBuilder;
import ch.aiko.engine.graphics.Renderable;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.graphics.Updatable;
import ch.aiko.pokemon.settings.Settings;

public class Button implements Renderable, Updatable {

	private static final int THICKNESS = 3;

	protected int x, y, w, h;
	protected String text;
	protected boolean selected = false;
	protected ButtonAction r = (Button b) -> {};

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

	public Button(int x, int y, int w, int h, String text, ButtonAction r) {
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

	public Button setAction(ButtonAction r) {
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

	public ButtonAction getAction() {
		return r;
	}

	public void update(Screen screen) {
		
	}

	public void render(Renderer renderer) {
		renderer.drawRect(x, y, w, h, selected ? 0xFFFF00FF : 0xFF000000, THICKNESS);
		renderer.fillRect(x + THICKNESS, y + THICKNESS, w - 2 * THICKNESS, h - 2 * THICKNESS, 0xFFFFFFFF);

		int xstart = x + (w - getWidth(renderer.getScreen(), new Font(Settings.font, 0, h / 2), text)) / 2;
		int ystart = y + (h - getHeight(renderer.getScreen(), new Font(Settings.font, 0, h / 2))) / 2;
		
		renderer.drawText(text, Settings.font, h / 2, 0, xstart, ystart, 0xFF000000);
	}

	public static final int getWidth(Screen s, Font f, String text) {
		FontMetrics metrics = s.getGraphics().getFontMetrics(f);
		return metrics.stringWidth(text);
	}

	public static final int getHeight(Screen s, Font f) {
		FontMetrics metrics = s.getGraphics().getFontMetrics(f);
		return metrics.getHeight();
	}

	public void buttonPressed() {
		r.buttonPressed(this);
	}

	public Layer toLayer(int layer) {
		return new LayerBuilder().setRenderable(this).setUpdatable(this).setLayer(layer).toLayer();
	}

}
