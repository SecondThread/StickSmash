package entities.attacks;

import math.Rect;
import math.Vec;

public class Damage {

	private Rect hitbox;
	private double percentDamage;
	private Vec hitVelocity;
	
	public Damage(Rect hitbox, double percentDamage, Vec hitVelocity) {
		this.hitbox=hitbox;
		this.percentDamage=percentDamage;
		this.hitVelocity=hitVelocity;
	}
	
}
