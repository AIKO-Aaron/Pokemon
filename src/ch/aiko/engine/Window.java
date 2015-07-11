package ch.aiko.engine;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public abstract class Window extends JFrame implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, WindowStateListener, WindowListener, HierarchyBoundsListener, ComponentListener {

	private static final long serialVersionUID = 3071280581916760929L;

	public static int WIDTH;
	public static int HEIGHT;
	public static int FPS = 60;
	public static int UPS = 60;
	public static String title;

	public static float SCALE_X, SCALE_Y;
	public static Pixel pixels;

	private long lastTime;
	private int ups = 59, fps = 59;

	protected static Menu menu;
	private static boolean menuOpen = false;
	public static boolean autoclear = true;

	public static double delta, ddelta;

	public static BufferedImage Background = null;

	public static Menu getMenu() {
		return menu;
	}

	public static final Window instance = new Window() {
		private static final long serialVersionUID = 3875512653667267851L;

		public void draw() {}

		public void update(double delta) {}
	};

	public static void create(String title, int width, int height, int fps, int ups, Window w) {
		WIDTH = width;
		HEIGHT = height;
		Window.title = title;
		FPS = fps;
		UPS = ups;

		w.init(WIDTH, HEIGHT, title);
	}

	public static void create(String title, int w, int h, Window wi) {
		WIDTH = w;
		HEIGHT = h;
		Window.title = title;

		wi.init(w, h, title);
	}

	public static void create(String title, int w, int h) {
		WIDTH = w;
		HEIGHT = h;
		Window.title = title;

		instance.init(w, h, title);
	}

	public static void setFPS(int fps) {
		Window.FPS = fps;
	}

	public static void setUPS(int ups) {
		Window.UPS = ups;
	}

	public boolean isMenuOpened() {
		return menuOpen;
	}

	public static void openMenu(Menu menu) {
		menu.onOpen();
		Window.menu = menu;
		menuOpen = true;
	}

	public static void closeMenu() {
		menu.onClose();
		menu = null;
		menuOpen = false;
	}

	public Window() {}

	public void init(int w, int h, String s) {
		setSize(w, h);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(s);
		setVisible(true);

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addWindowListener(this);
		addHierarchyBoundsListener(this);
		addComponentListener(this);

		componentResized(null);

		pixels = new Pixel(WIDTH * HEIGHT);

		new Thread() {
			public void run() {
				while (isVisible()) {
					long time = System.nanoTime();
					m_draw(ddelta);
					long end = System.nanoTime();
					ddelta = (end - time) / 10000000;
					double sleepTime = ((1000000000D / FPS) - (end - time));
					pause(sleepTime);
				}
			}
		}.start();

		new Thread() {
			public void run() {
				double delta = System.nanoTime();
				while (isVisible()) {
					long now = System.nanoTime();
					m_update(delta);
					double sleepTime = ((1000000000D / UPS) - (System.nanoTime() - now));
					pause(sleepTime);
					Window.delta = sleepTime / 10000000;
					delta = sleepTime;
				}
			}
		}.start();
	}

	public void pause(double nanos) {
		long start = System.nanoTime();
		while (start > System.nanoTime() - nanos) {
			if (start - (System.nanoTime() - nanos) > 1000000) try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void paint(Graphics g) {
		if (pixels == null) return;
		int[] pi = pixels.pixelColors.clone();
		BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		img.setRGB(0, 0, WIDTH, HEIGHT, pi, 0, WIDTH);
		// g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(img.getScaledInstance(getWidth(), getHeight(), BufferedImage.SCALE_DEFAULT), 0, 0, getWidth(), getHeight(), null);
		if (Background != null) g.drawImage(Background, 0, 0, (int) (WIDTH * SCALE_X), (int) (HEIGHT * SCALE_Y), null);
		if (autoclear) Renderer.black();
		if (autoclear) Renderer.clear();
	}

	private final void m_draw(double delta) {
		if (menu == null || !menu.doesPauseGame()) draw();
		if (menu != null) menu.draw(delta);
		repaint();
		fps++;
	}

	private final void m_update(double delta) {
		if (menu != null) menu.update(delta / 10000000);
		if (menu == null || !menu.doesPauseGame()) update(delta / 10000000);

		ups++;

		long now = System.nanoTime();

		if (now - lastTime >= 1000000000) {
			System.out.println("FPS: " + fps + " UPS: " + ups);
			fps = 0;
			ups = 0;
			lastTime = now;
		}
	}

	public abstract void draw();

	public abstract void update(double delta);

	public void keyPressed(KeyEvent e) {
		KeyBoard.keyPressed(e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		KeyBoard.keyReleased(e.getKeyCode());
	}

	public void keyTyped(KeyEvent e) {}

	public void mouseWheelMoved(MouseWheelEvent arg0) {
		Mouse.mouseWheel(arg0.getScrollAmount());
	}

	public void mouseDragged(MouseEvent e) {
		Mouse.updateMouse(e.getX(), e.getY());
		Mouse.mousePressed(e.getButton());
	}

	public void mouseMoved(MouseEvent e) {
		Mouse.updateMouse(e.getX(), e.getY());
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		Mouse.updateMouse(e.getX(), e.getY());
		Mouse.mousePressed(e.getButton());
	}

	public void mouseReleased(MouseEvent e) {
		Mouse.updateMouse(e.getX(), e.getY());
		Mouse.mouseReleased(e.getButton());
	}

	public void actionPerformed(ActionEvent arg0) {}

	public void windowActivated(WindowEvent e) {}

	public void windowClosed(WindowEvent e) {}

	public void windowClosing(WindowEvent e) {
		System.out.println("Closed Window");
	}

	public void windowDeactivated(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {}

	public void windowOpened(WindowEvent e) {}

	public void windowStateChanged(WindowEvent e) {}

	public void ancestorMoved(HierarchyEvent e) {

	}

	public void ancestorResized(HierarchyEvent e) {
		SCALE_X = (float) getWidth() / (float) WIDTH;
		SCALE_Y = (float) getHeight() / (float) HEIGHT;
		repaint();
	}

	public void componentResized(ComponentEvent e) {
		SCALE_X = (float) getWidth() / (float) WIDTH;
		SCALE_Y = (float) getHeight() / (float) HEIGHT;
		repaint();
	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentShown(ComponentEvent e) {

	}

	public void componentHidden(ComponentEvent e) {

	}
}
