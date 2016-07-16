package ch.aiko.pokemon.entity;

import java.awt.event.KeyEvent;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderable;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.Sprite;
import ch.aiko.engine.sprite.SpriteSheet;
import ch.aiko.pokemon.level.Level;

public class Player extends Entity {

	private int speed = 6;
	private SpriteSheet sprites;
	private int dir = 0; // down, up, left, right
	private int playerLayer = 0;

	private Sprite[] walkingAnims = new Sprite[4 * 4];
	private int anim = 0, curAnim = 0;
	private boolean walking = false;

	public Player() {
		sprites = new SpriteSheet("/ch/aiko/pokemon/textures/player/player_boy.png", 32, 32);
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

		if (screen.getInput().isKeyPressed(KeyEvent.VK_LEFT)) xx--;
		if (screen.getInput().isKeyPressed(KeyEvent.VK_RIGHT)) xx++;
		if (screen.getInput().isKeyPressed(KeyEvent.VK_UP)) yy--;
		if (screen.getInput().isKeyPressed(KeyEvent.VK_DOWN)) yy++;

		if (xx == 0 && yy == 0) {
			walking = false;
			anim = 0;
			curAnim = 0;
		} else {

			// TODO collision detection :'(
			// 2. Multi position collisions
			Layer layer = screen.getLayer((Renderable) this).getParent();
			Level level = (Level) layer;

			int xspeed = xx * speed;
			int yspeed = yy * speed;

			if (collides(level, screen.getRenderer().getWidth() / 2 + xspeed, screen.getRenderer().getHeight() / 2 + yspeed)) {
				while (collides(level, screen.getRenderer().getWidth() / 2 + xspeed, screen.getRenderer().getHeight() / 2) && xspeed != 0)
					xspeed -= xx;
				while (collides(level, screen.getRenderer().getWidth() / 2, screen.getRenderer().getHeight() / 2 + yspeed) && yspeed != 0)
					yspeed -= yy;

			}

			dir = xx > 0 ? 3 : xx < 0 ? 2 : yy < 0 ? 1 : yy > 0 ? 0 : 2;

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
			}
		}
	}

	public boolean isMoving() {
		return walking;
	}

}
