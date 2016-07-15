package ch.aiko.pokemon.entity;

import java.awt.event.KeyEvent;

import ch.aiko.engine.graphics.Screen;

public class Player extends Entity {

	private int speed = 6;

	public void update(Screen screen) {
		if (screen.getInput().isKeyPressed(KeyEvent.VK_LEFT)) xPos -= speed;
		if (screen.getInput().isKeyPressed(KeyEvent.VK_RIGHT)) xPos += speed;
		if (screen.getInput().isKeyPressed(KeyEvent.VK_UP)) yPos -= speed;
		if (screen.getInput().isKeyPressed(KeyEvent.VK_DOWN)) yPos += speed;
	}

}
