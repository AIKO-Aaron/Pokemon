package ch.aiko.pokemon.pokemon.attack;

import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.pokemon.TeamPokemon;
import ch.aiko.pokemon.pokemon.Type;

public class Move {
	
	protected String name;
	protected Type type;
	protected int damage;
	protected Runnable after;
	protected AttackType attackType;
	
	public Move(String name, Type t, int damage, AttackType at, Runnable after) {
		this.name = Language.current.getValue(name);
		this.type = t;
		this.damage = damage;
		this.after = after;
		this.attackType = at;
	}
	
	public String getName() {
		return name;
	}
	public Type getType() {
		return type;
	}
	public int getDamage() {
		return damage;
	}
	
	public void attack(TeamPokemon teamPokemon, TeamPokemon defender) {
		
	}

	public static enum AttackType {
		PHYSICAL, SPECIAL, STAT;
	}
	
}
