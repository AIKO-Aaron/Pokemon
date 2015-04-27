package ch.aiko.pokemon.mob;

import java.awt.Point;

import ch.aiko.engine.Renderer;
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
		Point p = pathFind(mainFrame, Frame.getLevel().getPlayer().x, Frame.getLevel().getPlayer().y, speed);
		
		x += getMaxSpeedX(mainFrame, -p.x, speed);
		y += getMaxSpeedY(mainFrame, -p.y, speed);
	}

	public void paint() {		
		Renderer.fillRect(Frame.getLevel().getPlayer().getX(), Frame.getLevel().getPlayer().getY()-Frame.getLevel().getPlayer().h + 10, 32, 32, 0xFFFF00FF);
	}

}
