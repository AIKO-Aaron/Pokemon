package ch.aiko.pokemon;

import java.util.Random;

import javax.swing.UIManager;

import ch.aiko.engine.graphics.Renderer;
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

		Level level = new Level();
		level.addRenderable((Renderer r) -> {
			for (int i = 0; i < r.getWidth() * r.getHeight(); i++)
				r.getPixels()[i] = rand.nextInt(0xFFFFFF) + 0xFF000000;
		});

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
