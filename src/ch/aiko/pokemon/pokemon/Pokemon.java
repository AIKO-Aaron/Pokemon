package ch.aiko.pokemon.pokemon;

import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.pokemon.sprite.SpriteSheet;

public class Pokemon {
	
	public static final int HEIGHT = 80;
	public static final int WIDTH = 80;
	
	public static SpriteSheet pokemons = new SpriteSheet("/ch/aiko/pokemon/textures/diamond-pearl-frame2.png", WIDTH, HEIGHT);

	protected int id, l;
	protected String name;
	protected Type type1, type2;
	protected Sprite sprite;

	protected Pokemon evolution;
	
	public Pokemon(int x, int y, int id, int evolution, int lvlToEv) {
		sprite = pokemons.getSprite(x, y);
		this.name = Language.current.getValue("pok" + id);
		this.id = id;
		this.evolution = Pokemons.get(evolution);
		this.l = lvlToEv;
		
		Pokemons.pokemons.put(id, this);
	}
	
	public int getLevelEvolve() {
		return l;
	}

	public String getName() {
		return name;
	}

	public String getName(Language l) {
		return l.getValue("pok" + id);
	}
	
	public String getLocalizationString() {
		return "pok" + id;
	}
	
	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
}
