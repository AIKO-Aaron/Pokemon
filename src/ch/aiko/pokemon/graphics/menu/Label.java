package ch.aiko.pokemon.graphics.menu;

import java.awt.Font;

import ch.aiko.engine.graphics.Renderer;
import ch.aiko.pokemon.settings.Settings;

public class Label extends MenuObject {

	protected int x, y;
	protected String text;
	protected int size = 25;
	protected int color = 0xFFFF00FF;
	
	public Label(int x, int y, String text) {
		this.x = x;
		this.y = y;
		this.text = text;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setColor(int col) {
		this.color = col;
	}
	
	public void render(Renderer r) {
		r.drawText(text, new Font(Settings.font, 0, size), x, y, color);
	}
	
}
