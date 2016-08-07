package ch.aiko.pokemon.entity.trainer;

import java.util.Random;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.Sprite;
import ch.aiko.engine.sprite.SpriteSheet;
import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.entity.player.Player;
import ch.aiko.pokemon.level.Level;
import ch.aiko.pokemon.pokemons.TeamPokemon;

public class Trainer extends Entity {

	public TeamPokemon[] team = new TeamPokemon[6];
	public String name, battletext, inbattletext, wintext, losttext;
	public boolean inbattle;
	public int dir, sight;
	public int spinFunc;
	public int spinTime;
	private int spinTimer = 0;

	protected SpriteSheet sprites;
	protected Sprite[] walkingAnims = new Sprite[4 * 4];

	public Trainer() {
		sprites = new SpriteSheet("/ch/aiko/pokemon/textures/player/player_boy.png", 32, 32).removeColor(0xFF88B8B0);
		for (int i = 0; i < 4 * 4; i++) {
			walkingAnims[i] = sprites.getSprite(i, false);
		}
	}

	@Override
	public void render(Renderer renderer) {
		Level level = (Level) renderer.getScreen().getTopLayer("Level");
		sprite = walkingAnims[dir * 4];
		renderer.drawSprite(sprite, xPos, yPos);
		int xdistance = sight * level.fieldWidth;
		int ydistance = sight * level.fieldHeight;
		renderer.drawRect(xPos + (dir == 2 ? -xdistance : dir == 3 ? sprite.getWidth() : 0), yPos + (dir == 1 ? -ydistance : dir == 0 ? sprite.getHeight() : 0), dir == 2 || dir == 3 ? xdistance : sprite.getWidth(), dir == 0 || dir == 1 ? ydistance : sprite.getHeight(), 0xFFFF00FF);
	}

	@Override
	public void update(Screen s, Layer l) {
		Level level = (Level) s.getTopLayer("Level");
		Player holder = (Player) (level).getTopLayer("Player");
		if (holder != null) {
			int x = holder.getX();
			int y = holder.getY();
			int w = holder.getWidth();
			int h = holder.getHeight();
			int xdistance = sight * level.fieldWidth;
			int ydistance = sight * level.fieldHeight;
			if (x + w + (dir == 2 ? xdistance : 0) > xPos && x < xPos + sprite.getWidth() + (dir == 3 ? xdistance : 0) && y + h + (dir == 1 ? ydistance : 0) > yPos && y < yPos + sprite.getHeight() + (dir == 0 ? ydistance : 0)) {
				holder.startBattle(s, this);
			}
		}

		if (spinFunc == 1) { // random spinning
			spinTimer++;
			if (spinTimer > spinTime) {
				dir = new Random().nextInt(4);
				spinTimer = 0;
			}
		} else if (spinFunc == 2) { // timed spinning
			spinTimer++;
			if (spinTimer > spinTime) {
				dir = (dir + 1) % 4;
				spinTimer = 0;
			}
		} else if (spinFunc == 3) { // Follow player
			int xv = xPos - holder.getX();
			int yv = yPos - holder.getY();
			dir = (Math.abs(xv) < Math.abs(yv)) ? (xv > 0 ? 3 : 2) : (yv > 0 ? 1 : 0);
		}
	}

}
