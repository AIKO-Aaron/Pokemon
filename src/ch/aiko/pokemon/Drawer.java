package ch.aiko.pokemon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import ch.aiko.pokemon.sprite.Sprite;

public class Drawer{
	
	private Frame f;
	
	public Drawer(Frame f) {
		this.f = f;
	}
	
	public void drawRect(int x, int y, int w, int h, int color) {
		for(int i = 0; i < w; i++) {
			if(i + x + y * f.getWidth() >= f.pixels.length) continue;
			f.pixels[i + x + y * f.getWidth()] = color;
			if(i + x + (y+h) * f.getWidth() >= f.pixels.length) continue;
			f.pixels[i + x + (y + h) * f.getWidth()] = color;
		}
		
		for(int i = 0; i < h; i++) {
			if((y + i) * f.getWidth() + x >= f.pixels.length) continue;
			f.pixels[(y + i) * f.getWidth() + x] = color;
			if((y + i) * f.getWidth() + x + w >= f.pixels.length) continue;
			f.pixels[(y + i) * f.getWidth() + x + w] = color;
		}
	}
	
	public void fillRect(int x, int y, int w, int h, int col) {
		for(int i = 0; i < w * h; i++) {
			if(x + (y + i / w) * f.getWidth() + i % w >= f.pixels.length || x + (y + i / w) * f.getWidth() + i % w < 0) continue;
			f.pixels[x + (y + i / w) * f.getWidth() + i % w] = col;
		}
	}
	
	public void drawText(String text, int x, int y, int size, int col) {
		Sprite sprite = new Sprite(createText(text, size, col));
		drawTile(sprite, x, y);
	}
	
	protected BufferedImage createText(String text, int size, int col) {
		Font f = new Font("Arial", 0, size);
		Point p = getLength(f, text);
		BufferedImage img = new BufferedImage(p.x + 3, p.y + size / 4, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setColor(new Color(1F, 1F, 1F, 0F));
		g.fillRect(0, 0, p.x, p.y);
		g.setColor(new Color(col));
		g.setFont(f);
		g.drawString(text, 0, size);
		return img;
	}
	
	protected Point getLength(Font f, String s) {
		AffineTransform tr = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(tr, true, true);
		return new Point((int) f.getStringBounds(s, frc).getWidth(), (int) f.getStringBounds(s, frc).getHeight());
	}
	
	public void drawTile(Sprite img, int x, int y) {
		int[] pi = img.getPixels();
		for (int i = 0; i < img.getWidth() + img.getHeight() * f.getWidth(); i++) {
			int xx = (i % img.getWidth() + x);
			int yy = (i / img.getWidth() + y + 22);

			if (xx < 0 || xx >= f.getWidth()) continue;

			if (xx + yy * f.getWidth() < f.pixels.length && i < pi.length && i >= 0 && xx + yy * f.getWidth() >= 0) f.pixels[xx + yy * f.getWidth()] = pi[i] == 0 ? getColor(xx, yy) : pi[i];

		}
	}
	
	public void drawTile(Sprite img, int x, int y, int w, int h) {
		drawTile(new Sprite(img.getImage(w, h)), x, y);
	}
	
	protected int getColor(int x, int y) {
		if(x + y * f.getWidth() < 0 || x + y * f.getWidth() >= f.pixels.length) return 0;
		return f.pixels[x + y * f.getWidth()];
	}
	
	public Frame getFrame() {
		return f;
	}

}
