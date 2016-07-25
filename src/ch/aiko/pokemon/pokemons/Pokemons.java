package ch.aiko.pokemon.pokemons;

public enum Pokemons {

	// MEGA-EVOLUTIONS

	MEGA_VENUSAUR(1, 3),
	MEGA_CHARIZARD_X(1, 6),
	MEGA_CHARIZARD_Y(2, 6),
	MEGA_BLASTOISE(1, 9),

	// "NORMAL" Pokemon

	BLUBASAUR(1, 16, 2),
	IVYSAUR(2, 32, 3),
	VENUSAUR(3, MEGA_VENUSAUR),
	CHARMANDER(4, 16, 5),
	CHARMELEON(5, 36, 6),
	CHARIZARD(6, MEGA_CHARIZARD_X, MEGA_CHARIZARD_Y),
	SQUIRTLE(7, 16, 8),
	WARTORTLE(8, 36, 9),
	BLASTOISE(9, MEGA_BLASTOISE),

	// END
	;

	private int lvlForEvo;
	private int pokedexNumber;
	private boolean hasMega = false, isMega = false;
	private Pokemons[] megas = new Pokemons[0];
	private int index, evolvesTo, evolvesFrom;

	private Pokemons(int pokedexNumber, int lvlForEvo, int evolvesTo) {
		this.lvlForEvo = lvlForEvo;
		this.pokedexNumber = pokedexNumber;
		this.evolvesTo = evolvesTo;
		PokeUtil.pokemons.add(this);
	}

	private Pokemons(int pokedexNumber, int lvlForEvo, int evolvesTo, Pokemons... megas) {
		this.lvlForEvo = lvlForEvo;
		this.pokedexNumber = pokedexNumber;
		this.evolvesTo = evolvesTo;
		this.hasMega = megas.length != 0;
		this.megas = megas;
		PokeUtil.pokemons.add(this);
	}

	private Pokemons(int pokedexNumber, Pokemons... megas) {
		this.lvlForEvo = Integer.MAX_VALUE;
		this.pokedexNumber = pokedexNumber;
		this.hasMega = megas.length != 0;
		this.megas = megas;
		PokeUtil.pokemons.add(this);
	}

	private Pokemons(int pokedexNumber) {
		this.lvlForEvo = Integer.MAX_VALUE;
		this.pokedexNumber = pokedexNumber;
		PokeUtil.pokemons.add(this);
	}

	private Pokemons(int index, int evolvesFrom) {
		this.lvlForEvo = Integer.MAX_VALUE;
		this.pokedexNumber = -1;
		this.isMega = true;
		this.index = index;
		this.evolvesFrom = evolvesFrom;
	}

	public boolean hasMegaEvolution() {
		return hasMega;
	}

	public int getEvolvesInto() {
		return evolvesTo;
	}

	public Pokemons getEvolvedPokemon() {
		return PokeUtil.get(evolvesTo);
	}

	public Pokemons getChild() {
		if (isMega) {
			return PokeUtil.get(evolvesFrom);
		} else {
			for (Pokemons p : PokeUtil.pokemons) {
				if (p.getEvolvesInto() == pokedexNumber) return p;
			}
		}
		return Pokemons.BLUBASAUR;
	}

	public Pokemons[] getMegaEvolutions() {
		return megas;
	}

	public Pokemons getMegaEvolution(int i) {
		if (megas.length <= 0) return this;
		for (Pokemons p : megas) {
			if (p.getIndex() == i) return p;
		}
		return megas[0];
	}

	public boolean canEvolve(int lvl) {
		return lvl >= lvlForEvo;
	}

	public int getPokedexNumber() {
		return pokedexNumber;
	}

	public boolean isMegaEvolution() {
		return isMega;
	}

	public String getPathToAnimation(PokemonType type) {
		String curNum = "" + (isMega ? evolvesFrom : pokedexNumber);
		while (curNum.length() < 3)
			curNum = "0" + curNum;
		return "/ch/aiko/pokemon/textures/pokemon/" + (type == PokemonType.OWNED ? "back/" : "front/") + curNum + (isMega ? "m" + index : "") + ".gif";
	}

	public int getIndex() {
		return index;
	}

}
