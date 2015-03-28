package ch.aiko.pokemon;

import java.util.HashMap;

import javax.swing.UIManager;

import ch.aiko.pokemon.entity.PressurePlate;
import ch.aiko.pokemon.graphics.Animation;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.level.Level;
import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.mob.Trainer;
import ch.aiko.pokemon.pokemon.Pokemons;
import ch.aiko.pokemon.pokemon.TeamPokemon;
import ch.aiko.pokemon.pokemon.attack.Move;
import ch.aiko.pokemon.pokemon.attack.Moves;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.pokemon.sprite.SpriteSheet;
import ch.aiko.pokemon.sprite.Tile;

public class Pokemon {

	public static Frame frame;
	public static Tile tile;
	public static Player player;
	public static Level level1, level2;

	public static SpriteSheet sheet = new SpriteSheet("/ch/aiko/pokemon/textures/img.jpg", 100, 100);
	public static SpriteSheet sheet1 = new SpriteSheet("/ch/aiko/pokemon/textures/Sprites.png", 16, 16);
	public static SpriteSheet pokemons = new SpriteSheet("/ch/aiko/pokemon/textures/diamond-pearl-frame2.png", 80, 80);
	
	public static Pokemons pokes;
	public static Moves moves;
	
	public static boolean debug = false;
	

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(args.length >= 1 && args[0] != null && args[0].equalsIgnoreCase("--debug-mode=true")) debug = true;

		Settings.load();
		Language.setup();
		
		pokes = new Pokemons();
		moves = new Moves();
		
		player = new Player(sheet1.getSprite(2, 0), 320, 320, 32, 32);
		level1 = new Level(player, "/ch/aiko/pokemon/level/Level.png", generateCoding(true), 16, 16);
		level2 = new Level(player, "/ch/aiko/pokemon/level/Level2.png", generateCoding(true), 8, 8);
				
		TeamPokemon teampokemon1 = new TeamPokemon(player, Pokemons.get("Pikachu"), 1, 1, 1, 1, 1, 1, 1, 1, 0, new Move[]{Moves.NULL, Moves.NULL, Moves.NULL, Moves.NULL});
		TeamPokemon teampokemon2 = new TeamPokemon(player, Pokemons.get("Sandamer"), 1, 1, 1, 1, 1, 1, 1, 1, 1, new Move[]{Moves.NULL, Moves.NULL, Moves.NULL, Moves.NULL});
		TeamPokemon teampokemon3 = new TeamPokemon(player, Pokemons.get("Taubsi"), 1, 1, 1, 1, 1, 1, 1, 1, 2, new Move[]{Moves.NULL, Moves.NULL, Moves.NULL, Moves.NULL});
		TeamPokemon teampokemon4 = new TeamPokemon(player, Pokemons.get("Pikachu"), 1, 1, 1, 1, 1, 1, 1, 1, 3, new Move[]{Moves.NULL, Moves.NULL, Moves.NULL, Moves.NULL});
		TeamPokemon teampokemon5 = new TeamPokemon(player, Pokemons.get("Pikachu"), 1, 1, 1, 1, 1, 1, 1, 1, 4, new Move[]{Moves.NULL, Moves.NULL, Moves.NULL, Moves.NULL});
		TeamPokemon teampokemon6 = new TeamPokemon(player, Pokemons.get("Pikachu"), 1, 1, 1, 1, 1, 1, 1, 1, 5, new Move[]{Moves.NULL, Moves.NULL, Moves.NULL, Moves.NULL});
		teampokemon1.xp(1);

		Trainer test = new Trainer(320, 320, 1, true, true, new TeamPokemon[]{teampokemon6}){
			Animation t;
			
			public void userInit() {
				System.out.println("Initializing: " + t);
				t = new Animation(new SpriteSheet("/ch/aiko/pokemon/textures/TrainerSprites.png", 32, 32), 60);
				System.out.println("Initialized: " + t);
			}
			
			public void userUpdate(Drawer d) {
				
			}
			
			public void userDraw(Drawer d) {
				t.drawNext(d, x, y);
			}
		};
		level1.addMob(test);
		
		Trainer profOak = new Trainer(320, 320, 0);
		level2.addMob(profOak);
		
		//level1.addTile(new Tile(sheet1.getSprite(0), 434, 240, false));
		
		frame = new Frame(level1);
		frame.loopStart();
	}

	private static HashMap<Integer, Tile> generateCoding(boolean b) {
		HashMap<Integer, Tile> coding = new HashMap<Integer, Tile>();
		coding.put(0xFF000000, sheet1.getSprite(0, 0).toTile(0, 0, b));
		coding.put(0xFFFF0000, sheet1.getSprite(1, 0).toTile(0, 0, b));
		coding.put(0xFFFFFFFF, new Sprite(0xFF00FFFF, 16, 16).toTile(0, 0, false));
		coding.put(0xFF000099, new PressurePlate(0, 0, Pokemons.NULL.getSprite().copy()){public void onStepOn(Player p, int x, int y, Frame f){tel(p, f);}});
		return coding;
	}

	private static void tel(Player p, Frame f) {
		if(debug) System.out.println(p.getX() + ":" + p.getY());
		if(f.getLevel() == level1) p.teleport(f, level2, 216, 315);
		else p.teleport(f, level1, 460, 960);
	}
	
	public static void save() {
		System.out.println("Saving current Configuration to: " + Settings.getPath() + "/settings/settings.cfg");
	}
	
	public static void reloadTextures() {
		sheet = new SpriteSheet("/textures/img.jpg", 100, 100);
		sheet1 = new SpriteSheet("/textures/Sprites.png", 16, 16);
		pokemons = new SpriteSheet("/textures/diamond-pearl-frame2.png", 80, 80);
	}
	
	public static Frame getMainFrame() {
		return frame;
	}
}
