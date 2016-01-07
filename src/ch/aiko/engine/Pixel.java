package ch.aiko.engine;

import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Pixel {

	public int[] pixelColors;
	public BufferedImage img;

	public Pixel(int width, int height) {
		img = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		pixelColors = ((DataBufferInt) (img.getRaster().getDataBuffer())).getData();
	}

}
