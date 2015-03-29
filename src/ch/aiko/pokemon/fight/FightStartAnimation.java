package ch.aiko.pokemon.fight;

import ch.aiko.pokemon.graphics.Animation;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.pokemon.sprite.SpriteSheet;
import ch.aiko.util.ImageUtil;

public class FightStartAnimation extends Animation {

	public static Sprite grass_day = new Sprite(ImageUtil.loadImageInClassPath("/ch/aiko/pokemon/textures/fight_ground/grass_day.png"));
	
	public FightStartAnimation() {
		super(new SpriteSheet("/ch/aiko/pokemon/textures/test.png", 16, 16), 60, false);
		
		//AudioUtil.playSound("ch/aiko/pokemon/sounds/fightOpening.mp3");
	}

}
