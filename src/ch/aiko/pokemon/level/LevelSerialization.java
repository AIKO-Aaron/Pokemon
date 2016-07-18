package ch.aiko.pokemon.level;

import java.util.ArrayList;

import ch.aiko.as.ASArray;
import ch.aiko.as.ASDataType;
import ch.aiko.as.ASField;
import ch.aiko.as.ASObject;
import ch.aiko.as.SerializationReader;

public class LevelSerialization extends ASDataType {

	private Level level;
	
	public LevelSerialization(Level l) {
		level = l;
	}
	
	public void load(ASObject c) {
		ASField layers = c.getField("Layers");
		if (layers != null) level.layerCount = SerializationReader.readInt(layers.data, 0);
		for (int i = 0; i < level.layerCount; i++) {
			ASArray array = c.getArray("Tiles" + i);
			if (array != null) level.tileData.add(array.getShortData());
		}
		ASObject palette = c.getObject("Palette");
		if (palette != null) level.lp = new LevelPalette(palette, this);
		ASField size = c.getField("Size");
		if (size != null) level.fieldSize = SerializationReader.readInt(size.data, 0);

		level.decode();
	}

	public void getData(ASObject thisObject) {
		if (level.tileData == null) level.tileData = new ArrayList<short[]>();
		if (level.lp == null) level.lp = new LevelPalette("Palette", this);
		thisObject.addField(ASField.Integer("Layers", level.tileData.size()));
		for (int i = 0; i < level.tileData.size(); i++) {
			ASArray tiles = ASArray.Short("Tiles" + i, level.tileData.get(i));
			thisObject.addArray(tiles);
		}
		thisObject.addObject(level.lp.toObject());
		thisObject.addField(ASField.Integer("Size", level.fieldSize));
	}
	
}
