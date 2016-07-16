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
	}

	ASDataBase db;
	ArrayList<Sprite> spriteCodings;

	public LevelWriter(String name) {
		db = new ASDataBase(name);
		Level level = new Level();
		level.type.name = "LevelData";
		level.type.reload();
		spriteCodings = new ArrayList<Sprite>();

		loadLayout(level);
		loadSprites();
		db.addObject(SpriteSerialization.serializeSprites());
		createCoding(level);

		level.type.reload();
		db.addObject(level.type);
	}

	public void loadSprites() {
		SpriteSheet spritesheet = SpriteSerialization.createFromImage("/ch/aiko/pokemon/textures/Sprites.png", 16, 16);

		addCoding(spritesheet.getSprite(0, 0), false);
		addCoding(spritesheet.getSprite(1, 0), true);

	}

	public void addCoding(Sprite s, boolean solid) {
		spriteCodings.add(s);
	}

	public void createCoding(Level level) {
		for (int i = 0; i < spriteCodings.size(); i++) {
			System.out.print(level.tileData[i] + (i % 64 == 0 ? "\n" : ","));
			level.lp.addCoding((short) i, spriteCodings.get(i), i);
		}
	}

	public void loadLayout(Level level) {
		for (int i = 0; i < level.tileData.length; i++) {
			level.tileData[i] = (short) ((i / level.fieldSize <= 1 || i / level.fieldSize >= level.fieldSize - 2 || i % level.fieldSize <= 1 || i % level.fieldSize >= level.fieldSize - 2) ? 1 : 0);
		}
	}

	public void write(String string) {
		if (new File(string).exists()) new File(string).delete();
		db.saveToFile(string);
	}

}
