package ch.aiko.pokemon.entity;

import java.awt.Graphics;

import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.mob.player.Player;
import ch.aiko.pokemon.sprite.Sprite;

public class PressurePlate extends Entity {

	public PressurePlate(int x, int y, Sprite s) {
		super(x, y, s);
	}
	
	public void onStepOn(Player p, int x, int y, Frame f) {
	}

	public void update(Frame f) {
		int px = Frame.getLevel().getPlayer().x;
		int py = Frame.getLevel().getPlayer().y;
		int pw = Frame.getLevel().getPlayer().w;
		int ph = Frame.getLevel().getPlayer().h;
		
		if(px <= x + w && px + pw > x && py <= y + h && py + ph > y) onStepOn(Frame.getLevel().getPlayer(), px, py, f);
	}

	public void paint() {
		
	}
	
	public PressurePlate copy() {
		return new PressurePlate(x, y, sprite.copy()) {
			public void update(Frame mainFrame) {
				super.update(mainFrame);
			}

			public void paint() {
				super.paint();
			}

			public void onStepOn(Player p, int x, int y, Frame f) {
				PressurePlate.this.onStepOn(p, x, y, f);
			}
		};
	}

}
