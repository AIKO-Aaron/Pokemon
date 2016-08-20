package ch.aiko.pokemon.entity.player;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import ch.aiko.as.ASArray;
import ch.aiko.as.ASDataBase;
import ch.aiko.as.ASObject;
import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.Sprite;
import ch.aiko.engine.sprite.SpriteSheet;
import ch.aiko.modloader.ModLoader;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.basic.ModUtils;
import ch.aiko.pokemon.basic.PokemonEvents;
import ch.aiko.pokemon.entity.Direction;
import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.entity.trainer.Trainer;
import ch.aiko.pokemon.fight.Fight;
import ch.aiko.pokemon.graphics.menu.MenuObject;
import ch.aiko.pokemon.graphics.menu.TextBox;
import ch.aiko.pokemon.level.Level;
import ch.aiko.pokemon.pokemons.PokemonType;
import ch.aiko.pokemon.pokemons.Pokemons;
import ch.aiko.pokemon.pokemons.TeamPokemon;
import ch.aiko.util.FileUtil;

public class Player extends Entity {

	protected int xoff, yoff;
	protected int speed = 6;
	protected SpriteSheet sprites;
	protected int dir = 0; // down, up, left, right
	protected int playerLayer = 0;

	protected Sprite[] walkingAnims = new Sprite[4 * 4];
	protected int anim = 0, curAnim = 0, send = 0;
	protected boolean walking = false;
	public boolean isPaused = false;
	public TeamPokemon[] team = new TeamPokemon[Pokemon.TeamSize];
	public Fight currentFight;
	public ArrayList<Integer> trainersDefeated = new ArrayList<Integer>();

	public static final boolean CAN_WALK_SIDEWAYS = true;

	public static final int PLAYER_RENDERED_LAYER = 10;

	public int getWidth() {
		return sprite.getWidth();
	}

	public int getHeight() {
		return sprite.getHeight();
	}

	@Override
	public int getX() {
		return xPos + xoff;
	}

	@Override
	public int getY() {
		return yPos + yoff;
	}

	public int getRealX() {
		return xPos;
	}

	public int getRealY() {
		return yPos;
	}

	@Override
	public void setPosition(int x, int y) {
		xPos = x;
		yPos = y;
	}

	public void setPositionInLevel(int x, int y) {
		xPos = x - xoff;
		yPos = y - yoff;
	}

	protected Player() {}

	public Player(int x, int y) {
		sprites = new SpriteSheet("/ch/aiko/pokemon/textures/player/player_boy.png", 32, 32).removeColor(0xFF88B8B0);
		for (int i = 0; i < 4 * 4; i++) {
			walkingAnims[i] = sprites.getSprite(i, false);
		}
		xPos = x;
		yPos = y;
	}

	public boolean isTeamEmpty() {
		if (team == null) return true;
		for (TeamPokemon pom : team)
			if (pom != null) return false;
		return true;
	}

	public void save() {
		Pokemon.out.println("Saving");
		try {
			ASDataBase base = new ASDataBase("Player");
			ASObject teamObj = new ASObject("Team");
			for (TeamPokemon pok : team) {
				if (pok != null) {
					pok.reload();
					teamObj.addObject(pok.toObject());
				}
			}
			
			ASObject tdo = new ASObject("TDO");
			int[] td = new int[trainersDefeated.size()];
			for(int i = 0; i< td.length; i++) td[i] = trainersDefeated.size();
			tdo.addArray(ASArray.Integer("TD", td));
			
			base.addObject(teamObj);
			base.addObject(tdo);
			base.saveToFile(FileUtil.getRunningJar().getParent() + "/player.bin");
		} catch (Throwable t) {
			t.printStackTrace(Pokemon.out);
		}
	}

	@Override
	public void render(Renderer renderer) {
		renderer.setOffset(-xPos, -yPos);
		sprite = walkingAnims[dir * 4 + curAnim];
		xoff = renderer.getWidth() / 2;
		yoff = renderer.getHeight() / 2;
		renderer.drawSprite(sprite, xPos + xoff, yPos + yoff);
	}

	public boolean collides(Level level, int xoff, int yoff) {
		for (int i = 0; i < sprite.getWidth(); i++) {
			for (int j = 0; j < sprite.getHeight(); j++) {
				if (level.isSolid(xPos + xoff + i, yPos + yoff + j, playerLayer)) { return true; }
			}
		}
		return false;
	}

	public boolean isInside(int xx, int yy) {
		int width = sprite == null ? 32 : sprite.getWidth();
		int height = sprite == null ? 32 : sprite.getHeight();
		return xx > getX() && xx < getX() + width && yy > getY() && yy < getY() + height;
	}

