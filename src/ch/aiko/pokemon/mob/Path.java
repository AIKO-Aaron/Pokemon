package ch.aiko.pokemon.mob;

import java.util.ArrayList;
import java.util.Stack;

import ch.aiko.pokemon.Frame;
import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.sprite.Sprite;

public class Path {

	private static final boolean DEBUG = true;

	public static ArrayList<Point> getPath(Frame f, int srcx, int srcy, int destx, int desty, int speed, Entity e, int depth) {
		if (DEBUG) printlnPoint(new Point(destx, desty, null), "Finding Path to");
		ArrayList<Point> visited = new ArrayList<Point>();
		Stack<Point> on = new Stack<Point>();
		ArrayList<Point> path = new ArrayList<Point>();

		// Tile[] tiles = f.getLevel().getTilesOn(e);
		// for (Tile t : tiles)
		// if (t.solid) return convert(path);
		if (depth == 0) return path;

		on.push(new Point(srcx, srcy, null));

		while (!on.isEmpty()) {
			Point tmp = on.pop();

			if (DEBUG) printPoint(tmp, "	Currently on");
			if (DEBUG) printlnPoint(new Point(destx, desty, null), "");

			// f.getLevel().drawTile(new Sprite(0xFFFF00FF, e.w, e.h), tmp.x, tmp.y);

			int dx = 0;
			int dy = 0;

			if (tmp.x < destx) dx++;
			if (tmp.x > destx) dx--;
			if (tmp.y < desty) dy++;
			if (tmp.y > desty) dy--;

			if (DEBUG) printlnPoint(new Point(dx, dy, null), "		Direction");

			int xd = getMaxSpeedX(f, tmp.x, tmp.y, e.w, e.h, dx, speed);
			int yd = getMaxSpeedY(f, tmp.x, tmp.y, e.w, e.h, dy, speed);

			if (DEBUG) printlnPoint(new Point(xd, yd, null), "		Movement");

			if (xd == 0 && tmp.x != destx) {
				if (DEBUG) System.err.println("Collision ! Trying to find another path to desired Position");
				int i, j;
				for (i = tmp.y; i >= 0; i -= speed) {
					if (getMaxSpeedX(f, tmp.x, i, e.w, e.h, dx, speed) > 0) break;
				}
				for (j = tmp.y; j < f.getHeight(); j += speed) {
					if (getMaxSpeedX(f, tmp.x, j, e.w, e.h, dx, speed) > 0) break;
				}
				// System.out.println(i + ":" + j);
				if (i == 0 && j == f.getHeight()) return (path);
				ArrayList<Point> way1 = getPath(f, tmp.x, i - e.h, destx, desty, speed, e, depth - 1);
				ArrayList<Point> way2 = getPath(f, tmp.x, j + e.h, destx, desty, speed, e, depth - 1);

				if(way1.size() == 0 && way2.size() == 0) return path;
				
				if (way1.size() == 0) way1 = new ArrayList<Point>(way2.size() + 1);
				if (way2.size() == 0) way2 = new ArrayList<Point>(way1.size() + 1);

				ArrayList<Point> shorter = way1.size() < way2.size() ? way1 : way2;
				ArrayList<Point> wayToStartOfShorter = way1.size() < way2.size() ? getPath(f, srcx, srcy, tmp.x, i - e.w, speed, e, depth - 1) : getPath(f, srcx, srcy, tmp.x, j + e.w, speed, e, depth - 1);

				path = join(shorter, wayToStartOfShorter);

				System.out.println(way1.size() + ":" + way2.size());
			}

			Point next = new Point(tmp.x + (xd * dx), tmp.y + (yd * dy), tmp);

			if (!checkPoint(visited, next)) {
				on.push(next);
				visited.add(next);
			}

			if (checkPoint(tmp, new Point(destx, desty, null))) {
				if (DEBUG) System.out.println("Found Path");
				path.add(tmp);
				while (tmp.prev != null) {
					tmp = tmp.prev;
					path.add(tmp);
				}
				if (path.size() > 1) printlnPoint(path.get(path.size() - 2), "Position to go");
				return (path);
			}
		}

		return (path);
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
		System.out.print(s + ": (" + p.x + "|" + p.y + ")");
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

	static class Point {
		public int x, y;
		public Point prev;

		public Point(int x, int y, Point prev) {
			this.x = x;
			this.y = y;
			this.prev = prev;
		}
	}
}
