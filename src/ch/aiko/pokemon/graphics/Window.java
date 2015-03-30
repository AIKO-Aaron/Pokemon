package ch.aiko.pokemon.graphics;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public abstract class Window extends JFrame implements ActionListener, WindowListener, WindowStateListener, HierarchyBoundsListener, MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {

	private static Timer timer;
	public boolean running = false;

	public int mouseX;
	public int mouseY;
	public int mouseScreenX;
	public int mouseScreenY;

	public int mouseButton = 0;
	public boolean mousePressed = false;
	public boolean mouseInWindow = true;

	private boolean[] isKeyPressed = new boolean[256];
	private HashMap<Integer, Integer> keys = new HashMap<Integer, Integer>();

	public Window(int width, int height) {
		setLayout(null);
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(this);
		addWindowStateListener(this);
		addHierarchyBoundsListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);

		timer = new Timer(1000 / 60, this);
		timer.start();

		setup();
		setVisible(true);
	}

	public Window() {
		setLayout(null);
		setSize(960, 540);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(this);
		addWindowStateListener(this);
		addHierarchyBoundsListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);

		timer = new Timer(1000 / 60, this);
		// timer.start();

		setup();
		setVisible(true);
	}

	public void setFPS(int fps) {
		timer.setDelay(1000 / fps);
	}

	public void loopStart() {
		running = true;
	}

	public void loopStop() {
		running = false;
		timer.stop();
	}

	public void quit() {
		loopStop();
		System.exit(0);
	}

	public void actionPerformed(ActionEvent e) {
		if (running) {
			update();
			repaint();
		}
	}

	public void windowOpened(WindowEvent e) {}

	public void windowClosing(WindowEvent e) {quit();}

	public void windowClosed(WindowEvent e) {
		quit();
	}

	public void windowIconified(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowActivated(WindowEvent e) {}

	public void windowDeactivated(WindowEvent e) {}

	public void windowStateChanged(WindowEvent e) {}

	public void ancestorMoved(HierarchyEvent e) {}

	public void ancestorResized(HierarchyEvent e) {}

	public void mouseWheelMoved(MouseWheelEvent e) {

	}

	public void mouseClicked(MouseEvent e) {
		mousePressed = true;
		mouseX = e.getX();
		mouseY = e.getY();
		mouseScreenX = e.getXOnScreen();
		mouseScreenY = e.getYOnScreen();
		mouseButton = e.getButton();
		mousePressed = false;
	}

	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		mouseX = e.getX();
		mouseY = e.getY();
		mouseScreenX = e.getXOnScreen();
		mouseScreenY = e.getYOnScreen();
		mouseButton = e.getButton();
	}

	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
	}

	public void mouseEntered(MouseEvent e) {
		mouseInWindow = true;
	}

	public void mouseExited(MouseEvent e) {
		mouseInWindow = false;
	}

	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		mouseScreenX = e.getXOnScreen();
		mouseScreenY = e.getYOnScreen();
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		mouseScreenX = e.getXOnScreen();
		mouseScreenY = e.getYOnScreen();
	}

	public void keyTyped(KeyEvent e) {
		if(!keys.containsKey(e.getKeyCode())) keys.put(e.getKeyCode(), 0);
		keys.put(e.getKeyCode(), keys.get(e.getKeyCode()) + 1);
	}

	public void keyPressed(KeyEvent e) {
		keyTyped(e);
		if (e.getKeyCode() < isKeyPressed.length) isKeyPressed[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < isKeyPressed.length) isKeyPressed[e.getKeyCode()] = false;
	}

	public boolean isKeyPressed(int keyCode) {
		if (keyCode < isKeyPressed.length) return isKeyPressed[keyCode];

		return false;
	}
	
	public int getTimesPressed(int keyCode) {
		if(!keys.containsKey(keyCode)) return 0;
		int ret = keys.get(keyCode);
		if(ret > 0) keys.put(keyCode, keys.get(keyCode) - 1);
		return ret;
	}
	
	public void press(int keyCode) {
		if(!keys.containsKey(keyCode)) keys.put(keyCode, 1);
		else keys.put(keyCode, keys.get(keyCode) + 1);
	}

	public abstract void update();

	public abstract void setup();

	public abstract void paint(Graphics g);
}
