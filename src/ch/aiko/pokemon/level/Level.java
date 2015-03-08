package ch.aiko.pokemon.level;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import ch.aiko.pokemon.Drawer;
import ch.aiko.pokemon.Frame;
import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.entity.PressurePlate;
import ch.aiko.pokemon.mob.Mob;
import ch.aiko.pokemon.mob.Player;
import ch.aiko.pokemon.sprite.Sprite;
import ch.aiko.pokemon.sprite.Tile;
import ch.aiko.util.ImageUtil;

public class Level {
	private List<Tile> tiles = new ArrayList<Tile>();
	private List<Entity> mobs = new ArrayList<Entity>();

	private Player p;
	private Point camera = new Point(0, 0);

	private Frame f;
	private BufferedImage img;

	private int width, height;
	private int tileWidth, tileHeight;

	public int displaywidth;

	public Level(Player p, String image, HashMap<Integer, Tile> coding, int TileWidth, int TileHeight) {
		if (p == null) System.exit(1);
		this.p = p;

		img = ImageUtil.loadImageInClassPath(image);

		width = img.getWidth();
		height = img.getHeight();
		this.tileWidth = TileWidth;
		this.tileHeight = TileHeight;

		generate(coding);
	}

	private void generate(HashMap<Integer, Tile> coding) {
		int[] pixels = new int[width * height];
		pixels = img.getRGB(0, 0, width, height, pixels, 0, width);

		for (Entry<Integer, Tile> tiles : coding.entrySet()) {
			Tile t = tiles.getValue();
			if (t.getWidth() != width || t.getHeight() != height) {
				t.sprite.setImage(ImageUtil.toBufferedImage(t.sprite.getImage().getScaledInstance(tileWidth, tileHeight, BufferedImage.SCALE_REPLICATE)));
				coding.put(tiles.getKey(), t);
			}
		}

		for (int i = 0; i < width * height; i++) {
			int rgb = pixels[i];
			Tile t = coding.get(rgb);
			if (t == null) continue;
			if (t instanceof Entity) {
				if (t instanceof PressurePlate) {
					PressurePlate e = ((PressurePlate) t).copy();
					e.x = i % width * tileWidth;
					e.y = i / width * tileHeight;
					e.resize(tileWidth, tileHeight);
					addEntity(e);
				} else {
					Entity e = ((Entity) t).copy();
					e.x = i % width * tileWidth;
					e.y = i / width * tileHeight;
					e.resize(tileWidth, tileHeight);
					addEntity(e);
				}
			} else {
				Tile tt = new Tile(t.sprite, i % width * tileWidth, i / width * tileHeight, t.solid);
				addTile(tt);
			}
		}
	}

	public void addTile(Tile t) {
		tiles.add(t);
	}

	public void addMob(Mob m) {
		mobs.add(m);
	}

	public void addEntity(Entity m) {
		mobs.add(m);
	}

	public void addEntity(Entity m, int x, int y) {
		Entity m1 = m;
		m1.setX(x);
		m1.setY(y);
		mobs.add(m1);
	}

	public void update(Frame f) {
		for (int i = 0; i < mobs.size(); i++) {
			Entity m = mobs.get(i);
			if (m == null) continue;
			m.update(f);
		}

		p.update(f);
	}

	public void paint(Graphics g, Frame f) {
		this.f = f;

		if (tiles == null || mobs == null) return;

		displaywidth = f.getWidth();

		for (Tile tile : tiles) {
			if (tile == null || tile.x + tile.getWidth() < camera.x || tile.x > camera.x + displaywidth || tile.y + tile.getHeight() < camera.y || tile.y > camera.y + f.getHeight()) continue;
			drawTile(tile);
		}
		for (int i = 0; i < mobs.size(); i++) {
			Entity tile = mobs.get(i);
			if (tile == null) continue;
			if (tile.getsRendered()) drawTile(tile.getSprite(), tile.getX(), tile.getY());
			tile.paint(g, f);
		}

		drawTile(p.getSprite(), p.getX(), p.getY());
		p.paint(g, f);

		for (int i = 0; i < mobs.size(); i++) {
			Entity tile = mobs.get(i);
			if (tile == null) continue;
			tile.paintOverPlayer(g, f);
		}

		p.paintOverPlayer(g, f);
	}

