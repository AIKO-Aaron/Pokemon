package ch.aiko.pokemon.graphics;

import ch.aiko.pokemon.sprite.SpriteSheet;

public class Animation {

	private SpriteSheet sheet;
	private int maxIndex, index, tmpIndex, time;
	private boolean repeat = true;
	private boolean finished;
	
	/**
	 * Tries to load the images from the SpriteSheet. It goes from upper left to the down right corner. 
	 * You have to call drawNext() every Frame. When called, it draws the next image on the 
	 * 
	 * @param pictures The SpriteSheet, which has all Images, which belong to the animation
	 */
	public Animation(SpriteSheet pictures, int time) {
		this.sheet = pictures;
		this.maxIndex = pictures.getSpriteCount();
		this.time = time;
	}
	
	public Animation(SpriteSheet pictures, int time, boolean repeat) {
		this.sheet = pictures;
		this.maxIndex = pictures.getSpriteCount();
		this.time = time;
		this.repeat = repeat;
	}
	
	public void drawNext(Drawer d, int x, int y) {
		if(finished) {
			System.err.println("No more Frames to draw");
			return;
		}
		
		d.drawTile(sheet.getSprite(index), x, y);
		
		if(sheet.getSprite(index).getY() == 1024) System.err.println("dhelp");
		
		//System.out.println(sheet.getSprite(index).getX() + ":"+ sheet.getSprite(index).getY());
		
		tmpIndex++;
		
		if(tmpIndex >= time) {			
			tmpIndex = 0;
			index++;
			if(!repeat && index == maxIndex) {finished = true; return;}
			index %= maxIndex;			
		}
	}
	
	public boolean isFinished() {
		return finished;
	}
}
