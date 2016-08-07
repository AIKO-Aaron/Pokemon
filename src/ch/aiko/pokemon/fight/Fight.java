package ch.aiko.pokemon.fight;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.util.Stack;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.LayerBuilder;
import ch.aiko.engine.graphics.LayerContainer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.sprite.Sprite;
import ch.aiko.engine.sprite.SpriteSheet;
import ch.aiko.pokemon.entity.player.Player;
import ch.aiko.pokemon.entity.trainer.Trainer;
import ch.aiko.pokemon.graphics.menu.Animation;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.pokemon.pokemons.PokemonType;
import ch.aiko.pokemon.pokemons.Pokemons;
import ch.aiko.pokemon.pokemons.TeamPokemon;

public class Fight extends LayerContainer {

	public TeamPokemon pok1;
	public TeamPokemon pok2 = new TeamPokemon(Pokemons.get(1), PokemonType.ENEMY, "MyPokemonNickName2", 2, 2, 0, 0, 0, 0, 0, 1);

	public Stack<Layer> openMenus = new Stack<Layer>();
	public Sprite background;
	public Screen s;

	public Font f = new Font("Arial", 0, 25);
	public int color, color2;
	public PixelRenderer renderer;
	
	public Fight(Screen s, Player p, Trainer t) {
		this.s = s;
		background = new Sprite("/ch/aiko/pokemon/textures/fight_background/grass_day.png").getScaledInstance(s.getFrameWidth(), s.getFrameHeight());
		Sprite ground = new Sprite("/ch/aiko/pokemon/textures/fight_ground/grass_day.png").getScaledInstance(s.getFrameWidth(), s.getFrameHeight());
		background.getImage().getGraphics().drawImage(ground.getImage(), 0, 0, null);
		renderer = new PixelRenderer(background, background.getWidth(), background.getHeight());

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
		openMenu(new FightMenu(s));
		openMenu(new Animation(s, new SpriteSheet("/ch/aiko/pokemon/textures/player/player_fight_boy.png", 80, 80, 300, 300).removeColor(0xFF88B8B0), false, 7).setPosition(150, s.getFrameHeight() - 300));
		// SoundPlayer.playSound("/ch/aiko/pokemon/sounds/TrainerFight.mp3"); // why is music disabled? Because I'm testing and it's annoying...

		// Set background of the renderer to the background image. It can be modified afterwards
		s.getRenderer().setClearPixels(renderer.pixels);
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
		renderer.fillRect(0, 0, 50, 50, color);
		renderer.fillRect(50, 0, 50, 50, color2);
		renderer.drawString(pok2.getNickName(), 50, 50, 50, 0xFF000000);
	}

	@Override
	public void layerUpdate(Screen s, Layer l) {
		if (popKeyPressed(KeyEvent.VK_N)) {
			pok1.advance();
		} else if (popKeyPressed(KeyEvent.VK_M)) {
			pok1.mega();
		} else if (popKeyPressed(KeyEvent.VK_B)) {
			pok1.gainXP(pok1.getXPToLevel());
		} else if (popKeyPressed(KeyEvent.VK_V)) {
			pok1.addHP(+1);
		} else if (popKeyPressed(KeyEvent.VK_C)) {
			pok1.addHP(-1);
		}

		if (popKeyPressed(KeyEvent.VK_U)) {
			pok2.advance();
		} else if (popKeyPressed(KeyEvent.VK_I)) {
			pok2.mega();
		} else if (popKeyPressed(KeyEvent.VK_Z)) {
			pok2.gainXP(pok2.getXPToLevel());
		}

		/**
		 * int col1 = 0x00FF00; // green int col2 = 0xFFFF00; // yellow int col3 = 0xFF0000; // red
		 * 
		 * int mod = 100 * pok1.getHP() / pok1.getMaxHP(); int mod2 = 100 * pok2.getHP() / pok2.getMaxHP();
		 * 
		 * int col1amount = mod; int col2amount = 100 - Math.abs(50 - mod); int col3amount = Math.abs(100 - mod);
		 * 
		 * Vector3f vec = new Vector3f(col1amount, col2amount, col3amount); vec.normalize();
		 * 
		 * int col = 0xFF << 24 | (int) (col1 * vec.x) + (int) (col2 * vec.y) + (int) (col3 * vec.z); color = col; // color = 0xFF << 24 | (int) (0xFF - mod) << 16 | ((int) (mod) & 0xFF) << 8 | (Math.abs(0x7F - mod)) << 16 | (Math.abs(0x7F - mod)) << 8; color2 = 0xFF << 24 | (int) (0xFF - mod2) << 16 | ((int) (mod2) & 0xFF) << 8 | (Math.abs(0x7F - mod2)) << 16 | (Math.abs(0x7F - mod2)) << 8;
		 */
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

}
