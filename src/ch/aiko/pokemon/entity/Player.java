package ch.aiko.pokemon.entity;

import java.awt.event.KeyEvent;

import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.Sprite;
import ch.aiko.engine.sprite.SpriteSheet;

public class Player extends Entity {

	private int speed = 6;
	private SpriteSheet sprites;
	private int dir = 0; // down, up, left, right

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
			dir = xx > 0 ? 3 : xx < 0 ? 2 : yy < 0 ? 1 : yy > 0 ? 0 : -1;
			walking = true;
			xPos += xx * speed;
			yPos += yy * speed;
			anim++;
			if (anim > 15) {
				anim = 0;
				curAnim++;
				curAnim %= 4;
			}
		}
	}

	public boolean isMoving() {
		return walking;
	}

}
