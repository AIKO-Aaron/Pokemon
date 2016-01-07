package ch.aiko.pokemon.mob.player;

import java.awt.Point;
import java.awt.event.KeyEvent;

import ch.aiko.engine.KeyBoard;
import ch.aiko.engine.Menu;
import ch.aiko.engine.Renderer;
import ch.aiko.engine.Window;
import ch.aiko.pokemon.fight.Fight;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.graphics.menu.PlayerMenu;
import ch.aiko.pokemon.level.Level;
import ch.aiko.pokemon.mob.Mob;
import ch.aiko.pokemon.pokemon.TeamPokemon;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.pokemon.sprite.SpriteSheet;

public class Player extends Mob {

	public TeamPokemon[] team = new TeamPokemon[6];

	public int speed = 7, anim, direction;
	private boolean isPaused, fighting;
	private boolean walking = false;
	private boolean opened = false;

	private Point lastPlace;

	private Gender gender;
	// 0-3 Down
	// 4-7 Up
	// 8-11 Left
	// 12-15 Right
	private SpriteSheet sheet;

	// Auto-path finding creates error if false
	private static final boolean collide = true;
	public static Fight fight;

	public Player(int x, int y, Gender g) {
		super(null, x, y);
		setGender(g);
	}

	public Player(int x, int y, int w, int h, Gender g) {
		super(null, x, y, w, h);
		setGender(g);
	}

	public void setGender(Gender g) {
		this.gender = g;

		switch (g) {
			case BOY:
				sheet = new SpriteSheet("/ch/aiko/pokemon/textures/player/player_boy.png", 32, 32);
				break;
			case GIRL:
				sheet = new SpriteSheet("/ch/aiko/pokemon/textures/player/player_girl.png", 32, 32);
				break;
		}
	}

	public void update(Frame f) {
		if (isPaused) return;
		if (isOnTile(f)) ;

		this.lastPlace = Frame.getLevel().getCamera();

		float xmovement = 0;
		float ymovement = 0;

		if (f.isKeyPressed(Settings.getInstance().getIntegerValue("keyUp"))) ymovement--;
		if (f.isKeyPressed(Settings.getInstance().getIntegerValue("keyDown"))) ymovement++;
		if (f.isKeyPressed(Settings.getInstance().getIntegerValue("keyLeft"))) xmovement--;
		if (f.isKeyPressed(Settings.getInstance().getIntegerValue("keyRight"))) xmovement++;

		if (xmovement < 0) direction = 2;
		if (xmovement > 0) direction = 3;
		if (ymovement > 0) direction = 0;
		if (ymovement < 0) direction = 1;
		
		int xmodifier = xmovement < 0 ? -1 : 1;
		int ymodifier = ymovement < 0 ? -1 : 1;

		if (KeyBoard.getTimesPressed(KeyEvent.VK_U) > 0) System.out.println(x + ":" + y);

		walking = ymovement != 0 || xmovement != 0;

		if (checkCollisionX(f, xmovement, speed) && collide) xmovement = getMaxSpeedX(f, xmovement, speed) * xmodifier;
		else xmovement *= speed;
		if (checkCollisionY(f, ymovement, speed) && collide) ymovement = getMaxSpeedY(f, ymovement, speed) * ymodifier;
		else ymovement *= speed;
		
		x += xmovement;
		y += ymovement;

		Frame.getLevel().setCamera(x - Frame.WIDTH / 2, y - Frame.HEIGHT / 2);

		if (KeyBoard.getTimesPressed(KeyEvent.VK_X) > 0) {
			Frame.openMenu(new PlayerMenu(this));
		}

		if (!opened && Settings.isFirstLaunch) {
			Frame.openMenu(new Menu() {
				public void draw(double d) {
					Renderer.drawTextOffset("Press X to open Menu", x - 100, y - 50, 25, 0xFFFF00FF);
				}

				public String name() {
					return "FirstLaunch";
				}

				public void onOpen() {

				}

				public void onClose() {

				}

				public void update(double delta) {
					if (KeyBoard.getTimesPressed(KeyEvent.VK_X) > 0) Window.closeMenu();
				}
			});
			opened = true;
		}

		if (isWalking() && !fighting) {
			anim++;
			anim %= 30;
		} else {
			anim = 10;
		}
	}

	public boolean isWalking() {
		return walking;
	}

	public void paint() {
		int offset = 0;
		if (isWalking()) {
			offset++;
			if (anim >= 10) offset++;
			if (anim >= 20) offset++;
		}

		Renderer.drawSprite(sheet.getSprite(direction * 4 + offset).removeColor(0xFF88B8B0), x, y);
		Renderer.drawRectOffset(x, y, w, h, 0xFFFF00FF);
	}

	public void teleport(Frame f, Level l, int x, int y) {
		f.setLevel(l);
		this.x = x;
		this.y = y;
	}

	public void setPokemon(int pos, TeamPokemon teamPokemon) {
		if (pos < 0 || pos >= 6) return;
		team[pos] = teamPokemon;
	}

	public boolean addPokemon(TeamPokemon pok) {
		int i = getTeamSize();
		if (i >= 5) return false;

		team[i + 1] = pok;

		return true;
	}

	public TeamPokemon getPokemon(int index) {
		if (index < 0 || index >= 6) return team[0];
		return team[index];
	}

	public int getTeamSize() {
		for (int i = 0; i < team.length; i++) {
			if (team[i] == null) return i;
		}
		return team.length;
	}

	public void setPaused(boolean b) {
		this.isPaused = b;
	}

	public int getX() {
		if (lastPlace == null) return 0;
		return x - lastPlace.x;
	}

	public int getY() {
		if (lastPlace == null) return 0;
		return y - lastPlace.y;
	}

	public boolean isInFight() {
		return fighting;
	}

	public void setFighting(boolean value) {
		fighting = value;
	}
	
	public void setFight(Fight fight) {
		Player.fight = fight;
	}

	public Gender getGender() {
		return gender;
	}
}
