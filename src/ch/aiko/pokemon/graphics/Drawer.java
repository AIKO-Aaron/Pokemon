package ch.aiko.pokemon.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import ch.aiko.pokemon.settings.Settings;
import ch.aiko.pokemon.sprite.Sprite;

public class Drawer {

	private Frame f;

	public Drawer(Frame f) {
		this.f = f;
	}

	public void drawRect(int x, int y, int w, int h, int color) {
		for (int i = 0; i < w; i++) {
			if (i + x + y * f.getWidth() >= f.pixels.length) continue;
			f.pixels[i + x + y * f.getWidth()] = color;
			if (i + x + (y + h) * f.getWidth() >= f.pixels.length) continue;
			f.pixels[i + x + (y + h) * f.getWidth()] = color;
		}

		for (int i = 0; i < h; i++) {
			if ((y + i) * f.getWidth() + x >= f.pixels.length) continue;
			f.pixels[(y + i) * f.getWidth() + x] = color;
			if ((y + i) * f.getWidth() + x + w >= f.pixels.length) continue;
			f.pixels[(y + i) * f.getWidth() + x + w] = color;
		}
	}

	public void fillRect(int x, int y, int w, int h, int col) {
		for (int i = 0; i < w * h; i++) {
			if (x + (y + i / w) * f.getWidth() + i % w >= f.pixels.length || x + (y + i / w) * f.getWidth() + i % w < 0) continue;
			f.pixels[x + (y + i / w) * f.getWidth() + i % w] = col;
		}
	}

	public void drawText(String text, int x, int y, int size, int col, String font) {
		Sprite sprite = new Sprite(createText(text, size, col, font));
		drawTile(sprite, x, y);
	}
	
	public void drawText(String text, int x, int y, int size, int col) {
		Sprite sprite = new Sprite(createText(text, size, col, Settings.font));
		drawTile(sprite, x, y);
	}

	protected BufferedImage createText(String text, int size, int col, String font) {
		String font1 = font.contains("fonts/") ? font.split("/")[font.split("/").length - 1].split("\\.")[0].replace("-", " ") : font;
		
		if (!exists(font1)) {
			//System.out.println(font);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			try {
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, extract(font)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Font f = new Font(font, 0, size);
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

	protected Font getDefaultFont(String font) {
		for(Font f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) if(f.getFontName().equalsIgnoreCase(font)) return f;
		return null;
	}
	
	protected boolean exists(String font) {
		// font = font.replace("-", " ").split("/")[font.replace("-", " ").split("/").length - 1].split("\\.")[0];
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		for (Font f : e.getAllFonts()) {
			//System.out.println(f.getFontName() + ":" + font);
			if (f.getFontName().equalsIgnoreCase(font)) return true;
		}

		return false;
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
	
	/**
	 * Draws an Image. White background gets replaced by the third argument.
	 * After that it gets drawn to the screen.
	 * 
	 * @param img The Image that is going to be drawn, as a Sprite instance.
	 * @param x The x coordinate of the start.
	 * @param y The y coordinate of the start.
	 * @param backCol The background color that should be put in instead of black.
	 */
	public void drawTile(Sprite img, int x, int y, int backCol) {
		int[] pi = img.getPixels();
		for (int i = 0; i < img.getWidth() + img.getHeight() * f.getWidth(); i++) {
			int xx = (i % img.getWidth() + x);
			int yy = (i / img.getWidth() + y + 22);

			if (xx < 0 || xx >= f.getWidth()) continue;

			if (xx + yy * f.getWidth() < f.pixels.length && i < pi.length && i >= 0 && xx + yy * f.getWidth() >= 0) f.pixels[xx + yy * f.getWidth()] = pi[i] == 0 ? backCol : pi[i];

		}
	}

	public void drawTile(Sprite img, int x, int y, int w, int h) {
		drawTile(new Sprite(img.getImage(w, h)), x, y);
	}

	protected int getColor(int x, int y) {
		if (x + y * f.getWidth() < 0 || x + y * f.getWidth() >= f.pixels.length) return 0;
		return f.pixels[x + y * f.getWidth()];
	}

	public Frame getFrame() {
		return f;
	}

	public File extract(String filePath) {
		try {
			File f = File.createTempFile(filePath, null);
			FileOutputStream resourceOS = new FileOutputStream(f);
			byte[] byteArray = new byte[1024];
			int i;
			InputStream classIS = getClass().getResourceAsStream("/" + filePath);
			while ((i = classIS.read(byteArray)) > 0) {
				resourceOS.write(byteArray, 0, i);
			}
			classIS.close();
			resourceOS.close();
			return f;
		} catch (Exception e) {
			System.out.println("An error has occurred while extracting a resource. This may mean the program is missing functionality, please contact the developer.\nError Description:\n" + e.getMessage());
			//e.printStackTrace();
			return null;
		}
	}
}
