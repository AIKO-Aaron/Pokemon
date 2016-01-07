package ch.aiko.engine;

import static ch.aiko.engine.Window.HEIGHT;
import static ch.aiko.engine.Window.WIDTH;
import static ch.aiko.engine.Window.pixels;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.util.ImageUtil;

public class Renderer {

	public static void fillRect(int x, int y, int w, int h, int color) {
		for (int yy = y; yy < y + h; yy++) {
			for (int xx = x; xx < x + w; xx++) {
				int index = xx + yy * WIDTH;
				if (index >= pixels.pixelColors.length || index < 0 || xx < 0 || xx >= WIDTH) continue;
				pixels.pixelColors[index] = color;
			}
		}
	}

	public static void drawPixels(int x, int y, int widthOfPixelArray, int heightOfPixelArray, int[] pi) {
		for (int xx = 0; xx < widthOfPixelArray; xx++) {
			for (int yy = 0; yy < heightOfPixelArray; yy++) {
				int index1 = xx + yy * widthOfPixelArray;
				int index2 = (xx + x) + (yy + y) * WIDTH;
				if (index1 < 0 || index2 < 0 || index1 >= pi.length || index2 >= pixels.pixelColors.length || xx + x < 0 || xx + x >= WIDTH) continue;
				pixels.pixelColors[index2] = pi[index1] == 0 ? getColor(index2) : pi[index1];
			}
		}
	}

	public static void drawImage(int x, int y, BufferedImage img, int layer) {
		pixels.img.getGraphics().drawImage(img, x, y, null);
	}

	public static void drawImage(int x, int y, int w, int h, BufferedImage img, int layer) {
		drawImage(x, y, ImageUtil.toBufferedImage(img.getScaledInstance(w, h, BufferedImage.SCALE_FAST)), layer);
	}

	public static void drawImage(int x, int y, BufferedImage img) {
		drawImage(x, y, img, 0);
	}

	public static void drawImage(float x, float y, BufferedImage img) {
		drawImage((int) x, (int) y, img);
	}

	public static int getColor(int x, int y) {
		return x + y * WIDTH < pixels.pixelColors.length && x + y * WIDTH >= 0 ? pixels.pixelColors[x + y * WIDTH] : 0xFF000000;
	}

	public static int getColor(int index) {
		int i = index < 0 || index > WIDTH * HEIGHT ? 0 : pixels.pixelColors[index];
		return i;
	}

	public static void drawImage(float x, float y, BufferedImage loadImageInClassPath, int i) {
		drawImage((int) x, (int) y, loadImageInClassPath, i);
	}

	public static void drawRect(int x, int y, int w, int h, int color) {
		for (int xx = x; xx < x + w; xx++) {
			int index1 = xx + y * WIDTH;
			int index2 = xx + (y + h) * WIDTH;

			if (xx >= WIDTH) continue;

			if (index1 >= 0 && index1 < pixels.pixelColors.length) pixels.pixelColors[index1] = color;
			if (index2 >= 0 && index2 < pixels.pixelColors.length) pixels.pixelColors[index2] = color;

		}
		for (int yy = y; yy < y + h; yy++) {
			int index1 = x + yy * WIDTH;
			int index2 = x + w + yy * WIDTH;

			if (x >= WIDTH) continue;

			if (index1 >= 0 && index1 < pixels.pixelColors.length) pixels.pixelColors[index1] = color;
			if (index2 >= 0 && index2 < pixels.pixelColors.length) pixels.pixelColors[index2] = color;
		}
	}

	/**
	 * for (int xx = x; xx < x + w; xx++) { if (xx <= WIDTH && xx >= 0 && y >= 0 && y <= HEIGHT && !(xx + y * WIDTH >= pixels.pixelColors.length)) pixels.pixelColors[xx + y * WIDTH] = color; if (xx <= WIDTH && xx >= 0 && (y + h) >= 0 && (y + h) <= HEIGHT && !(xx + (y + h) * WIDTH >= pixels.pixelColors.length)) pixels.pixelColors[xx + (y + h) * WIDTH] = color; }
	 */

	public static void drawText(String text, int x, int y, int size, int col, String font) {
		Graphics g = pixels.img.getGraphics();

		Font f = Settings.userFont.deriveFont((float) size);
		FontMetrics fm = g.getFontMetrics(f);
		int yOff = (int) fm.getLineMetrics(text, g).getHeight();
		g.setFont(f);
		g.setColor(new Color(col));
		g.drawString(text, x, y + yOff);
	}

	public static void drawText(String text, int x, int y, int size, int col) {
		// drawImage(x, y, createText(text, size, col, Settings.font));
		drawText(text, x, y, size, col, Settings.font);
	}

	protected Font getDefaultFont(String font) {
		for (Font f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts())
			if (f.getFontName().equalsIgnoreCase(font)) return f;
		return null;
	}

	protected static boolean exists(String font) {
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		for (Font f : e.getAllFonts()) {
			if (f.getFontName().equalsIgnoreCase(font)) return true;
		}

		return false;
	}

	protected static Point getLength(Font f, String s) {
		AffineTransform tr = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(tr, true, true);
		return new Point((int) f.getStringBounds(s, frc).getWidth(), (int) f.getStringBounds(s, frc).getHeight());
	}

	public static File extract(String filePath) {
		try {
			File f = File.createTempFile(filePath, null);
			FileOutputStream resourceOS = new FileOutputStream(f);
			byte[] byteArray = new byte[1024];
			int i;
			InputStream classIS = Renderer.class.getResourceAsStream("/" + filePath);
			while ((i = classIS.read(byteArray)) > 0) {
				resourceOS.write(byteArray, 0, i);
			}
			classIS.close();
			resourceOS.close();
			return f;
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
	}

	public static void black() {
		for (int i = 0; i < pixels.pixelColors.length; i++) {
			pixels.pixelColors[i] = Window.Background != null ? 0x00000000 : 0xFF000000;
		}
	}

	/** METHODS FOR POKEMON PROJECT */
	public static void drawRectOffset(int x, int y, int w, int h, int color) {
		drawRect(x - Frame.getLevel().getCamera().x, y - Frame.getLevel().getCamera().y, w, h, color);
	}

	public static void drawSprite(Sprite s, int x, int y) {
		drawPixels(x - Frame.getLevel().getCamera().x, y - Frame.getLevel().getCamera().y, s.getWidth(), s.getHeight(), s.getPixels());
	}

	public static void drawTextInLevel(String text, int x, int y, int size, int col, String font) {
		drawText(text, x - Frame.getLevel().getCamera().x, y - Frame.getLevel().getCamera().y, size, col, font);
	}

	public static void drawTextOffset(String text, int x, int y, int size, int col) {
		drawText(text, x - Frame.getLevel().getCamera().x, y - Frame.getLevel().getCamera().y, size, col);
	}

	public static void drawImage(BufferedImage img, int x, int y) {

	}

}
