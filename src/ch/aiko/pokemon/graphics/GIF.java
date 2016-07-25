package ch.aiko.pokemon.graphics;

import ch.aiko.util.ImageFrame;
import ch.aiko.util.ImageUtil;

public class GIF {

	public ImageFrame[] animation;
	public float scale = 1f;
	
	public GIF(String path) {
		animation = ImageUtil.readGifInClassPath(path, 1);
	}
	
	public GIF(String path, float scale) {
		animation = ImageUtil.readGifInClassPath(path, scale);
		this.scale = scale;
	}
	
}
