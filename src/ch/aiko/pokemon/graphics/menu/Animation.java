package ch.aiko.pokemon.graphics.menu;

import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.SpriteSheet;

public class Animation extends Menu {

	protected boolean repeat = false;
	protected SpriteSheet sheet;
	protected int time, currentAnim;
	protected int timePerFrame = 30;
	protected int xpos, ypos;

	public Animation(Screen parent, SpriteSheet pts, boolean repeated, int timePerFrame) {
		super(parent);
		x_for_close = false;
		manage_buttons = false;
		this.sheet = pts;
		this.repeat = repeated;
		this.timePerFrame = timePerFrame;
	}

	public Animation(Screen parent, SpriteSheet pts, boolean repeated) {
		super(parent);
		x_for_close = false;
		manage_buttons = false;
		this.sheet = pts;
		this.repeat = repeated;
	}

	public Animation setPosition(int x, int y) {
		xpos = x;
		ypos = y;
		return this;
	}

	public Animation setRepeated(boolean b) {
		repeat = b;
		return this;
	}

	public Animation setTimePerFrame(int tpf) {
		timePerFrame = tpf;
		return this;
	}

	public int getPosX() {
		return xpos;
	}

	public int getPosY() {
		return ypos;
	}

	public boolean isRepeated() {
		return repeat;
	}

	public SpriteSheet getSheet() {
		return sheet;
	}

	public int getTimePerFrame() {
		return timePerFrame;
	}

	public void onOpen() {}

	public void onClose() {}

	public boolean stopsUpdating() {
		return false;
	}

	public void renderMenu(Renderer r) {
		r.drawSprite(sheet.getSprite(currentAnim), xpos, ypos);
	}

	public void updateMenu(Screen s) {
		time++;
		if (time >= timePerFrame) {
			time = 0;
			currentAnim = (currentAnim + 1) % sheet.getSpriteCount();
			if (!repeat && currentAnim == 0) closeMe();
		}
	}

	public String getName() {
		return "Animation[" + hashCode() + "]";
	}

}
