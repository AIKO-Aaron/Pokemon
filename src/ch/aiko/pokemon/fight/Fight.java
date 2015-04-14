package ch.aiko.pokemon.fight;

import static ch.aiko.pokemon.sound.SoundPlayer.loopSound;
import static ch.aiko.pokemon.sound.SoundPlayer.stopLoop;

import java.awt.event.KeyEvent;

import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
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

	private Sprite backP, frontE, hpP, hpE;
	private int pokP = 0, pokT = 0;
	private int hpPosP, hpPosE, speed = 5;

	public Fight(Frame f, Player p, Trainer t, Location loc, Time time) {
		p.setFighting(true);
		this.p = p;
		this.t = t;
		this.loc = loc;
		this.time = time;

		text = new TextBox(f, p, t.getText());
		Pokemon.frame.openMenu(text);

		// Init sprites used
		ground = new Sprite(ImageUtil.loadImageInClassPath(loc.getGroundPath(time)));
		background = new Sprite(ImageUtil.loadImageInClassPath(loc.getBackGroundPath(time)));
		backP = p.team[pokP].getBackSprite();
		hpP = p.team[pokP].getPlayerHpBar();
		hpPosP = 192 * p.team[pokP].getHp() / p.team[pokP].getMaxHp();

		frontE = t.getPokemon(pokT).getSprite();
		hpE = t.getPokemon(pokT).getEnemyHpBar();
		hpPosE = 192 * t.getPokemon(pokT).getHp() / t.getPokemon(pokT).getMaxHp();

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
			player_anim = new MoveAnimation(new SpriteSheet("/ch/aiko/pokemon/textures/player/player_fight_" + p.getGender().name().toLowerCase() + ".png", 80, 80, player_height, player_height).removeColor(0xFF88B8B0), 30, 0, Frame.HEIGHT - player_height, false, 0x00000000);
			player_anim.setSpeed(50 * (Pokemon.frame.getWidth() / Frame.WIDTH));
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

		if (d.getFrame().getTimesPressed(KeyEvent.VK_U) > 0) t.getPokemon(pokT).setHp(t.getPokemon(pokT).getHp() - 1);
		if (d.getFrame().getTimesPressed(KeyEvent.VK_K) > 0) t.getPokemon(pokT).setHp(t.getPokemon(pokT).getHp() + 1);
	}

	public void update(Frame f) {}

	public void draw(Frame f) {
		if (ground_anim == null) return;

		updateEnemyHp();
		updatePlayerHp();

		f.getDrawer().drawTile(background, 0, 0, Frame.WIDTH, Frame.HEIGHT);

		if (!ground_anim.isFinished()) {
			ground_anim.drawNext(f.getDrawer(), -Frame.WIDTH, 0);
		} else {
			f.getDrawer().drawTile(ground, 0, 0, Frame.WIDTH, Frame.HEIGHT);
		}

		if (!player_anim.isFinished()) {
			player_anim.drawNext(f.getDrawer(), Frame.WIDTH, Frame.HEIGHT - player_height);
		} else {
			f.getDrawer().drawTile(backP, 0, Frame.HEIGHT - player_height, player_height, player_height);
			f.getDrawer().drawTile(hpP, Frame.WIDTH - 528, Frame.HEIGHT - 200);

			f.getDrawer().drawTile(frontE, Frame.WIDTH - player_height - 110, 50, player_height, player_height);
			f.getDrawer().drawTile(hpE, -20, 0);
		}
	}

	private void updateEnemyHp() {
		int pos = (int) (192F / (float) t.getPokemon(pokT).getMaxHp() * (float) t.getPokemon(pokT).getHp());
		if (hpPosE != pos) {
			hpPosE += hpPosE < pos ? Math.min(speed, Math.abs(pos - hpPosE)) : Math.max(-speed, -Math.abs(pos - hpPosE));
		}

		int color = (int) (Math.abs((float) hpPosE / 192F - 1F) * 255F) << 16 | (int) ((float) hpPosE / 192F * 255F) << 8 | 0xFF000000;
		Sprite bar = new Sprite(color, 192, 8);
		bar.top(new Sprite(0xFF000000, 192 - hpPosE, 8), hpPosE, 0);
		hpE.top(bar, 220, 120);
	}

	private void updatePlayerHp() {
		int pos = (int) (192F / (float) p.getPokemon(pokP).getMaxHp() * (float) p.getPokemon(pokP).getHp());
		if (hpPosP != pos) {
			hpPosP += hpPosP < pos ? Math.min(speed, Math.abs(pos - hpPosP)) : Math.max(-speed, -Math.abs(pos - hpPosP));
		}

		int color = (int) (Math.abs((float) hpPosP / 192F - 1F) * 255F) << 16 | (int) ((float) hpPosP / 192F * 255F) << 8 | 0xFF000000;
		Sprite bar = new Sprite(color, 192, 8);
		bar.top(new Sprite(0xFF000000, 192 - hpPosP, 8), hpPosP, 0);
		hpP.top(bar, 304, 96);
	}

	public void onOpen(Drawer d) {
		p.setPaused(true);

		if (player != null) player.close();

		stopLoop();
		loopSound("ch/aiko/pokemon/sounds/TrainerFight.mp3", Settings.GAIN, 0, 535, new PlaybackListener() {
			public void playbackFinished(PlaybackEvent evt) {
				stopLoop();
				loopSound("ch/aiko/pokemon/sounds/TrainerFight.mp3", Settings.GAIN, 517, 3675);
			}
		});
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
