package ch.aiko.engine;

import static ch.aiko.engine.Window.HEIGHT;
import static ch.aiko.engine.Window.WIDTH;
import static ch.aiko.engine.Window.pixels;

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

import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.util.ImageUtil;

public class Renderer {

	public static void clear() {
		for (int i = 0; i < pixels.pixelColors.length; i++) {
			// pixels.pixelColors[i] = 0xFF000000;
			// pixels.pixelLayers[i] = Integer.MIN_VALUE;
			pixels.pixelSet[i] = false;
		}
	}

	public static void fillRect(int x, int y, int w, int h, int color, int layer) {
		for (int yy = y; yy < y + h; yy++) {
			for (int xx = x; xx < x + w; xx++) {
				int index = xx + yy * WIDTH;
				if (index >= pixels.pixelColors.length || index < 0 || xx < 0 || xx >= WIDTH || (layer < pixels.pixelLayers[index] && pixels.pixelSet[index])) continue;
				pixels.pixelColors[index] = color;
				pixels.pixelLayers[index] = layer;
				pixels.pixelSet[index] = true;
			}
		}
	}

	public static void fillRect(int x, int y, int w, int h, int color) {
		fillRect(x, y, w, h, color, 0);
	}

	public static void drawPixels(int x, int y, int widthOfPixelArray, int heightOfPixelArray, int[] pi, int layer) {
		for (int xx = 0; xx < widthOfPixelArray; xx++) {
			for (int yy = 0; yy < heightOfPixelArray; yy++) {
				int index1 = xx + yy * widthOfPixelArray;
				int index2 = (xx + x) + (yy + y) * WIDTH;
				if (index1 < 0 || index2 < 0 || index1 >= pi.length || index2 >= pixels.pixelColors.length || xx + x < 0 || xx + x >= WIDTH || (layer < pixels.pixelLayers[index2] && pixels.pixelSet[index2])) continue;
				pixels.pixelSet[index2] = true;
				pixels.pixelColors[index2] = pi[index1] == 0 ? getColor(index2) : pi[index1];
				pixels.pixelLayers[index2] = layer;
			}
		}
	}

	public static void drawPixels(int x, int y, int widthOfPixelArray, int[] pi, int layer) {
		drawPixels(x, y, widthOfPixelArray, pi.length / widthOfPixelArray, pi, layer);
	}

	public static void drawImage(int x, int y, BufferedImage img, int layer) {
		int[] pixels = new int[img.getWidth() * img.getHeight()];
		pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
		drawPixels(x, y, img.getWidth(), img.getHeight(), pixels, layer);
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
		if (i == 0xFF000000) pixels.pixelSet[index] = false;
		return i;
	}

	public static void drawImage(float x, float y, BufferedImage loadImageInClassPath, int i) {
		drawImage((int) x, (int) y, loadImageInClassPath, i);
	}

	public static void drawRect(int x, int y, int w, int h, int color, int layer) {
		for (int xx = x; xx < x + w; xx++) {
			int index1 = xx + y * WIDTH;
			int index2 = xx + (y + h) * WIDTH;

			if (xx >= WIDTH) continue;

			if (index1 >= 0 && index1 < pixels.pixelColors.length && !(layer < pixels.pixelLayers[index1] && pixels.pixelSet[index1])) {
				pixels.pixelColors[index1] = color;
				pixels.pixelLayers[index1] = layer;
				pixels.pixelSet[index1] = true;
			}

			if (index2 >= 0 && index2 < pixels.pixelColors.length && !(layer < pixels.pixelLayers[index2] && pixels.pixelSet[index2])) {
				pixels.pixelColors[index2] = color;
				pixels.pixelLayers[index2] = layer;
				pixels.pixelSet[index2] = true;
			}
		}
		for (int yy = y; yy < y + h; yy++) {
			int index1 = x + yy * WIDTH;
			int index2 = x + (yy + h) * WIDTH;

			if (x >= WIDTH) continue;

			if (index1 >= 0 && index1 < pixels.pixelColors.length && !(layer < pixels.pixelLayers[index1] && pixels.pixelSet[index1])) {
				pixels.pixelColors[index1] = color;
				pixels.pixelLayers[index1] = layer;
				pixels.pixelSet[index1] = true;
			}

			if (index2 >= 0 && index2 < pixels.pixelColors.length && !(layer < pixels.pixelLayers[index2] && pixels.pixelSet[index2])) {
				pixels.pixelColors[index2] = color;
				pixels.pixelLayers[index2] = layer;
				pixels.pixelSet[index2] = true;
			}

		}
	}

	public static void drawText(String text, int x, int y, int size, int col, String font) {
		drawImage(x, y, createText(text, size, col, font));
	}

	public static void drawText(String text, int x, int y, int size, int col) {
		drawImage(x, y + 30, createText(text, size, col, Settings.font));
	}

	public static void drawRect(int x, int y, int width, int height, int color) {
		drawRect(x, y, width, height, color, 0);
	}

	public static BufferedImage createText(String text, int size, int col, String font) {
		String font1 = font.contains("fonts/") ? font.split("/")[font.split("/").length - 1].split("\\.")[0].replace("-", " ") : font;

		if (!exists(font1)) {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			try {
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, extract(font)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Font f = new Font(font, 0, size);
		Point p = getLength(f, text);
		BufferedImage img = new BufferedImage(p.x + 3, p.y + size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setColor(new Color(1F, 1F, 1F, 0F));
		g.fillRect(0, 0, p.x, p.y);
		g.setColor(new Color(col));
		g.setFont(f);
		g.drawString(text, 0, size);
		return img;
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
			if (pixels.pixelSet[i]) continue;
			pixels.pixelColors[i] = Window.Background != null ? 0x00000000 : 0xFF000000;
			pixels.pixelLayers[i] = Integer.MIN_VALUE;
		}
	}

	/** METHODS FOR POKEMON PROJECT */
	public static void drawSprite(Sprite s, int x, int y) {
		drawPixels(x - Frame.getLevel().getCamera().x, y - Frame.getLevel().getCamera().y, s.getWidth(), s.getPixels(), 0);
	}

	public static void drawSprite(Sprite s, int x, int y, int layer) {
		drawPixels(x - Frame.getLevel().getCamera().x, y - Frame.getLevel().getCamera().y, s.getWidth(), s.getPixels(), layer);
	}

}
