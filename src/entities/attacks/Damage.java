package entities.attacks;

import java.util.ArrayList;

import entities.Entity;
import game.Game;
import graphics.SpriteLoader;
import math.Rect;
import math.Vec;

public class Damage extends Entity {

	private Rect hitbox;
	private double percentDamage;
	private Vec hitVelocity;
	private int hitLagFrames;
	private int teamCreatedBy;
	private Rect toRender=null;
	
	private static final boolean shouldRenderDamage=true;
	
	public Damage(Rect hitbox, double percentDamage, Vec hitVelocity, int hitLagFrames, int teamCreatedBy) {
		this.hitbox=hitbox;
		this.percentDamage=percentDamage;
		this.hitVelocity=hitVelocity;
		this.hitLagFrames=hitLagFrames;
		this.teamCreatedBy=teamCreatedBy;
	}
	
	public Rect getHitbox() {
		return hitbox;
	}
	
	public double getPercentDamage() {
		return percentDamage;
	}
	
	public Vec getHitVelocity() {
		return hitVelocity;
	}
	
	public int getHitLagFrames() {
		return hitLagFrames;
	}
	
	public int getTeam() {
		return teamCreatedBy;
	}

	public void runScan(boolean isFacingRight, Vec offset) {
		Rect oldHitbox=hitbox;
		if (!isFacingRight) {
			hitVelocity=hitVelocity.flipX();
			hitbox=hitbox.flipX();
		}
		hitbox=hitbox.offsetBy(offset);
		toRender=hitbox;
		
		ArrayList<Entity> entities=Game.getScene().getEntities();
		for (Entity e:entities)
			e.processDamage(this);
		
		if (!isFacingRight)
			hitVelocity=hitVelocity.flipX();
		hitbox=oldHitbox;
	}
	
	public void render() {
		if (!shouldRenderDamage)
			return;
		if (toRender!=null)
			toRender.render();
	}
}
