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
	 * Tries to find a path through the Level. Return empty List if no path coulll be found
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
		int speed = (int) speed0;
		ArrayList<Point> path = new ArrayList<Point>();

		int xd = 0, yd = 0, x = e.x, y = e.y;

		if (e.x < x) xd = 1;
		if (e.x > x) xd = -1;
		if (e.y < y) yd = 1;
		if (e.y > y) yd = -1;

		while (x != destx || y != desty) {
			int xm = x != destx ? getMaxSpeedX(f, x, y, e.w, e.h, xd, speed) : 0;
			int ym = y != desty ? getMaxSpeedY(f, x, y, e.w, e.h, yd, speed) : 0;

			if ((xm == 0 && x != destx) || (ym == 0 && y != desty)) {
				if (xm == 0 && x != destx) {
					int yp, dis1 = 0, dis2 = 0;

					for (int yy = y; yy > 0; yy--) {
						// Frame f, int xmov, float speed, int w, int h, int y
						if (!e.checkCollisionX(f, xd, speed, e.w, e.h, yy)) break;
						dis1++;
					}

					for (int yy = y; yy < f.getLevel().getHeight(); yy++) {
						if (!e.checkCollisionX(f, xd, speed, e.w, e.h, yy)) break;
						dis2++;
					}
					
					yp = dis1 < dis2 ? -1 : 1;
					ym = yp;
				}

				if (ym == 0 && y != desty) {

				}

				// break;
			} else {
				x += xm;
				y += ym;

				path.add(new Point(destx - x, desty - y));
			}
		}

		return path;
	}

	public static void printPoint(Point p, String s) {
		System.out.println(s + ": (" + p.x + "|" + p.y + ")");
	}

	public static ArrayList<Point> join(ArrayList<Point> a1, ArrayList<Point> a2) {
		ArrayList<Point> a3 = new ArrayList<Point>();
		for (Point p : a1)
			a3.add(p);
		for (Point p : a2)
			a3.add(p);
		return a1;
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

	public Point pathFind(Frame f, int x, int y, float speed) {
		System.out.println(x + "," + y);
		for (Point p : getPathTo(f, this, x, y, speed)) {
			System.out.println(p.x + ":" + p.y);
			return p;
		}

		// if(getPathTo(f, this.x, this.y, w, h, x, y, speed) != null) return getPathTo(f, this.x, this.y, w, h, x, y, speed).get(0);
		if (f != null) return new Point(0, 0);
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

			dir = dis1 < dis2 ? -1 : 1;

			// System.out.println(dis1 + "," + dis2 + ": " + dir);
			xmov = (int) (dir * speed);
		}

		// System.out.println("XMOV: " + xmov);

		return new Point(xmov, ymov);
	}
}
