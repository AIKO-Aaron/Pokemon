package ch.aiko.pokemon.fight;

import static ch.aiko.pokemon.sound.SoundPlayer.loopSound;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.graphics.Drawer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.graphics.MoveAnimation;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.pokemon.graphics.menu.TextBox;
import ch.aiko.pokemon.mob.Trainer;
import ch.aiko.pokemon.mob.player.Player;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.pokemon.sprite.SpriteSheet;
import ch.aiko.util.ImageUtil;

public class Fight extends Menu {

	private Player p;
	private Trainer t;

	private TextBox text;
	MoveAnimation ground_anim, player_anim, enemy_anim;

	private javazoom.jl.player.Player player;

	private int player_height = 320;

	private Sprite ground;
	private Sprite background;

	private Location loc = Location.GRASS;
	private Time time = Time.DAY;

	public Fight(Frame f, Player p, Trainer t, Location loc, Time time) {
		p.setFighting(true);
		this.p = p;
		this.t = t;
		this.loc = loc;
		this.time = time;

		text = new TextBox(f, p, t.getText());
		Pokemon.frame.openMenu(text);

		ground = new Sprite(ImageUtil.loadImageInClassPath(loc.getGroundPath(time)));
		background = new Sprite(ImageUtil.loadImageInClassPath(loc.getBackGroundPath(time)));

		player = loopSound("ch/aiko/pokemon/sounds/FightOpening_1.mp3", Settings.GAIN);

		System.out.println("Started Fight between: " + p + " and " + t);
	}

	public static void close() {

	}

	public void paint(Drawer d) {
		if (text.isOpened()) return;

		if (ground_anim == null) {
			ground_anim = new FightStartAnimation(loc.getGroundPath(time), 256, 128);
		}

		if (player_anim == null) {
			player_anim = new MoveAnimation(new SpriteSheet("/ch/aiko/pokemon/textures/player_fight_" + p.getGender().name().toLowerCase() + ".png", 80, 80, player_height, player_height).removeColor(0xFF88B8B0), 30, 0, Frame.HEIGHT - player_height, true, 0x00000000);
			player_anim.setSpeed(25 * (Pokemon.frame.getWidth() / Frame.WIDTH));
			player_anim.setStartingTime(true);
		}

		d.fillRect(0, 0, Frame.WIDTH, Frame.HEIGHT, 0xFF000000);
		draw(d.getFrame());
	}

	/**
	 * 
	 * public FightStartAnimation(String s) { super(new SpriteSheet(s, 256, 128, Frame.WIDTH, Frame.HEIGHT), 60, 0, 0, false, 0x00000000); setSpeed(25 * (Pokemon.frame.getWidth() / Frame.WIDTH)); }
	 * 
	 */

	public void update(Drawer d) {
		update(d.getFrame());
	}

	public void update(Frame f) {}

	public void draw(Frame f) {
		if (ground_anim == null) return;

		f.getDrawer().drawTile(background, 0, 0, Frame.WIDTH, Frame.HEIGHT);

		if (!ground_anim.isFinished()) {
			ground_anim.drawNext(f.getDrawer(), -Frame.WIDTH, 0);
		} else {
			f.getDrawer().drawTile(ground, 0, 0, Frame.WIDTH, Frame.HEIGHT);
		}

		if (!player_anim.isFinished()) {
			player_anim.drawNext(f.getDrawer(), Frame.WIDTH, Frame.HEIGHT - player_height);
		} else {
			System.out.println("done");
			// f.getDrawer().drawTile(ground, 0, 0, Frame.WIDTH, Frame.HEIGHT);
		}
	}

	public void onOpen(Drawer d) {
		p.setPaused(true);

		if (player != null) player.close();
		loopSound("ch/aiko/pokemon/sounds/TrainerFight.mp3", Settings.GAIN);
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
