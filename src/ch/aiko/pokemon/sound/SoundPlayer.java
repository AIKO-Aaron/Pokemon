package ch.aiko.pokemon.sound;

import java.io.BufferedInputStream;
import java.io.IOException;

import ch.aiko.pokemon.settings.Settings;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.PlaybackListener;

public class SoundPlayer {

	private static boolean running = false;
	public static Player currentLoop;
	public static int times_played = 0;

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
	public static boolean playing = false;

	public static Player loopSound(final float volume) {
		if (currentLoop != null || playing) currentLoop.close();
		if (music != null || playing) music.close();
		playing = true;
		times_played = 0;
		running = false;
		try {

			// 0, Integer.MAX_VALUE
			music = new MusicThread(volume, 0, Integer.MAX_VALUE, null);
			music.start();
			return currentLoop;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Player loopSound(final float volume, PlaybackListener l) {
		if (currentLoop != null || playing) currentLoop.close();
		if (music != null || playing) music.close();
		playing = true;
		times_played = 0;
		running = false;
		try {

			// 0, Integer.MAX_VALUE
			music = new MusicThread(volume, 0, Integer.MAX_VALUE, l);
			music.start();
			return currentLoop;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Player loopSound(final float volume, int s, int end) {
		if (currentLoop != null || playing) currentLoop.close();
		if (music != null || playing) music.close();
		playing = true;
		times_played = 0;
		running = false;
		try {
			music = new MusicThread(volume, s, end, null);
			music.start();
			return currentLoop;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Player loopSound(final float volume, int s, int end, PlaybackListener l) {
		if (currentLoop != null || playing) currentLoop.close();
		if (music != null || playing) music.close();
		playing = true;
		times_played = 0;
		running = false;
		try {
			music = new MusicThread(volume, s, end, l);
			music.start();
			return currentLoop;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Player loopSound(String file, final float volume) {
		times_played = 0;
		if (music != null) music.close();
		running = false;
		if (currentLoop != null) currentLoop.close();
		SoundPlayer.file = file;
		return loopSound(volume);
	}
	
	public static Player loopSound(String file, final float volume, PlaybackListener l) {
		times_played = 0;
		if (music != null) music.close();
		running = false;
		if (currentLoop != null) currentLoop.close();
		SoundPlayer.file = file;
		return loopSound(volume, l);
	}
	
	public static Player loopSound(String file, final float volume, int st, int en, PlaybackListener l) {
		times_played = 0;
		if (music != null) music.close();
		running = false;
		if (currentLoop != null) currentLoop.close();
		SoundPlayer.file = file;
		return loopSound(volume, st, en, l);
	}

	public static Player loopSound(String file, final float volume, final int s, final int e) {
		times_played = 0;
		if (music != null) music.close();
		running = false;
		if (currentLoop != null) currentLoop.close();
		SoundPlayer.file = file;
		return loopSound(volume, s, e);
	}

	public static void changeLoop(String s) {
		times_played = 0;
		final BufferedInputStream bis = new BufferedInputStream(SoundPlayer.class.getClassLoader().getResourceAsStream(s));
		try {
			currentLoop = new Player(bis);
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}

	public static void stopLoop() {
		int timeout = 100;
		while((music == null || currentLoop == null) && playing) {
			System.out.println("Nothing Playing");
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			timeout--;
			if(timeout == 0) return;
		}
		
		times_played = 0;
		running = false;
		currentLoop.close();
	}

	public static void startloop() {
		times_played = 0;
		running = true;
		if (music == null) loopSound("/ch/aiko/pokemon/sounds/1sec.mp3", -100F);
		else loopSound(Settings.GAIN);
	}

	/** Debug-Tool
	public static void find() {
		Set<String> files = new Reflections("ch.aiko.pokemon.sounds", new ResourcesScanner()).getResources(new com.google.common.base.Predicate<String>() {
			public boolean apply(String arg0) {
				return arg0.endsWith(".mp3");
			}
		});

		for (String s : files) {
			System.out.println("Found Sound File: " + s);
		}
	}*/

	public static void loopSound() {

	}

	public static class MusicThread extends Thread {
		public float volume;
		private int start_frame;
		private int end_frame;
		private PlaybackListener listener;
		
		public MusicThread(float volume, int start_frame, int end_frame, PlaybackListener l) {
			this.volume = volume;
			this.start_frame = start_frame;
			this.end_frame = end_frame;
			this.listener = l;
		}

		public void run() {
			running = true;
			while (running && !isInterrupted()) {
				try {
					BufferedInputStream bis = new BufferedInputStream(SoundPlayer.class.getClassLoader().getResourceAsStream(file));
					currentLoop = new Player(bis);
					currentLoop.setListener(listener);
					currentLoop.play(start_frame, end_frame, volume);
					bis.close();
					times_played++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public void close() {
			interrupt();
			playing = false;
		}
	}
}
