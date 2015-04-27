package ch.aiko.pokemon.graphics.menu;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import ch.aiko.engine.KeyBoard;
import ch.aiko.engine.Menu;
import ch.aiko.engine.Renderer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.mob.player.Player;
import ch.aiko.pokemon.settings.Settings;

public class FontMenu extends Menu {

	private Player p;
	private Font[] fonts;
	private Font[] user;
	private int index;
	private int y;

	public FontMenu(Player p) {
		this.p = p;
		System.out.println("Getting Fonts");
		fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		ArrayList<Font> userFonts = new ArrayList<Font>();

		Set<String> files = new Reflections("ch/aiko/pokemon/fonts", new ResourcesScanner()).getResources(new com.google.common.base.Predicate<String>() {
			public boolean apply(String arg0) {
				return true;
			}
		});
		for (String s : files)
			userFonts.add(registerFont(s));

		System.out.println(userFonts.size());
		user = userFonts.toArray(new Font[userFonts.size()]);
	}

	public void onOpen() {
		p.setPaused(true);
	}

	public void onClose() {
		p.setPaused(false);
	};

	public void draw() {
		Renderer.fillRect(0, 0, Frame.WIDTH, Frame.HEIGHT, 0xFF000000);
		if ((20 + (fonts.length - y)) * 25 > fonts.length) Renderer.drawText("Default Fonts", 0, 0, 25, 0xFFFFFFFF);

		for (int i = y; i < fonts.length - user.length + 2 && i < Frame.HEIGHT / 25 + index; i++) {
			if (i < 0 || i >= fonts.length) continue;
			Renderer.drawText(fonts[i].getFontName() + (fonts[i].getFontName().equalsIgnoreCase(Settings.font) ? " (!) " : "") + (i == index ? "  <---" : ""), 50, (i + 1 - y) * 25, 25, 0xFFFFFFFF, fonts[i].getFontName());
			// System.out.println(fonts[i]);
		}

		if (y + Frame.HEIGHT / 25 < fonts.length) return;

		Renderer.drawText("User Fonts", 10, (3 + (fonts.length - y)) * 25, 25, 0xFFFFFFFF);

		for (int i = 0; i < user.length; i++) {
			Renderer.drawText(user[i].getFontName() + (getFileName(user[index - fonts.length].getFontName().replace(" ", "-")).equalsIgnoreCase(Settings.font) ? " (!) " : "") + (i == index - fonts.length ? "  <---" : ""), 50, (i + fonts.length - y + 4) * 25, 25, 0xFFFFFFFF, user[i].getFontName());
		}
	}

	public void update(double delta) {
		// System.out.println(y);
		int i = Frame.HEIGHT / 25;
		if (KeyBoard.getTimesPressed(KeyEvent.VK_U) > 0) index = fonts.length - 1;
		if (KeyBoard.getTimesPressed(Settings.getInteger("keyMenu")) > 0) Frame.closeMenu();
		if (KeyBoard.getTimesPressed(Settings.getInteger("keyUp")) > 0) {
			index = index > 0 ? index - 1 : 0;
			if (index - 5 <= y) y = index > 5 ? index - 5 : 0;
		}
		if (KeyBoard.getTimesPressed(Settings.getInteger("keyDown")) > 0) {
			index = index < fonts.length + user.length - 1 ? index + 1 : fonts.length + user.length - 1;
			if (index + 5 >= i + y) y = index - i + 5;
		}
		if (KeyBoard.getTimesPressed(Settings.getInteger("keyLeft")) > 0) {
			index = index - 10 >= 0 ? index - 10 : 0;
			if (index - 10 <= y) y = index - 10;
		}
		if (KeyBoard.getTimesPressed(Settings.getInteger("keyRight")) > 0) {
			index = index + 10 < fonts.length + user.length - 2 ? index + 10 : fonts.length + user.length - 2;
			if (index + 10 >= i + y) y = index - i + 10;
		}

		if (KeyBoard.getTimesPressed(KeyEvent.VK_SPACE) > 0) {
			Settings.set("font", index < fonts.length ? fonts[index].getFontName() : getFileName(user[index - fonts.length].getFontName().replace(" ", "-")));
		}
	}

	public String getFileName(String fontName) {
		Set<String> files = new Reflections("fonts", new ResourcesScanner()).getResources(new com.google.common.base.Predicate<String>() {
			public boolean apply(String arg0) {
				return true;
			}
		});
		for (String f : files)
			if (f.contains(fontName)) return f;

		return null;
	}

	public Font registerFont(String font) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font f = null;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, extract(font));
			ge.registerFont(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			e.printStackTrace();
			return null;
		}
	}

	public String name() {
		return "FontSelection";
	}

}
