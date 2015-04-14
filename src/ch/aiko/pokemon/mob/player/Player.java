package ch.aiko.pokemon.mob.player;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

import ch.aiko.pokemon.graphics.Drawer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.pokemon.graphics.menu.PlayerMenu;
import ch.aiko.pokemon.level.Level;
import ch.aiko.pokemon.mob.Mob;
import ch.aiko.pokemon.pokemon.TeamPokemon;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.pokemon.sprite.SpriteSheet;

public class Player extends Mob {

	public TeamPokemon[] team = new TeamPokemon[6];

	public int speed = 4, anim, direction;
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

		this.lastPlace = f.getLevel().getCamera();

		int xmovement = 0;
		int ymovement = 0;

		if (f.isKeyPressed(Settings.getInstance().getIntegerValue("keyUp"))) ymovement--;
		if (f.isKeyPressed(Settings.getInstance().getIntegerValue("keyDown"))) ymovement++;
		if (f.isKeyPressed(Settings.getInstance().getIntegerValue("keyLeft"))) xmovement--;
		if (f.isKeyPressed(Settings.getInstance().getIntegerValue("keyRight"))) xmovement++;

		if (xmovement < 0) direction = 2;
		if (xmovement > 0) direction = 3;
		if (ymovement > 0) direction = 0;
		if (ymovement < 0) direction = 1;

		if (f.getTimesPressed(KeyEvent.VK_U) > 0) System.out.println(x + ":" + y);

		walking = ymovement == 0 && xmovement == 0;

		if (checkCollisionX(f, xmovement, speed) || checkCollisionY(f, ymovement, speed)) {
			// for (Side s : getSides(f, xmovement, ymovement, speed)) {
			// Point p = getSideCoords(s);
			// new Particle(p.x, p.y, 30, 10, f.getLevel());
			// }

		}

		int xs = speed;
		int ys = speed;

		if (checkCollisionX(f, xmovement, xs) && collide) xs = getMaxSpeedX(f, xmovement, speed);
		if (checkCollisionY(f, ymovement, ys) && collide) ys = getMaxSpeedY(f, ymovement, speed);

		x += xmovement * xs;
		y += ymovement * ys;

		f.getLevel().setCamera(x - Frame.WIDTH / 2, y - Frame.HEIGHT / 2);

		if (f.getTimesPressed(KeyEvent.VK_X) > 0) {
			f.openMenu(new PlayerMenu(this));
		}

		if (!opened && Settings.isFirstLaunch) {
			f.openMenu(new Menu() {
				public void paint(Drawer d) {
					d.drawText("Press X to open Menu", getXOnScreen(d) - 100, getYOnScreen(d) - 50, 25, 0xFFFF00FF);
				}

				public void update(Drawer d) {
					if (d.getFrame().getTimesPressed(KeyEvent.VK_X) > 0) d.getFrame().closeMenu();
				}

				public void onClose(Drawer d) {
					System.out.println("Trying to close menu");
				}

				public String name() {
					return "FirstLaunch";
				}
			});
			opened = true;
		}

		if (!isWalking() && !fighting) {
			anim++;
			anim %= 30;
		} else {
			anim = 0;
		}
	}

	public boolean isWalking() {
		return walking;
	}

	public void paint(Graphics g, Frame f) {
		int offset = 0;
		if (!isWalking()) {
			offset++;
			if (anim >= 10) offset++;
			if (anim >= 20) offset++;
		}

		f.getDrawer().drawTile(sheet.getSprite(direction * 4 + offset).removeColor(0xFF88B8B0), x - f.getLevel().getCamera().x, y - f.getLevel().getCamera().y);
	}

	public void teleport(Frame f, Level l, int x, int y) {
		f.setLevel(l);
		this.x = x;
		this.y = y;
	}

	public void paintOverPlayer(Graphics g, Frame f) {

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

	public void paint(Drawer d) {

	}

	public int getXOnScreen(Drawer d) {
		return x - d.getFrame().getLevel().getCamera().x;
	}

	public int getYOnScreen(Drawer d) {
		return y - d.getFrame().getLevel().getCamera().y;
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

	public Gender getGender() {
		return gender;
	}
}
