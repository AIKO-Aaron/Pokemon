package ch.aiko.pokemon.fight;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Stack;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.LayerBuilder;
import ch.aiko.engine.graphics.LayerContainer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.Sprite;
import ch.aiko.engine.sprite.SpriteSheet;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.attacks.Attack;
import ch.aiko.pokemon.entity.player.Player;
import ch.aiko.pokemon.entity.trainer.Trainer;
import ch.aiko.pokemon.graphics.menu.Animation;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.pokemon.pokemons.PokemonType;
import ch.aiko.pokemon.pokemons.Pokemons;
import ch.aiko.pokemon.pokemons.TeamPokemon;
import ch.aiko.pokemon.settings.Settings;

public class Fight extends LayerContainer {

	public TeamPokemon pok1;
	public TeamPokemon pok2 = new TeamPokemon(Pokemons.get(1), PokemonType.ENEMY, "MyPokemonNickName2", new Attack[] {}, 2, 2, 0, 0, 0, 0, 0, 1);

	public Stack<Layer> openMenus = new Stack<Layer>();
	public Sprite background;
	public Screen s;

	public Font f = new Font("Arial", 0, 25);
	public int color, color2;
	public static final int HP_SCALE = 3;
	public Point ep, pp;

	public int pi, ti;

	public Player cp;
	public Trainer ct;

	public Fight(Screen s, Player p, Trainer t) {
		this.s = s;

		background = new Sprite("/ch/aiko/pokemon/textures/fight_background/grass_day.png").getScaledInstance(s.getFrameWidth(), s.getFrameHeight());
		Sprite ground = new Sprite("/ch/aiko/pokemon/textures/fight_ground/grass_day.png").getScaledInstance(s.getFrameWidth(), s.getFrameHeight());
		Sprite playerhp = new Sprite("/ch/aiko/pokemon/textures/hp_player.png");
		playerhp = playerhp.getScaledInstance(playerhp.getWidth() * HP_SCALE, playerhp.getHeight() * HP_SCALE);
		Sprite enemyhp = new Sprite("/ch/aiko/pokemon/textures/hp_enemy.png");
		enemyhp = enemyhp.getScaledInstance(enemyhp.getWidth() * HP_SCALE, enemyhp.getHeight() * HP_SCALE);

		ep = new Point(0, 25);
		pp = new Point(s.getWidth() - playerhp.getWidth(), 40);

		background.getImage().getGraphics().drawImage(ground.getImage(), 0, 0, null);
		background.getImage().getGraphics().drawImage(playerhp.getImage(), pp.x, pp.y, null);
		background.getImage().getGraphics().drawImage(enemyhp.getImage(), ep.x, ep.y, null);

		cp = p;
		ct = t;

		pok1 = p.team[0];
		pok2 = t.team[0];

		addLayer(new LayerBuilder().setLayer(5).setRenderable(pok1).setUpdatable(pok1).toLayer());
		addLayer(new LayerBuilder().setLayer(5).setRenderable(pok2).setUpdatable(pok2).toLayer());
	}

	public int getStringWidth(String s) {
		FontMetrics metrics = background.getImage().getGraphics().getFontMetrics(f);
		return metrics.stringWidth(s);
	}

	public int getStringHeight() {
		FontMetrics metrics = background.getImage().getGraphics().getFontMetrics(f);
		return metrics.getHeight();
	}

	@Override
	public void onOpen() {
		openMenu(new FightMenu(s, this));
		openMenu(new Animation(s, new SpriteSheet("/ch/aiko/pokemon/textures/player/player_fight_boy.png", 80, 80, 300, 300).removeColor(0xFF88B8B0), false, 7).setPosition(150, s.getFrameHeight() - 300));
		// SoundPlayer.playSound("/ch/aiko/pokemon/sounds/TrainerFight.mp3"); // why is music disabled? Because I'm testing and it's annoying...

		// Set background of the renderer to the background image. It can be modified afterwards
		s.getRenderer().setClearPixels(background.getPixels());
	}

	@Override
	public void onClose() {
		s.getRenderer().removeClearImage();
	}

	public int getLevel() {
		return 1;
	}

	public boolean stopsRendering() {
		return true;
	}

