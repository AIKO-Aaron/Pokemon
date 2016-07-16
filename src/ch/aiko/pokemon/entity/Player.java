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

			// TODO collision detection :'(
			// 2. Multi position collisions
			Layer layer = screen.getLayer((Renderable) this).getParent();
			Level level = (Level) layer;

			int x = xPos + xx * speed;
			int y = yPos + yy * speed;

			boolean isSolid = false;
			for(int i = 0; i<  sprite.getWidth(); i++) {
				for(int j = 0; j < sprite.getHeight(); j++) {					
					isSolid |= level.isSolid(x + screen.getRenderer().getWidth() / 2 + i, y + screen.getRenderer().getHeight() / 2 + j, playerLayer);
				}
			}
			
			
			if (isSolid) {
				xx = yy = 0;
			} else {
				xPos = x;
				yPos = y;
			}

			// Just animate when player's walking
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
