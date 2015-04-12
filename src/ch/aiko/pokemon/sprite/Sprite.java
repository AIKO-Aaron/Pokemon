package ch.aiko.pokemon.sprite;

import java.awt.Image;
import java.awt.image.BufferedImage;

import ch.aiko.util.ImageUtil;

public class Sprite {

	private int[] pixels;
	private BufferedImage img;
	private int width, height;
	private int x = 0, y = 0;

	public Sprite(BufferedImage img, int x, int y, int width, int height) {
		width = maxWidth(x, width, img);
		height = maxHeight(y, height, img);
		
		if(width == 0 || height == 0) return;
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		if(width * height < 0) return;
		
		pixels = new int[width * height];
		pixels = img.getRGB(x, y, width, height, pixels, 0, width);
		this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		this.img.setRGB(0, 0, width, height, pixels, 0, width);
	}

	public Sprite(BufferedImage img) {
		this.img = img;
		if (img == null) return;
		this.width = img.getWidth();
		this.height = img.getHeight();
		pixels = new int[img.getWidth() * img.getHeight()];
		pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
	}

	public Sprite(int color, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				img.setRGB(x, y, color);
			}
		}
		this.img = img;
		this.width = img.getWidth();
		this.height = img.getHeight();
		pixels = new int[img.getWidth() * img.getHeight()];
		pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
	}

	public Sprite(int[] pixels, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x + y * width >= pixels.length) continue;
				img.setRGB(x, y, pixels[x + y * width]);
			}
		}
		this.img = img;
		this.width = img.getWidth();
		this.height = img.getHeight();
		this.pixels = pixels;
	}

	private void loadPixels() {
		pixels = new int[img.getWidth() * img.getHeight()];
		pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
	}

	public BufferedImage getImage() {
		return img;
	}

	public int[] getPixels() {
		return pixels;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Tile toTile(int x, int y) {
		return new Tile(this, x, y, false);
	}

	public Tile toTile(int x, int y, int w, int h, boolean solid) {
		return new Tile(this, x, y, solid);
	}

	public Tile toTile(int x, int y, boolean solid) {
		return new Tile(this, x, y, solid);
	}

	public Sprite setImage(Image i) {
		BufferedImage image = ImageUtil.toBufferedImage(i);
		if (i == null) return this;
		img = image;
		width = image.getWidth();
		height = image.getHeight();
		loadPixels();
		return this;
	}

	public Sprite copy() {
		return new Sprite(pixels, width, height);
	}

	public void clear() {
		System.out.println("Clearing: " + this);
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] == 0) {
				pixels[i] = 0;
			}
		}
	}

	public BufferedImage getImage(int w, int h) {
		return ImageUtil.resize(img, w, h);
	}

	public int[] getPixels(int w, int h) {
		return ImageUtil.resize(img, w, h).getRGB(0, 0, w, h, new int[w * h], 0, w);
	}

	public void setAlpha(int alpha) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = alpha << 24 | getRed(i) | getBlue(i) | getGreen(i);
		}
	}

	public void setAlpha(int alpha, int x, int y) {
		int i = x + y * width;
		pixels[i] = alpha << 24 | getRed(i) | getBlue(i) | getGreen(i);
	}

	public void setAlpha(int alpha, int index) {
		pixels[index] = alpha << 24 | getRed(index) | getBlue(index) | getGreen(index);
	}

	public void setRed(int r) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = getAlpha(i) | r << 16 | getBlue(i) | getGreen(i);
		}
	}

	public void setGreen(int g) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = getAlpha(i) | g << 8 | getRed(i) | getBlue(i);
		}
	}

	public void setBlue(int b) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = getAlpha(i) | getRed(i) | b << 0 | getGreen(i);
		}
	}

	public void setRed(int r, int x, int y) {
		int i = x + y * width;
		pixels[i] = r << 16 | getAlpha(i) | getBlue(i) | getGreen(i);
	}

	public void setRed(int r, int index) {
		pixels[index] = r << 16 | getAlpha(index) | getBlue(index) | getGreen(index);
	}

	public void setGreen(int g, int x, int y) {
		int i = x + y * width;
		pixels[i] = g << 8 | getRed(i) | getBlue(i) | getAlpha(i);
	}

	public void setGreen(int g, int index) {
		pixels[index] = g << 8 | getRed(index) | getBlue(index) | getAlpha(index);
	}

	public void setBlue(int b, int x, int y) {
		int i = x + y * width;
		pixels[i] = b << 0 | getRed(i) | getAlpha(i) | getGreen(i);
	}

	public void setBlue(int b, int index) {
		pixels[index] = b << 0 | getRed(index) | getAlpha(index) | getGreen(index);
	}

	public int getAlpha(int x, int y) {
		int ret = pixels[x + y * width];
		ret = (ret & 0xFF000000) >> 24;
		return ret;
	}

	public int getRed(int x, int y) {
		int ret = pixels[x + y * width];
		ret = (ret & 0x00FF0000) >> 16;
		return ret;
	}

	public int getGreen(int x, int y) {
		int ret = pixels[x + y * width];
		ret = (ret & 0x0000FF00) >> 8;
		return ret;
	}

	public int getBlue(int x, int y) {
		int ret = pixels[x + y * width];
		ret = (ret & 0x000000FF) >> 0;
		return ret;
	}

	public int getAlpha(int i) {
		int ret = pixels[i];
		ret = (ret & 0xFF000000) >> 24;
		return ret;
	}

	public int getRed(int i) {
		int ret = pixels[i];
		ret = (ret & 0x00FF0000) >> 16;
		return ret;
	}

	public int getGreen(int i) {
		int ret = pixels[i];
		ret = (ret & 0x0000FF00) >> 8;
		return ret;
	}

	public int getBlue(int i) {
		int ret = pixels[i];
		ret = (ret & 0x000000FF) >> 0;
		return ret;
	}

	public int getColor(int x, int y) {
		return pixels[x + y * width];
	}
	
	public Sprite removeColor(int color) {
		for(int i = 0; i< pixels.length; i++) {
			if(pixels[i] == color) pixels[i] = 0;
		}
		return this;
	}
	
	public int maxWidth(int x, int width, BufferedImage img) {
		if(img.getWidth() <= x + width) return img.getWidth() - x;
		else return width;
	}
	
	public int maxHeight(int y, int height, BufferedImage img) {
		if(img.getHeight() <= y + height) return img.getHeight() - y;
		else return height;
	}
	
	public Sprite getScaledInstance(int newWidth, int newHeight) {
		return new Sprite(ImageUtil.toBufferedImage(img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH)));
	}
}