	public boolean stopsUpdating() {
		return true;
	}

	@Override
	public void layerRender(Renderer r) {
		r.fillRect(pp.x + 56 * HP_SCALE, pp.y + 9 * HP_SCALE, (48 * HP_SCALE), 2 * HP_SCALE, 0xFFFFFFFF);
		r.fillRect(ep.x + 44 * HP_SCALE, ep.y + 9 * HP_SCALE, (48 * HP_SCALE), 2 * HP_SCALE, 0xFFFFFFFF);
		r.fillRect(pp.x + 56 * HP_SCALE, pp.y + 9 * HP_SCALE, (int) ((48 * HP_SCALE) * pok1.getCurrentHealthPoints() / pok1.getMaxHP()), 2 * HP_SCALE, color);
		r.fillRect(ep.x + 44 * HP_SCALE, ep.y + 9 * HP_SCALE, (int) ((48 * HP_SCALE) * pok2.getCurrentHealthPoints() / pok2.getMaxHP()), 2 * HP_SCALE, color2);

		r.drawText(pok2.getNickName(), Settings.font, 18, 0, ep.x + 15, ep.y - 4, 0xFF000000);
		r.drawText(pok1.getNickName(), Settings.font, 18, 0, pp.x + 15, pp.y - 4, 0xFF000000);

		r.drawText(pok1.getLevel() + "", Settings.font, 18, 0, pp.x + 90 * HP_SCALE, pp.y - 4, 0xFF000000);
		r.drawText(pok2.getLevel() + "", Settings.font, 18, 0, ep.x + 94 * HP_SCALE, ep.y - 4, 0xFF000000);
	}

	@Override
	public void layerUpdate(Screen s, Layer l) {
		if (Pokemon.DEBUG) {
			if (popKeyPressed(KeyEvent.VK_N)) {
				pok1.advance();
			} else if (popKeyPressed(KeyEvent.VK_M)) {
				pok1.mega();
			} else if (popKeyPressed(KeyEvent.VK_B)) {
				pok1.gainXP(pok1.getXPToLevel());
			} else if (popKeyPressed(KeyEvent.VK_V)) {
				pok1.hit(-1);
			} else if (popKeyPressed(KeyEvent.VK_C)) {
				pok1.hit(+1);
			}

			if (popKeyPressed(KeyEvent.VK_U)) {
				pok2.advance();
			} else if (popKeyPressed(KeyEvent.VK_I)) {
				pok2.mega();
			} else if (popKeyPressed(KeyEvent.VK_Z)) {
				pok2.gainXP(pok2.getXPToLevel());
			}
		}

		if (pok2.isKO() && ct.getTeamLength() <= ++ti) {
			cp.winBattle(ct);
			ct.defeated = true;
		} else if (pok1.isKO() && cp.getTeamLength() <= ++pi) {
			cp.lostBattle(ct);
		}

		color = pok1.getHP() > pok1.getMaxHP() / 2 ? 0xFF00FF00 : pok1.getHP() > pok1.getMaxHP() / 8 ? 0xFFFFFF00 : 0xFFFF0000;
		color2 = pok2.getHP() > pok2.getMaxHP() / 2 ? 0xFF00FF00 : pok2.getHP() > pok2.getMaxHP() / 8 ? 0xFFFFFF00 : 0xFFFF0000;
	}

	@Override
	public String getName() {
		return "Fight";
	}

	public void openMenu(Menu menu) {
		menu.onOpen();
		openMenus.add(addLayer(menu));
	}

	public void closeTopMenu() {
		Layer topLayer = openMenus.pop();
		((Menu) topLayer).onClose();
		removeLayer(topLayer);
	}

	public void closeAllMenus() {
		while (openMenus.isEmpty())
			closeTopMenu();
	}

	public void closeMenu(Menu m) {
		openMenus.remove(m);
		m.onClose();
		removeLayer(m);
	}

	public void attack(Attack attack) {
		pok2.hit(attack.attackDamage);
		pok1.hit(attack.backFire);
	}

	public void trainer_attack(Attack attack) {
		pok1.hit(attack.attackDamage);
		pok2.hit(attack.backFire);
	}

}
