package ch.aiko.pokemon.fight;

import ch.aiko.pokemon.graphics.Animation;
import ch.aiko.pokemon.sprite.SpriteSheet;
import ch.aiko.util.AudioUtil;

public class FightStartAnimation extends Animation {

	public FightStartAnimation() {
		super(new SpriteSheet("/ch/aiko/pokemon/textures/TrainerSprites.png", 32, 32), 60);
		
		AudioUtil.playSound("ch/aiko/pokemon/sounds/fightOpening.mp3");
	}
	
	

}
