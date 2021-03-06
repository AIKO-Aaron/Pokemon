package ch.aiko.pokemon.pokemons;

import ch.aiko.as.ASDataType;
import ch.aiko.as.ASField;
import ch.aiko.as.ASObject;
import ch.aiko.as.ASString;
import ch.aiko.as.SerializationReader;
import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderable;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.graphics.Updatable;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.attacks.Attack;
import ch.aiko.pokemon.attacks.AttackUtil;
import ch.aiko.pokemon.graphics.GIFAnimation;

public class TeamPokemon extends ASDataType implements Renderable, Updatable {

	protected GIFAnimation animation;

	protected PokemonType holder;
	protected Pokemons type;
	protected String nickname;
	private Attack[] moveSet;
	protected PokemonState currentState;

	protected float damageToDeal = 0;

	// STATS
	protected float healthPoints;
	protected int maxHP;
	protected int attack;
	protected int specAttack;
	protected int defense;
	protected int specDefense;
	protected int speed;
	protected int level;
	protected int xp;

	public int yOff;
	public boolean down, isDown;

	public TeamPokemon(ASObject obj1) {
		init(obj1);
	}

	public TeamPokemon(Pokemons type, PokemonType holder, String nickname, Attack[] moveSet, int hp, int maxHP, int atk, int satk, int def, int sdef, int speed, int xp) {
		this.name = "Pok";
		this.healthPoints = hp;
		this.maxHP = maxHP;
		this.type = type;
		this.holder = holder;
		this.nickname = nickname;
		attack = atk;
		specAttack = satk;
		defense = def;
		specDefense = sdef;
		this.speed = speed;
		this.xp = xp;
		level = (int) Math.pow(xp, 1F / 3F);
		this.setMoveSet(moveSet);
		currentState = PokemonState.NORMAL;

		animation = new GIFAnimation(PokeUtil.getAnimation(type, holder), 0, 0);
	}

	public void load(ASObject c) {
		attack = SerializationReader.readInt(c.getField("ATK").data, 0);
		specAttack = SerializationReader.readInt(c.getField("SAT").data, 0);
		defense = SerializationReader.readInt(c.getField("DFS").data, 0);
		specDefense = SerializationReader.readInt(c.getField("SDF").data, 0);
		speed = SerializationReader.readInt(c.getField("SPD").data, 0);
		xp = SerializationReader.readInt(c.getField("XP").data, 0);
		healthPoints = SerializationReader.readInt(c.getField("HP").data, 0);
		maxHP = SerializationReader.readInt(c.getField("MHP").data, 0);
		type = PokeUtil.get(SerializationReader.readInt(c.getField("NUM").data, 0));
		holder = PokeUtil.getType(SerializationReader.readInt(c.getField("TYP").data, 0));
		currentState = PokeUtil.getState(SerializationReader.readInt(c.getField("STATE").data, 0));
		nickname = c.getString("NCN").toString();
		ASObject atks = c.getObject("ATKS");
		if (atks != null) {
			moveSet = (new Attack[atks.stringCount]);
			for (int i = 0; i < getMoveSet().length; i++) {
				moveSet[i] = AttackUtil.getAttack(atks.strings.get(i).toString());
			}
		} else moveSet = new Attack[] { AttackUtil.getAttack("Tackle"), AttackUtil.getAttack("Flammenwurf") };
		level = (int) Math.pow(xp, 1F / 3F);
		animation = new GIFAnimation(PokeUtil.getAnimation(type, holder), 0, 0);
	}

	public void getData(ASObject thisObject) {
		thisObject.addField(ASField.Integer("ATK", attack));
		thisObject.addField(ASField.Integer("SAT", specAttack));
		thisObject.addField(ASField.Integer("DFS", defense));
		thisObject.addField(ASField.Integer("SDF", specDefense));
		thisObject.addField(ASField.Integer("SPD", speed));
		thisObject.addField(ASField.Integer("XP", xp));
		thisObject.addField(ASField.Integer("HP", (int) healthPoints));
		thisObject.addField(ASField.Integer("MHP", maxHP));
		thisObject.addField(ASField.Integer("NUM", type.getPokedexNumber()));
		thisObject.addField(ASField.Integer("TYP", holder.in));
		thisObject.addField(ASField.Integer("STATE", currentState == null ? PokemonState.NORMAL.getID() : currentState.getID()));
		thisObject.addString(ASString.Create("NCN", nickname.toCharArray()));
		ASObject atks = new ASObject("ATKS");
		for (Attack a : getMoveSet()) {
			if (a != null) atks.addString(ASString.Create("MOVE", a.attackName));
		}
		thisObject.addObject(atks);
	}

