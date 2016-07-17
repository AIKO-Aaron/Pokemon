package ch.aiko.pokemon.fight;

import java.util.Stack;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.LayerContainer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.graphics.menu.Menu;

public class Fight extends LayerContainer {

	public Stack<Layer> openMenus = new Stack<Layer>();
	
	public Fight(Screen s) {
		openMenu(new FightMenu(s));
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
