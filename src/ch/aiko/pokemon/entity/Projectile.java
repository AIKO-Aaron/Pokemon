package ch.aiko.pokemon.entity;

import ch.aiko.pokemon.entity.particle.Particle;
import ch.aiko.pokemon.graphics.Frame;
import ch.aiko.pokemon.sprite.Sprite;

public class Projectile extends Entity {

	private int xMot, yMot;
	private int speed = 1;
	private int deathTime = 0;
	
	public Projectile(int x, int y, int xMot, int yMot, int deathTime) {
		super(x, y, new Sprite(0xFF000000, 16, 16));
		this.xMot = xMot;
		this.yMot = yMot;
		this.deathTime = deathTime;
	}

	public void update(Frame f) {
		if(checkCollisionX(f, xMot, speed) || checkCollisionY(f, yMot, speed)) {
			new Particle(x, y, (int) (60 * 0.5f), 60, Frame.getLevel());
			Frame.getLevel().removeEntity(this);
		}
		
		x += xMot * speed;
		y += yMot * speed;
		
		deathTime--;
		if(deathTime <= 0) Frame.getLevel().removeEntity(this);
	}

	public void paint() {
		
	}

}
