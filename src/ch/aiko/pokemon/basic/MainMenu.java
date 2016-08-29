package ch.aiko.pokemon.basic;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.LayerContainer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.client.PokemonClient;
import ch.aiko.pokemon.graphics.menu.Button;
import ch.aiko.pokemon.graphics.menu.MenuObject;
import ch.aiko.pokemon.graphics.menu.TextField;

import com.sun.glass.events.KeyEvent;

public class MainMenu extends LayerContainer {

	public static final int WIDTH = 400;
	public static final int HEIGHT = 100;

	protected boolean mpopen;

	public MainMenu() {
		Pokemon.getScreen().setClearColor(0xFF00FF00);
		Pokemon.getScreen().addLayer(this);
		addLayer(new Button(0, HEIGHT * 0, WIDTH, HEIGHT, "SinglePlayer", (MenuObject sender) -> startSinglePlayer()));
		addLayer(new Button(0, HEIGHT * 1, WIDTH, HEIGHT, "MultiPlayer", (MenuObject sender) -> openMultiPlayerMenu()));
		//addLayer(new Button(0, HEIGHT * 2, WIDTH, HEIGHT, "Test", (MenuObject sender) -> test()));
	}

	public void test() {
		PokemonClient client = new PokemonClient("10.0.0.96", "0000-0000-0000-0000-0000");
		client.sendBytes(new byte[] { 0x41, 0x61, 0x72, 0x6F, 0x6E, 0x3F });
	}
	
	public void openMultiPlayerMenu() {
		if (mpopen) return;
		Layer tf = addLayer(new TextField(WIDTH, HEIGHT, WIDTH, HEIGHT, "IP", (MenuObject sender) -> startMultiPlayer((TextField) sender)));
		addLayer(new Button(WIDTH, HEIGHT * 2, WIDTH, HEIGHT, "Connect", (MenuObject sender) -> startMultiPlayer((TextField) tf)));
		mpopen = true;
	}

	public void startMultiPlayer(TextField sender) {
		String ip = sender.getText();
		Pokemon.getScreen().removeAllLayers();
		Pokemon.getScreen().setClearColor(0xFF000000);
		GameHandler.gameStarted = true;
		Pokemon.pokemon.start(ip);
	}

	public void startSinglePlayer() {
		Pokemon.getScreen().removeAllLayers();
		Pokemon.getScreen().setClearColor(0xFF000000);
		GameHandler.gameStarted = true;
		Pokemon.pokemon.start(null);
	}

	@Override
	public void layerRender(Renderer r) {

	}

	@Override
	public void layerUpdate(Screen s, Layer l) {
		if (popKeyPressed(KeyEvent.VK_ESCAPE)) System.exit(0);
	}

	@Override
	public String getName() {
		return "MainMenu";
	}

	@Override
	public int getLevel() {
		return 0;
	}

	@Override
	public boolean stopsRendering() {
		return false;
	}

	@Override
	public boolean stopsUpdating() {
		return false;
	}

}
