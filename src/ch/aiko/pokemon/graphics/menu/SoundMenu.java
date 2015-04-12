package ch.aiko.pokemon.graphics.menu;

import java.awt.event.KeyEvent;

import ch.aiko.pokemon.graphics.Drawer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.mob.player.Player;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.pokemon.sound.SoundPlayer;

public class SoundMenu extends Menu {

	private Player p;
	private int index;

	public SoundMenu(Player p) {
		this.p = p;

		index = (int) Settings.GAIN + 50;

	}

	public void onClose(Drawer d) {
		p.setPaused(false);
		SoundPlayer.stopLoop();

		Settings.set("gain", "" + (index - 50));
		Settings.GAIN = index - 50;
	}

	public void onOpen(Drawer d) {
		p.setPaused(true);
		SoundPlayer.loopSound("ch/aiko/pokemon/sounds/beep15.mp3", index);
	}

	public String name() {
		return "SoundMenu";
	}

	public void update(Drawer d) {
		if (d.getFrame().getTimesPressed(KeyEvent.VK_X) > 0) d.getFrame().closeMenu();

		if (d.getFrame().getTimesPressed(KeyEvent.VK_DOWN) > 0) index = index > 1 ? index - 1 : 1;
		if (d.getFrame().getTimesPressed(KeyEvent.VK_UP) > 0) index = index <= 100 - 1 ? index + 1 : 100;
	}

	public void paint(Drawer d) {
		d.fillRect(0, 0, Frame.WIDTH, Frame.HEIGHT, 0xFF000000);
		d.drawText(Language.translate("VOLUME") + ": " + index, 10, 50, 75, 0xFFFFFFFF);
		SoundPlayer.music.volume = index - 50;
	}

}
