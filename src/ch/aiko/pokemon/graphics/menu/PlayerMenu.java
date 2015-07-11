package ch.aiko.pokemon.graphics.menu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import ch.aiko.engine.KeyBoard;
import ch.aiko.engine.Menu;
import ch.aiko.engine.Renderer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.mob.player.Player;
import ch.aiko.pokemon.settings.Settings;

public class PlayerMenu extends Menu {
	private static ArrayList<PlayerMenuFields> fields = new ArrayList<PlayerMenuFields>();

	public static enum PlayerMenuFields {
		POKEDEX(null),
		POKEMON(null),
		BAG(null),
		MENU(null),
		LANGUAGE(null),
		SAVE(null),
		BACK(null);

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

	public String name() {
		return "SideMenu";
	}

	public void onOpen() {
		// Pause the actions of the Player
		p.setPaused(true);

		// Init Actions of the Menu Fields
		PlayerMenuFields.BACK.setAction(new Runnable() {
			public void run() {
				Frame.closeMenu();
			}
		});
		PlayerMenuFields.POKEMON.setAction(new Runnable() {
			public void run() {
				Frame.openMenu(new PokemonMenu(p));
			}
		});
		PlayerMenuFields.LANGUAGE.setAction(new Runnable() {
			public void run() {
				Frame.openMenu(new LanguageMenu(p));
			}
		});
		PlayerMenuFields.MENU.setAction(new Runnable() {
			public void run() {
				Frame.openMenu(new MainMenu(p));
			}
		});
	}

	public void onClose() {
		// Unpause the Players actions
		p.setPaused(false);
	}

	public void update(double delta) {
		// Key input
		if (KeyBoard.getTimesPressed(Settings.getInteger("keyMenu")) > 0) Frame.closeMenu();
		if (KeyBoard.getTimesPressed(Settings.getInteger("keyUp")) > 0) index = index > 0 ? index - 1 : 0;
		if (KeyBoard.getTimesPressed(Settings.getInteger("keyDown")) > 0) index = index < fields.size() - 1 ? index + 1 : fields.size() - 1;

		if (KeyBoard.getTimesPressed(KeyEvent.VK_SPACE) > 0) run(index);
	}

	public void draw(double d) {
		PlayerMenuFields.class.getName();
		int width = Frame.WIDTH / 3;
		int x = Frame.WIDTH - width - 1;
		int y = 0;
		int height = Frame.HEIGHT - y - 1;
		int length = fields.size();

		// Background of the Menu
		Renderer.fillRect(x, y, width, height, 0xFFFFFFFF);

		// Black Rectangle around Background
		Renderer.drawRect(x, y, width, height, 0xFF000000);

		// Menu Fields (look above)
		for (int i = 0; i < length; i++) {
			Renderer.drawRect(x, (height / length) * i + y, width, 1, 0xFF000000);
			Renderer.drawText(fields.get(i).getName(), x + 50, (height / length) * i + (height / length / 2) - 12, 25, 0xFF0000FF);
		}

		// Rectangle around selected Menu Field
		Renderer.drawRect(x - 1, Math.max(-1, (height / length) * index + 1 + y), width + 1, height / length - 2, 0xFFFF00FF);
		Renderer.drawRect(x - 0, Math.max(-1, (height / length) * index + 0 + y), width + 0, height / length - 0, 0xFFFF00FF);
		Renderer.drawRect(x + 1, Math.max(-1, (height / length) * index - 1 + y), width - 1, height / length + 2, 0xFFFF00FF);
	}

	public void run(int index) {
		fields.get(index).performAction();
	}
}