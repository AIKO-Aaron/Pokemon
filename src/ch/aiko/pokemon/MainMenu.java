package ch.aiko.pokemon;

import java.awt.event.KeyEvent;

import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.mob.Trainer;

public class MainMenu extends Menu {
	
	private Runnable i1 = new Runnable() {
		public void run() {
			Pokemon.reloadTextures();
			Trainer.reloadTextures();
		}
	};
	
	private Runnable[] fields = new Runnable[]{i1};
	private String[] labels = new String[]{"Reload Textures"};
	private int index = 0;
	
	private Player p;
	
	public MainMenu(Player p) {
		this.p = p;
	}
	
	public void onOpen(Drawer d) {
		p.setPaused(true);
	}
	
	public void onClose(Drawer d) {
		p.setPaused(false);
	}

	public void paint(Drawer d) {
		d.fillRect(0, 0, d.getFrame().getWidth(), d.getFrame().getHeight(), 0xFF000000);
		for(int i = 0; i < fields.length; i++) {
			d.drawText(labels[i] + (i == index ? "  <---" : ""), 10, i * 25, 25, 0xFFFFFFFF, "fonts/Sans.ttf");
		}
	}
	
	public void update(Drawer d) {
		if (d.getFrame().getTimesPressed(KeyEvent.VK_X) > 0) d.getFrame().closeMenu();
		if (d.getFrame().getTimesPressed(KeyEvent.VK_UP) > 0) index = index > 0 ? index - 1 : 0;
		if (d.getFrame().getTimesPressed(KeyEvent.VK_DOWN) > 0) index = index < fields.length - 1 ? index + 1 : fields.length - 1;

		if (d.getFrame().getTimesPressed(KeyEvent.VK_SPACE) > 0) fields[index].run();
	}
}