	@Override
	public void update(Screen screen, Layer l) {
		if (isPaused) return;
		int xx = 0, yy = 0;

		Level level = (Level) getParent();

		if (input.popKeyPressed(KeyEvent.VK_X)) {
			PlayerMenu menu = (PlayerMenu) level.openMenu(new PlayerMenu(screen));
			ModLoader.performEvent(new PokemonEvents.PlayerOpenMenuEvent(menu));
		}

		if (input.isKeyPressed(KeyEvent.VK_LEFT)) xx--;
		if (input.isKeyPressed(KeyEvent.VK_RIGHT)) xx++;
		if (input.isKeyPressed(KeyEvent.VK_UP)) yy--;
		if (input.isKeyPressed(KeyEvent.VK_DOWN)) yy++;

		if (!CAN_WALK_SIDEWAYS) {
			if (xx != 0) yy = 0;
		}

		if (xx == 0 && yy == 0) {
			walking = false;
			anim = 0;
			curAnim = 0;
		} else {
			int xspeed = xx * speed;
			int yspeed = yy * speed;

			dir = xspeed > 0 ? 3 : xspeed < 0 ? 2 : yspeed < 0 ? 1 : yspeed > 0 ? 0 : dir;

			if (collides(level, screen.getRenderer().getWidth() / 2 + xspeed, screen.getRenderer().getHeight() / 2 + yspeed)) {
				while (collides(level, screen.getRenderer().getWidth() / 2 + xspeed, screen.getRenderer().getHeight() / 2) && xspeed != 0)
					xspeed -= xx;
				while (collides(level, screen.getRenderer().getWidth() / 2, screen.getRenderer().getHeight() / 2 + yspeed) && yspeed != 0)
					yspeed -= yy;
			}

			dir = xspeed > 0 ? 3 : xspeed < 0 ? 2 : yspeed < 0 ? 1 : yspeed > 0 ? 0 : dir;

			boolean isSolid = xspeed == 0 && yspeed == 0;
			if (!isSolid) {
				xPos += xspeed;
				yPos += yspeed;
			}
			walking = !isSolid;

			// Just animate when player's walking
			if (walking) {
				anim++;
				if (anim > 15) {
					anim = 0;
					curAnim++;
					curAnim %= 4;
				}
			} else curAnim = anim = 0;
		}

		send++;
		send %= 30;

		if (Pokemon.client != null && walking) {
			Pokemon.client.sendText("/spos/" + xPos + "/" + yPos + "/" + dir);
			Pokemon.client.sendText("/slvl/" + level.path);
		} else if (Pokemon.client != null && send == 0) {
			// Pokemon.client.ifNotSet("/spos/" + xPos + "/" + yPos + "/" + dir);
		}
	}

	public void startBattle(Screen screen, Trainer t) {
		screen.addLayer(new TextBox(t.name + ": " + t.battletext, (MenuObject sender) -> currentFight = (Fight) screen.addLayer(new Fight(screen, this, t)), true));
	}

	public boolean isMoving() {
		return walking;
	}

	public void setPaused(boolean b) {
		isPaused = b;
	}

	public void setDirection(int dir2) {
		dir = dir2;
	}

	public void setDirection(Direction dir2) {
		dir = dir2.getDir();
	}

	public int getDirection() {
		return dir;
	}

	@Override
	public String getName() {
		return "Player";
	}

	@Override
	public int getLevel() {
		return PLAYER_RENDERED_LAYER;
	}

	public void winBattle(Trainer ct) {
		Screen screen = Pokemon.getScreen();
		trainersDefeated.add(ct.getID());
		screen.removeLayer(currentFight);
		screen.addLayer(new TextBox(ct.losttext, (b)->{}, true));
	}
	
	public void lostBattle(Trainer ct) {
		getScreen().removeLayer(currentFight);
		getScreen().addLayer(new TextBox(ct.wintext, (b)->{}, true));
	}

	public void load() {
		ASDataBase base = ASDataBase.createFromFile(FileUtil.getRunningJar().getParent() + "/player.bin");
		if (base != null) {
			ASObject teamObj = base.getObject("Team");
			int index = 0;
			if (team != null) {
				for (int i = 0; i < teamObj.objectCount; i++) {
					ASObject obj = teamObj.objects.get(i);
					if (obj != null) team[index++] = new TeamPokemon(obj);
					System.out.println("Added: " + team[index - 1].getNickName());
				}
			}
			if (teamObj == null || index == 0) {
				team[0] = new TeamPokemon(Pokemons.get(6), PokemonType.OWNED, "Exterminator", ModUtils.convertToAttacks("Tackle", "Verzweifler"), 5, 10, 10, 10, 10, 10, 10, 10);
				System.out.println("Team empty...");
			}

			ASObject tdo = base.getObject("TDO");
			if (tdo != null) {
				int[] td = tdo.getArray("TD").getIntData();
				trainersDefeated = new ArrayList<Integer>();
				for (int i : td)
					trainersDefeated.add(i);
			}
		} else {
			System.err.println("No player save found...");
			team[0] = new TeamPokemon(Pokemons.get(6), PokemonType.OWNED, "Exterminator", ModUtils.convertToAttacks("Tackle", "Verzweifler"), 5, 10, 10, 10, 10, 10, 10, 10);
		}
	}

	public int getTeamLength() {
		int length = 0;
		for (TeamPokemon pok : team)
			if (pok != null) ++length;
		return length;
	}

}
