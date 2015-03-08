package ch.aiko.pokemon.sprite;

import java.awt.image.BufferedImage;

import ch.aiko.util.ImageUtil;

public class SpriteSheet {

	private int[] pixels;
	private BufferedImage img;
	private int spriteWidth;
	private int spriteHeight;

	public SpriteSheet(BufferedImage img, int sW, int sH) {
		this.spriteWidth = sW;
		this.spriteHeight = sH;
		this.img = img;
		pixels = new int[img.getWidth() * img.getHeight()];
		pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
	}
	
	public SpriteSheet(String imgpath, int sW, int sH) {
		BufferedImage img = ImageUtil.loadImageInClassPath(imgpath);
		this.spriteWidth = sW;
		this.spriteHeight = sH;
		this.img = img;
		pixels = new int[img.getWidth() * img.getHeight()];
		pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
	}
	
	public SpriteSheet(Sprite s, int w, int h) {
		this.spriteWidth = w;
		this.spriteHeight = h;
		this.img = s.getImage();
		this.pixels = s.getPixels();
	}
	
	public Sprite getSprite(int x, int y) {
		return new Sprite(img, x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
	}

	public Sprite getSprite(int i) {
		int w = img.getWidth() / spriteWidth;
		return new Sprite(img, (i%w) * spriteWidth, (i/w) * spriteHeight, spriteWidth, spriteHeight);
	}
}
