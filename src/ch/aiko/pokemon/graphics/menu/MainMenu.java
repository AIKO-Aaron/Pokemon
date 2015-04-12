package ch.aiko.pokemon.graphics.menu;

import java.awt.event.KeyEvent;

import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.graphics.Drawer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.mob.Trainer;
import ch.aiko.pokemon.mob.player.Player;
import ch.aiko.pokemon.settings.Settings;

public class MainMenu extends Menu {

	private Runnable i1 = new Runnable() {
		public void run() {
			Pokemon.reloadTextures();
			Trainer.reloadTextures();
		}
	};

	private Runnable i2 = new Runnable() {
		public void run() {
			Pokemon.getMainFrame().openMenu(new FontMenu(p));
		}
	};
	
	private Runnable i3 = new Runnable() {
		public void run() {
			Pokemon.getMainFrame().openMenu(new SoundMenu(p));
		}
	};

	private Runnable[] fields = new Runnable[] { i1, i2, i3 };
	private String[] labels = new String[] { "REL_TEX", "FONT", "VOLUME" };
	private int index = 0;

	private Player p;

	public MainMenu(Player p) {
		this.p = p;
	}

	public void onOpen(Drawer d) {
		p.setPaused(true);
	}

	public void onClose(Drawer d) {
		p.setPaused(false);
	}

	public void paint(Drawer d) {
		d.fillRect(0, 0, Frame.WIDTH, Frame.HEIGHT, 0xFF000000);
		for (int i = 0; i < fields.length; i++) {
			d.drawText(Language.translate(labels[i]) + (i == index ? "  <---" : ""), 10, i * 25, 25, 0xFFFFFFFF);
		}
	}

	public void update(Drawer d) {
		if (d.getFrame().getTimesPressed(Settings.getInteger("keyMenu")) > 0) d.getFrame().closeMenu();
		if (d.getFrame().getTimesPressed(Settings.getInteger("keyUp")) > 0) index = index > 0 ? index - 1 : 0;
		if (d.getFrame().getTimesPressed(Settings.getInteger("keyDown")) > 0) index = index < fields.length - 1 ? index + 1 : fields.length - 1;

		if (d.getFrame().getTimesPressed(KeyEvent.VK_SPACE) > 0) fields[index].run();
	}

	public String name() {
		return "MiscallaniousChunk";
	}
}
