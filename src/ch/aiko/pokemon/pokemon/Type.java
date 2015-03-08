package ch.aiko.pokemon.pokemon;

import java.util.ArrayList;

import ch.aiko.pokemon.language.Language;

public enum Type {
	
	NULL(0, new int[]{0}, new int[]{0}, new int[]{0}),
	NORMAL(1, new int[]{}, new int[]{6, 9}, new int[]{8}),
	FIGHT(2, new int[]{1, 6, 9, 15, 17}, new int[]{3, 4, 7, 14, 18}, new int[]{8}),
	AIR(3, new int[]{2, 7, 12}, new int[]{6, 9, 13}, new int[]{}),
	POISON(4, new int[]{12, 18}, new int[]{4, 5, 6, 8}, new int[]{9}),
	GROUND(5, new int[]{4, 6, 9, 10, 13}, new int[]{7, 12}, new int[]{3}),
	ROCK(6, new int[]{3, 7, 10, 15}, new int[]{2, 5, 9}, new int[]{}),
	BUG(7, new int[]{12, 14, 17}, new int[]{2, 3, 4, 8, 9, 10, 18}, new int[]{}),
	GHOST(8, new int[]{8, 14}, new int[]{17}, new int[]{1}),
	STEEL(9, new int[]{6, 15, 18}, new int[]{9, 10, 11, 13}, new int[]{}),
	FIRE(10, new int[]{7, 9, 12, 15}, new int[]{6, 10, 11, 16}, new int[]{}),
	WATER(11, new int[]{5, 6, 10}, new int[]{11, 12, 16}, new int[]{}),
	GRASS(12, new int[]{5, 6, 11}, new int[]{3, 4, 7, 9, 10, 12, 16}, new int[]{}),
	ELECTRIC(13, new int[]{3, 11}, new int[]{12, 13, 16}, new int[]{5}),
	PSYCHIC(14, new int[]{2, 4}, new int[]{9, 14}, new int[]{17}),
	ICE(15, new int[]{3, 5, 12, 16}, new int[]{9, 10, 11, 15}, new int[]{8}),
	DRAGON(16, new int[]{16}, new int[]{9}, new int[]{18}),
	DARK(17, new int[]{8, 14}, new int[]{2, 17, 18}, new int[]{}),
	FAIRY(18, new int[]{2, 16, 17}, new int[]{4, 9, 10}, new int[]{18});
		
	public static ArrayList<Type> types;
	
	public static Type get(int id) {
		for(Type t : types) {
			if(t.getID() == id) return t;
		}
		return Type.NULL;
	}
	
	private int id;
	
	private ArrayList<Type> goodAgainst = new ArrayList<Type>();
	private ArrayList<Type> badAgainst = new ArrayList<Type>();
	private ArrayList<Type> noDamageAgainst = new ArrayList<Type>();
	
	private int[] good, bad, res;
	
	private Type(int id, int[] idsGood, int[] idsBad, int[] idsResistant) {
		this.id = id;
		init();
		good(idsGood);
		bad(idsBad);
		res(idsResistant);
		good = idsGood;
		bad = idsBad;
		res = idsResistant;
	}
	
	private void init() {
		if(types == null) types = new ArrayList<Type>();
		types.add(this);
	}
	
	private void good(int[] goods) {
		goodAgainst = new ArrayList<Type>();
		for(int i : goods) {
			goodAgainst.add(Type.get(i));
		}
	}
	
	private void bad(int[] bads) {
		badAgainst = new ArrayList<Type>();
		for(int i : bads) {
			badAgainst.add(Type.get(i));
		}
	}
	
	private void res(int[] res) {
		noDamageAgainst = new ArrayList<Type>();
		for(int i : res) {
			noDamageAgainst.add(Type.get(i));
		}
	}
	
	public int getID() {
		return id;
	}
	
	public Type[] getTypesGoodAgainst() {
		if(goodAgainst.contains(Type.NULL)) good(good);
		return goodAgainst.toArray(new Type[goodAgainst.size()]);
	}
	
	public Type[] getTypesBadAgainst() {
		if(badAgainst.contains(Type.NULL)) bad(bad);
		return badAgainst.toArray(new Type[badAgainst.size()]);
	}
	
	public Type[] getTypesResistantAgainst() {
		if(noDamageAgainst.contains(Type.NULL)) res(res);
		return noDamageAgainst.toArray(new Type[noDamageAgainst.size()]);
	}
	
	public boolean isGoodAgainst(Type t) {
		if(goodAgainst.contains(Type.NULL)) good(good);
		return goodAgainst.contains(t);
	}
	
	public boolean isBadAgainst(Type t) {
		if(badAgainst.contains(Type.NULL)) bad(bad);
		return badAgainst.contains(t);
	}
	
	public boolean doesDamageAgainst(Type t) {
		if(noDamageAgainst.contains(Type.NULL)) res(res);
		return !noDamageAgainst.contains(t);
	}

	public String getName() {
		return Language.current.getValue(name());
	}
	
}
