package ch.aiko.pokemon.sprite;

import java.awt.image.BufferedImage;

public class Tile {

	public Sprite sprite;
	public int x, y;
	public boolean solid;
	public int w, h;

	public Tile(Sprite sprite, int x, int y, boolean solid) {
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		this.solid = solid;
		this.w = sprite.getWidth();
		this.h = sprite.getHeight();
	}

	public Tile(Sprite sprite, int x, int y, int w, int h, boolean solid) {
		this.sprite = sprite.setImage((BufferedImage) sprite.getImage().getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH));
		this.x = x;
		this.y = y;
		this.solid = solid;
		this.w = w;
		this.h = h;
	}

	public int[] getPixels() {
		if (sprite == null) return new int[0];
		return sprite.getPixels();
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

}
