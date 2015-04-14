package ch.aiko.pokemon.sprite;

import java.awt.image.BufferedImage;

import ch.aiko.util.ImageUtil;

public class SpriteSheet {

	private int[] pixels;
	private BufferedImage img;
	private int spriteWidth;
	private int spriteHeight;

	private int entireWidth, entireHeight, xOff, yOff;

	public SpriteSheet(BufferedImage img, int sW, int sH) {
		this.spriteWidth = sW;
		this.spriteHeight = sH;
		this.img = img;
		pixels = new int[img.getWidth() * img.getHeight()];
		pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());

		entireWidth = img.getWidth();
		entireHeight = img.getHeight();
	}

	public SpriteSheet(String imgpath, int sW, int sH) {
		BufferedImage img = ImageUtil.loadImageInClassPath(imgpath);
		this.spriteWidth = sW;
		this.spriteHeight = sH;
		this.img = img;
		pixels = new int[img.getWidth() * img.getHeight()];
		pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());

		entireWidth = img.getWidth();
		entireHeight = img.getHeight();
	}

	public SpriteSheet(String imgpath, int sW, int sH, int nW, int nH) {
		BufferedImage img = ImageUtil.loadImageInClassPath(imgpath);
		this.spriteWidth = nW;
		this.spriteHeight = nH;

		int width = img.getWidth() / sW * nW;
		int height = (int) ((float) img.getHeight() / (float) sH * (float) nH);

		//img = ImageUtil.toBufferedImage(img.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH));
		img = ImageUtil.resize(img, width, height);
		
		this.img = img;
		pixels = new int[img.getWidth() * img.getHeight()];
		pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());

		entireWidth = img.getWidth();
		entireHeight = img.getHeight();
	}
	
	public SpriteSheet offset(int x, int y) {
		this.xOff = x;
		this.yOff = y;
		
		return this;
	}

	public SpriteSheet(Sprite s, int w, int h) {
		this.spriteWidth = w;
		this.spriteHeight = h;
		this.img = s.getImage();
		this.pixels = s.getPixels();

		entireWidth = img.getWidth();
		entireHeight = img.getHeight();
	}

	public Sprite getSprite(int x, int y) {
		x += xOff;
		y += yOff;
		return new Sprite(img, x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
	}

	public Sprite getSprite(int i) {
		int w = img.getWidth() / spriteWidth;
		int x = (i % w) * spriteWidth + xOff;
		int y = (i / w) * spriteHeight + yOff;
		return new Sprite(img, x, y, spriteWidth, spriteHeight);
	}

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public int getSpriteHeight() {
		return spriteHeight;
	}

	public int getSheetWidth() {
		return entireWidth;
	}

	public int getSheetHeight() {
		return entireHeight;
	}

	public SpriteSheet removeColor(int color) {
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] == color) {
				pixels[i] = 0x00000000;
				img.setRGB(i % entireWidth, i / entireWidth, 0x00000000);
			}
		}
		return this;
	}

	public SpriteSheet replaceColor(int color, int newC) {
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] == color) {
				pixels[i] = newC;
				img.setRGB(i % entireWidth, i / entireWidth, newC);
			}
		}
		return this;
	}
	
	public int getSpriteCount() {
		// return (getSheetWidth() / getSpriteWidth()) * (getSheetWidth() / getSpriteWidth());
		return (getSheetWidth() * getSheetHeight()) / (getSpriteWidth() * getSpriteHeight());
	}
}
