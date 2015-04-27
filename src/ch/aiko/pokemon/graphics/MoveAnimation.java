package ch.aiko.pokemon.graphics;

import ch.aiko.engine.Renderer;
import ch.aiko.pokemon.sprite.SpriteSheet;

public class MoveAnimation extends Animation {

	private int destx, desty, xOff, yOff, color, speed = 1, time = 0;
	protected boolean doesStay, xF, yF, timer;

	public MoveAnimation(SpriteSheet pictures, int time, int destx, int desty, boolean b) {
		super(pictures, time, false);
		this.doesStay = b;
		this.destx = destx;
		this.desty = desty;
		this.setColor(0xFF000000);
	}

	public MoveAnimation(SpriteSheet pictures, int time, int destx, int desty, boolean b, int color) {
		super(pictures, time, false);
		this.doesStay = b;
		this.destx = destx;
		this.desty = desty;
		this.setColor(color);
	}

	public void drawNext(int x, int y) {
		if (x + xOff == destx && y + yOff == desty && !doesStay && (timer ? index + 1 >= sheet.getSpriteCount() : true)) {
			finished = true;
		}

		if (x + this.xOff != destx && !xF) {
			this.xOff += (this.xOff + x) - destx < 0 ? 1 * Math.min(speed, Math.abs((this.xOff + x) - destx)) : -1 * Math.min(speed, Math.abs((this.xOff + x) - destx));
		} else if (!xF) {
			xF = true;
		}

		if (y + this.yOff != desty && !yF) {
			this.yOff += (this.yOff + y) - desty < 0 ? 1 * Math.min(speed, Math.abs((this.yOff + y) - desty)) : -1 * Math.min(speed, Math.abs((this.yOff + y) - desty));
		} else if (!yF) {
			yF = true;
		}

		x += this.xOff * Frame.delta;
		y += this.yOff * Frame.delta;

		Renderer.drawImage(x, y, sheet.getSprite(index).getImage());

		if (time > 0) {
			time--;
			return;
		} else if (timer && (!xF || !yF)) return;
		else tmpIndex++;

		if (tmpIndex >= time) {
			tmpIndex = 0;
			index++;
			if (index == maxIndex) index--;
		}
	}

	public void setSpeed(int newSpeed) {
		this.speed = Math.abs(newSpeed);
	}

	public void setStartTime(int time) {
		this.time = time;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setStartingTime(boolean b) {
		timer = b;
	}

}
