package ch.aiko.pokemon.graphics;

import ch.aiko.pokemon.Drawer;
import ch.aiko.pokemon.sprite.SpriteSheet;

public class Animation {

	private SpriteSheet sheet;
	private int maxIndex, index, tmpIndex, time;
	
	/**
	 * Tries to load the images from the SpriteSheet. It goes from upper left to the down right corner. 
	 * You have to call drawNext() every Frame. When called, it draws the next image on the 
	 * 
	 * @param pictures
	 */
	public Animation(SpriteSheet pictures, int time) {
		this.sheet = pictures;
		this.maxIndex = pictures.getSpriteCount();
		this.time = time;
	}
	
	public void drawNext(Drawer d, int x, int y) {
		//int xx = index % sheet.getSheetWidth();
		//int yy = index / sheet.getSheetWidth();
		
		d.drawTile(sheet.getSprite(index), x, y);
		
		tmpIndex++;
		
		if(tmpIndex >= time) {
			System.out.println("Showing next Frame");
			
			tmpIndex = 0;
			index++;
			index %= maxIndex;
		}
	}
	
}
