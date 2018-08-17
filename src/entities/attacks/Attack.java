package entities.attacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import entities.particles.Particle;
import graphics.Sprite;
import math.Rect;
import math.Vec;

public class Attack {

	private int frame=0;
	private ArrayList<Integer> partFrameLengths=new ArrayList<>();
	private ArrayList<Sprite> partSprites=new ArrayList<>();
	private HashMap<Integer, Vec> velocityAtFrame=new HashMap<>();
	private HashMap<Integer, Damage> damageAtFrame=new HashMap<>();
	private HashSet<Integer> canGrabAtFrame=new HashSet<>();
	private HashSet<Integer> particleFrame=new HashSet<>();
	private HashMap<Integer, Rect> additionalGrab=new HashMap<>();
	private boolean airOnlyAttack;
	private boolean isRecoveryAttack;
	private int noAttackAfterLength;
	
	public Attack(boolean airOnlyAttack, int noAttackAfterLength) {
		this.airOnlyAttack=airOnlyAttack;
		this.noAttackAfterLength=noAttackAfterLength;
	}
	
	//------------------------------------------------------------
	//				Creation
	public void addPart(int numFrames, Sprite frameSprite) {
		partFrameLengths.add(numFrames);
		partSprites.add(frameSprite);
	}

	public void addVelocityCue(int frame, Vec velocity) {
		velocityAtFrame.put(frame, velocity);
	}
	
	public void addDamageFrame(int frame, Damage damage) {
		damageAtFrame.put(frame, damage);
	}
	
	public void addGrabCue(int frame) {
		canGrabAtFrame.add(frame);
	}
	
	public void markAsRecoveryAttack() {
		isRecoveryAttack=true;
	}
	
	public void addAdditionalGrab(int frame, Rect grabBox) {
		additionalGrab.put(frame, grabBox);
	}
	
	//------------------------------------------------------------
	//				Use
	public void start() {
		frame=0;
	}

	public void update(boolean isGrounded, boolean isFacingRight, Vec playerPosition) {
		frame++;
		if (airOnlyAttack&&isGrounded) {
			frame=countFrames();
			return;
		}
		if (damageAtFrame.containsKey(frame)) {
			damageAtFrame.get(frame).runScan(isFacingRight, playerPosition);
		}
		if (particleFrame.contains(frame)) {
			Particle.createBulletParticle(playerPosition, isFacingRight);
		}
	}
	
	public Vec getVelocity(boolean facingRight) {
		if (velocityAtFrame.containsKey(frame)) {
			Vec cued=velocityAtFrame.get(frame);
			if (facingRight)
				return cued;
			else
				return new Vec(-cued.x(), cued.y());
		}
		return null;
	}
	
	public Rect getAdditionalGrabBox(boolean facingRight) {
		Rect box=additionalGrab.getOrDefault(frame, null);
		if (box==null) return box;
		if (!facingRight) return box.flipX();
		else return box;
	}
	
	public boolean canGrab() {
		return canGrabAtFrame.contains(frame);
	}
	
	public boolean isOver() {
		return frame>=countFrames();
	}
	
	public boolean isRecoveryAttack() {
		return isRecoveryAttack;
	}
	
	public Sprite getCurrentSprite() {
		int framesLeft=frame;
		int partOn=0;
		while (framesLeft>=0&&partOn<partFrameLengths.size()) {
			if (framesLeft<partFrameLengths.get(partOn))
				return partSprites.get(partOn);
			framesLeft-=partFrameLengths.get(partOn);
			partOn++;
		}
		return partSprites.get(partSprites.size()-1);
	}
	
	private int countFrames() {
		int total=0;
		for (int i:partFrameLengths) total+=i;
		return total;
	}
	
	public int getNoAttackAfterLenght() {
		return noAttackAfterLength;
	}
	
	public void addBulletParticleFrame(int frame) {
		particleFrame.add(frame);
	}
	
	
}
