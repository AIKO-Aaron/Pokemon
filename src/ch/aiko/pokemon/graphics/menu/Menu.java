package ch.aiko.pokemon.graphics.menu;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.LayerContainer;
import ch.aiko.engine.graphics.Renderable;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.graphics.Updatable;
import ch.aiko.pokemon.entity.Player;

public abstract class Menu extends Layer implements Renderable, Updatable {

	protected Screen parent;
	protected Player holder;

	public Menu(Screen parent) {
		this.parent = parent;
		this.holder = (Player) ((LayerContainer) parent.getTopLayer("Level")).getTopLayer("Player").getRenderable();
	}

	public void layerRender(Renderer r) {
		r.setOffset(0, 0);
	}

	/**
	 * Gets the level of the current layer. The player gets rendered on layer 1 so yeah... probably over 1
	 */
	public int getLevel() {
		return Player.PLAYER_RENDERED_LAYER + 1;
	}

	public Renderable getRenderable() {
		return this;
	}

	public Updatable getUpdatable() {
		return this;
	}

	public void render(Renderer r) {}

	public void update(Screen s) {}

	public abstract void onOpen();

	public abstract void onClose();

}
