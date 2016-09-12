package ch.aiko.pokemon.graphics.menu;

import java.awt.event.KeyEvent;

import ch.aiko.engine.graphics.Layer;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.level.Level;

public class Chat extends Menu {

	public static boolean OPEN;
	private boolean closing = false;
	public static final int stringSize = 30;

	public Chat(Screen parent) {
		super(parent);
	}

	public void onOpen() {
		TextField tf = new TextField(parent.getFrameWidth() - 400, 300, 400, 40, "", (f) -> sendChat((TextField) f));
		tf.setOrientation(TextField.RIGHT);
		tf.defaultText = false;
		addLayer(tf);
		
		tf.selected = true;

		OPEN = true;
	}

	public void sendChat(TextField field) {
		if (field.getText().replace(" ", "").equalsIgnoreCase("")) return;
		Level.addChatMessage("[" + Pokemon.player.getPlayerName() + "] " + field.getText().replace("\\n", "\n"));
		if (Pokemon.ONLINE) Pokemon.client.sendText("/chat/[" + Pokemon.player.getPlayerName() + "] " + field.getText().replace("\\n", "\n"));
		field.setText("");
		closeMe();
	}

	public void onClose() {
		removeAllLayers();
		OPEN = false;
	}

	public void renderMenu(Renderer r) {

	}

	public void updateMenu(Screen s, Layer l) {
		if (l.isKeyPressed(KeyEvent.VK_ESCAPE)) closing = true;
		else if (closing) closeMe();
	}

	public String getName() {
		return "Chat";
	}

}
