package ch.aiko.pokemon.mob;

import java.awt.Point;
import java.util.ArrayList;

import ch.aiko.pokemon.Frame;
import ch.aiko.pokemon.entity.Entity;
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
		// sprite.setImage(ImageUtil.resize(s.getImage(), w, h));
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
	 * Tries to find a path through the Level.
	 * Return empty List if no path coulll be found
	 * 
	 * @param f The mainFrame. Used to get the Level with its collision-detection
	 * @param posx1 The x position of the source
	 * @param posy1 The y position of the source
	 * @param w The width of the source
	 * @param h The height of the source
	 * @param x The desired new location x coordinate
	 * @param y The desired new location y coordinate
	 * @param speed The speed with which your source walks/flies
	 * @return A List with all steps the source has to tak to get to the location.
	 */
	public static ArrayList<Point> getPathTo(Frame f, int posx1, int posy1, int w, int h, int x, int y, float speed) {
		ArrayList<Point> path = new ArrayList<Point>();

		return path;
	}

	public Point pathFind(Frame f, int x, int y, float speed) {
		int xmov = 0, ymov = 0;
		if (this.x < x) xmov++;
		if (this.x > x) xmov--;

		if (this.y < y) ymov++;
		if (this.y > y) ymov--;

		if (checkCollisionX(f, xmov, speed, w, h, y) && checkCollisionY(f, ymov, speed, w, h, xmov, 0)) {
			int dir = 1;
			int dis1 = 0, dis2 = 0;
			for (int yy = y; yy > 0; yy--) {
				dis1--;
				if (!checkCollisionX(f, xmov, speed, w, h, yy)) break;
			}
			for (int yy = y; yy > f.getLevel().getHeight(); yy++) {
				dis2++;
				if (!checkCollisionX(f, xmov, speed, w, h, yy)) break;
			}
			int max = Math.max(Math.abs(dis1), dis2);
			// System.out.println(max);

			dir = dis1 < dis2 ? -1 : 1;
			ymov = (int) (dir * speed);
		}

		if (checkCollisionY(f, ymov, speed, w, h, x) && checkCollisionX(f, xmov, speed, w, h, 0, ymov)) {
			int dir = 0;
			int dis1 = 0, dis2 = 0;
			for (int xx = x; xx > 0; xx--) {
				dis1--;
				if (!checkCollisionY(f, ymov, speed, w, h, xx)) break;
			}
			for (int xx = x; xx > f.getLevel().getWidth(); xx++) {
				dis2++;
				if (!checkCollisionY(f, ymov, speed, w, h, xx)) break;
			}

			int max = Math.max(Math.abs(dis1), dis2);
			System.out.println(max + "x");

			dir = dis1 < dis2 ? -1 : 1;

			// System.out.println(dis1 + "," + dis2 + ": " + dir);
			xmov = (int) (dir * speed);
		}

		// System.out.println("XMOV: " + xmov);

		return new Point(xmov, ymov);
	}
}
