package ch.aiko.pokemon.mob;

import static ch.aiko.pokemon.mob.Direction.DOWN;
import static ch.aiko.pokemon.mob.Direction.LEFT;
import static ch.aiko.pokemon.mob.Direction.RIGHT;
import static ch.aiko.pokemon.mob.Direction.UP;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import ch.aiko.pokemon.fight.Fight;
import ch.aiko.pokemon.graphics.Drawer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.pokemon.TeamPokemon;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.pokemon.sprite.SpriteSheet;

public class Trainer extends Mob {

	private static SpriteSheet sheet = new SpriteSheet("/ch/aiko/pokemon/textures/TrainerSprites.png", 32 * 3, 32 * 4);
	private static final int speed = 1;
	private static final int anim = 60;

	private Sprite upStill, rightStill, upMoving1, leftMoving1, rightMoving1, downStill, leftStill, rightMoving2, downMoving1, leftMoving2, upMoving2, downMoving2;
	private Sprite current = upStill;

	private int walkTime = 0;
	private boolean walking;
	private boolean oneDir = false; // experimental

	private Direction dir = Direction.LEFT;
	private boolean wantToFight, doesMove;
	private int seeWidth;
	private ArrayList<TeamPokemon> team = new ArrayList<TeamPokemon>();
	
	private Fight fight;
	private boolean fighting;

	public Trainer(int x, int y, int trainerType) {
		this(x, y, trainerType, false, false, 0, new TeamPokemon[] {});
	}

	public Trainer(int x, int y, int trainerType, boolean stationnairy) {
		this(x, y, trainerType, stationnairy, false, 0, new TeamPokemon[] {});
	}

	public Trainer(int x, int y, int type, boolean statio, boolean wantToFight, TeamPokemon[] team) {
		this(x, y, type, statio, wantToFight, 10, team);
	}

	public Trainer(int x, int y, int trainerType, boolean stationnairy, boolean fight, int seight, TeamPokemon... pokemons) {
		super(new Sprite(0, 1, 1), x, y);
		w = 32;
		h = 32;

		this.team = convert(pokemons);
		this.seeWidth = seight;
		this.doesMove = !stationnairy;
		this.wantToFight = fight;

		SpriteSheet local = new SpriteSheet(sheet.getSprite(trainerType), 32, 32);

		upStill = local.getSprite(0).removeColor(0xFFFFFFFF);
		rightStill = local.getSprite(1).removeColor(0xFFFFFFFF);
		leftStill = local.getSprite(6).removeColor(0xFFFFFFFF);
		downStill = local.getSprite(5).removeColor(0xFFFFFFFF);

		upMoving1 = local.getSprite(2).removeColor(0xFFFFFFFF);
		rightMoving1 = local.getSprite(4).removeColor(0xFFFFFFFF);
		leftMoving1 = local.getSprite(3).removeColor(0xFFFFFFFF);
		downMoving1 = local.getSprite(8).removeColor(0xFFFFFFFF);

		upMoving2 = local.getSprite(10).removeColor(0xFFFFFFFF);
		rightMoving2 = local.getSprite(7).removeColor(0xFFFFFFFF);
		leftMoving2 = local.getSprite(9).removeColor(0xFFFFFFFF);
		downMoving2 = local.getSprite(11).removeColor(0xFFFFFFFF);

		userInit();
	}

	public void userUpdate(Drawer d) {

	}

