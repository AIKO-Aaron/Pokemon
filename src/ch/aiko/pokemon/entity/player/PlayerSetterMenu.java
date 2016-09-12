package ch.aiko.pokemon.entity.player;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.graphics.menu.Button;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.pokemon.graphics.menu.TextBox;
import ch.aiko.pokemon.graphics.menu.TextField;
import ch.aiko.pokemon.language.Language;
import ch.aiko.pokemon.level.Level;

public class PlayerSetterMenu extends Menu {

	public PlayerSetterMenu(Screen parent) {
		super(parent);
	}

	public void onOpen() {
		addLayer(new TextBox(Language.translate("oakgreet1"), (r) -> done1()));
	}

	public void onClose() {}

	public void renderMenu(Renderer r) {}

	public void updateMenu(Screen s, Layer l) {}

	public String getName() {
		return "PlayerSetter";
	}

	public void done1() {
		removeAllLayers();
		input.reset();
		addButton(new Button(400, 180, 400, 100, Language.translate("Boy"), (r) -> chooseGender(Player.BOY)), 10, 0);
		addButton(new Button(400, 280, 400, 100, Language.translate("Girl"), (r) -> chooseGender(Player.GIRL)), 10, 1);
	}

	public void chooseGender(int g) {
		removeAllLayers();
		removeAllButtons();
		input.reset();
		Pokemon.player.setGender(g);
		addLayer(new TextBox(Language.translate("oakgreet2"), (r) -> done2()));
	}

	public void done2() {
		removeAllLayers();
		input.reset();
		TextField tf = new TextField(0, 440, 960, 100, "Name", (r) -> chooseName(((TextField) r).getText()));
		tf.setOrientation(TextField.CENTERED);
		addLayer(tf);
	}

	public void chooseName(String name) {
		removeAllLayers();
		input.reset();
		Pokemon.player.name = name;
		addLayer(new TextBox(Language.translate("oakgreet3"), (r) -> done3()));
	}

	public void done3() {
		removeAllLayers();
		input.reset();

		if (Pokemon.ONLINE) {
			Pokemon.client.sendText("/PGN/" + Pokemon.player.gender + "/" + Pokemon.player.name);
		}

		Pokemon.pokemon.handler.setLevel(new Level("/ch/aiko/pokemon/level/test.layout"));
		Pokemon.pokemon.handler.level.addPlayer(Pokemon.player);
	}

}
