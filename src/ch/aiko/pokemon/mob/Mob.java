package ch.aiko.pokemon.mob;

import java.awt.Point;
import java.util.ArrayList;

import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.pokemon.sprite.Tile;
import ch.aiko.util.ImageUtil;

public abstract class Mob extends Entity {

	public Mob(Sprite s, int x, int y) {
		super(x, y, s);
		w = s.getWidth();
		h = s.getHeight();
	}

	public Mob(Sprite s, int x, int y, int w, int h) {
		super(x, y, s);
		this.w = w;
		this.h = h;
		sprite.setImage(ImageUtil.resize2(s.getImage(), (float) w / (float) sprite.getWidth(), (float) h / (float) sprite.getHeight()));
	}

	public int getWidth() {
		return w;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public int getHeight() {
		return h;
	}

	public void setHeight(int h) {
		this.h = h;
	}

	public boolean checkCollisionX(Frame f, int xmov, float speed) {
		return f.getLevel().checkCollisionX((int) (x + speed * xmov), y, w, h);
	}

	public boolean checkCollisionY(Frame f, int ymov, float speed) {
		return f.getLevel().checkCollisionY(x, (int) (y + speed * ymov), w, h);
	}

	public float getMaxSpeedX(Frame f, int xmov, float speed) {
		while (f.getLevel().checkCollisionX((int) (x + speed * xmov), y, w, h) && speed > 0) {
			speed--;
		}
		return speed;
	}

	public float getMaxSpeedY(Frame f, int ymov, float maxSpeed) {
		while (f.getLevel().checkCollisionY(x, (int) (y + maxSpeed * ymov), w, h) && maxSpeed > 0) {
			maxSpeed--;
		}
		return maxSpeed;
	}

	public int getMaxSpeedX(Frame f, int xmov, int speed) {
		while (f.getLevel().checkCollisionX((int) (x + speed * xmov), y, w, h) && speed > 0) {
			speed--;
		}
		return speed;
	}

	public int getMaxSpeedY(Frame f, int ymov, int maxSpeed) {
		while (f.getLevel().checkCollisionY(x, (int) (y + maxSpeed * ymov), w, h) && maxSpeed > 0) {
			maxSpeed--;
		}
		return maxSpeed;
	}

	public boolean isOnTile(Frame f) {
		return f.getLevel().isOnTile(this);
	}

	public boolean isOnTile(Frame f, Tile t) {
		return x < t.x && x + getWidth() > t.x;
	}

	public ArrayList<Point> getPathTo(Frame f, int x, int y, float speed) {
		ArrayList<Point> path = new ArrayList<Point>();

		return path;
	}

	/**
	 * Tries to find a path through the Level. Return empty List if no path could be found
	 * 
	 * @param f
	 *            The mainFrame. Used to get the Level with its collision-detection
	 * @param posx1
	 *            The x position of the source
	 * @param posy1
	 *            The y position of the source
	 * @param w
	 *            The width of the source
	 * @param h
	 *            The height of the source
	 * @param x
	 *            The desired new location x coordinate
	 * @param y
	 *            The desired new location y coordinate
	 * @param speed
	 *            The speed with which your source walks/flies
	 * @return A List with all steps the source has to take to get to the location. Ideally for moving objects with collision detection
	 */
	public static ArrayList<Point> getPathTo(Frame f, Entity e, int destx, int desty, float speed0) {
		return Path.getPathSimple(f, e.x, e.y, destx, desty, (int) speed0, e);
	}

	public static int getMaxSpeedX(Frame f, int x, int y, int w, int h, int xmov, int speed) {
		while (f.getLevel().checkCollisionX(x + speed * xmov, y, w, h) && speed > 0) {
			speed--;
		}
		return speed;
	}

	public static int getMaxSpeedY(Frame f, int x, int y, int w, int h, int ymov, int maxSpeed) {
		while (f.getLevel().checkCollisionY(x, (int) (y + maxSpeed * ymov), w, h) && maxSpeed > 0) {
			maxSpeed--;
		}
		return maxSpeed;
	}

	public void teleport(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	public Point pathFind(Frame f, int x, int y, int speed) {
		int xMov = 0;
		int yMov = 0;

		if (this.x < x) xMov++;
		if (this.x > x) xMov--;
		if (this.y < y) yMov++;
		if (this.y > y) yMov--;
		
		
		return new Point(xMov, yMov);
	}
}
