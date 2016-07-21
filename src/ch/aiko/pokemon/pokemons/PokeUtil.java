package ch.aiko.pokemon.pokemons;

import java.util.ArrayList;

public class PokeUtil {

	public static final ArrayList<Pokemons> pokemons = new ArrayList<Pokemons>();

	public static Pokemons get(int pokedexNumber) {
		if (pokemons.size() > pokedexNumber && pokemons.get(pokedexNumber).getPokedexNumber() == pokedexNumber) return pokemons.get(pokedexNumber);
		for (Pokemons p : pokemons) {
			if (p.getPokedexNumber() == pokedexNumber) return p;
		}
		return Pokemons.BLUBASAUR;
	}
}
