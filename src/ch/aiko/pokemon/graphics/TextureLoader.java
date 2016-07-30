package ch.aiko.pokemon.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import ch.aiko.modloader.LoadedMod;
import ch.aiko.modloader.ModLoader;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.util.ImageUtil;

import javax.imageio.ImageIO;

public class TextureLoader {
	
	public static BufferedImage loadImage(String path, Class<?> caller) {
		try {
			return ImageIO.read(caller.getClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static GIF loadGIF(String path, float scale) {
		if(path.startsWith("/")) path = path.substring(1);
		for(LoadedMod mod : ModLoader.loadedMods) {
			InputStream inStream = mod.loader.getResourceAsStream(path);
			InputStream inStream2 = mod.loader.getResourceAsStream("/" + path);
			if(inStream != null) {
				try {
					System.out.println("Found image " + path + " in Mod: " + mod.name);
					return new GIF(ImageUtil.readGif(inStream, scale), scale);
				} catch (Throwable e) {
					e.printStackTrace(Pokemon.out);
				}
			} else if(inStream2 != null) {
				try {
					System.out.println("Found image " + path + " in Mod: " + mod.name);
					return new GIF(ImageUtil.readGif(inStream2, scale), scale);
				} catch (Throwable e) {
					e.printStackTrace(Pokemon.out);
				}
			}
		}
		Pokemon.out.err("Error loading " + path + ". It cannot be found. EVERYBODY PANIC!!!");
		return null;
	}
	
}
