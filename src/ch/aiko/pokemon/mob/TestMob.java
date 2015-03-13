package ch.aiko.pokemon.mob;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import ch.aiko.pokemon.Drawer;
import ch.aiko.pokemon.Frame;
import ch.aiko.pokemon.sprite.Sprite;

public class TestMob extends Mob {

	private static final float speed = 0F;

	public TestMob(Sprite s, int x, int y) {
		super(s, x, y);
	}

	public TestMob(Sprite s, int x, int y, int w, int h) {
		super(s, x, y, w, h);
	}

	public void update(Frame mainFrame) {

	}

	public void paint(Graphics g, Frame mainFrame) {
		// paint(mainFrame.getDrawer(), mainFrame.getLevel().getCamera());

		Point p = pathFind(mainFrame, mainFrame.getLevel().getPlayer().x, mainFrame.getLevel().getPlayer().y, speed);

		if (p.x != 0) x = p.x;
		if (p.y != 0) y = p.y;
	}

	public void paint(Drawer d, Point camera) {
		//d.fillRect(x - camera.x, y - camera.y, w, h, 0xFFFF00FF);
	}

	public void paintOverPlayer(Graphics g, Frame f) {
		g.setColor(Color.PINK);
		g.fillRect(x - f.getLevel().getCamera().x / 2, y - f.getLevel().getCamera().y / 2, w, h);
	}

}
