package ch.aiko.pokemon.fight;

import java.util.Stack;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.LayerContainer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.Sprite;
import ch.aiko.engine.sprite.SpriteSheet;
import ch.aiko.pokemon.graphics.menu.Animation;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.pokemon.sound.SoundPlayer;

public class Fight extends LayerContainer {

	public Stack<Layer> openMenus = new Stack<Layer>();
	public Sprite background;
	public Screen s;

	public Fight(Screen s) {
		this.s = s;
		background = new Sprite("/ch/aiko/pokemon/textures/fight_background/grass_day.png").getScaledInstance(s.getFrameWidth(), s.getFrameHeight());
	}

	public void onOpen() {
		openMenu(new FightMenu(s));
		openMenu(new Animation(s, new SpriteSheet("/ch/aiko/pokemon/textures/player/player_fight_boy.png", 80, 80, 200, 200).removeColor(0xFF88B8B0), false, 7).setPosition(0, 0));
		// SoundPlayer.playSound("/ch/aiko/pokemon/sounds/TrainerFight.mp3"); // why not? Because I'm testing and it's annoying...
	}

	public int getLevel() {
		return 1;
	}

	public boolean stopsRendering() {
		return true;
	}

	public boolean stopsUpdating() {
		return true;
	}

	public void layerRender(Renderer r) {
		r.drawSprite(background, 0, 0);
	}

	public String getName() {
		return "Fight";
	}

	public void openMenu(Menu menu) {
		menu.onOpen();
		openMenus.add(addLayer(menu));
	}

	public void closeTopMenu() {
		Layer topLayer = openMenus.pop();
		((Menu) topLayer).onClose();
		removeLayer(topLayer);
	}

	public void closeAllMenus() {
		while (openMenus.isEmpty())
			closeTopMenu();
	}

	public void closeMenu(Menu m) {
		openMenus.remove(m);
		m.onClose();
		removeLayer(m);
	}

}
