package ch.aiko.pokemon.fight;

import java.io.InputStream;
import java.util.Map;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.graphics.Drawer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.pokemon.graphics.menu.TextBox;
import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.mob.Trainer;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.util.AudioUtil;
import ch.aiko.util.FileUtil;
import ch.aiko.util.ImageUtil;

public class Fight extends Menu {

	private Player p;
	private Trainer t;

	private TextBox text;
	FightStartAnimation anim;
	
	private BasicPlayer player;
	
	private Sprite ground;
	private Sprite background;

	public Fight(Frame f, Player p, Trainer t) {
		p.setFighting(true);
		this.p = p;
		this.t = t;

		text = new TextBox(f, p, t.getText());
		Pokemon.frame.openMenu(text);

		player = playSound("ch/aiko/pokemon/sounds/fightOpening_1.mp3");
		// AudioUtil.playSound("ch/aiko/pokemon/sounds/fightOpening.mp3");
		System.out.println("Started Fight between: " + p + " and " + t);
	}

	public static void close() {
		
	}
	
	// TODO Remove
	// Method from AikoUtil (AudioUtil)
	// Only here to be fastly modified for testing
	// Use AudioUtil.playSound(<soundfile>)
	public static BasicPlayer playSound(InputStream in, BasicPlayerListener li, double d) {
		final BasicPlayer player = new BasicPlayer();
		try {
			player.open(in);
			player.setPan(d);
		} catch (BasicPlayerException e1) {
			e1.printStackTrace();
		}

		player.addBasicPlayerListener(li);

		try {
			player.play();
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
		return player;
	}

	// This one is also only here because testing
	@SuppressWarnings("rawtypes")
	public static BasicPlayer playSound(String path) {
		return playSound(FileUtil.LoadFileInClassPathAsStream(path), new BasicPlayerListener() {
			public void stateUpdated(BasicPlayerEvent arg0) {
				System.out.println("New State: " + arg0.getCode());
			}

			public void setController(BasicController arg0) {

			}

			public void progress(int arg0, long arg1, byte[] arg2, Map arg3) {

			}

			public void opened(Object arg0, Map arg1) {

			}
		}, 1.0);
	}

	public void paint(Drawer d) {
		if (text.isOpened()) return;

		if (anim == null) anim = new FightStartAnimation();

		d.fillRect(0, 0, d.getFrame().getWidth(), d.getFrame().getHeight(), 0xFF000000);
		draw(d.getFrame());
	}

	public void update(Drawer d) {
		update(d.getFrame());
	}

	public void update(Frame f) {}

	public void draw(Frame f) {
		if (anim == null) return;

		f.getDrawer().drawTile(new Sprite(ImageUtil.loadImageInClassPath("/ch/aiko/pokemon/textures/fight_background/grass_day.png")), 0, 0, f.getWidth(), f.getHeight());
		
		if (!anim.isFinished()) {
			anim.drawNext(f.getDrawer(), -Pokemon.frame.getWidth(), 0);
		}
		else {
			f.getDrawer().drawTile(new Sprite(ImageUtil.loadImageInClassPath("/ch/aiko/pokemon/textures/fight_ground/grass_day.png")), 0, 0, f.getWidth(), f.getHeight());
		}
	}

	public void onOpen(Drawer d) {
		p.setPaused(true);
				
		try {
			player.stop();
			playSound("ch/aiko/pokemon/sounds/TrainerFight.mp3");
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}

	public void onClose(Drawer d) {
		p.setPaused(false);
	}

	public Trainer getTrainer() {
		return t;
	}

	public String name() {
		return "FightScreen";
	}

	public boolean canClose() {
		return false;
	}

	public boolean opened() {
		return text.isOpened();
	}

}
