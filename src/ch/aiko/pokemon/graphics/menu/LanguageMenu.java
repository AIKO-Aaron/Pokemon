package ch.aiko.pokemon.graphics.menu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import ch.aiko.engine.KeyBoard;
import ch.aiko.engine.Menu;
import ch.aiko.engine.Renderer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.mob.player.Player;
import ch.aiko.pokemon.settings.Settings;

public class LanguageMenu extends Menu {

	public static final int Background_Color = 0xFF000000;
	public static final int Text_size = 75;

	private Player p;
	private int index;
	private int starting_point;
	private int max;

	public LanguageMenu(Player p) {
		this.p = p;
	}

	public void onOpen() {
		p.setPaused(true);
		max = Frame.HEIGHT / Text_size;
	}

	public void onClose() {
		p.setPaused(false);
	}

	public void draw() {
		Renderer.fillRect(0, 0, Frame.WIDTH, Frame.HEIGHT, Background_Color);

		ArrayList<Language> langs = Language.getLanguages();

		for (int i = starting_point; i < langs.size(); i++) {
			Renderer.drawText(langs.get(i).getValue(langs.get(i).getName()) + " " + (langs.get(i) == Language.current ? Language.translate("selected") : "") + " " + (i == index ? "  <---" : ""), 0, Text_size * (i - starting_point), Text_size, 0xFF000000 | ~Background_Color);
		}
	}

	public void update(double d) {
		if (KeyBoard.getTimesPressed(KeyEvent.VK_X) > 0) Frame.closeMenu();

		if (KeyBoard.getTimesPressed(Settings.getInteger("keyA")) > 0) Language.setCurrentLanguage(index);
		if (KeyBoard.getTimesPressed(KeyEvent.VK_UP) > 0) index = index > 0 ? index - 1 : 0;
		if (KeyBoard.getTimesPressed(KeyEvent.VK_DOWN) > 0) index = index < Language.getLanguages().size() - 1 ? index + 1 : Language.getLanguages().size() - 1;

		if (index - starting_point < 0) starting_point--;
		if (index - starting_point + 1 >= max) starting_point++;
	}

	public String name() {
		return "LanguageSelection";
	}
}
