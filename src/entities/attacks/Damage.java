package entities.attacks;

import java.util.ArrayList;

import entities.Entity;
import game.Game;
import math.Rect;
import math.Vec;

public class Damage {

	private Rect hitbox;
	private double percentDamage;
	private Vec hitVelocity;
	private int hitLagFrames;
	private int teamCreatedBy;
	
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

	public void runScan(boolean isFacingRight) {
		ArrayList<Entity> entities=Game.getEntities();
		if (!isFacingRight)
			hitVelocity=hitVelocity.flipX();
		for (Entity e:entities)
			e.processDamage(this);
		if (!isFacingRight)
			hitVelocity=hitVelocity.flipX();
	}
	
}
