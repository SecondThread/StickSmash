package entities.particles;

import entities.Entity;
import game.Game;
import graphics.Sprite;
import graphics.SpriteLoader;
import math.Lerp;
import math.Vec;

public class Particle extends Entity {
	
	private Sprite mySprite;
	private int lifetimeCounter;
	private int lifetimeLength;
	private double startScale, endScale;
	private double startAlpha, endAlpha;
	private double alpha, scale;
	private Vec position;
	private Vec velocity;
	private boolean facingRight;
	
	public static void createDoubleJumpParticle(Vec position) {
		Particle toCreate=new Particle(position, Vec.zero, SpriteLoader.doubleJumpParticleSprite, true, 60, .1, 1.5, .5, 0);
		Game.getScene().addEntity(toCreate);
	}
	
	public static void createRunTurnSprite(Vec position, boolean nowFacingRight) {
		Particle toCreate=new Particle(position, (nowFacingRight?Vec.left:Vec.right).scale(1), SpriteLoader.runTurnParticleSprite, !nowFacingRight, 80, .4, .4, .5, 0);
		Game.getScene().addEntity(toCreate);
	}
	
	public static void createStunParticle(Vec position) {
		double scale=Math.random()*.5;
		Particle toCreate=new Particle(position, Vec.zero, SpriteLoader.stunParticleSprite, true, 60, scale, scale, 1, 1);
		Game.getScene().addEntity(toCreate);
	}
	
	private Particle(Vec position, Vec velocity, Sprite mySprite, boolean facingRight, int lifetimeLength, double startScale, double endScale, double startAlpha, double endAlpha) {
		this.position=position;
		this.velocity=velocity;
		this.mySprite=mySprite;
		this.facingRight=facingRight;
		this.lifetimeLength=lifetimeLength;
		this.startScale=startScale;
		this.endScale=endScale;
		this.startAlpha=startAlpha;
		this.endAlpha=endAlpha;
		update();
	}

	public void update() {
		lifetimeCounter++;
		if (lifetimeCounter>lifetimeLength) {
			Game.getScene().destroyEntity(this);
			return;
		}
		position=position.add(velocity);
		
		double time=lifetimeCounter/(double)lifetimeLength;
		scale=Lerp.lerp(startScale, endScale, time);
		alpha=Lerp.lerp(startAlpha, endAlpha, time);
	}
	
	public void render() {
		mySprite.drawAlphaAndSize(position, alpha, facingRight?scale:-scale, scale);
	}
	
	public int getRenderOrder() {
		return -1;
	}
	
}
