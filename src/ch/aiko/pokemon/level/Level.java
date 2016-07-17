package ch.aiko.pokemon.level;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Stack;

import ch.aiko.as.ASArray;
import ch.aiko.as.ASDataBase;
import ch.aiko.as.ASDataType;
import ch.aiko.as.ASField;
import ch.aiko.as.ASObject;
import ch.aiko.as.SerializationReader;
import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.LayerBuilder;
import ch.aiko.engine.graphics.LayerContainer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.SpriteSerialization;
import ch.aiko.engine.sprite.SpriteSheet;
import ch.aiko.engine.sprite.Tile;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.entity.player.Player;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.util.ImageUtil;

public class Level extends LayerContainer {

	public static final int TILE_SIZE = 32;

	private SpriteSheet sheet = new SpriteSheet("/ch/aiko/pokemon/textures/Sprites.png", 16, 16);

	public ASDataType type;
	public int fieldSize = 64;
	public short[] tileData;
	public Layer[] tiles;
	public LevelPalette lp;

	public Stack<Layer> openMenus = new Stack<Layer>();

	public Level() {
		SpriteSerialization.TILE_SIZE = TILE_SIZE;
		tiles = new Layer[fieldSize * fieldSize];
		resetOffset = false;
		type = new ASDataType() {
			public void load(ASObject c) {
				ASArray array = c.getArray("Tiles");
				if (array != null) tileData = array.getShortData();
				ASObject palette = c.getObject("Palette");
				if (palette != null) lp = new LevelPalette(palette, this);
				ASField size = c.getField("Size");
				if (size != null) fieldSize = SerializationReader.readInt(size.data, 0);

				decode();
			}

			public void getData(ASObject thisObject) {
				if (tileData == null) tileData = new short[fieldSize * fieldSize];
				if (lp == null) lp = new LevelPalette("Palette", this);
				ASArray tiles = ASArray.Short("Tiles", tileData);
				thisObject.addArray(tiles);
				thisObject.addObject(lp.toObject());
				thisObject.addField(ASField.Integer("Size", fieldSize));
			}
		};
	}

	public void getData(ASObject obj) {
		type.getData(obj);
	}

	public void decode() {
		for (int indexed = 0; indexed < tileData.length; indexed++) {
			Tile tile = lp.getCoding(tileData[indexed], (indexed % fieldSize) * TILE_SIZE, (indexed / fieldSize) * TILE_SIZE);
			tiles[indexed] = addLayer(new LayerBuilder().setName("Tile" + indexed).setRenderable(tile).toLayer());
		}
	}
	
	public void loadLevel(String pathToLevel, HashMap<Integer, Integer> palette) {
		if (pathToLevel.endsWith(".png")) {
			if (palette == null) return;
			BufferedImage img = ImageUtil.loadImageInClassPath(pathToLevel);
			for (int xpos = 0; xpos < img.getWidth(); xpos++) {
				for (int ypos = 0; ypos < img.getHeight(); ypos++) {
					System.out.println(palette);
					System.out.println("Searching for: " + img.getRGB(xpos, ypos));
					Integer index = palette.get(0xFF000000 | img.getRGB(xpos, ypos));
					if (index == null) index = 0;
					addRenderable(new Tile(sheet.getSprite(index), xpos * sheet.getSpriteWidth(), ypos * sheet.getSpriteHeight(), 0));
				}
			}
		} else if (pathToLevel.endsWith(".bin")) {
			ASDataBase db = ASDataBase.createFromResource(pathToLevel);
			if (db == null) {
				System.err.println("DB == null");
				return;
			}
			SpriteSerialization.deserializeSprites(db);
			ASObject levelData = db.getObject("LevelData");
			type.load(levelData);
		}
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

	public void layerRender(Renderer r) {}

	public void layerUpdate(Screen s) {
		if (s.getInput().popKeyPressed(KeyEvent.VK_ESCAPE)) {
			if (openMenus.isEmpty()) Pokemon.pokemon.handler.window.quit();
			else closeTopMenu();
		}
	}

	public Tile getTile(int x, int y) {
		if (x + y * fieldSize > tiles.length) return null;
		return (Tile) tiles[x + y * fieldSize].getRenderable();
	}

	public boolean isSolid(int x, int y, int layer) {
		int edge = TILE_SIZE * fieldSize;
		if (x < 0 || x > edge || y < 0 || y > edge) return true; // Out of bounds

		int xcol = x / TILE_SIZE;
		int ycol = y / TILE_SIZE;

		return getTile(xcol, ycol).layer > layer;
	}

	public String getName() {
		return "Level";
	}

	public void openMenu(Menu menu) {
		menu.onOpen();
		openMenus.add(addLayer(menu));
	}

	public void closeTopMenu() {
		Layer topLayer = openMenus.pop();
		((Menu) topLayer).onClose();
		removeLayer(topLayer);
	}

	public void closeAllMenus() {
		while (openMenus.isEmpty())
			closeTopMenu();
	}
	
	public void closeMenu(Menu m) {
		openMenus.remove(m);
		m.onClose();
		removeLayer(m);
	}

	public void addEntity(Entity p) {
		addLayer(new LayerBuilder().setRenderable(p).setUpdatable(p).setLayer(1).toLayer());
	}

	public void addPlayer(Player p) {
		addLayer(new LayerBuilder().setRenderable(p).setUpdatable(p).setLayer(Player.PLAYER_RENDERED_LAYER).setName("Player").toLayer());
	}

}
