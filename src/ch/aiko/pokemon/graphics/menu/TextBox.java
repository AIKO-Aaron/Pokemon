package ch.aiko.pokemon.graphics.menu;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import ch.aiko.pokemon.graphics.Drawer;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.settings.Settings;

public class TextBox extends Menu {

	private Player p;
	private String text;
	private boolean open = false;
	private int index, maxIndex;

	public TextBox(Player p, String textKey) {
		text = Language.translate(textKey);

		Font f = new Font(Settings.font, 0, 25);
		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
		int textWidth = (int) f.getStringBounds(text, frc).getWidth();

		System.out.println(text.length());
		System.out.println(textWidth);

		this.p = p;
		
		open = true;
	}

	public String name() {
		return "TextBox:" + text;
	}

	public void onOpen(Drawer d) {
		open = true;
		p.setPaused(true);
	}

	public void onClose(Drawer d) {
		open = false;
		p.setPaused(false);
	}

	public void paint(Drawer d) {
		d.fillRect(0, d.getFrame().getHeight() - 50, d.getFrame().getWidth(), 50, 0xFFFFFFFF);
		d.drawRect(0, d.getFrame().getHeight() - 50, d.getFrame().getWidth(), 50, 0xFF000000);
	}

	public void update(Drawer d) {
		if (d.getFrame().getTimesPressed(KeyEvent.VK_SPACE) > 0 || d.getFrame().getTimesPressed(KeyEvent.VK_ESCAPE) > 0) {

		}
	}

	public boolean isOpened() {
		return open;
	}
}
