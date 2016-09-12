package ch.aiko.pokemon.level;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Stack;

import ch.aiko.as.ASDataBase;
import ch.aiko.as.ASObject;
import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.LayerBuilder;
import ch.aiko.engine.graphics.LayerContainer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.SpriteSerialization;
import ch.aiko.engine.sprite.Tile;
import ch.aiko.modloader.ModLoader;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.basic.PokemonEvents;
import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.entity.player.Player;
import ch.aiko.pokemon.graphics.menu.Chat;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.pokemon.settings.Settings;

public class Level extends LayerContainer {

	public static final boolean DEBUG = false;

	public LevelSerialization type;
	// public int fieldSize = 64;
	public int fieldWidth, fieldHeight;
	public ArrayList<short[]> tileData = new ArrayList<short[]>();
	public ArrayList<Layer[]> tiles = new ArrayList<Layer[]>();
	public int layerCount = 0;
	public LevelPalette lp;
	public String path;

	public static boolean rendering = false;
	public static ArrayList<String> texts = new ArrayList<String>();
	public static ArrayList<Integer> time = new ArrayList<Integer>();
	public static Font font = new Font(Settings.font, 0, Chat.stringSize);

	private int tileSize = 32;
	public Stack<Layer> openMenus = new Stack<Layer>();
	private ArrayList<Entity> entities = new ArrayList<Entity>();

	public Level() {
		SpriteSerialization.TILE_SIZE = tileSize;
		type = new LevelSerialization(this);
		tiles = new ArrayList<Layer[]>();
		resetOffset = false;
		fieldWidth = 64;
		fieldHeight = 64;
	}

	public Level(int tileSize) {
		this.tileSize = tileSize;
		SpriteSerialization.TILE_SIZE = tileSize;
		type = new LevelSerialization(this);
		tiles = new ArrayList<Layer[]>();
		resetOffset = false;
		fieldWidth = 64;
		fieldHeight = 64;
	}

	/**
	 * Doesn't load the level! Use {@link ch.aiko.pokemon.level.Level#reload() reload} to load from path
	 * 
	 * @param path
	 *            The Path to the layout / level file
	 */
	public Level(String path) {
		SpriteSerialization.TILE_SIZE = tileSize;
		type = new LevelSerialization(this);
		tiles = new ArrayList<Layer[]>();
		resetOffset = false;
		this.path = path;
		fieldWidth = 64;
		fieldHeight = 64;
	}

	/**
	 * Doesn't load the level! Use {@link ch.aiko.pokemon.level.Level#reload() reload} to load from path
	 * 
	 * @param path
	 *            The Path to the layout / level file
	 * @param tileSize
	 *            The size of each tile
	 */
	public Level(String path, int tileSize) {
		this.tileSize = tileSize;
		SpriteSerialization.TILE_SIZE = tileSize;
		type = new LevelSerialization(this);
		tiles = new ArrayList<Layer[]>();
		resetOffset = false;
		this.path = path;
		fieldWidth = 64;
		fieldHeight = 64;
	}

	public void setTileSize(int size) {
		SpriteSerialization.TILE_SIZE = tileSize;
		this.tileSize = size;
	}

	public void getData(ASObject obj) {
		type.getData(obj);
	}

	public void decode() {
		int addedTiles = 0;
		for (int layer = 0; layer < tileData.size(); layer++) {
			short[] data = tileData.get(layer);
			Layer[] current = new Layer[fieldWidth * fieldHeight];
			for (int indexed = 0; indexed < data.length; indexed++) {
				boolean b = false;
				for (int i = 0; i < layer; i++) {
					if (tileData.get(i)[indexed] == tileData.get(layer)[indexed]) b = true;
				}
				if (b) continue;
				addedTiles++;
				Tile tile = lp.getCoding(data[indexed], (indexed % fieldWidth) * tileSize, (indexed / fieldWidth) * tileSize);
				if (indexed < current.length) current[indexed] = addLayer(new LayerBuilder().setLayer(layer).setNeedsInput(false).setName("Tile" + indexed).setRenderable(tile).toLayer());
			}
			tiles.add(current);
		}
		Pokemon.out.println("Loaded " + layerCount + " layers for the level, with a total of " + (fieldWidth * fieldHeight * layerCount) + " tiles, but only " + addedTiles + " tiles were added");
		rendering = true;
	}

