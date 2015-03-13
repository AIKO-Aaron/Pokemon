package ch.aiko.pokemon.mob;

import java.util.ArrayList;
import java.util.Stack;

import ch.aiko.pokemon.Frame;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.entity.Entity;

public class Path {

	private static final boolean DEBUG = false;
	
	public static ArrayList<java.awt.Point> getPath(Frame f, int srcx, int srcy, int destx, int desty, int speed, Entity e) {
		if(DEBUG) printlnPoint(new Point(destx, desty, null), "Finding Path to");
		ArrayList<Point> visited = new ArrayList<Point>();
		Stack<Point> on = new Stack<Point>();
		ArrayList<Point> path = new ArrayList<Point>();

		//if(destx !=0) return convert(path);
		
		on.push(new Point(srcx, srcy, null));

		while (!on.isEmpty()) {
			Point tmp = on.pop();
			
			f.getDrawer().drawTile(Pokemon.sheet1.getSprite(0), tmp.x, tmp.y);		

			if(DEBUG) printPoint(tmp, "	Currently on");
			if(DEBUG) printlnPoint(new Point(destx, desty, null), "");
			
			int dx = 0;
			int dy = 0;

			if (tmp.x < destx) dx++;
			if (tmp.x > destx) dx--;
			if (tmp.y < desty) {
				System.out.println("Going down");
				dy++;
			}
			if (tmp.y > desty) {
				System.out.println("Going up");
				dy--;
			}

			int xd = getMaxSpeedX(f, tmp.x, tmp.y, e.w, e.h, dx, speed);
			int yd = getMaxSpeedY(f, tmp.x, tmp.y, e.w, e.h, dy, speed);

			/**if (xd > tmp.x - destx && tmp.x - destx > 0) xd = tmp.x - destx;
			if (xd > destx - tmp.x && destx - tmp.x > 0) xd = destx - tmp.x;
			if (yd > tmp.y - desty && tmp.y - desty > 0) yd = tmp.y - desty;
			if (yd > desty - tmp.y && desty - tmp.y > 0) yd = desty - tmp.y;*/
			
			Point next = new Point(tmp.x + (xd * dx), tmp.y + (yd * dy), tmp);

			if (!checkPoint(visited, next)) {
				on.push(next);
				visited.add(next);
			}

			if (checkPoint(tmp, new Point(destx, desty, null))) {
				if(DEBUG) System.out.println("Found Path");
				
				path.add(tmp);
				while(tmp.prev != null) {
					tmp = tmp.prev;
					path.add(tmp);					
				}
				return convert(path);
			}
		}

		return convert(path);
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
		if(xmov * speed == 0) return 0; 
		while (f.getLevel().checkCollisionX(x + speed * xmov, y, w, h) && speed > 0) {
			speed--;
		}
		return speed;
	}

	public static int getMaxSpeedY(Frame f, int x, int y, int w, int h, int ymov, int maxSpeed) {
		if(ymov * maxSpeed == 0) return 0; 
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
