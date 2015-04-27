package ch.aiko.engine;

public class Pixel {

	public int[] pixelColors;
	public int[] pixelLayers;
	public boolean[] pixelSet;

	public Pixel(int size) {
		pixelColors = new int[size];
		pixelLayers = new int[size];
		pixelSet = new boolean[size];
	}

}
