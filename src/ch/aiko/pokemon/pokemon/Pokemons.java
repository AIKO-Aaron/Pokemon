package ch.aiko.pokemon.pokemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import ch.aiko.pokemon.language.Language;

public class Pokemons {
	
	public static final HashMap<Integer, Pokemon> pokemons = new HashMap<Integer, Pokemon>();
	
	public static Pokemon NULL = new Pokemon(0, 0, 0, 0);
	
	public static Pokemon Pok1 = new Pokemon(0, 0, 1, 2);
	public static Pokemon Pok2 = new Pokemon(1, 0, 2, 3);
	public static Pokemon Pok3 = new Pokemon(2, 0, 3, 0);
	public static Pokemon Pok4 = new Pokemon(3, 0, 4, 5);
	public static Pokemon Pok5 = new Pokemon(4, 0, 5, 6);
	public static Pokemon Pok6 = new Pokemon(5, 0, 6, 0);
	public static Pokemon Pok7 = new Pokemon(6, 0, 7, 8);
	public static Pokemon Pok8 = new Pokemon(7, 0, 8, 9);
	public static Pokemon Pok9 = new Pokemon(8, 0, 9, 0);
	public static Pokemon Pok10 = new Pokemon(9, 0, 10, 0);
	public static Pokemon Pok11 = new Pokemon(10, 0, 11, 0);
	public static Pokemon Pok12 = new Pokemon(11, 0, 12, 0);
	public static Pokemon Pok13 = new Pokemon(12, 0, 13, 0);
	public static Pokemon Pok14 = new Pokemon(13, 0, 14, 0);
	public static Pokemon Pok15 = new Pokemon(14, 0, 15, 0);
	public static Pokemon Pok16 = new Pokemon(15, 0, 16, 0);
	public static Pokemon Pok17 = new Pokemon(16, 0, 17, 0);
	public static Pokemon Pok18 = new Pokemon(17, 0, 18, 0);
	public static Pokemon Pok19 = new Pokemon(18, 0, 19, 0);
	public static Pokemon Pok20 = new Pokemon(19, 0, 20, 0);
	public static Pokemon Pok21 = new Pokemon(20, 0, 21, 0);
	public static Pokemon Pok22 = new Pokemon(21, 0, 22, 0);
	public static Pokemon Pok23 = new Pokemon(22, 0, 23, 0);
	public static Pokemon Pok24 = new Pokemon(23, 0, 24, 0);
	public static Pokemon Pok25 = new Pokemon(24, 0, 25, 0);
	public static Pokemon Pok26 = new Pokemon(25, 0, 26, 0);
	public static Pokemon Pok27 = new Pokemon(26, 0, 27, 0);
	public static Pokemon Pok28 = new Pokemon(27, 0, 28, 0);
	
	public static Pokemon get(int id) {
		if(!pokemons.containsKey(id)) return NULL;
		return pokemons.get(id);
	}
	
	public static ArrayList<Pokemon> getAll() {
		ArrayList<Pokemon> ret = new ArrayList<Pokemon>();
		for(Entry<Integer, Pokemon> pok : pokemons.entrySet()) {
			ret.add(pok.getValue());
		}
		return ret;
	}
	
	public static Pokemon get(String locName) {
		for(Pokemon p : getAll()) {
			if(p.getName().equalsIgnoreCase(locName)) return p;
		}
		return NULL;
	}
	
	public static Pokemon get(String name, Language l) {
		return get(Language.translate(l.getKey(name)));
	}
	
}
