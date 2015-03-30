package ch.aiko.pokemon.mob;

import java.awt.Graphics;
import java.awt.Point;

import ch.aiko.pokemon.graphics.Drawer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.sprite.Sprite;

public class TestMob extends Mob {

	private static final int speed = 1;

	public TestMob(Sprite s, int x, int y) {
		super(s, x, y);
	}

	public TestMob(Sprite s, int x, int y, int w, int h) {
		super(s, x, y, w, h);
	}

	public void update(Frame mainFrame) {
		Point p = pathFind(mainFrame, mainFrame.getLevel().getPlayer().x, mainFrame.getLevel().getPlayer().y, speed);
		
		x += getMaxSpeedX(mainFrame, -p.x, speed);
		y += getMaxSpeedY(mainFrame, -p.y, speed);
	}

	public void paint(Graphics g, Frame mainFrame) {
		mainFrame.getDrawer().fillRect(mainFrame.getLevel().getPlayer().getX(), mainFrame.getLevel().getPlayer().getY()-mainFrame.getLevel().getPlayer().h + 10, 32, 32, 0xFFFF00FF);
	}

	public void paint(Drawer d, Point camera) {

	}

	public void paintOverPlayer(Graphics g, Frame f) {

	}

}