	public Level setPath(String path) {
		this.path = path;
		return this;
	}

	public Level loadLevel(String pathToLevel) {
		path = pathToLevel;

		addClientLog("Loaded Level");

		removeAllLayers();
		tiles.clear();
		tileData.clear();
		layerCount = 0;

		Pokemon.out.println("Loading Level");
		if (pathToLevel.endsWith(".bin")) {
			ASDataBase db = ASDataBase.createFromResource(pathToLevel);
			if (db == null) {
				System.err.println("DB == null");
				return this;
			}
			SpriteSerialization.deserializeSprites(db);
			ASObject levelData = db.getObject("LevelData");
			type.load(levelData);
		} else if (pathToLevel.endsWith(".layout")) {
			LayoutLoader loader = new LayoutLoader(pathToLevel);
			loader.loadLevel(this);
		}

		return this;
	}

	public static void addChatMessage(String string, int duration) {
		texts.add(string);
		time.add(duration);
	}

	public static void addChatMessage(String string) {
		texts.add(string);
		time.add(5 * 60);
	}

	public static void addClientLog(String log, int duration) {
		addChatMessage("[Client] " + log, duration);
	}

	public static void addClientLog(String log) {
		addChatMessage("[Client] " + log, 5 * 60);
	}

	public int getLevel() {
		return -1;
	}

	public boolean stopsRendering() {
		return true;
	}

	public boolean stopsUpdating() {
		return true;
	}

	public final int getStringWidth(Screen s, Font f, String text) {
		FontMetrics metrics = s.getGraphics().getFontMetrics(f);
		return metrics.stringWidth(text);
	}

	public final int getStringHeight(Screen s, Font f) {
		FontMetrics metrics = s.getGraphics().getFontMetrics(f);
		return metrics.getHeight();
	}

	@Override
	public void layerRender(Renderer r) {}

	@Override
	public void postRender(Renderer r) {
		if (DEBUG) {
			for (int i = 0; i < tiles.size(); i++) {
				Layer[] ls = tiles.get(i);
				for (int j = 0; j < ls.length; j++) {
					Layer l = ls[j];
					if (l == null || l.getRenderable() == null) continue;
					Tile t = (Tile) l.getRenderable();
					if (t.layer > 0) r.drawRect(t.x, t.y, t.w, t.h, 0xFFFF00FF);
				}
			}
		}

		int xof = r.getXOffset();
		int yof = r.getYOffset();
		r.setOffset(0, 0);

		for (int i = 0; i < texts.size() && i < time.size();) {
			int value = time.get(i);
			String key = texts.get(i);
			int x = r.getWidth() - 400;
			r.fillRect(x, 55 + Chat.stringSize * (i + 1), 400, Chat.stringSize * key.split("\n").length, 0xFFFFFFFF);
			int val = 0xFF - (value * 0xFF / 60) & 0xFF;
			if (value > 60) val = 0;
			for (String s : key.split("\n")) {
				r.drawText(s, font, x, 50 + Chat.stringSize * ++i, 0xFF << 24 | val << 16 | val << 8 | val);
			}
		}

		r.setOffset(xof, yof);
	}

