package ch.aiko.pokemon.mob;

import java.awt.Graphics;
import java.awt.Point;

import ch.aiko.pokemon.Drawer;
import ch.aiko.pokemon.Frame;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.sprite.Sprite;

public class TestMob extends Mob {

	private static final float speed = 1F;

	public TestMob(Sprite s, int x, int y) {
		super(s, x, y);

		new Thread("Hello") {
			public void run() {
				while (true) {
					//System.err.println("err");
					//if(Pokemon.getMainFrame() != null) Pokemon.getMainFrame().repaint();
				}
			}
		}.start();
	}

	public TestMob(Sprite s, int x, int y, int w, int h) {
		super(s, x, y, w, h);
	}

	public void update(Frame mainFrame) {
		Point p = pathFind(mainFrame, mainFrame.getLevel().getPlayer().x, mainFrame.getLevel().getPlayer().y, speed);

		if (p.x != 0) x = p.x;
		if (p.y != 0) y = p.y;
	}

	public void paint(Graphics g, Frame mainFrame) {
	}

	public void paint(Drawer d, Point camera) {

	}

	public void paintOverPlayer(Graphics g, Frame f) {

	}

}
