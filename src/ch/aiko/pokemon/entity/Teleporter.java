package ch.aiko.pokemon.entity;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.entity.player.Player;
import ch.aiko.pokemon.level.Level;

public class Teleporter extends Entity {

	private Level level;
	private int dx, dy;

	public Teleporter(int x, int y, Level dest, int destX, int destY) {
		super(x, y);
		level = dest;
		dx = destX;
		dy = destY;
	}
	
	public Teleporter(int x, int y, Level dest, int destX, int destY, int w, int h) {
		super(x, y);
		this.w = w;
		this.h = h;
		level = dest;
		dx = destX;
		dy = destY;
	}

	public void teleportPlayer(Screen s) {
		Level old = (Level) s.getTopLayer("Level");
		Player p = (Player) (old).getTopLayer("Player");
		Pokemon.pokemon.handler.setLevel(level);
		level.addPlayer(p);
		p.setPositionInLevel(dx, dy);
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawRect(xPos, yPos, w, h, 0xFFFF0000);
	}
	
	@Override
	public void update(Screen s, Layer layer) {
		Level l = (Level) s.getTopLayer("Level");
		if(l == null) return;
		Player p = (Player) (l).getTopLayer("Player");
		if(p == null) return;
		boolean b = false;
		for (int i = 0; i < w * h && !b; i++)
			if (p.isInside(xPos + i % w, yPos + i / w)) b = true;
		if (b) teleportPlayer(s);
	}

}
