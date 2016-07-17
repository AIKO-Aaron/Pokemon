package ch.aiko.pokemon.entity.player;

import java.awt.event.KeyEvent;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderable;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.Sprite;
import ch.aiko.engine.sprite.SpriteSheet;
import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.level.Level;

public class Player extends Entity {

	private int speed = 6;
	private SpriteSheet sprites;
	private int dir = 0; // down, up, left, right
	private int playerLayer = 0;

	private Sprite[] walkingAnims = new Sprite[4 * 4];
	private int anim = 0, curAnim = 0;
	private boolean walking = false;
	
	public static final int PLAYER_RENDERED_LAYER = 10;

	public Player() {
		sprites = new SpriteSheet("/ch/aiko/pokemon/textures/player/player_boy.png", 32, 32).removeColor(0xFF88B8B0);
		for (int i = 0; i < 4 * 4; i++) {
			walkingAnims[i] = sprites.getSprite(i);
		}
	}

	public void render(Renderer renderer) {
		renderer.setOffset(-xPos, -yPos);
		sprite = walkingAnims[dir * 4 + curAnim];
		renderer.drawSprite(sprite, renderer.getWidth() / 2 + xPos, renderer.getHeight() / 2 + yPos);
	}

	public boolean collides(Level level, int xoff, int yoff) {
		for (int i = 0; i < sprite.getWidth(); i++) {
			for (int j = 0; j < sprite.getHeight(); j++) {
				if (level.isSolid(xPos + xoff + i, yPos + yoff + j, playerLayer)) return true;
			}
		}
		return false;
	}

	public void update(Screen screen) {
		int xx = 0, yy = 0;

		Layer layer = screen.getLayer((Renderable) this).getParent();
		Level level = (Level) layer;

		if(screen.getInput().popKeyPressed(KeyEvent.VK_X)) level.openMenu(new PlayerMenu(screen));
		
		if (screen.getInput().isKeyPressed(KeyEvent.VK_LEFT)) xx--;
		if (screen.getInput().isKeyPressed(KeyEvent.VK_RIGHT)) xx++;
		if (screen.getInput().isKeyPressed(KeyEvent.VK_UP)) yy--;
		if (screen.getInput().isKeyPressed(KeyEvent.VK_DOWN)) yy++;

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
	}

	public boolean isMoving() {
		return walking;
	}

}
