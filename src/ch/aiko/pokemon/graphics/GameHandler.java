package ch.aiko.pokemon.graphics;

import java.util.Timer;
import java.util.TimerTask;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.graphics.Window;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.entity.player.OtherPlayer;
import ch.aiko.pokemon.entity.player.Player;
import ch.aiko.pokemon.level.Level;

public class GameHandler {

	public Window window;
	public Screen screen;
	public Level level;
	public Player p;

	public GameHandler(Level level, Player p) {
		screen = new Screen(960, 540) {
			private static final long serialVersionUID = 9052690094292517622L;

			public void stopThreads() {
				super.stopThreads();
				quit();
			}
		};
		this.level = level;
		this.p = p;
		// Screen setup
		screen.setResetOffset(false);
		screen.ps = Pokemon.out;
		setLevel(level);
		level.addPlayer(p);
		// Open Window with our screen
		window = new Window("Pokemon", screen);
	}

	private void quit() {
		if (Pokemon.ONLINE) {
			saveData();
			Pokemon.client.sendText("/q/");
		}
	}

	public void saveData() {
		if (Pokemon.ONLINE) Pokemon.client.sendText("/spos/" + Pokemon.player.getRealX() + "/" + Pokemon.player.getRealY() + "/" + Pokemon.player.getDirection());
	}

	public void setLevel(Level l) {
		p.setPaused(true);
		Layer current = null;
		if ((current = screen.getTopLayer("Level")) != null) screen.removeLayer(current);
		l.reload();
		if (Pokemon.ONLINE) if (Pokemon.client != null && Pokemon.client.players != null) for (OtherPlayer p : Pokemon.client.players) {
			p.add(l);
		}
		this.level = l;
		screen.addLayer(l);
		new Timer().schedule(new TimerTask() {
			public void run() {
				p.setPaused(false);
			}
		}, 100);
		if (Pokemon.ONLINE) Pokemon.client.sendText("/slvl/" + l.path);
	}

	public void start() {
		window.start();
	}

}
