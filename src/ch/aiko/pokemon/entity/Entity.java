package ch.aiko.pokemon.entity;

import ch.aiko.engine.graphics.Renderable;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.graphics.Updatable;
import ch.aiko.engine.sprite.Sprite;

public class Entity implements Renderable, Updatable {

	protected Sprite sprite;
	protected int xPos, yPos;
	protected int w, h;

	public Entity() {
		xPos = 0;
		yPos = 0;
		sprite = new Sprite(0xFFFF00FF, 16, 16);
	}

	public Entity(int x, int y) {
		xPos = x;
		yPos = y;
		sprite = new Sprite(0xFFFF00FF, 16, 16);
	}

	public Entity(Sprite s) {
		xPos = 0;
		yPos = 0;
		sprite = s;
	}

	public Entity(int x, int y, Sprite s) {
		xPos = x;
		yPos = y;
		sprite = s;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public int getX() {
		return xPos;
	}

	public int getY() {
		return yPos;
	}

	public void setPosition(int x, int y) {
		xPos = x;
		yPos = y;
	}

	public void move(int xamount, int yamount) {
		xPos += xamount;
		yPos += yamount;
	}

	public void update(Screen screen) {}

	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, xPos, yPos);
	}

}
