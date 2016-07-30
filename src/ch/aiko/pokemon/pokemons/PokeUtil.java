package ch.aiko.pokemon.pokemons;

import java.util.ArrayList;

import ch.aiko.modloader.ModLoader;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.graphics.GIF;
import ch.aiko.pokemon.graphics.TextureLoader;

public class PokeUtil {

	public static final float SCALE = 2F;
	public static final float OWN_MOD = 1.5F;

	public static final ArrayList<Pokemons> pokemons = new ArrayList<Pokemons>();
	public static final ArrayList<PokemonType> types = new ArrayList<PokemonType>();
	public static final ArrayList<GIF> frontAnimations = new ArrayList<GIF>();
	public static final ArrayList<GIF> backAnimations = new ArrayList<GIF>();

	public static int index = 0;
	public static int START_INDEX = 0;
	public static int MAX_POKEMON = 0;

	public static void registerPokemon(Pokemons p) {
		if (p.getPokedexNumber() == 1) START_INDEX = pokemons.size();
		pokemons.add(p);
		++MAX_POKEMON;
		Pokemon.out.println("Registered: " + p.getName());
	}

	public static void registerPokemon(Pokemons p, Class<?> loader) {
		pokemons.add(p);
		++MAX_POKEMON;
		Pokemon.out.println("Registered: " + p.getName());
	}

	public static void loadEmAll() {
		for (Pokemons p : pokemons) {
			frontAnimations.add(TextureLoader.loadGIF(p.getPathToAnimation(PokemonType.ENEMY), SCALE).replaceColor(0xFFFFFFFF, 0));
			backAnimations.add(TextureLoader.loadGIF(p.getPathToAnimation(PokemonType.OWNED), SCALE * OWN_MOD).replaceColor(0xFFFFFFFF, 0));
			Pokemon.out.println("Loaded: " + p.getName());
			index++;
			ModLoader.bar3.setValue(100 * index / MAX_POKEMON);
		}
	}

	public static void load(int num) {
		load(get(num));
	}

	public static void load(Pokemons p) {
		frontAnimations.add(TextureLoader.loadGIF(p.getPathToAnimation(PokemonType.ENEMY), SCALE).replaceColor(0xFFFFFFFF, 0));
		backAnimations.add(TextureLoader.loadGIF(p.getPathToAnimation(PokemonType.OWNED), SCALE * OWN_MOD).replaceColor(0xFFFFFFFF, 0));
		Pokemon.out.println("Loaded: " + p.getName());
		index++;
	}

	public static GIF getFrontAnimation(Pokemons p) {
		return frontAnimations.get(pokemons.indexOf(p));
	}

	public static GIF getBackAnimation(Pokemons p) {
		return backAnimations.get(pokemons.indexOf(p));
	}

	public static GIF getAnimation(Pokemons type, PokemonType holder) {
		return holder == PokemonType.OWNED ? getBackAnimation(type) : getFrontAnimation(type);
	}

	public static Pokemons get(int pokedexNumber) {
		if (pokemons.size() > pokedexNumber + START_INDEX && pokemons.get(pokedexNumber + START_INDEX).getPokedexNumber() == pokedexNumber) return pokemons.get(pokedexNumber + START_INDEX);
		for (Pokemons p : pokemons) {
			if (p.getPokedexNumber() == pokedexNumber) return p;
		}
		return get(1);
	}

	public static PokemonType getType(int readInt) {
		if (types.size() > readInt && types.get(readInt).in == readInt) return types.get(readInt);
		for (PokemonType p : types) {
			if (p.in == readInt) return p;
		}
		return PokemonType.ENEMY;
	}

}
