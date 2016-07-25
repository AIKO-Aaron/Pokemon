package ch.aiko.pokemon.pokemons;

import java.awt.event.KeyEvent;

import ch.aiko.as.ASDataType;
import ch.aiko.as.ASField;
import ch.aiko.as.ASObject;
import ch.aiko.as.ASString;
import ch.aiko.as.SerializationReader;
import ch.aiko.engine.graphics.Renderable;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.graphics.Updatable;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.graphics.GIFAnimation;

public class TeamPokemon extends ASDataType implements Renderable, Updatable {

	public static final float SCALE = 2F;
	
	protected GIFAnimation animation;
	protected Pokemons type;
	protected String nickname;
	protected int healthPoints;
	protected int attack;
	protected int specAttack;
	protected int defense;
	protected int specDefense;
	protected int speed;
	protected int level;
	protected int xp;

	public TeamPokemon(ASObject obj1) {
		init(obj1);
	}

	public TeamPokemon(Pokemons type, String nickname, int atk, int satk, int def, int sdef, int speed, int xp) {
		this.name = "Pok";
		this.type = type;
		this.nickname = nickname;
		attack = atk;
		specAttack = satk;
		defense = def;
		specDefense = sdef;
		this.speed = speed;
		this.xp = xp;
		level = (int) Math.pow(xp, 1 / 3);

		animation = new GIFAnimation(type.getPathToAnimation(), 0, 0, SCALE).replaceColor(0xFFFFFFFF, 0);
	}

	public void load(ASObject c) {
		attack = SerializationReader.readInt(c.getField("ATK").data, 0);
		specAttack = SerializationReader.readInt(c.getField("SAT").data, 0);
		defense = SerializationReader.readInt(c.getField("DFS").data, 0);
		specDefense = SerializationReader.readInt(c.getField("SDF").data, 0);
		level = SerializationReader.readInt(c.getField("SPD").data, 0);
		xp = SerializationReader.readInt(c.getField("XP").data, 0);
		level = (int) Math.pow(xp, 1 / 3);
		healthPoints = SerializationReader.readInt(c.getField("HP").data, 0);
		type = PokeUtil.get(SerializationReader.readInt(c.getField("NUM").data, 0));
		nickname = c.getString("NCN").toString();
		animation = new GIFAnimation(type.getPathToAnimation(), 0, 0, SCALE).replaceColor(0xFFFFFFFF, 0);
	}

	public void getData(ASObject thisObject) {
		thisObject.addField(ASField.Integer("ATK", attack));
		thisObject.addField(ASField.Integer("SAT", specAttack));
		thisObject.addField(ASField.Integer("DFS", defense));
		thisObject.addField(ASField.Integer("SDF", specDefense));
		thisObject.addField(ASField.Integer("SPD", speed));
		thisObject.addField(ASField.Integer("XP", xp));
		thisObject.addField(ASField.Integer("HP", healthPoints));
		thisObject.addField(ASField.Integer("NUM", type.getPokedexNumber()));
		thisObject.addString(ASString.Create("NCN", nickname.toCharArray()));
	}

	public void gainXP(int amount) {
		xp += amount;
		level = (int) Math.pow(xp, 1 / 3);
		if (type.canEvolve(level)) evolve();
	}

	public int getXPToLevel() {
		return (int) Math.pow(level + 1, 3) - xp;
	}

	private void evolve() {
		Pokemon.out.err("I don't do a thing and I'm proud of it!");
	}

	public void render(Renderer renderer) {
		animation.render(renderer, renderer.getWidth() - 250 - animation.getMaxWidth(), (int) (310 - animation.getMaxHeight() * animation.getScale()));
	}

	public void update(Screen screen) {
		animation.update(screen);

		if (screen.popKeyPressed(KeyEvent.VK_N)) {
			type = PokeUtil.get(type.getPokedexNumber() + 1);
			animation = new GIFAnimation(type.getPathToAnimation(), 0, 0).replaceColor(0xFFFFFFFF, 0).replaceColor(0xFF000000, 0);
		}
	}

}
