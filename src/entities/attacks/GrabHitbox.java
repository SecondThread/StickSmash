package entities.attacks;

import java.util.ArrayList;

import entities.Entity;
import game.Game;
import math.Rect;
import math.Vec;

public class GrabHitbox {
	
	private Entity grabbing;
	private Rect hitbox;
	private int team;
	private boolean facingRight;
	private Vec grabPosition;
	
	public GrabHitbox(Entity grabbing, Rect hitbox, int team, boolean facingRight, Vec grabPosition) {
		this.grabbing=grabbing;
		this.hitbox=hitbox;
		this.team=team;
		this.facingRight=facingRight;
		this.grabPosition=grabPosition;
	}
	
	public int getTeam() {
		return team;
	}
	
	public Rect getHitbox() {
		return hitbox;
	}
	
	public boolean getGrabberFacingRight() {
		return facingRight;
	}
	
	public Entity getGrabber() {
		return grabbing;
	}
	
	public Vec getGrabPosition() {
		return grabPosition;
	}
	
	public Entity runScan() {
		ArrayList<Entity> entities=Game.getScene().getEntities();
		for (Entity e:entities) {
			if (e.processGrab(this))
				return e;
		}
		return null;
	}
	
}
