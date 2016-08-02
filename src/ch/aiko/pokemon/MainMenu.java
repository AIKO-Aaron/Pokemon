package ch.aiko.pokemon;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.LayerContainer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.graphics.menu.Button;
import ch.aiko.pokemon.graphics.menu.MenuObject;
import ch.aiko.pokemon.graphics.menu.TextField;

public class MainMenu extends LayerContainer {

	public MainMenu() {
		Pokemon.getScreen().setClearColor(0xFF00FF00);
		Pokemon.getScreen().addLayer(this);
		addLayer(new Button(0, 0, 200, 50, "SinglePlayer", (MenuObject sender) -> startSinglePlayer()));
		addLayer(new Button(0, 50, 200, 50, "MultiPlayer", (MenuObject sender) -> openMultiPlayerMenu()));
	}

	public void openMultiPlayerMenu() {
		removeAllLayers();
		addLayer(new TextField(0, 0, 200, 50, 25, "IP", (MenuObject sender) -> startMultiPlayer((TextField) sender)));
	}

	public void startMultiPlayer(TextField sender) {
		System.out.println(sender.getText());
		String ip = sender.getText();
		Pokemon.getScreen().removeAllLayers();
		Pokemon.getScreen().setClearColor(0xFF000000);
		Pokemon.pokemon.start(ip);
	}

	public void startSinglePlayer() {
		System.out.println("SinglePlayer");
		Pokemon.getScreen().removeAllLayers();
		Pokemon.getScreen().setClearColor(0xFF000000);
		Pokemon.pokemon.start(null);
	}

	public void layerRender(Renderer r) {

	}

	public void layerUpdate(Screen s, Layer l) {

	}

	public String getName() {
		return "MainMenu";
	}

	public int getLevel() {
		return 0;
	}

	public boolean stopsRendering() {
		return false;
	}

	public boolean stopsUpdating() {
		return false;
	}

}
