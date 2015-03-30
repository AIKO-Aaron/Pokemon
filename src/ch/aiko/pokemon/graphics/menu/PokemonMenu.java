package ch.aiko.pokemon.graphics.menu;

import java.awt.event.KeyEvent;

import ch.aiko.pokemon.graphics.Drawer;
import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.pokemon.Pokemon;

public class PokemonMenu extends Menu {

	private Player p;
	private int index;

	public PokemonMenu(Player p) {
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
		int x = (d.getFrame().getWidth() - Pokemon.WIDTH) / 2 - Pokemon.WIDTH / 2;
		int y = (d.getFrame().getHeight() - Pokemon.HEIGHT) / 2 - Pokemon.HEIGHT;

		for (int i = 0; i < p.getTeamSize(); i++) {
			d.drawTile(p.getPokemon(i).getSprite(), x + ((i % 2) * p.getPokemon(i).getSprite().getWidth()), y + ((i / 2) * p.getPokemon(i).getSprite().getHeight()));
		}
		int w = p.getPokemon(index).getSprite().getWidth();
		int h = p.getPokemon(index).getSprite().getWidth();
		d.drawRect(x + (index % 2) * w, y + (index / 2) * h + 22, w, h, 0xFFFF00FF);
	}

	public void update(Drawer d) {
		if (d.getFrame().getTimesPressed(KeyEvent.VK_X) > 0) d.getFrame().closeMenu();

		if (d.getFrame().getTimesPressed(KeyEvent.VK_UP) > 0) index = (index - 1 + 6) % p.getTeamSize();
		if (d.getFrame().getTimesPressed(KeyEvent.VK_DOWN) > 0) index = (index + 1 + 6) % p.getTeamSize();
	}
	
	public String name() {
		return "PokemonSelection";
	}

}
