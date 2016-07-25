package ch.aiko.pokemon.graphics;

import java.util.Timer;
import java.util.TimerTask;

import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.graphics.Window;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.entity.player.Player;
import ch.aiko.pokemon.level.Level;

public class GraphicsHandler {

	public Window window;
	public Screen screen;
	public Level level;
	public Player p;

	public GraphicsHandler(Level level, Player p) {
		screen = new Screen(960, 540);
		this.p = p;
		// Screen setup
		screen.setResetOffset(false);
		screen.ps = Pokemon.out;
		this.level = level;
		screen.addLayer(level);

		// Open Window with our screen
		window = new Window("Pokemon", screen);
	}

	public void setLevel(Level l) {
		p.setPaused(true);
		screen.removeLayer(screen.getTopLayer("Level"));
		l.reload();
		this.level = l;
		screen.addLayer(l);
		new Timer().schedule(new TimerTask() {
			public void run() {
				p.setPaused(false);
			}
		}, 100);
	}

	public void start() {
		window.start();
	}

}