	@Override
	public void layerUpdate(Screen sc, Layer layer) {
		if (!Chat.OPEN) {
			if (getInput().popKeyPressed(KeyEvent.VK_ESCAPE)) {
				if (openMenus.isEmpty()) Pokemon.pokemon.handler.window.quit();
				else closeTopMenu();
			} else if (popKeyPressed(KeyEvent.VK_R)) Pokemon.pokemon.handler.setLevel(this);
			if (popKeyPressed(KeyEvent.VK_T)) openMenu(new Chat(sc));
			if (popKeyPressed(KeyEvent.VK_N)) addChatMessage("Test");
		}

		if (!rendering) return;
		for (int i = 0; i < texts.size() && i < time.size(); i++) {
			if (time.get(i) <= 0) {
				texts.remove(i);
				time.remove(i);
			} else time.set(i, time.get(i) - 1);
		}

		int i = 0;
		for (String key : texts) {
			for (String s : key.split("\n")) {
				if (getStringWidth(sc, font, s) > 400) {
					if (s.contains(" ")) {
						String write = "", t = s.split(" ")[0];
						int l = 0;
						while (getStringWidth(sc, font, write + t) < 400) {
							write += t;
							t = s.split(" ")[++l];
						}
						if (i < 0 || i >= texts.size()) continue;
						texts.set(i, key = key.replace(write + s.substring(write.length()), write + "\n" + s.substring(write.length())));
						++i;
					} else {
						String write = "";
						char t = s.charAt(0);
						int l = 0;
						while (getStringWidth(sc, font, write + t) < 400) {
							write += t;
							t = s.charAt(++l);
						}
						if (i < 0 || i >= texts.size()) continue;
						texts.set(i, key = key.replace(write + s.substring(write.length()), write + "\n" + s.substring(write.length())));
						++i;
					}
				}
			}
		}

		int min = Math.min(texts.size(), time.size());
		for (int j = min; j < time.size(); j++)
			time.remove(j);
		for (int j = min; j < texts.size(); j++)
			texts.remove(j);
	}

	public ArrayList<Tile> getTile(int x, int y) {
		if (x + y * fieldWidth >= fieldWidth * fieldHeight) return new ArrayList<Tile>();
		ArrayList<Tile> ret = new ArrayList<Tile>();
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i)[x + y * fieldWidth] != null) ret.add((Tile) tiles.get(i)[x + y * fieldWidth].getRenderable());
		}
		return ret;
	}

	public boolean isSolid(int x, int y, int layer) {
		int xedge = tileSize * fieldWidth;
		int yedge = tileSize * fieldHeight;
		if (x < 0 || x > xedge || y < 0 || y > yedge) return true; // Out of bounds

		int xcol = x / tileSize;
		int ycol = y / tileSize;
		int xof = x % tileSize;
		int yof = y % tileSize;

		for (Tile t : getTile(xcol, ycol)) {
			if (t != null && t.isSolid(xof, yof, layer)) return true;
		}

		for (Entity e : entities) {
			if (e.getX() < x && e.getX() + e.getSprite().getWidth() > x) {
				if (e.getY() < y && e.getY() + e.getSprite().getHeight() > y) { return true; }
			}
		}

		return false;
	}

	@Override
	public String getName() {
		return "Level";
	}

	public Menu openMenu(Menu menu) {
		menu.onOpen();
		ModLoader.performEvent(new PokemonEvents.MenuOpeningEvent(menu));
		openMenus.add(addLayer(menu));
		return menu;
	}

	public void closeTopMenu() {
		Layer topLayer = openMenus.pop();
		while (!(topLayer instanceof Menu))
			topLayer = openMenus.pop();
		ModLoader.performEvent(new PokemonEvents.MenuClosingEvent((Menu) topLayer));
		((Menu) topLayer).onClose();
		removeLayer(topLayer);
	}

	public void closeAllMenus() {
		while (!openMenus.isEmpty())
			closeTopMenu();
	}

	public void closeMenu(Menu m) {
		openMenus.remove(m);
		ModLoader.performEvent(new PokemonEvents.MenuClosingEvent(m));
		m.onClose();
		removeLayer(m);
	}

	public void addEntity(Entity p) {
		addLayer(p);
		entities.add(p);
	}

	public void addPlayer(Player p) {
		addLayer(p);
	}

	/**
	 * Load the level from the path previously set (with {@link #setPath(String path) setPath(path)} or {@link #loadLevel(String path) loadLevel(path)}
	 * 
	 * @return this
	 */
	public Level reload() {
		if (path != null) loadLevel(path);
		return this;
	}

	public void removeEntity(Entity p) {
		removeLayer(p);
		entities.remove(p);
	}

}
