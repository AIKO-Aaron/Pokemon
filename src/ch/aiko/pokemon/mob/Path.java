package ch.aiko.pokemon.mob;

import java.awt.Point;
import java.util.ArrayList;

import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.graphics.Frame;

public class Path {
	
	@SuppressWarnings("unused")
	private static final boolean DEBUG = false;

	@SuppressWarnings("unused")
	public static ArrayList<Point> getPath(Frame f, int srcx, int srcy, int destx, int desty, int speed, Entity e, int depth) {
		ArrayList<Point> path = new ArrayList<Point>();
		path.add(new Point(srcy, srcy));

		int dx = destx - srcx;
		int dy = desty - srcy;

		int xx = srcx;
		int yy = srcy;

		int xMov = 0;
		int yMov = 0;

		if (srcx < destx) xMov++;
		if (srcx > destx) xMov--;
		if (srcy < desty) yMov++;
		if (srcy > desty) yMov--;

		while (xx != destx || yy != desty) {
			int xSpeed = getMaxSpeedX(f, xx, yy, e.w, e.h, xMov, Math.min(speed, Math.abs(xx - destx))) * xMov;
			int ySpeed = getMaxSpeedY(f, xx, yy, e.w, e.h, yMov, Math.min(speed, Math.abs(yy - desty))) * yMov;

			if (xSpeed == 0 && ySpeed == 0) {
				if (xx != destx) {
					int i, j;
					for (i = yy; i >= 0; i--) {
						if (getMaxSpeedX(f, xx, i, e.w, e.h, xMov, speed) > 0) break;
					}
					for (j = yy; j < f.getHeight(); j++) {
						if (getMaxSpeedX(f, xx, j, e.w, e.h, xMov, speed) > 0) break;
					}

					double w1 = Hypothenuse(xx - srcx, Math.abs(yy) - i);
					double w2 = Hypothenuse(xx - srcx, Math.abs(yy) + j);

					// TODO Add more precise path length methods to find the nearest path

					if (w1 < w2) yy = i;
					else yy = j;

					path.add(new Point(xx, yy));

					//printPoint(new Point((int) w1, (int) w2), "Path Length1");
					//printlnPoint(new Point(i, j), "Path Length");
					
					continue;
				}
				
				if (yy != desty) {
					int i, j;
					for (i = xx; i >= 0; i--) {
						if (getMaxSpeedY(f, i, yy, e.w, e.h, yMov, speed) > 0) break;
					}
					for (j = xx; j < f.getWidth(); j++) {
						if (getMaxSpeedY(f, j, yy, e.w, e.h, yMov, speed) > 0) break;
					}

					double w1 = Hypothenuse(Math.abs(xx) - i, yy - srcy);
					double w2 = Hypothenuse(Math.abs(xx) + j, yy - srcy);

					// TODO Add more precise path length methods to find the nearest path

					if (w1 < w2) xx = i;
					else xx = j;

					path.add(new Point(xx, yy));
					
					continue;
				}
			}

			if (xx != destx) xx += xSpeed;
			if (yy != desty) yy += ySpeed;
		}

		return path;
	}

	public static ArrayList<Point> getPathSimple(Frame f, int srcx, int srcy, int destx, int desty, int speed, Entity e) {
		ArrayList<Point> path = new ArrayList<Point>();
		int xx = srcx;
		int yy = srcy;
		path.add(new Point(xx, yy));

		int xMov = 0;
		int yMov = 0;

		if (xx < destx) xMov++;
		if (xx > destx) xMov--;
		if (yy < desty) yMov++;
		if (yy > desty) yMov--;

		while (xx != destx || yy != desty) {
			int xSpeed = getMaxSpeedX(f, xx, yy, e.w, e.h, xMov, Math.min(speed, Math.abs(xx - destx))) * xMov;
			int ySpeed = getMaxSpeedY(f, xx, yy, e.w, e.h, yMov, Math.min(speed, Math.abs(yy - desty))) * yMov;

			if (xx != destx) xx += xSpeed;
			if (yy != desty) yy += ySpeed;

			if (xSpeed == 0 && ySpeed == 0) {
				if (xx != destx || yy != desty) {
					Point p = getPath(f, srcx, srcy, destx, desty, speed, e, 5).get(1);
					return getPathSimple(f, srcx, srcy, p.x, p.y, speed, e);
				}
			}

			path.add(new Point(xx, yy));
		}

		/**
		 * for (int i = 0; i < path.size(); i++) { printlnPoint(path.get(i), "Path (" + i + ")"); }
		 */

		return path;
	}

	public static boolean checkPoint(Point p1, Point p2) {
		if (p1.x == p2.x && p1.y == p2.y) return true;
		return false;
	}

	public static boolean checkPoint(ArrayList<Point> all, Point p) {
		for (Point p1 : all)
			if (p1.x == p.x && p1.y == p.y) return true;
		return false;
	}

	public static void printlnPoint(Point p, String s) {
		System.out.println(s + ": (" + p.x + "|" + p.y + ")");
	}

	public static void printPoint(Point p, String s) {
		System.out.print(s + ": (" + p.x + "|" + p.y + ") ");
	}

	public static ArrayList<Point> join(ArrayList<Point> a1, ArrayList<Point> a2) {
		ArrayList<Point> a3 = new ArrayList<Point>();
		for (Point p : a1)
			a3.add(p);
		for (Point p : a2)
			a3.add(p);

		return a3;
	}

	public static int getMaxSpeedX(Frame f, int x, int y, int w, int h, int xmov, int speed) {
		if (xmov * speed == 0) return 0;
		while (f.getLevel().checkCollisionX(x + speed * xmov, y, w, h) && speed > 0) {
			speed--;
		}
		return speed;
	}

	public static int getMaxSpeedY(Frame f, int x, int y, int w, int h, int ymov, int maxSpeed) {
		if (ymov * maxSpeed == 0) return 0;
		while (f.getLevel().checkCollisionY(x, (int) (y + maxSpeed * ymov), w, h) && maxSpeed > 0) {
			maxSpeed--;
		}
		return maxSpeed;
	}

	public static ArrayList<java.awt.Point> convert(ArrayList<Point> p) {
		ArrayList<java.awt.Point> ret = new ArrayList<java.awt.Point>();
		for (Point p1 : p)
			ret.add(new java.awt.Point(p1.x, p1.y));
		return ret;
	}

	// /|
	// / |
	// / |
	// length/ |
	// / | yd
	// / |
	// / |
	// /_______|
	// xd
	public static final double Hypothenuse(double Kathete1, double Kathete2) {
		return Math.sqrt(Math.pow(Kathete1, 2) + Math.pow(Kathete2, 2));
	}

	public static final double Kathete(double Hypothenuse, double Kathete1) {
		return Math.sqrt(Math.pow(Hypothenuse, 2) - Math.pow(Kathete1, 2));
	}
}
