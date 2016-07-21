package ch.aiko.pokemon;

import ch.aiko.pokemon.entity.Teleporter;
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

		Level l2 = new Level("/ch/aiko/pokemon/level/center.layout");
		
		Player p = new Player(32 * 3, 32 * 2);
		Level level = new Level();

		//level.loadLevel("/ch/aiko/pokemon/level/level2.bin", null);
		level.loadLevel("/ch/aiko/pokemon/level/test.layout");
		level.addPlayer(p);
		
		level.addEntity(new Teleporter(8 * 32 - 16, 11 * 32, l2, 8 * 32 - 16, 10 * 32));
		l2.addEntity(new Teleporter(8 * 32 - 16, 11 * 32, level, 8 * 32 - 16, 12 * 32));		
		
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
