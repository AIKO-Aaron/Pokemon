package ch.aiko.pokemon.level;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import ch.aiko.engine.graphics.LayerContainer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.SpriteSheet;
import ch.aiko.engine.sprite.Tile;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.util.ImageUtil;

public class Level extends LayerContainer {

	private SpriteSheet sheet = new SpriteSheet("/ch/aiko/pokemon/textures/Sprites.png", 16, 16);
	
	public Level() {
		resetOffset = false;
	}
	
	public void loadLevel(String pathToLevel, HashMap<Integer, Integer> palette) {
		if(pathToLevel.endsWith(".png")) {
			BufferedImage img = ImageUtil.loadImageInClassPath(pathToLevel);
			for(int xpos = 0; xpos < img.getWidth(); xpos++) {
				for(int ypos = 0; ypos < img.getHeight(); ypos++) {
					System.out.println(palette);
					System.out.println("Searching for: " + img.getRGB(xpos, ypos));
					Integer index = palette.get(0xFF000000 | img.getRGB(xpos, ypos));
					if(index == null) index = 0;
					addRenderable(new Tile(sheet.getSprite(index), xpos * sheet.getSpriteWidth(), ypos * sheet.getSpriteHeight(), false));
				}
			}
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
	
	public void layerRender(Renderer r) {
	}
	
	public void layerUpdate(Screen s) {
		if(s.getInput().popKeyPressed(KeyEvent.VK_ESCAPE)) Pokemon.pokemon.handler.window.quit();
	}

}
