package ch.aiko.pokemon.graphics;

import ch.aiko.pokemon.sprite.SpriteSheet;

public class MoveAnimation extends Animation {

	private int destx, desty, xOff, yOff, color, speed = 1;
	protected boolean doesStay, xF, yF;

	public MoveAnimation(SpriteSheet pictures, int time, int destx, int desty, boolean b) {
		super(pictures, time, false);
		this.doesStay = b;
		this.destx = destx;
		this.desty = desty;
		this.color = 0xFF000000;
	}

	public MoveAnimation(SpriteSheet pictures, int time, int destx, int desty, boolean b, int color) {
		super(pictures, time, false);
		this.doesStay = b;
		this.destx = destx;
		this.desty = desty;
		this.color = color;
	}

	public void drawNext(Drawer d, int x, int y) {
		if (x + xOff == destx && y + yOff == desty && !doesStay) {
			finished = true;
		}

		if (x + this.xOff != destx && !xF) {
			this.xOff += speed;
		} else if (!xF) {
			xF = true;
		}

		if (y + this.yOff != desty && !yF) {
			this.yOff += speed;
		} else if (!yF) {
			yF = true;
		}

		x += this.xOff;
		y += this.yOff;

		
		d.drawTile(sheet.getSprite(index), x, y, color);

		tmpIndex++;

		if (tmpIndex >= time) {
			tmpIndex = 0;
			index++;
			if (index == maxIndex) index--;
		}
	}

	public void setSpeed(int newSpeed) {
		this.speed = newSpeed;
	}
	
}
