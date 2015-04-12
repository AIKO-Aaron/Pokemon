package ch.aiko.pokemon.sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Set;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

public class SoundPlayer {

	private static boolean running = false;
	private static Player currentLoop;

	public static Player playSound(String s) {
		try {
			final BufferedInputStream bis = new BufferedInputStream(SoundPlayer.class.getClassLoader().getResourceAsStream(s));
			final Player player = new Player(bis);
			new Thread(s) {
				public void run() {
					try {
						player.play();
					} catch (JavaLayerException e) {
						e.printStackTrace();
					} finally {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			return player;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String file;
	public static MusicThread music;

	public static Player loopSound(final float volume) {
		try {
			music = new MusicThread(volume);
			music.start();
			return currentLoop;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Player loopSound(String file, final float volume) {
		if(music != null) music.close();
		running = false;
		if(currentLoop != null) currentLoop.close();
		SoundPlayer.file = file;
		return loopSound(volume);
	}

	public static void changeLoop(String s) {
		final BufferedInputStream bis = new BufferedInputStream(SoundPlayer.class.getClassLoader().getResourceAsStream(s));
		try {
			currentLoop = new Player(bis);
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}

	public static void stopLoop() {
		running = false;
		currentLoop.close();
	}

	public static void startloop() {
		running = true;
		loopSound("/ch/aiko/pokemon/sounds/1sec.mp3", -100F);
	}

	// Debug-Tool
	public static void find() {
		Set<String> files = new Reflections("ch.aiko.pokemon.sounds", new ResourcesScanner()).getResources(new com.google.common.base.Predicate<String>() {
			public boolean apply(String arg0) {
				return arg0.endsWith(".mp3");
			}
		});

		for (String s : files) {
			System.out.println("Found Sound File: " + s);
		}
	}

	public static class MusicThread extends Thread {
		public float volume;

		public MusicThread(float volume) {
			this.volume = volume;
		}

		public void run() {
			running = true;
			while (running && !isInterrupted()) {
				try {
					BufferedInputStream bis = new BufferedInputStream(SoundPlayer.class.getClassLoader().getResourceAsStream(file));
					currentLoop = new Player(bis);
					currentLoop.play(volume);
					bis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public void close() {
			interrupt();
		}
	}
}
