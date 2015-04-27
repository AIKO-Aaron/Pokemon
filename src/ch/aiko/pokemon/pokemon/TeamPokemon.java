package ch.aiko.pokemon.pokemon;

import ch.aiko.engine.Renderer;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.mob.Trainer;
import ch.aiko.pokemon.mob.player.Player;
import ch.aiko.pokemon.pokemon.attack.Move;
import ch.aiko.pokemon.settings.Settings;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.pokemon.sprite.SpriteSheet;

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
		if (lvl >= pokemon.getLevelEvolve() && pokemon.getLevelEvolve() != -1) System.out.println("Evolve");
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

	public Sprite getBackSprite() {
		return pokemon.getBackSprite();
	}

	public int getHeight() {
		return pokemon.getHeight();
	}

	public int getWidth() {
		return pokemon.getWidth();
	}

	private static SpriteSheet hp_player = new SpriteSheet("/ch/aiko/pokemon/textures/hp_player.png", 132, 42);
	private static SpriteSheet hp_enemy = new SpriteSheet("/ch/aiko/pokemon/textures/hp_enemy.png", 132, 42);

	// Green = 0xFF64A068
	// Orange = 0xFFCC9C68
	// Red = 0xFFC85C54

	// 48 * 2
	// 192 * 8

	public Sprite getPlayerHpBar() {
		String name = getName();

		Sprite nameSprite = new Sprite(Renderer.createText(name, 40, 0xFFFFFFFF, Settings.font));
		Sprite hpSprite = new Sprite(Renderer.createText(hp + "", 30, 0xFFFFFFFF, Settings.font));
		Sprite maxHpSprite = new Sprite(Renderer.createText("/ " + maxHP, 30, 0xFFFFFFFF, Settings.font));
		Sprite lvlSprite = new Sprite(Renderer.createText("Level: " + lvl, 40, 0xFFFFFFFF, Settings.font));
		Sprite HP = new Sprite(Renderer.createText(Language.translate("HP"), 25, 0xFFFFFFFF, Settings.font));
		int color = (int) (Math.abs((float) hp / (float) maxHP - 1) * 255) << 16 | (int) (255 * (float) hp / (float) maxHP) << 8 | 0xFF000000;
		Sprite bar = new Sprite(color, 192 / maxHP * hp, 8);

		Sprite s = hp_player.getSprite(0).getScaledInstance(528, 168);

		s.top(nameSprite, 128, 27);
		s.top(hpSprite, 300, 112);
		s.top(maxHpSprite, 380, 112);
		s.top(lvlSprite, 330, 27);
		s.top(HP, 250, 85);
		s.top(bar, 304, 96);

		return s;
	}

	public Sprite getEnemyHpBar() {
		String name = getName();

		Sprite nameSprite = new Sprite(Renderer.createText(name, 40, 0xFFFFFFFF, Settings.font));
		Sprite lvlSprite = new Sprite(Renderer.createText("Level: " + lvl, 40, 0xFFFFFFFF, Settings.font));
		Sprite s = hp_enemy.getSprite(0).getScaledInstance(528, 168);
		Sprite HP = new Sprite(Renderer.createText(Language.translate("HP"), 25, 0xFFFFFFFF, Settings.font));
		
		int color = (int) (Math.abs((float) hp / (float) maxHP - 1) * 255) << 4 | (int) (255 * (float) hp / (float) maxHP) << 8 | 0xFF000000;
		Sprite bar = new Sprite(color, 192 / maxHP * hp, 8);

		s.top(nameSprite, 30, 52);
		s.top(lvlSprite, 250, 52);
		s.top(HP, 175, 109);
		s.top(bar, 220, 120);

		return s;
	}
}
