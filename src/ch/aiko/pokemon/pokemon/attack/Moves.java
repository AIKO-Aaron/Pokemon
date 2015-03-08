package ch.aiko.pokemon.pokemon.attack;

import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.pokemon.TeamPokemon;
import ch.aiko.pokemon.pokemon.Type;
import ch.aiko.pokemon.pokemon.attack.Move.AttackType;

public class Moves {

	static {
		System.out.println(Language.translate("init of moves"));
	}
	
	public static final Move NULL = new Move("null", Type.NORMAL, 0, AttackType.PHYSICAL, new Runnable(){public void run(){}}){public void attack(TeamPokemon t1, TeamPokemon t2){}};
	
}
