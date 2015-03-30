package ch.aiko.pokemon.fight;

import ch.aiko.pokemon.graphics.Drawer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.mob.Trainer;

public class Fight extends Menu {

	private Player p;
	private Trainer t;

	private FightStartAnimation anim;

	public Fight(Player p, Trainer t) {
		p.setFighting(true);

		System.out.println("Started Fight between: " + p + " and " + t);

		anim = new FightStartAnimation();
	}

	public void paint(Drawer d) {
		d.fillRect(0, 0, d.getFrame().getWidth(), d.getFrame().getHeight(), 0xFF000000);
		draw(d.getFrame());
	}

	public void update(Drawer d) {
		update(d.getFrame());
	}

	public void update(Frame f) {

	}

	public void draw(Frame f) {
		if (anim.isFinished()) {
			System.out.println("done");
		} else {
			anim.drawNext(f.getDrawer(), 0, 0);
		}
	}

	public void onOpen(Drawer d) {
		p.setPaused(true);
	}

	public void onClose(Drawer d) {
		p.setPaused(false);
	}
	
	public String name() {
		return "FightScreen";
	}

}
