package ch.aiko.pokemon.entity;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ch.aiko.pokemon.Frame;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.pokemon.sprite.Tile;

public abstract class Entity extends Tile {

	//protected Sprite sprite;

	protected boolean getsRendered = true;

	public Entity(int x, int y, Sprite s) {
		super(s, x, y, false);
		//this.sprite = s.copy();
	}

	public abstract void update(Frame mainFrame);

	public abstract void paint(Graphics g, Frame f);

	public abstract void paintOverPlayer(Graphics g, Frame f);

	public int getX() {
		
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public boolean getsRendered() {
		return getsRendered;
	}

	public void setGetsRenderered(boolean i) {
		getsRendered = i;
	}

	public boolean checkCollisionX(Frame f, int xmov, float speed) {
		return f.getLevel().checkCollisionX((int) (x + speed * xmov), y, w, h);
	}

	public boolean checkCollisionY(Frame f, int ymov, float speed) {
		return f.getLevel().checkCollisionY(x, (int) (y + speed * ymov), w, h);
	}

	public boolean checkCollisionX(Frame f, int xmov, float speed, int w, int h) {
		return f.getLevel().checkCollisionX((int) (x + speed * xmov), y, w, h);
	}

	public boolean checkCollisionY(Frame f, int ymov, float speed, int w, int h) {
		return f.getLevel().checkCollisionY(x, (int) (y + speed * ymov), w, h);
	}

	public boolean checkCollisionX(Frame f, int xmov, float speed, int w, int h, int xoff, int yoff) {
		return f.getLevel().checkCollisionX((int) (x + speed * xmov + xoff), y + yoff, w, h);
	}

	public boolean checkCollisionY(Frame f, int ymov, float speed, int w, int h, int xoff, int yoff) {
		return f.getLevel().checkCollisionY(x + xoff, (int) (y + speed * ymov + yoff), w, h);
	}
	
	public boolean checkCollisionX(Frame f, int xmov, float speed, int w, int h, int y) {
		return f.getLevel().checkCollisionX((int) (x + speed * xmov), y, w, h);
	}

	public boolean checkCollisionY(Frame f, int ymov, float speed, int w, int h, int x) {
		return f.getLevel().checkCollisionY(x, (int) (y + speed * ymov), w, h);
	}

	public static enum Side {
		LEFT, RIGHT, TOP, BOTTOM;
	}

	public Side[] getSides(Frame f, int xm, int ym, int speed) {
		ArrayList<Side> sides = new ArrayList<Side>();
		if (checkCollisionX(f, xm, speed, 1, 1, w / 2, 0) || checkCollisionY(f, ym, speed, 1, 1, w / 2, 0)) {
			sides.add(Side.TOP);
		} else if (checkCollisionX(f, xm, speed, 1, 1, 0, h / 2) || checkCollisionY(f, ym, speed, 1, 1, 0, h / 2)) {
			sides.add(Side.LEFT);
		} else if (checkCollisionX(f, xm, speed, 1, 1, w / 2, h) || checkCollisionY(f, ym, speed, 1, 1, w / 2, h)) {
			sides.add(Side.BOTTOM);
		} else if (checkCollisionX(f, xm, speed, 1, 1, w, h / 2) || checkCollisionY(f, ym, speed, 1, 1, w, h / 2)) {
			sides.add(Side.RIGHT);
		}
		return sides.toArray(new Side[sides.size()]);
	}

	public Point getSideCoords(Side s) {
		switch (s) {
			case TOP:
				return new Point(x + w / 2, y);
			case BOTTOM:
				return new Point(x + w / 2, y + h);
			case LEFT:
				return new Point(x, y + h / 2);
			case RIGHT:
				return new Point(x + w - 1, y + h / 2);
		}
		return new Point(x, y);
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public Entity copy() {
		return new Entity(x, y, sprite.copy()) {
			public void update(Frame mainFrame) {
				Entity.this.update(mainFrame);
			}

			public void paint(Graphics g, Frame f) {
				Entity.this.paint(g, f);
			}

			public void paintOverPlayer(Graphics g, Frame f) {
				Entity.this.paintOverPlayer(g, f);
			}
		};
	}

	public void resize(int tileWidth, int tileHeight) {
		this.w = tileWidth;
		this.h = tileHeight;
		this.sprite.setImage(sprite.getImage().getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH));
	}
}
