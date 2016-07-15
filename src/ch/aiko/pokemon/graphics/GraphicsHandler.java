package ch.aiko.pokemon.graphics;

import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.graphics.Window;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.level.Level;

public class GraphicsHandler {

	public Window window;
	public Screen screen;
	public Level level;

	public GraphicsHandler(Level level) {
		screen = new Screen(960, 540);

		// Screen setup
		screen.ps = Pokemon.out;
		this.level = level;
		screen.addLayer(level);

		// Open Window with our screen
		window = new Window("Pokemon", screen);
	}

}
