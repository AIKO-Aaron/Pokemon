package ch.aiko.pokemon;

import ch.aiko.modloader.ModLoader;
import ch.aiko.pokemon.client.PokemonClient;
import ch.aiko.pokemon.entity.Teleporter;
import ch.aiko.pokemon.entity.player.Player;
import ch.aiko.pokemon.graphics.GraphicsHandler;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.level.Level;
import ch.aiko.pokemon.pokemons.PokeUtil;
import ch.aiko.pokemon.pokemons.Pokemons;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.util.FileUtil;
import ch.aiko.util.Log;
import ch.aiko.util.PropertyUtil;

import javax.swing.UIManager;

public class Pokemon {

	public static boolean PRELOAD = false;
	public static final boolean ONLINE = true;

	public static Pokemon pokemon;
	public static boolean DEBUG = false;
	public static PropertyUtil serverUUIDs = new PropertyUtil(FileUtil.LoadFile(FileUtil.getRunningJar().getParent() + "/uuids.properties"));
	public static final Log out = new Log(Pokemon.class);
	/**
	 * The only player you'll need
	 */
	public static Player player;

	public GraphicsHandler handler;

	public Pokemon() {
		Settings.load();
		Language.setup();

		// If we are in eclipse
		boolean isDir = FileUtil.getRunningJar().isDirectory();

		out.println("Starting Modloader...");
		ModLoader.loadMods(out, (isDir ? FileUtil.getRunningJar().getParent() : FileUtil.getRunningJar().getAbsolutePath()) + "/mods/", () -> load());
		out.println("Done loading mods. Starting threads...");

		if (PRELOAD) PokeUtil.loadEmAll();

		if(handler != null) handler.start();
	}

	public void load() {
		if (!ONLINE) {
			out.println("Core engine started loading");

			Pokemons.init();

			player = new Player(32 * 3, 32 * 2);

			Level level = new Level();
			Level l2 = new Level("/ch/aiko/pokemon/level/center.layout");

			// level.loadLevel("/ch/aiko/pokemon/level/level2.bin", null);
			level.loadLevel("/ch/aiko/pokemon/level/test.layout");
			level.addPlayer(player);

			level.addEntity(new Teleporter(8 * 32 - 16, 11 * 32, l2, 8 * 32 - 16, 10 * 32));
			l2.addEntity(new Teleporter(8 * 32 - 16, 11 * 32, level, 8 * 32 - 16, 12 * 32));

			handler = new GraphicsHandler(level, player);
			out.println("Core engine done loading");
		} else {
			String ip = "10.0.0.96";
			String uuid = serverUUIDs.getValue(ip);
			System.out.println(uuid);
			
			PokemonClient client = new PokemonClient("10.0.0.96", uuid); // Now you know my ip :(
			
			// handler = new GraphicsHandler(null, null);
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--debug-mode=true")) DEBUG = true;
			if (arg.equalsIgnoreCase("-preload")) PRELOAD = true;
		}
		pokemon = new Pokemon();
	}
}
