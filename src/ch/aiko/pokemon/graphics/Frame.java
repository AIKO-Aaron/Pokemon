package ch.aiko.pokemon.graphics;

import java.awt.Graphics;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.graphics.menu.Menu;
import ch.aiko.pokemon.level.Level;
import ch.aiko.util.ImageUtil;

public class Frame extends Window {
	private static final long serialVersionUID = 8804880656221396300L;

	public static final int WIDTH = 960;
	public static final int HEIGHT = 540;

	public int[] pixels;
	private Level level;

	public static int FPS = 64;
	public static Timer timer = new Timer();

	int count = 0;
	int fps = 3;
	long lastTime = System.currentTimeMillis();

	public static Thread Draw, Update;

	private boolean isMenuOpened = false;
	private Menu openedMenu;

	private Drawer drawer = new Drawer(this);

	public Frame(Level level) {
		this.level = level;
		setFPS(120);
	}

	public void update() {
		if (getTimesPressed(KeyEvent.VK_ESCAPE) > 0 && !level.getPlayer().isInFight()) if (isMenuOpened && openedMenu.canClose()) closeMenu();
		else quit();

		if (level == null) return;
		level.update(Frame.this);

		if (isMenuOpened && openedMenu != null) openedMenu.update(drawer);

		long time = System.currentTimeMillis();
		if (time - lastTime > 1000) {
			lastTime = time;
			fps = count;
			if (fps < 60) Frame.FPS++;
			if (fps > 60) Frame.FPS--;
			if(fps != 60) {
				timer.cancel();
				Draw = Update = null;
				setup();
			}
			setTitle("Pokemon | " + fps + " FPS");
			count = 0;
		}
		count++;

	}

	public void paint(Graphics g) {
		clear();

		if (level == null) return;
		level.paint(g, Frame.this);
		level.paint(drawer);
		if (isMenuOpened && openedMenu != null) openedMenu.paint(drawer);

		BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		if (pixels.length < WIDTH * HEIGHT) clear();
		img.setRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
		img = ImageUtil.toBufferedImage(img.getScaledInstance(getWidth(), getHeight() - 22, BufferedImage.SCALE_SMOOTH));
		g.drawImage(img, 0, 22, getWidth(), getHeight() - 22, null);

		if (isMenuOpened && openedMenu != null) openedMenu.paint(g);
	}

	public void setup() {
		setTitle("Pokemon | " + fps + "FPS");
		pixels = new int[WIDTH * HEIGHT];
		clear();
		
		timer = new Timer();

		if (Draw == null) Draw = new Thread("Draw") {
			public void run() {
				timer.schedule(new TimerTask() {
					public void run() {
						fps++;
						repaint();
					}
				}, 0, 1000 / FPS);
			}
		};

		if (Update == null) Update = new Thread("Update") {
			public void run() {
				timer.schedule(new TimerTask() {
					public void run() {
						update();
						
						//System.out.println(mouseX + ":" + mouseY);
					}
				}, 0, 1000 / FPS);
			}
		};

		if (!Draw.isAlive()) Draw.start();
		if (!Update.isAlive()) Update.start();

	}

	public void clear() {
		if(level != null && level.getPlayer().isInFight()) return;
		
		pixels = new int[WIDTH * HEIGHT];
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = 0xFF000000;
	}

	public void ancestorResized(HierarchyEvent e) {
		setup();
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void openMenu(Menu m) {
		if (openedMenu != null) openedMenu.onClose(drawer);
		this.isMenuOpened = true;
		this.openedMenu = m;
		m.onOpen(new Drawer(this));
	}

	public void closeMenu() {
		openedMenu.onClose(new Drawer(this));
		this.isMenuOpened = false;
		this.openedMenu = null;
	}

	public void quit() {
		System.out.println("Saving and exiting game");
		save();

		super.quit();
	}

	public void save() {
		Pokemon.save();
	}

	public Drawer getDrawer() {
		return drawer;
	}

	public static void reloadTextures() {

	}

	public Menu getOpenedMenu() {
		return openedMenu;
	}
}
