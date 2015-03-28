package ch.aiko.pokemon.fight;

import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.mob.Trainer;

public class Fight {

	private Player p;
	private Trainer t;
	
	public Fight(Player p, Trainer t) {
		p.setFighting(true);
		
		System.out.println("Started Fight between: " + p + " and " + t);
		
		new FightStartAnimation();
	}

}
