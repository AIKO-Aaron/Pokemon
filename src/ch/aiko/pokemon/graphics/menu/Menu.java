package ch.aiko.pokemon.graphics.menu;

import java.awt.Graphics;

import ch.aiko.pokemon.graphics.Drawer;


public  abstract class Menu {
	
	public abstract String name();
	
	public void update(Drawer d) {
		
	}
	
	public void paint(Drawer d) {
		
	}
	
	public void onOpen(Drawer d) {
		
	}
	
	public void onClose(Drawer d) {
		
	}

	public void paint(Graphics g) {
		
	}

}
