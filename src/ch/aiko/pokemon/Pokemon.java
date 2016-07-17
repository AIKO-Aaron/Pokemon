package ch.aiko.pokemon;

import ch.aiko.pokemon.entity.player.Player;
import ch.aiko.pokemon.graphics.GraphicsHandler;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.level.Level;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.util.Log;

import javax.swing.UIManager;

public class Pokemon {

	public static Pokemon pokemon;
	public static boolean DEBUG = false;
	public static final Log out = new Log(Pokemon.class);

	public GraphicsHandler handler;

	public Pokemon() {
		Settings.load();
		Language.setup();

		Player p = new Player();
		Level level = new Level();

		level.loadLevel("/ch/aiko/pokemon/level/level1.bin", null);
		level.addPlayer(p);
				
		handler = new GraphicsHandler(level);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (args.length >= 1 && args[0] != null && args[0].equalsIgnoreCase("--debug-mode=true")) DEBUG = true;
		pokemon = new Pokemon();
	}
}
