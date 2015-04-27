package ch.aiko.pokemon.graphics.menu;

import java.awt.event.KeyEvent;

import ch.aiko.engine.KeyBoard;
import ch.aiko.engine.Menu;
import ch.aiko.engine.Renderer;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.mob.player.Player;
import ch.aiko.pokemon.pokemon.Pokemon;

public class PokemonMenu extends Menu {

	private Player p;
	private int index;

	public PokemonMenu(Player p) {
		this.p = p;
	}

	public void onOpen() {
		p.setPaused(true);
	}

	public void onClose() {
		p.setPaused(false);
	}

	public void draw() {
		Renderer.fillRect(0, 0, Frame.WIDTH, Frame.HEIGHT, 0xFF000000);
		int x = (Frame.WIDTH - Pokemon.WIDTH) / 2 - Pokemon.WIDTH / 2;
		int y = (Frame.HEIGHT - Pokemon.HEIGHT) / 2 - Pokemon.HEIGHT;

		for (int i = 0; i < p.getTeamSize(); i++) {
			Renderer.drawImage(x + ((i % 2) * p.getPokemon(i).getSprite().getWidth()), y + ((i / 2) * p.getPokemon(i).getSprite().getHeight()), p.getPokemon(i).getSprite().getImage());
		}
		int w = p.getPokemon(index).getSprite().getWidth();
		int h = p.getPokemon(index).getSprite().getWidth();
		Renderer.drawRect(x + (index % 2) * w, y + (index / 2) * h, w, h, 0xFFFF00FF);
	}

	public void update(double d) {
		if (KeyBoard.getTimesPressed(KeyEvent.VK_X) > 0) Frame.closeMenu();

		if (KeyBoard.getTimesPressed(KeyEvent.VK_UP) > 0) index = (index - 1 + 6) % p.getTeamSize();
		if (KeyBoard.getTimesPressed(KeyEvent.VK_DOWN) > 0) index = (index + 1 + 6) % p.getTeamSize();
	}
	
	public String name() {
		return "PokemonSelection";
	}

}