	public void drawTile(Sprite img, int x, int y) {
		int[] pi = img.getPixels();
		for (int i = 0; i < img.getWidth() + img.getHeight() * displaywidth; i++) {
			int xx = (i % img.getWidth() + x) - camera.x;
			int yy = (i / img.getWidth() + y + 22) - camera.y;

			if (xx < 0 || xx >= displaywidth) continue;

			if (xx + yy * displaywidth < f.pixels.length && i < pi.length && i >= 0 && xx + yy * displaywidth >= 0) f.pixels[xx + yy * displaywidth] = pi[i] == 0 ? getColor(xx, yy) : pi[i];

		}
	}

	protected int getColor(int x, int y) {
		if (x + y * f.getWidth() < 0 || x + y * f.getWidth() >= f.pixels.length) return 0;
		return f.pixels[x + y * f.getWidth()];
	}

	public void drawTile(Tile t) {
		int[] pi = t.getPixels();
		for (int i = 0; i < t.getWidth() * t.getHeight(); i++) {
			int xx = (i % t.getWidth() + t.x) - camera.x;
			int yy = (i / t.getWidth() + t.y + 22) - camera.y;

			if (xx < 0 || xx >= displaywidth) continue;

			if (xx + yy * displaywidth < f.pixels.length && i < pi.length && i >= 0 && xx + yy * displaywidth >= 0) f.pixels[xx + yy * displaywidth] = pi[i];
		}
	}

	public Point getCamera() {
		return new Point(camera.x, camera.y - 22);
	}

	public void setCamera(int x, int y) {
		camera.x = x;
		camera.y = y;
	}

	public boolean checkCollisionX(int mobx, int moby, int mobwidth, int mobheight) {
		for (Tile tile : tiles) {
			if (!(moby < tile.y + tile.getHeight() && moby + mobheight > tile.y)) continue;
			if (!tile.solid) continue;

			if (tile.x + tile.getWidth() > mobx && tile.x < mobx + mobwidth) return true;
		}
		return false;
	}

	public boolean isOnTile(Mob mob) {
		for (Tile tile : tiles) {
			if (mob.getX() >= tile.x && mob.getX() <= tile.x + tile.getWidth()) return true;
		}
		return false;
	}

	public Tile[] getTilesOn(Entity e) {
		ArrayList<Tile> ti = new ArrayList<Tile>();
		for (Tile t : tiles) {
			if (checkCollisionX(e.getX(), e.getY(), e.getWidth(), e.getHeight()) || checkCollisionY(e.getX(), e.getY(), e.getWidth(), e.getHeight())) ti.add(t);
		}
		return ti.toArray(new Tile[ti.size()]);
	}

	public boolean checkCollisionY(int mobx, int moby, int mobwidth, int mobheight) {
		for (Tile tile : tiles) {
			if (!(mobx < tile.x + tile.getWidth() && mobx + mobwidth > tile.x)) continue;
			if (!tile.solid) continue;

			if (tile.y + tile.getHeight() > moby && tile.y < moby + mobheight) return true;
		}
		return false;
	}

	public Player getPlayer() {
		return p;
	}

	public void removeEntity(Entity e) {
		mobs.remove(e);
	}

	public Point getCoordinatesFromOnScreenPoint(int x, int y) {
		Point p = new Point(0, 0);
		p.x = camera.x + x;
		p.y = camera.y + y;
		return p;
	}

	public void paint(Drawer drawer) {
		p.paint(drawer);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
