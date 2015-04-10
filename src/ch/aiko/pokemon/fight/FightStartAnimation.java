package ch.aiko.pokemon.fight;

import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.graphics.MoveAnimation;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.pokemon.sprite.SpriteSheet;
import ch.aiko.util.ImageUtil;

public class FightStartAnimation extends MoveAnimation {

	public static Sprite grass_day = new Sprite(ImageUtil.loadImageInClassPath("/ch/aiko/pokemon/textures/fight_ground/grass_day.png"));
	
	//256, 128
	
	public FightStartAnimation() {
		super(new SpriteSheet("/ch/aiko/pokemon/textures/fight_ground/grass_day.png", 256, 128, Pokemon.frame.getWidth(), Pokemon.frame.getHeight()), 60, 0, 0, false);
		setSpeed(10);
	}

}
