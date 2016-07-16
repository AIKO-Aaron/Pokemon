package ch.aiko.pokemon.graphics.menu;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderable;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.graphics.Updatable;
import ch.aiko.pokemon.entity.Player;
import ch.aiko.pokemon.level.Level;

public abstract class Menu extends Layer implements Renderable, Updatable {

	protected Screen parent;
	protected Player holder;
	protected Level level;

	public Menu(Screen parent) {
		this.parent = parent;
		this.level = (Level) parent.getTopLayer("Level");
		this.holder = (Player) (level).getTopLayer("Player").getRenderable();
	}

	/**
	 * Gets the level of the current layer. The player gets rendered on layer 1 so yeah... probably over 1
	 */
	public int getLevel() {
		return Player.PLAYER_RENDERED_LAYER + 1;
	}
	
	public boolean stopsRendering() {
		return false;
	}

	public boolean stopsUpdating() {
		return true;
	}

	public Renderable getRenderable() {
		return this;
	}

	public Updatable getUpdatable() {
		return this;
	}

	public final void render(Renderer r) {
		int x = r.getXOffset();
		int y = r.getYOffset();
		r.setOffset(0, 0);
		renderMenu(r);
		r.setOffset(x, y);
	}

	public final void update(Screen s) {
		updateMenu(s);
	}
	
	public void closeMe() {
		level.closeMenu(this);
	}

	public abstract void onOpen();

	public abstract void onClose();

	public abstract void renderMenu(Renderer r);

	public abstract void updateMenu(Screen s);

}
