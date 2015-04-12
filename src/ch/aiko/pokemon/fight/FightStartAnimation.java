package ch.aiko.pokemon.fight;

import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.graphics.MoveAnimation;
import ch.aiko.pokemon.sprite.SpriteSheet;

public class FightStartAnimation extends MoveAnimation {		
	public FightStartAnimation(String s, int w, int h) {
		super(new SpriteSheet(s, w, h, Frame.WIDTH, Frame.HEIGHT), 60, 0, 0, false, 0x00000000);
		setSpeed(25 * (Pokemon.frame.getWidth() / Frame.WIDTH));
	}
}
