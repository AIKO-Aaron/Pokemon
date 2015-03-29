package ch.aiko.pokemon.fight;

import ch.aiko.pokemon.Frame;
import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.mob.Trainer;

public class Fight {

	private Player p;
	private Trainer t;
	
	private FightStartAnimation anim;
	
	public Fight(Player p, Trainer t) {
		p.setFighting(true);
		
		System.out.println("Started Fight between: " + p + " and " + t);
		
		anim = new FightStartAnimation();
	}
	
	public void update(Frame f) {
		
	}
	
	public void draw(Frame f) {
		anim.drawNext(f.getDrawer(), 0, 0);
	}
	

}
