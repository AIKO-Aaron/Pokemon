package ch.aiko.pokemon.pokemons;

public enum Pokemons {

	BLUBASAUR(1, 16),
	IVYSAUR(2, 32),
	VENUSAUR(3),
	CHARMANDER(4, 16),
	CHARMELEON(5, 36),
	CHARIZARD(6),
	SQUIRTLE(7, 16),
	WARTORTLE(8, 36),
	BLASTOISE(9);

	private int lvlForEvo;
	private int pokedexNumber;

	private Pokemons(int pokedexNumber, int lvlForEvo) {
		this.lvlForEvo = lvlForEvo;
		this.pokedexNumber = pokedexNumber;
		PokeUtil.pokemons.add(this);
	}

	private Pokemons(int pokedexNumber) {
		this.lvlForEvo = 101;
		this.pokedexNumber = pokedexNumber;
		PokeUtil.pokemons.add(this);
	}

	public boolean canEvolve(int lvl) {
		return lvl >= lvlForEvo;
	}

	public int getPokedexNumber() {
		return pokedexNumber;
	}

	public String getAnimationName() {
		String curNum = "" + pokedexNumber;
		while (curNum.length() < 3)
			curNum = "0" + curNum;
		return curNum;
	}

	public String getPathToAnimation() {
		String curNum = "" + pokedexNumber;
		while (curNum.length() < 3)
			curNum = "0" + curNum;
		return "/ch/aiko/pokemon/textures/pokemon/" + curNum + ".gif";
	}

}
