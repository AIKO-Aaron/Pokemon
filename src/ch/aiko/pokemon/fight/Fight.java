package ch.aiko.pokemon.fight;

import static ch.aiko.pokemon.sound.SoundPlayer.loopSound;
import static ch.aiko.pokemon.sound.SoundPlayer.stopLoop;

import java.awt.event.KeyEvent;

import ch.aiko.engine.KeyBoard;
import ch.aiko.engine.Menu;
import ch.aiko.engine.Renderer;
import ch.aiko.engine.Window;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.graphics.MoveAnimation;
import ch.aiko.pokemon.graphics.menu.TextBox;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.mob.Trainer;
import ch.aiko.pokemon.mob.player.Player;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.pokemon.sprite.SpriteSheet;
import ch.aiko.util.ImageUtil;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class Fight extends Menu {

	private Player p;
	private Trainer t;

	public TextBox text;
	MoveAnimation ground_anim, player_anim, enemy_anim;

	private javazoom.jl.player.Player player;

	private int player_height = 320;

	private Sprite ground;
	//private Sprite background;

	private Location loc = Location.GRASS;
	private Time time = Time.DAY;

	private Sprite backP, frontE, hpP, hpE;
	private int pokP = 0, pokT = 0;
	private int hpPosP, hpPosE, speed = 5;

	public Fight(Frame f, Player p, Trainer t, Location loc, Time time) {
		p.setFight(this);
		p.setFighting(true);
		this.p = p;
		this.t = t;
		this.loc = loc;
		this.time = time;

		text = new TextBox(p, t.getText());
		Frame.openMenu(text);

		// Init sprites used
		ground = new Sprite(ImageUtil.loadImageInClassPath(loc.getGroundPath(time)));
		//background = new Sprite(ImageUtil.loadImageInClassPath(loc.getBackGroundPath(time)));
		backP = p.team[pokP].getBackSprite();
		hpP = p.team[pokP].getPlayerHpBar();
		hpPosP = 192 * p.team[pokP].getHp() / p.team[pokP].getMaxHp();

		frontE = t.getPokemon(pokT).getSprite();
		hpE = t.getPokemon(pokT).getEnemyHpBar();
		hpPosE = 192 * t.getPokemon(pokT).getHp() / t.getPokemon(pokT).getMaxHp();

		player = loopSound("ch/aiko/pokemon/sounds/FightOpening_1.mp3", Settings.GAIN);

		System.out.println("Started Fight between: " + p + " and " + t);
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

		int startX = -20;
		int startY = 0;

		Renderer.drawImage(startX, startY, hpE.getImage());
		Renderer.drawText(t.getPokemon(pokT).getName(), startX + 30, startY + 30, 40, 0xFFFFFFFF);
		Renderer.drawText("Level: " + t.getPokemon(pokT).getLvl(), startX + 250, startY + 30, 40, 0xFFFFFFFF);
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

		int startX = 432;
		int startY = 340;

		Renderer.drawImage(startX, startY, hpP.getImage(), 5);
		Renderer.drawText(p.team[pokP].getName(Language.current), startX + 128, startY, 50, 0xFFFFFFFF);
		Renderer.drawText(Language.translate("HP") + " " + p.team[pokP].getHp() + "/" + p.team[pokP].getMaxHp(), startX + 180, startY + 69, 30, 0xFFFFFFFF);
		Renderer.drawText("Level: " + p.team[pokP].getLvl(), startX + 330, startY + 12, 42, 0xFFFFFFFF);
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

	public void onOpen() {
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

	public void onClose() {
		p.setPaused(false);

	}

	public void update(double delta) {
		if (KeyBoard.getTimesPressed(KeyEvent.VK_U) > 0) t.getPokemon(pokT).setHp(t.getPokemon(pokT).getHp() - 1);
		if (KeyBoard.getTimesPressed(KeyEvent.VK_K) > 0) t.getPokemon(pokT).setHp(t.getPokemon(pokT).getHp() + 1);
	}

	public void draw(double delta) {
		// if(toString()!=null)return;
		if (Window.Background == null) Window.Background = ImageUtil.loadImageInClassPath(loc.getBackGroundPath(time));

		Frame.getLevel().getCamera().x = 0;
		Frame.getLevel().getCamera().y = 0;

		if (text.isOpened()) return;

		if (ground_anim == null) {
			ground_anim = new FightStartAnimation(loc.getGroundPath(time), 256, 128);
		}

		if (player_anim == null) {
			player_anim = new MoveAnimation(new SpriteSheet("/ch/aiko/pokemon/textures/player/player_fight_" + p.getGender().name().toLowerCase() + ".png", 80, 80, player_height, player_height).removeColor(0xFF88B8B0), 30, 0, Frame.HEIGHT - player_height, false, 0x00000000);
			player_anim.setSpeed(10 * (Pokemon.frame.getWidth() / Frame.WIDTH));
			player_anim.setStartingTime(true);
		}

		if (ground_anim == null) return;

		// Renderer.drawImage(0, 0, background.getImage(Frame.WIDTH, Frame.HEIGHT));
		if (!ground_anim.isFinished()) {
			ground_anim.drawNext(-Frame.WIDTH, 0, delta);
		} else {
			Renderer.drawImage(0, 0, ground.getImage(Frame.WIDTH, Frame.HEIGHT), -1);
		}
		if (!player_anim.isFinished()) {
			player_anim.drawNext(Frame.WIDTH, Frame.HEIGHT - player_height, delta);
		} else {
			Renderer.drawImage(0, Frame.HEIGHT - player_height, backP.getImage(player_height, player_height), 5);

			Renderer.drawImage(Frame.WIDTH - player_height - 110, 50, frontE.getImage(player_height, player_height));
		}
		updateEnemyHp();
		updatePlayerHp();
	}

	public boolean hasFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
