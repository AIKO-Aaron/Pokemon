package ch.aiko.engine;

public abstract class Menu {
	
	public abstract void onOpen();
	public abstract void onClose();
	public abstract void update(double delta);
	public abstract void draw();
	public abstract String name();
	
	public boolean doesPauseGame() {
		return false;
	}
	
	public boolean doesPausePlayer() {
		return true;
	}
	
	public boolean canClose() {
		return true;
	}
	
}
