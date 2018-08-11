package entities;

import entities.attacks.Damage;
import entities.attacks.GrabHitbox;
import game.Game;
import math.Vec;

public abstract class Entity {

	public Entity() {
		Game.getScene().addEntity(this);
	}
	
	public int getRenderOrder() {
		return 0;
	}
	
	public void update() {
		
	}
	
	public void render() {
		
	}
	
	public final void destroy() {
		Game.getScene().destroyEntity(this);
	}
	
	public void processDamage(Damage damage) {
		
	}
	
	//returns true if successfully grabbed
	public boolean processGrab(GrabHitbox grab) {
		return false;
	}
	
	public void releaseGrabbedEntityRequest() {
		
	}
	
	public void onReleasedFromGrab() {
		
	}
	
	public Vec isCameraFocusable() {
		return null;
	}
	
}
