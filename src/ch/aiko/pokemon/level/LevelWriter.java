package ch.aiko.pokemon.level;

import java.io.File;
import java.util.ArrayList;

import ch.aiko.as.ASDataBase;
import ch.aiko.engine.sprite.Sprite;
import ch.aiko.engine.sprite.SpriteSerialization;
import ch.aiko.engine.sprite.SpriteSheet;

public class LevelWriter {

	// Just to write the maps --> Testing only
	public static void main(String[] args) {
		LevelWriter writer = new LevelWriter("level1");
		writer.write(System.getProperty("user.home") + "/Desktop/level1.bin");
		System.out.println("Done");
	}

	ASDataBase db;
	ArrayList<Sprite> spriteCodings;
	ArrayList<Integer> solids;
	ArrayList<Integer> ids;

	public LevelWriter(String name) {
		db = new ASDataBase(name);
		Level level = new Level();
		level.type.name = "LevelData";
		level.type.reload();
		spriteCodings = new ArrayList<Sprite>();
		solids = new ArrayList<Integer>();
		ids = new ArrayList<Integer>();

		loadLayout(level);
		loadSprites();
		db.addObject(SpriteSerialization.serializeSprites());
		createCoding(level);

		level.type.reload();
		db.addObject(level.type);
	}

	public void loadSprites() {
		SpriteSheet spritesheet = SpriteSerialization.createFromImage("/ch/aiko/pokemon/textures/RubyTiles.png", 16, 16);

		addCoding(spritesheet.getSprite(0, 0), 0, 0);
		addCoding(spritesheet.getSprite(4, 2), 1, 1);

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 8; j++) {
				addCoding(spritesheet.getSprite(39 + i, 31 + j), (i != 6 && j != 7) ? 1 : 0, i + j * 7); // Pokecenter
			}
		}

	}

	public void addCoding(Sprite s, int solid, int id) {
		spriteCodings.add(s);
		solids.add(solid);
		ids.add(id);
	}

	public void addCoding(Sprite s, int solid) {
		int id = 0;
		for (int i = 0; i < ids.size() + 1; i++) {
			if (!ids.contains(id)) {
				addCoding(s, solid, id);
				return;
			}
			id++;
		}
	}

	public void createCoding(Level level) {
		for (int i = 0; i < spriteCodings.size(); i++) {
			level.lp.addCoding((short) i, spriteCodings.get(i), solids.get(i));
		}
		level.lp.reload();
	}

	public void loadLayout(Level level) {
		short[] tileLayer1 = new short[level.fieldSize * level.fieldSize];
		for (int i = 0; i < tileLayer1.length; i++) {
			tileLayer1[i] = 0;
		}
		level.tileData.add(tileLayer1);

		short[] tileLayer2 = new short[level.fieldSize * level.fieldSize];
		for (int i = 0; i < level.fieldSize; i++) {
			tileLayer2[i + 0 * level.fieldSize] = 1;
			tileLayer2[i + 1 * level.fieldSize] = 1;

			tileLayer2[i + (level.fieldSize - 2) * level.fieldSize] = 1;
			tileLayer2[i + (level.fieldSize - 1) * level.fieldSize] = 1;

			tileLayer2[0 + i * level.fieldSize] = 1;
			tileLayer2[1 + i * level.fieldSize] = 1;

			tileLayer2[level.fieldSize - 2 + i * level.fieldSize] = 1;
			tileLayer2[level.fieldSize - 1 + i * level.fieldSize] = 1;
		}
		int xoff = 5;
		int yoff = 5;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 8; j++) {
				tileLayer2[(i + xoff) + (j + yoff) * level.fieldSize] = (short) (j + i * 8 + 2);
			}
		}
		level.tileData.add(tileLayer2);
	}

	public void write(String string) {
		if (new File(string).exists()) new File(string).delete();
		db.saveToFile(string);
	}

}
