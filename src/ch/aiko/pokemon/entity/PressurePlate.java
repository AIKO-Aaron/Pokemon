package ch.aiko.pokemon.entity;

import java.awt.Graphics;

import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.sprite.Sprite;

public class PressurePlate extends Entity {

	public PressurePlate(int x, int y, Sprite s) {
		super(x, y, s);
	}
	
	public void onStepOn(Player p, int x, int y, Frame f) {
	}

	public void update(Frame f) {
		int px = f.getLevel().getPlayer().x;
		int py = f.getLevel().getPlayer().y;
		int pw = f.getLevel().getPlayer().w;
		int ph = f.getLevel().getPlayer().h;
		
		if(px <= x + w && px + pw > x && py <= y + h && py + ph > y) onStepOn(f.getLevel().getPlayer(), px, py, f);
	}

	public void paint(Graphics g, Frame f) {
		
	}

	public void paintOverPlayer(Graphics g, Frame f) {
		
	}
	
	public PressurePlate copy() {
		return new PressurePlate(x, y, sprite.copy()) {
			public void update(Frame mainFrame) {
				super.update(mainFrame);
			}

			public void paint(Graphics g, Frame f) {
				super.paint(g, f);
			}

			public void paintOverPlayer(Graphics g, Frame f) {
				super.paintOverPlayer(g, f);
			}
			
			public void onStepOn(Player p, int x, int y, Frame f) {
				PressurePlate.this.onStepOn(p, x, y, f);
			}
		};
	}

}
