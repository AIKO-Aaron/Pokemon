package ch.aiko.pokemon.mob;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import ch.aiko.pokemon.Drawer;
import ch.aiko.pokemon.MainMenu;
import ch.aiko.pokemon.Menu;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.language.LanguageMenu;
import ch.aiko.pokemon.pokemon.PokemonMenu;

public class PlayerMenu extends Menu {
	private static ArrayList<PlayerMenuFields> fields = new ArrayList<PlayerMenuFields>();

	public static enum PlayerMenuFields {
		POKEDEX(null), POKEMON(null), BAG(null), MENU(null), LANGUAGE(null), SAVE(null), BACK(null);

		private Runnable r;

		private PlayerMenuFields(Runnable r) {
			System.out.println("Adding " + name());
			fields.add(this);
			this.r = r;
		}

		public void setAction(Runnable r) {
			this.r = r;
		}

		public void performAction() {
			if (r != null) r.run();
		}

		public String getName() {
			return Language.current.getValue(name());
		}
	}

	Player p;

	int index = 0;

	public PlayerMenu(Player p) {
		this.p = p;
	}

	public void onOpen(Drawer d) {
		//Pause the actions of the Player
		p.setPaused(true);
		
		//Init Actions of the Menu Fields
		PlayerMenuFields.BACK.setAction(new Runnable() {
			public void run() {
				d.getFrame().closeMenu();
			}
		});
		PlayerMenuFields.POKEMON.setAction(new Runnable() {
			public void run() {
				d.getFrame().openMenu(new PokemonMenu(p));
			}
		});
		PlayerMenuFields.LANGUAGE.setAction(new Runnable() {
			public void run() {
				d.getFrame().openMenu(new LanguageMenu(p));
			}
		});
		PlayerMenuFields.MENU.setAction(new Runnable() {
			public void run() {
				d.getFrame().openMenu(new MainMenu(p));
			}
		});
	}

	public void onClose(Drawer d) {
		//Unpause the Players actions
		p.setPaused(false);
	}

	public void update(Drawer d) {
		//Key input
		if (d.getFrame().getTimesPressed(KeyEvent.VK_X) > 0) d.getFrame().closeMenu();
		if (d.getFrame().getTimesPressed(KeyEvent.VK_UP) > 0) index = index > 0 ? index - 1 : 0;
		if (d.getFrame().getTimesPressed(KeyEvent.VK_DOWN) > 0) index = index < fields.size() - 1 ? index + 1 : fields.size() - 1;

		if (d.getFrame().getTimesPressed(KeyEvent.VK_SPACE) > 0) run(index);
	}

	public void paint(Drawer d) {
		PlayerMenuFields.class.getName();
		int width = d.getFrame().getWidth() / 3;
		int x = d.getFrame().getWidth() - width - 1;
		int y = 22;
		int height = d.getFrame().getHeight() - y - 1;
		int length = fields.size();
		
		//Background of the Menu
		d.fillRect(x, y, width, height, 0xFFFFFFFF);

		//Black Rectangle around Background
		d.drawRect(x, y, width, height, 0xFF000000);

		//Menu Fields (look above)
		for (int i = 0; i < length; i++) {
			d.drawRect(x, (height / length) * i + y, width, 1, 0xFF000000);
			d.drawText(fields.get(i).getName(), x + 50, (height / length) * i + (height / length / 2) - 12, 25, 0xFF0000FF);
		}

		//Rectangle around selected Menu Field
		d.drawRect(x - 1, (height / length) * index + 1 + y, width + 1, height / length - 2, 0xFFFF00FF);
		d.drawRect(x - 0, (height / length) * index + 0 + y, width + 0, height / length - 0, 0xFFFF00FF);
		d.drawRect(x + 1, (height / length) * index - 1 + y, width - 2, height / length + 2, 0xFFFF00FF);
	}

	public void run(int index) {
		fields.get(index).performAction();
	}
}