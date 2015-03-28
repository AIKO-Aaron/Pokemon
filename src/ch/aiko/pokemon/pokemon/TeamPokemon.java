package ch.aiko.pokemon.pokemon;

import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.mob.Trainer;
import ch.aiko.pokemon.pokemon.attack.Move;
import ch.aiko.pokemon.sprite.Sprite;

public class TeamPokemon {

	protected Move[] moves = new Move[4];
	protected int lvl, hp, maxHP, att, satt, def, sdef, xpToLvl, pos;
	protected Pokemon pokemon;

	public TeamPokemon(Trainer t, Pokemon pok, int lvl, int hp, int maxHP, int Att, int Satt, int def, int Sdef, int xpToLvl, int pos, Move[] moves) {
		this.lvl = lvl;
		this.hp = hp;
		this.maxHP = maxHP;
		this.att = Att;
		this.satt = Satt;
		this.def = def;
		this.sdef = Sdef;
		this.xpToLvl = xpToLvl;
		this.pos = pos;
		this.pokemon = pok;
		this.moves = moves;
		
		t.setTeamPokemon(pos, this);
	}
	
	public TeamPokemon(Player p, Pokemon pok, int lvl, int hp, int maxHP, int Att, int Satt, int def, int Sdef, int xpToLvl, int pos, Move[] moves) {
		this.lvl = lvl;
		this.hp = hp;
		this.maxHP = maxHP;
		this.att = Att;
		this.satt = Satt;
		this.def = def;
		this.sdef = Sdef;
		this.xpToLvl = xpToLvl;
		this.pos = pos;
		this.pokemon = pok;
		this.moves = moves;

		p.setPokemon(pos, this);
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMaxHp() {
		return maxHP;
	}

	public void setMaxHp(int hp) {
		this.maxHP = hp;
	}

	public int getAtt() {
		return att;
	}

	public void setAtt(int att) {
		this.att = att;
	}

	public int getSatt() {
		return satt;
	}

	public void setSatt(int satt) {
		this.satt = satt;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public int getSdef() {
		return sdef;
	}

	public void setSdef(int sdef) {
		this.sdef = sdef;
	}

	public int getXpToLvl() {
		return xpToLvl;
	}

	public void xp(int xp) {
		this.xpToLvl -= xp;
		if (xpToLvl <= 0) levelUp();
	}

	public void levelUp() {
		System.out.println("Level Up");
		lvl++;
		if(lvl >= pokemon.getLevelEvolve() && pokemon.getLevelEvolve() != -1) System.out.println("Evolve");
		xpToLvl += lvl;
		if (xpToLvl <= 0) levelUp();
	}

	public void attack(TeamPokemon defender, int move) {
		if (move < 0 || move >= 4) move = 0;
		moves[move].attack(this, defender);
	}

	public String getName() {
		return pokemon.getName();
	}

	public String getName(Language l) {
		return pokemon.getName(l);
	}
	
	public String getLocalizationString() {
		return pokemon.getLocalizationString();
	}
	
	public Sprite getSprite() {
		return pokemon.getSprite();
	}
	
	public int getHeight() {
		return pokemon.getHeight();
	}
	
	public int getWidth() {
		return pokemon.getWidth();
	}
	
}
