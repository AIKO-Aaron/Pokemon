package ch.aiko.pokemon.entity.player;

import java.awt.event.KeyEvent;

import ch.aiko.as.ASDataBase;
import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.Sprite;
import ch.aiko.engine.sprite.SpriteSheet;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.entity.trainer.Trainer;
import ch.aiko.pokemon.fight.Fight;
import ch.aiko.pokemon.level.Level;
import ch.aiko.pokemon.pokemons.TeamPokemon;
import ch.aiko.util.FileUtil;

public class Player extends Entity {

	protected int xoff, yoff;
	protected int speed = 6;
	protected SpriteSheet sprites;
	protected int dir = 0; // down, up, left, right
	protected int playerLayer = 0;

	protected Sprite[] walkingAnims = new Sprite[4 * 4];
	protected int anim = 0, curAnim = 0, send = 0;
	protected boolean walking = false;
	public boolean isPaused = false;
	public TeamPokemon[] team = new TeamPokemon[6];

	public static final boolean CAN_WALK_SIDEWAYS = true;

	public static final int PLAYER_RENDERED_LAYER = 10;

	public int getWidth() {
		return sprite.getWidth();
	}

	public int getHeight() {
		return sprite.getHeight();
	}

	@Override
	public int getX() {
		return xPos + xoff;
	}

	@Override
	public int getY() {
		return yPos + yoff;
	}

	public int getRealX() {
		return xPos;
	}

	public int getRealY() {
		return yPos;
	}

	@Override
	public void setPosition(int x, int y) {
		xPos = x;
		yPos = y;
	}

	public void setPositionInLevel(int x, int y) {
		xPos = x - xoff;
		yPos = y - yoff;
	}

	protected Player() {}

	public Player(int x, int y) {
		sprites = new SpriteSheet("/ch/aiko/pokemon/textures/player/player_boy.png", 32, 32).removeColor(0xFF88B8B0);
		for (int i = 0; i < 4 * 4; i++) {
			walkingAnims[i] = sprites.getSprite(i, false);
		}
		xPos = x;
		yPos = y;
	}
	
	public void save() {
		ASDataBase base = new ASDataBase("Team");
		for(TeamPokemon pok : team) {
			if(pok != null) {
				pok.reload();
				base.addObject(pok);
			}
		}
		base.saveToFile(FileUtil.getRunningJar() + "/player.bin");
	}

	@Override
	public void render(Renderer renderer) {
		renderer.setOffset(-xPos, -yPos);
		sprite = walkingAnims[dir * 4 + curAnim];
		xoff = renderer.getWidth() / 2;
		yoff = renderer.getHeight() / 2;
		renderer.drawSprite(sprite, xPos + xoff, yPos + yoff);
	}

	public boolean collides(Level level, int xoff, int yoff) {
		for (int i = 0; i < sprite.getWidth(); i++) {
			for (int j = 0; j < sprite.getHeight(); j++) {
				if (level.isSolid(xPos + xoff + i, yPos + yoff + j, playerLayer)) { return true; }
			}
		}
		return false;
	}

	public boolean isInside(int xx, int yy) {
		int width = sprite == null ? 32 : sprite.getWidth();
		int height = sprite == null ? 32 : sprite.getHeight();
		return xx > getX() && xx < getX() + width && yy > getY() && yy < getY() + height;
	}

	@Override
	public void update(Screen screen, Layer l) {
		if (isPaused) return;
		int xx = 0, yy = 0;

		Level level = (Level) getParent();

		if (input.popKeyPressed(KeyEvent.VK_X)) level.openMenu(new PlayerMenu(screen));

		if (input.isKeyPressed(KeyEvent.VK_LEFT)) xx--;
		if (input.isKeyPressed(KeyEvent.VK_RIGHT)) xx++;
		if (input.isKeyPressed(KeyEvent.VK_UP)) yy--;
		if (input.isKeyPressed(KeyEvent.VK_DOWN)) yy++;

		if (!CAN_WALK_SIDEWAYS) {
			if (xx != 0) yy = 0;
		}

		if (xx == 0 && yy == 0) {
			walking = false;
			anim = 0;
			curAnim = 0;
		} else {
			int xspeed = xx * speed;
			int yspeed = yy * speed;

			dir = xspeed > 0 ? 3 : xspeed < 0 ? 2 : yspeed < 0 ? 1 : yspeed > 0 ? 0 : dir;

			if (collides(level, screen.getRenderer().getWidth() / 2 + xspeed, screen.getRenderer().getHeight() / 2 + yspeed)) {
				while (collides(level, screen.getRenderer().getWidth() / 2 + xspeed, screen.getRenderer().getHeight() / 2) && xspeed != 0)
					xspeed -= xx;
				while (collides(level, screen.getRenderer().getWidth() / 2, screen.getRenderer().getHeight() / 2 + yspeed) && yspeed != 0)
					yspeed -= yy;
			}

			dir = xspeed > 0 ? 3 : xspeed < 0 ? 2 : yspeed < 0 ? 1 : yspeed > 0 ? 0 : dir;

			boolean isSolid = xspeed == 0 && yspeed == 0;
			if (!isSolid) {
				xPos += xspeed;
				yPos += yspeed;
			}
			walking = !isSolid;

			// Just animate when player's walking
			if (walking) {
				anim++;
				if (anim > 15) {
					anim = 0;
					curAnim++;
					curAnim %= 4;
				}
			} else curAnim = anim = 0;
		}

		send++;
		send %= 3;

		if (Pokemon.client != null && walking) {
			Pokemon.client.sendText("/spos/" + xPos + "/" + yPos + "/" + dir);
			Pokemon.client.sendText("/slvl/" + level.path);
		}
	}

	public void startBattle(Screen screen, Trainer t) {
		screen.addLayer(new Fight(screen, this, t));
	}

	public boolean isMoving() {
		return walking;
	}

	public void setPaused(boolean b) {
		isPaused = b;
	}

	public void setDirection(int dir2) {
		dir = dir2;
	}

	public int getDirection() {
		return dir;
	}

	@Override
	public String getName() {
		return "Player";
	}

	@Override
	public int getLevel() {
		return PLAYER_RENDERED_LAYER;
	}

}
