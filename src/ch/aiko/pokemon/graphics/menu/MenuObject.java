package ch.aiko.pokemon.graphics.menu;

import java.awt.Font;
import java.awt.FontMetrics;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderable;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.graphics.Updatable;
import ch.aiko.pokemon.entity.player.Player;

public abstract class MenuObject extends Layer {

	public final int getStringWidth(Screen s, Font f, String text) {
		FontMetrics metrics = s.getGraphics().getFontMetrics(f);
		return metrics.stringWidth(text);
	}

	public final int getStringHeight(Screen s, Font f) {
		FontMetrics metrics = s.getGraphics().getFontMetrics(f);
		return metrics.getHeight();
	}

	public void render(Renderer r) {}

	public void update(Screen s, Layer l) {}

	public Renderable getRenderable() {
		return (Renderer r) -> render(r);
	}

	public Updatable getUpdatable() {
		return (Screen s, Layer l) -> update(s, l);
	}

	public int getLevel() {
		return Player.PLAYER_RENDERED_LAYER + 1;
	}

	public boolean stopsRendering() {
		return false;
	}

	public boolean stopsUpdating() {
		return false;
	}

	public String getName() {
		return null;
	}

}
