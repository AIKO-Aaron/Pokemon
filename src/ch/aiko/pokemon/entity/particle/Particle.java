package ch.aiko.pokemon.entity.particle;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.aiko.pokemon.entity.Entity;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.level.Level;
import ch.aiko.pokemon.sprite.Sprite;

public class Particle extends Entity {
	
	public static final Random random = new Random();
	public static final Sprite particle = new Sprite(0xFFAAAAAA, 3, 3);
	private List<Particle> particles = new ArrayList<Particle>();
	
	private int life;
	protected double xx, yy, xa, ya;
	
	public Particle(int x, int y, int life) {
		super(x, y, particle);
		this.life = life;
		this.xx = x;
		this.yy = y;
		
		this.xa = random.nextGaussian();
		this.ya = random.nextGaussian();
		
		//System.out.println(ya);
		
		getsRendered = false;
	}
	
	public Particle(int x, int y, int life, int amount, Level l) {
		this(x, y, life);
		for(int i = 0; i < amount - 1; i++) {
			particles.add(new Particle(x, y, life));
			l.addEntity(particles.get(i));
		}
		particles.add(this);
	}

	public void update(Frame mainFrame) {
		this.xx += xa;
		this.yy += ya;
		
		if(checkCollisionX(mainFrame, (int) xa, 1)) xa = 0;
		if(checkCollisionY(mainFrame, (int) ya, 1)) ya = 0;
		
		xa -= xa / life;
		ya -= ya / life;
		
		life--;
		
		if(life <= 0) mainFrame.getLevel().removeEntity(this);
		this.x = (int) xx;
		this.y = (int) yy;
	}

	public void paint(Graphics g, Frame f) {
		
	}

	public void paintOverPlayer(Graphics g, Frame f) {
		f.getLevel().drawTile(sprite, (int) xx, (int) yy);
	}

}