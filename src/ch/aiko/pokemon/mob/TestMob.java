package ch.aiko.pokemon.mob;

import java.awt.Graphics;
import java.awt.Point;

import ch.aiko.pokemon.Drawer;
import ch.aiko.pokemon.Frame;
import ch.aiko.pokemon.sprite.Sprite;

public class TestMob extends Mob {

	private static final float speed = 2;
	
	public TestMob(Sprite s, int x, int y) {
		super(s, x, y);
	}
	
	public TestMob(Sprite s, int x, int y, int w, int h) {
		super(s, x, y, w, h);
	}

	public void update(Frame mainFrame) {
		Point p = pathFind(mainFrame, mainFrame.getLevel().getPlayer().x, mainFrame.getLevel().getPlayer().y, speed);
		int xmov = p.x;
		int ymov = p.y;
		
		float xs = speed;
		float ys = speed;

		if (checkCollisionX(mainFrame, xmov, xs)) xs = getMaxSpeedX(mainFrame, xmov, speed);
		if (checkCollisionY(mainFrame, ymov, ys)) ys = getMaxSpeedY(mainFrame, ymov, speed);
		
		x += xmov * xs;
		y += ymov * ys;
	}

	public void paint(Graphics g, Frame f) {

		paint(f.getDrawer(), f.getLevel().getCamera());
	}
	
	public void paint(Drawer d, Point camera) {
		d.fillRect(x - camera.x, y - camera.y, w, h, 0xFFFF00FF);
	}

	public void paintOverPlayer(Graphics g, Frame f) {
	}

}
