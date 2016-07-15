package ch.aiko.pokemon;

import java.util.HashMap;
import java.util.Random;

import javax.swing.UIManager;

import ch.aiko.engine.graphics.LayerBuilder;
import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.entity.Player;
import ch.aiko.pokemon.graphics.GraphicsHandler;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.level.Level;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.util.Log;

public class Pokemon {

	public static Pokemon pokemon;
	public static boolean DEBUG = false;
	public static final Log out = new Log(Pokemon.class);

	// public static SpriteSheet sheet1 = new SpriteSheet("/ch/aiko/pokemon/textures/Sprites.png", 16, 16);
	// public static SpriteSheet pokemons = new SpriteSheet("/ch/aiko/pokemon/textures/diamond-pearl-frame2.png", 80, 80);

	public GraphicsHandler handler;
	private Random rand = new Random();

	public Pokemon() {
		Settings.load();
		Language.setup();

		Player p = new Player();
		Entity e = new Entity(50, 50);
		
		Level level = new Level(p);
		
		level.loadLevel("/ch/aiko/pokemon/level/Level.png", generateCoding(false));
		level.addLayer(new LayerBuilder().setRenderable(p).setUpdatable(p).toLayer());
		level.addLayer(new LayerBuilder().setRenderable(e).setUpdatable(e).toLayer());

		handler = new GraphicsHandler(level);
	}
	
	private static HashMap<Integer, Integer> generateCoding(boolean b) {
		HashMap<Integer, Integer> coding = new HashMap<Integer, Integer>();
		coding.put(0xFF000000, 0);
		coding.put(0xFFFF0000, 1);
		coding.put(0xFFFFFFFF, 2);
		coding.put(0xFF000099, 3);
		return coding;
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
