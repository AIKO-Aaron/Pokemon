package ch.aiko.pokemon.graphics.menu;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.LayerContainer;
import ch.aiko.engine.graphics.Renderable;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.graphics.Updatable;
import ch.aiko.pokemon.entity.player.Player;
import ch.aiko.pokemon.fight.Fight;
import ch.aiko.pokemon.level.Level;

public abstract class Menu extends LayerContainer implements Renderable, Updatable {

	protected int xOffset, yOffset;
	protected Screen parent;
	protected Player holder;
	protected Level level;
	protected Fight fight;
	protected int index, mouseSel;
	protected boolean x_for_close = true;
	protected boolean manage_buttons = true;

	protected ArrayList<Layer> buttons = new ArrayList<Layer>();

	public Menu(Screen parent) {
		resetOffset = false;
		this.parent = parent;
		this.level = (Level) parent.getTopLayer("Level");
		this.fight = (Fight) parent.getTopLayer("Fight");
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

	public void addButton(Button b, int layer, int index) {
		while (buttons.size() <= index) {
			buttons.add(null);
		}
		buttons.set(index, addLayer(b.toLayer(layer)));
	}

	public Button getButton(int index) {
		return (Button) buttons.get(index).getRenderable();
	}

	public void removeButton(Button b) {
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i).getRenderable() == b) {
				if (index > i) index--;
				removeLayer(buttons.get(i));
				buttons.remove(i);
				i--;
			}
		}
	}

	public void removeAllButtons() {
		for (Layer b : buttons)
			removeLayer(b);
		buttons.clear();
		index = 0;
	}

	private int x, y;

	public final void layerRender(Renderer r) {
		x = r.getXOffset();
		y = r.getYOffset();
		r.setOffset(xOffset, yOffset);
		renderMenu(r);
	}

	public final void postRender(Renderer r) {
		r.setOffset(x, y);
	}

	public final void layerUpdate(Screen s) {
		if (x_for_close && s.popKeyPressed(KeyEvent.VK_X)) closeMe();

		if (manage_buttons && buttons.size() > 0) {
			Point pos = s.getMousePosition();
			int mx = pos == null ? s.getMouseXInFrame() : pos.x;
			int my = pos == null ? s.getMouseYInFrame() : pos.y;

			if (s.popMouseKey(MouseEvent.BUTTON1)) if (((Button) buttons.get(index).getRenderable()).isInside(mx, my)) ((Button) buttons.get(index).getRenderable()).buttonPressed();

			if (s.popKeyPressed(KeyEvent.VK_SPACE)) ((Button) buttons.get(index).getRenderable()).buttonPressed();

			if (s.popKeyPressed(KeyEvent.VK_DOWN)) index = (index + 1) % buttons.size();
			if (s.popKeyPressed(KeyEvent.VK_UP)) index = index > 0 ? (index - 1) : buttons.size() - 1;

			for (int i = 0; i < buttons.size(); i++)
				if (i != mouseSel && ((Button) buttons.get(i).getRenderable()).isInside(mx, my)) mouseSel = index = i;

			for (int i = 0; i < buttons.size(); i++)
				((Button) buttons.get(i).getRenderable()).setSelected(i == index);
		}

		updateMenu(s);
	}

	public void closeMe() {
		if (level != null) level.closeMenu(this);
		if (fight != null) fight.closeMenu(this);
	}

	public abstract void onOpen();

	public abstract void onClose();

	public abstract void renderMenu(Renderer r);

	public abstract void updateMenu(Screen s);

}
