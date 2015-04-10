package ch.aiko.pokemon.fight;

import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.graphics.Drawer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.pokemon.graphics.menu.TextBox;
import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.mob.Trainer;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.util.ImageUtil;

public class Fight extends Menu {

	private Player p;
	private Trainer t;

	private TextBox text;
	FightStartAnimation anim;

	public Fight(Player p, Trainer t) {
		p.setFighting(true);
		this.p = p;
		this.t = t;

		text = new TextBox(p, "Hallo Welt, ich heisse Aaron bin 15 Jahre alt und lebe in der Schweiz");
		Pokemon.frame.openMenu(text);

		// AudioUtil.playSound("ch/aiko/pokemon/sounds/fightOpening.mp3");
		System.out.println("Playing Sound");
		System.out.println("Started Fight between: " + p + " and " + t);
	}

	public void paint(Drawer d) {
		if (text.isOpened()) return;
		
		System.out.println("closed");
		if(anim == null) anim = new FightStartAnimation();

		d.fillRect(0, 0, d.getFrame().getWidth(), d.getFrame().getHeight(), 0xFF000000);
		draw(d.getFrame());
	}

	public void update(Drawer d) {
		update(d.getFrame());
	}

	public void update(Frame f) {}

	public void draw(Frame f) {
		if (anim == null) return;

		if (!anim.isFinished()) anim.drawNext(f.getDrawer(), -Pokemon.frame.getWidth(), 0);
		else f.getDrawer().drawTile(new Sprite(ImageUtil.loadImageInClassPath("/ch/aiko/pokemon/textures/fight_ground/grass_day.png")), 0, 0, f.getWidth(), f.getHeight());
	}

	public void onOpen(Drawer d) {
		p.setPaused(true);
	}

	public void onClose(Drawer d) {
		p.setPaused(false);
	}

	public Trainer getTrainer() {
		return t;
	}

	public String name() {
		return "FightScreen";
	}

	public boolean canClose() {
		return false;
	}

}