	public void gainXP(int amount) {
		if (xp >= 1000000) return;
		xp += amount;
		if (xp >= 1000000) xp = 1000000;
		int ol = level;
		level = (int) Math.pow(xp, 1F / 3F);
		if (level > ol) System.out.println("Level up to: " + level);
		if (type.canEvolve(level)) evolve();
	}

	public int getXPToLevel() {
		return (int) Math.pow(level + 1, 3) - xp;
	}

	private void evolve() {
		int t = type.getEvolvesInto();
		if (t < 0 || t > PokeUtil.pokemons.size()) Pokemon.out.err("I don't do a thing and I'm proud of it!");
		else {
			setType(PokeUtil.get(t));
		}
	}

	@Override
	public void render(Renderer renderer) {
		int x = holder == PokemonType.OWNED ? 200 : (int) (renderer.getWidth() - 250 - animation.getMaxWidth() / 2);
		int y = holder == PokemonType.OWNED ? renderer.getHeight() - (int) (animation.getMaxHeight() * animation.getScale()) : (int) (310 - animation.getMaxHeight() * animation.getScale());
		animation.render(renderer, x, y + yOff);
	}

	public void setType(Pokemons t) {
		type = t;
		animation = new GIFAnimation(PokeUtil.getAnimation(type, holder), 0, 0);
	}

	public void advance() {
		if (type.isMegaEvolution()) type = PokeUtil.get(type.getChild().getPokedexNumber() + 1);
		else type = PokeUtil.get(type.getPokedexNumber() + 1);
		animation = new GIFAnimation(PokeUtil.getAnimation(type, holder), 0, 0);
	}

	@Override
	public void update(Screen screen, Layer l) {
		animation.update(screen, l);
		if (healthPoints <= 0) ko();
		if (healthPoints > getMaxHP()) healthPoints = getMaxHP();
		if (damageToDeal != 0 && healthPoints > 0) {
			float DAMAGE_PER_UPDATE = 0.01F * (damageToDeal + 0.5F);
			DAMAGE_PER_UPDATE = DAMAGE_PER_UPDATE < 0.2F ? 0.2F : DAMAGE_PER_UPDATE;
			healthPoints -= DAMAGE_PER_UPDATE;
			damageToDeal -= DAMAGE_PER_UPDATE;
		} else if (healthPoints <= 0) {
			damageToDeal = 0;
			healthPoints = 0;
			ko();
		}

		if (down) {
			yOff += 6;
			// int y = holder == PokemonType.OWNED ? screen.getRenderer().getHeight() - (int) (animation.getMaxHeight() * animation.getScale()) : (int) (310 - animation.getMaxHeight() * animation.getScale());
			// y += yOff;
			if (yOff > animation.getMaxHeight() + 100) {
				isDown = true;
			}
		}
	}

	public void ko() {
		currentState = PokemonState.DEFEATED;
	}

	public void mega() {
		if (type.hasMegaEvolution()) {
			int index = 1;
			setType(type.getMegaEvolution(index));
		} else if (type.isMegaEvolution()) {
			Pokemons sub = type.getChild();
			if (sub.getMegaEvolutions().size() > type.getIndex()) setType(sub.getMegaEvolution(type.getIndex() + 1));
		} else System.err.println("This pokmon doesn't have a mega-evolution");
	}

	public void hit(int damage) {
		damageToDeal += damage;
	}

	public String getNickName() {
		return nickname;
	}

	public int getHP() {
		return (int) healthPoints;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setHP(int i) {
		healthPoints = i;
	}

	public void addHP(int i) {
		healthPoints += i;
	}

	public Attack[] getMoveSet() {
		return moveSet;
	}

	public void setMoveSet(Attack[] moveSet) {
		this.moveSet = moveSet;
	}

	public float getCurrentHealthPoints() {
		return healthPoints;
	}

	public int getLevel() {
		return (int) Math.pow(xp, 1F / 3F);
	}

	public boolean isKO() {
		return currentState == PokemonState.DEFEATED;
	}

	public float getDamageToDeal() {
		return damageToDeal;
	}

	public void goDown() {
		down = true;
	}

	public boolean isDown() {
		return isDown;
	}
}
