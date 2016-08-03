package ch.aiko.pokemon.entity.player;

import java.awt.event.KeyEvent;

import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.Sprite;
import ch.aiko.engine.sprite.SpriteSheet;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.fight.Fight;
import ch.aiko.pokemon.level.Level;

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

	public static final boolean CAN_WALK_SIDEWAYS = true;

	public static final int PLAYER_RENDERED_LAYER = 10;

	public int getWidth() {
		return sprite.getWidth();
	}

	public int getHeight() {
		return sprite.getHeight();
	}

	public int getX() {
		return xPos + xoff;
	}

	public int getY() {
		return yPos + yoff;
	}

	public int getRealX() {
		return xPos;
	}

	public int getRealY() {
		return yPos;
	}

	public void setPosition(int x, int y) {
		xPos = x;
		yPos = y;
	}

	public void setPositionInLevel(int x, int y) {
		xPos = x - xoff;
		yPos = y - yoff;
	}

	public Player() {}

	public Player(int x, int y) {
		sprites = new SpriteSheet("/ch/aiko/pokemon/textures/player/player_boy.png", 32, 32).removeColor(0xFF88B8B0);
		for (int i = 0; i < 4 * 4; i++) {
			walkingAnims[i] = sprites.getSprite(i, false);
		}
		xPos = x;
		yPos = y;
	}

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

	public void update(Screen screen) {
		if (isPaused) return;
		int xx = 0, yy = 0;

		Level level = (Level) getParent();

		if (input.popKeyPressed(KeyEvent.VK_X)) level.openMenu(new PlayerMenu(screen));
		if (input.popKeyPressed(KeyEvent.VK_F)) startBattle(screen);

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

	private void startBattle(Screen screen) {
		screen.addLayer(new Fight(screen));
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

	public String getName() {
		return "Player";
	}

	public int getLevel() {
		return PLAYER_RENDERED_LAYER;
	}

}
