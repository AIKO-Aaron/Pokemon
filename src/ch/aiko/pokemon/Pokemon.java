package ch.aiko.pokemon;

import ch.aiko.engine.graphics.Screen;
import ch.aiko.modloader.ModLoader;
import ch.aiko.pokemon.attacks.Attack;
import ch.aiko.pokemon.basic.GameHandler;
import ch.aiko.pokemon.basic.MainMenu;
import ch.aiko.pokemon.basic.PokemonEvents;
import ch.aiko.pokemon.client.PokemonClient;
import ch.aiko.pokemon.entity.player.Player;
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

	public static final Log out = new Log(Pokemon.class);

	public static boolean PRELOAD = false;
	public static boolean ONLINE;
	public static Pokemon pokemon;
	public static boolean DEBUG = false;
	public static PropertyUtil serverUUIDs = new PropertyUtil(FileUtil.LoadFile(FileUtil.getRunningJar().getParent() + "/uuids.properties"));
	public static int TeamSize = 6;
	public GameHandler handler;

	/**
	 * The only player you'll need
	 */
	public static Player player;

	/**
	 * Only when online playing
	 */
	public static PokemonClient client;

	public Pokemon() {
		pokemon = this;
		Settings.load();
		Language.setup();
		Attack.init();
		handler = new GameHandler();
	}

	public void start(String ip) {
		// If we are in eclipse
		boolean isDir = FileUtil.getRunningJar().isDirectory();

		out.println("Starting Modloader...");
		ModLoader.loadMods((isDir ? FileUtil.getRunningJar().getParent() : FileUtil.getRunningJar().getParent()) + "/mods/", () -> load(ip));
		out.println("Done loading mods. Starting threads...");

		if (PRELOAD) PokeUtil.loadEmAll();
	}

	public void load(String ip) {
		out.println("Core engine started loading");
		Pokemons.init();
		if (ip == null) {
			ONLINE = false;

			player = new Player(32 * 3, 32 * 2);
			// Load Player
			player.load();
			
			Level level = new Level("/ch/aiko/pokemon/level/test.layout");

			handler.init(level, player);
		} else {
			ONLINE = true;
			connect(ip);
		}
		out.println("Core engine done loading");
		ModLoader.performEvent(new PokemonEvents.ClientStartEvent());
	}

	public void connect(String ip) {
		String uuid = serverUUIDs.getValue(ip);
		client = new PokemonClient(ip, uuid);
		client.waitFor();

		player = new Player(client.x, client.y);
		player.team = client.team;
		player.trainersDefeated = client.trainersDefeated;
		player.setDirection(client.dir);

		Level level = new Level(client.pathToLevel);
		handler.init(level, player);
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
		new MainMenu();
	}

	public static Screen getScreen() {
		return pokemon.handler.screen;
	}
}
