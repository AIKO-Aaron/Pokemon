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
	
	public Sprite getBackSprite() {
		int i = id;
		if(i < 252) {
			int gen = id < 151 ? 1 : 2;
			SpriteSheet sheet = new SpriteSheet("/ch/aiko/pokemon/textures/gen" + gen + ".png", 90, 80).offset(182, -16);
			Sprite s = sheet.getSprite((id-1) * 3).removeColor(0xFFE3DFE0);
			
			//Sprite.printSprite(s);
			
			return s;
		} else {
			//TODO Pokemon Back Sprite loading for the newer spritesheets
		}
		
		return getSprite();
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
