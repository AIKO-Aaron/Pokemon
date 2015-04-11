package ch.aiko.pokemon.graphics.menu;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import ch.aiko.pokemon.graphics.Drawer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.util.PokemonList;

public class TextBox extends Menu {

	private Frame f;
	private Player p;
	private String text;
	private boolean open = false;
	private int index, maxIndex, length, size;
	private AffineTransform affinetransform = new AffineTransform();
	private FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
	private PokemonList<Integer> starts = new PokemonList<Integer>();

	public TextBox(Frame frame, Player p, String textKey) {
		System.out.println(textKey);
		text = Language.translate(textKey);
		this.f = frame;

		length = f.getWidth() - 20;

		doMaths();

		this.p = p;

		open = true;
	}

	private void doMaths() {
		int start = 0;
		for (; start < text.split(" ").length;) {
			String s = "";
			for (int i = start; i < text.split(" ").length; i++) {
				s += text.split(" ")[i] + " ";
				if (lengthof(s) >= length) {
					size = s.length() - text.split(" ")[i].length() - 1;
					starts.add(starts.get(starts.size() - 1) + size);
					start = i;
					break;
				} else if (i == text.split(" ").length - 1) {
					start = text.split(" ").length;
					starts.add(text.length());
				}
			}
		}
		maxIndex = starts.size() - 1;
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
		d.fillRect(0, d.getFrame().getHeight() - 75, d.getFrame().getWidth(), 75, 0xFFFFFFFF);
		d.drawRect(0, d.getFrame().getHeight() - 75, d.getFrame().getWidth(), 75, 0xFF000000);
		d.drawText(text.substring(starts.get(index - 1), starts.get(index + 0)), 0, d.getFrame().getHeight() - 100, 25, 0xFF000000);
		if (index < maxIndex) d.drawText(text.substring(starts.get(index + 0), starts.get(index + 1)), 0, d.getFrame().getHeight() - 66, 25, 0xFF000000);
	}

	public void update(Drawer d) {
		if (d.getFrame().getTimesPressed(KeyEvent.VK_SPACE) > 0 || d.getFrame().getTimesPressed(KeyEvent.VK_ESCAPE) > 0) {
			index++;
			
			if(index >= maxIndex) {
				index = maxIndex;
				f.closeMenu();
			}
		}
	}

	public boolean isOpened() {
		return open;
	}

	public int lengthof(String s) {
		Font f = new Font(Settings.font, 0, 25);
		return (int) f.getStringBounds(s, frc).getWidth();
	}

	public boolean canClose() {
		return false;
	}
}
