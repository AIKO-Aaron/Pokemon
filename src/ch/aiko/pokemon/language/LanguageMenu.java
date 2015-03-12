package ch.aiko.pokemon.language;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import ch.aiko.pokemon.Drawer;
import ch.aiko.pokemon.Menu;
import ch.aiko.pokemon.mob.Player;
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
	
	public void onOpen(Drawer d) {
		p.setPaused(true);
		max = d.getFrame().getHeight() / Text_size;
	}
	
	public void onClose(Drawer d) {
		p.setPaused(false);
	}
	
	public void paint(Drawer d) {
		d.fillRect(0, 0, d.getFrame().getWidth(), d.getFrame().getHeight(), Background_Color);
		
		ArrayList<Language> langs = Language.getLanguages();
		
		for(int i = starting_point; i < langs.size(); i++) {			
			d.drawText(langs.get(i).getValue(langs.get(i).getName()) + " " + (langs.get(i) == Language.current ? Language.translate("selected") : "") + " " + (i == index ? "  <---" : ""), 0, Text_size * (i-starting_point), Text_size, 0xFF000000 |  ~Background_Color, "fonts/Sans.ttf");
		}
	}
	
	public void update(Drawer d) {
		if (d.getFrame().getTimesPressed(KeyEvent.VK_X) > 0) d.getFrame().closeMenu();
		
		if(d.getFrame().getTimesPressed(Settings.getInteger("keyA")) > 0) Language.setCurrentLanguage(index);
		if (d.getFrame().getTimesPressed(KeyEvent.VK_UP) > 0) index = index > 0 ? index - 1 : 0;
		if (d.getFrame().getTimesPressed(KeyEvent.VK_DOWN) > 0) index = index < Language.getLanguages().size() - 1 ? index + 1 : Language.getLanguages().size() - 1;
		
		if(index - starting_point < 0) starting_point--;
		if(index - starting_point + 1 >= max) starting_point++;
	}
	
}
