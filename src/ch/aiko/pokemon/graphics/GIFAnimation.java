package ch.aiko.pokemon.graphics;

import ch.aiko.engine.graphics.Renderable;
import ch.aiko.engine.graphics.Renderer;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.engine.graphics.Updatable;
import ch.aiko.util.ImageFrame;
import ch.aiko.util.ImageUtil;

public class GIFAnimation implements Renderable, Updatable {

	protected int x, y;
	protected ImageFrame[] animation;
	protected int curIndex = 0;
	protected long lastTime;
	int maxWidth = 0, maxHeight = 0;

	public GIFAnimation(String path, int x, int y) {
		this.x = x;
		this.y = y;
		animation = ImageUtil.readGifInClassPath(path, 2);
		lastTime = 0L;
		for (ImageFrame frame : animation) {
			maxWidth = Math.max(maxWidth, frame.getWidth());
			maxHeight = Math.max(maxHeight, frame.getHeight());
		}
	}
	
	public GIFAnimation(String path) {
		animation = ImageUtil.readGifInClassPath(path, 2);
		lastTime = 0L;
		for (ImageFrame frame : animation) {
			maxWidth = Math.max(maxWidth, frame.getWidth());
			maxHeight = Math.max(maxHeight, frame.getHeight());
		}
	}

	public GIFAnimation replaceColor(int old, int newc) {
		for (int i = 0; i < animation.length; i++) {
			ImageFrame ol = animation[i];
			animation[i] = new ImageFrame(ImageUtil.replaceColor(ol.getImage(), old, newc), ol.getDelay(), ol.getDisposal(), ol.getWidth(), ol.getHeight());
		}
		return this;
	}

	public void update(Screen screen) {
		if (lastTime == 0L) lastTime = System.currentTimeMillis();
		if (System.currentTimeMillis() - lastTime > animation[curIndex].getDelay() * 10) {
			curIndex++;
			curIndex %= animation.length;
			lastTime = System.currentTimeMillis();
		}
	}

	public void render(Renderer renderer) {
		renderer.drawImage(animation[curIndex].getImage(), x + (maxWidth - animation[curIndex].getWidth()) / 2, y+ (maxHeight - animation[curIndex].getHeight()) / 2);
	}
	
	public void render(Renderer renderer, int x, int y) {
		renderer.drawImage(animation[curIndex].getImage(), x + (maxWidth - animation[curIndex].getWidth()) / 2, y+ (maxHeight - animation[curIndex].getHeight()) / 2);
	}

	public int getMaxWidth() {
		return maxWidth;
	}
	
	public int getMaxHeight() {
		return maxHeight;
	}

}