	public final void update(Frame f) {
		userUpdate(f.getDrawer());
		
		
		
		/**if(fighting && fight != null) {
			fight.update(f);
		}*/
		
		Player p1 = f.getLevel().getPlayer();

		for (int i = 0; i < seeWidth && wantToFight; i++) {
			int xPos = (dir == LEFT || dir == RIGHT) ? speed * i + x : x;
			int yPos = (dir == UP || dir == DOWN) ? speed * i + y : y;
			
			if(p1.x + p1.w >= xPos && p1.x <= xPos && p1.y + p1.h >= yPos && p1.y <= yPos && !p1.isInFight()) {
				System.out.println("Fight");
				fighting = true;
				fight = new Fight(p1, this);
			}
		}

		if (!doesMove) {
			switch (dir) {
				case LEFT:
					current = leftStill;
				case RIGHT:
					current = rightStill;
				case DOWN:
					current = downStill;
				case UP:
					current = upStill;
			}
		}

		if (!doesMove) return;

		Point p = pathFind(f, f.getLevel().getPlayer().x, f.getLevel().getPlayer().y, speed);
		if (oneDir) {
			if (p.x != 0 && !checkCollisionX(f, p.x, speed)) {
				int xmov = p.x;

				if (xmov != 0) walking = true;
				else walking = false;

				float xs = speed;

				if (checkCollisionX(f, xmov, xs)) xs = getMaxSpeedX(f, xmov, speed);

				x += xmov * xs;

				if (walking) walkTime = (walkTime + 1) % anim;
				if (!walking) walkTime = 0;

				boolean left = xmov < 0;
				boolean right = xmov > 0;

				current = upStill;

				if (left && !walking) current = leftStill;
				if (right && !walking) current = rightStill;

				if (left && walking && walkTime / (anim / 2) == 0) current = leftMoving1;
				if (right && walking && walkTime / (anim / 2) == 0) current = rightMoving1;

				if (left && walking && walkTime / (anim / 2) == 1) current = leftMoving2;
				if (right && walking && walkTime / (anim / 2) == 1) current = rightMoving2;
			} else if (p.y != 0 && !checkCollisionY(f, p.y, speed)) {
				System.out.println("y");
				int ymov = p.y;

				if (ymov != 0) walking = true;
				else walking = false;

				float ys = speed;

				if (checkCollisionY(f, ymov, ys)) ys = getMaxSpeedY(f, ymov, speed);

				y += ymov * ys;

				if (walking) walkTime = (walkTime + 1) % anim;
				if (!walking) walkTime = 0;

				boolean up = ymov < 0;
				boolean down = ymov > 0;

				current = upStill;

				if (up && !walking) current = upStill;
				if (down && !walking) current = downStill;

				if (up && walking && walkTime / (anim / 2) == 1) current = upMoving1;
				if (down && walking && walkTime / (anim / 2) == 0) current = downMoving1;

				if (up && walking && walkTime / (anim / 2) == 1) current = upMoving2;
				if (down && walking && walkTime / (anim / 2) == 1) current = downMoving2;
			} else current = sprite;
		} else {
			int xmov = p.x;
			int ymov = p.y;

			if (xmov != 0 || ymov != 0) walking = true;
			else walking = false;

			float xs = speed;
			float ys = speed;

			if (checkCollisionX(f, xmov, xs)) xs = getMaxSpeedX(f, xmov, speed);
			if (checkCollisionY(f, ymov, ys)) ys = getMaxSpeedY(f, ymov, speed);

			x += xmov * xs;
			y += ymov * ys;

			if (walking) walkTime = (walkTime + 1) % anim;
			if (!walking) walkTime = 0;

			boolean up = ymov < 0;
			boolean down = ymov > 0;
			boolean left = xmov < 0;
			boolean right = xmov > 0;

			current = upStill;

			if (up && !walking) current = upStill;
			if (down && !walking) current = downStill;
			if (left && !walking) current = leftStill;
			if (right && !walking) current = rightStill;

			if (up && walking && walkTime / (anim / 2) == 1) current = upMoving1;
			if (down && walking && walkTime / (anim / 2) == 0) current = downMoving1;
			if (left && walking && walkTime / (anim / 2) == 0) current = leftMoving1;
			if (right && walking && walkTime / (anim / 2) == 0) current = rightMoving1;

			if (up && walking && walkTime / (anim / 2) == 1) current = upMoving2;
			if (down && walking && walkTime / (anim / 2) == 1) current = downMoving2;
			if (left && walking && walkTime / (anim / 2) == 1) current = leftMoving2;
			if (right && walking && walkTime / (anim / 2) == 1) current = rightMoving2;
		}
	}

	public final void paint(Graphics g, Frame f) {
		userDraw(f.getDrawer());
		
		if(fighting && fight != null) {
			fight.draw(f);
		}

		if (current != null) f.getLevel().drawTile(current, x, y);
	}

	public void userDraw(Drawer d) {

	}

	public void userInit() {

	}

	public void paintOverPlayer(Graphics g, Frame f) {

	}

	public static void reloadTextures() {
		sheet = new SpriteSheet("/ch/aiko/pokemon/textures/TrainerSprites.png", 32 * 3, 32 * 4);
	}

	public void setTeamPokemon(int pos, TeamPokemon teamPokemon) {
		team.set(pos, teamPokemon);
	}

	public ArrayList<TeamPokemon> convert(TeamPokemon[] tem) {
		ArrayList<TeamPokemon> out = new ArrayList<TeamPokemon>();
		for (TeamPokemon pok : tem)
			out.add(pok);
		return out;
	}
}
