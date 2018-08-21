package entities.particles;

import java.util.Random;

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
	private int renderOrder=-1;
	
	public static void createDoubleJumpParticle(Vec position) {
		new Particle(position, Vec.zero, SpriteLoader.doubleJumpParticleSprite, true, 60, .1, 1.5, .5, 0);
	}
	
	public static void createRunTurnSprite(Vec position, boolean nowFacingRight) {
		new Particle(position, (nowFacingRight?Vec.left:Vec.right).scale(1), SpriteLoader.runTurnParticleSprite, !nowFacingRight, 80, .4, .4, .5, 0);
	}
	
	public static void createStunParticle(Vec position) {
		double scale=Math.random()*.5;
		new Particle(position, Vec.zero, SpriteLoader.stunParticleSprite, true, 60, scale, scale, 1, 1);
	}
	
	public static void createSmokeParticle(Vec position) {
		Random random=new Random();
		Sprite sprite=SpriteLoader.airSmokeSprites[random.nextInt(SpriteLoader.airSmokeSprites.length)];
		Vec offset=new Vec(Math.random()*20-10, Math.random()*20-10);
		Particle created=new Particle(position.add(offset), Vec.zero, sprite, random.nextBoolean(), 100, 0.5, 0.6, 1, 0);
		created.renderOrder=2;
	}
	
	public static void createBulletParticle(Vec position, boolean facingRight) {
		System.out.println("Creating");
		Sprite sprite=SpriteLoader.carlosBullet;
		Vec velocity=(facingRight?Vec.right:Vec.left).scale(50);
		new Particle(position.add(velocity), velocity, sprite, facingRight, 120, .5, .5, 1, 1);
	}
	
	public static void createSnowballParticle(Vec position, boolean facingRight) {
		Sprite sprite=SpriteLoader.waddlesSnowball;
		Vec velocity=(facingRight?Vec.right:Vec.left).scale(25);
		new Particle(position.add(velocity), velocity, sprite, facingRight, 120, .5, .5, 1, 1);
	}
	
	public static void createKeyPressedParticle(Vec position) {
		new Particle(position, Vec.zero, SpriteLoader.keyPressedIndicatorSprite, true, 25, 0.4, 0.6, 1, 0);
	}
	
	public static void createHorizontalExplosionParticle(Vec position, boolean offRightSide) {
		Vec offset=offRightSide?Vec.left.scale(600):Vec.right.scale(600);
		new Particle(position.add(offset), Vec.zero, SpriteLoader.explosionRoundedSprite, !offRightSide, 240, 2, 2, 1, 0);
	}
	
	public static void createVerticleExplosionParticle(Vec position, boolean offTopSide) {
		Vec offset=offTopSide?Vec.down.scale(600):Vec.up.scale(600);
		int scale=offTopSide?2:-2;
		new Particle(position.add(offset), Vec.zero, SpriteLoader.explosionRoundedVerticalSprite, !offTopSide, 240, scale, scale, 1, 0);
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
		return renderOrder;
	}
	
}
