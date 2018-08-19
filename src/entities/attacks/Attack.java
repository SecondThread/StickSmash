package entities.attacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import entities.particles.Particle;
import graphics.Sprite;
import math.Rect;
import math.Vec;

public class Attack {
	
	public static int partUntilGroundedMaxLength=1_000;
	//set a component of the velocity to this and that component won't be changed
	public static double velocityValueToIgnore=867_5309;

	private int frame=0;
	private ArrayList<Integer> partFrameLengths=new ArrayList<>();
	private ArrayList<Sprite> partSprites=new ArrayList<>();
	private HashMap<Integer, Vec> velocityAtFrame=new HashMap<>();
	private HashMap<Integer, Damage> damageAtFrame=new HashMap<>();
	private HashSet<Integer> canGrabAtFrame=new HashSet<>();
	private HashSet<Integer> particleFrame=new HashSet<>();
	private HashMap<Integer, Rect> additionalGrab=new HashMap<>();
	private ArrayList<Boolean> stayInPartUntilGrounded=new ArrayList<>();
	private ArrayList<Vec> velocityInPartUntilGrouneded=new ArrayList<>();
	private ArrayList<Damage> damageInPartUntilGrounded=new ArrayList<>();
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
		
		stayInPartUntilGrounded.add(false);
		velocityInPartUntilGrouneded.add(null);
		damageInPartUntilGrounded.add(null);
	}
	
	public void addPartUntilGrounded(Sprite frameSprite, Vec velocityUntilGrounded, Damage damageUntilGrounded) {
		partFrameLengths.add(partUntilGroundedMaxLength);
		partSprites.add(frameSprite);
		
		stayInPartUntilGrounded.add(true);
		velocityInPartUntilGrouneded.add(velocityUntilGrounded);
		damageInPartUntilGrounded.add(damageUntilGrounded);
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
		
		int part=getPart();
		if (stayInPartUntilGrounded.get(part)&&isGrounded) {
			advanceToStartOfNextPart();
		}
		Damage toCreate=damageInPartUntilGrounded.get(part);
		if (toCreate!=null) {
			toCreate.runScan(isFacingRight, playerPosition);
		}
	}
	
	public Vec getVelocity(boolean facingRight, Vec oldVelocity) {
		if (velocityAtFrame.containsKey(frame)) {
			Vec cued=velocityAtFrame.get(frame);
			boolean setX=Math.abs(cued.x()-velocityValueToIgnore)>1;
			boolean setY=Math.abs(cued.y()-velocityValueToIgnore)>1;
			if (facingRight)
				return new Vec(setX?cued.x():oldVelocity.x(), setY?cued.y():oldVelocity.y());
			else
				return new Vec(setX?-cued.x():oldVelocity.x(), setY?cued.y():oldVelocity.y());
		}
		
		int part=getPart();
		Vec cued=velocityInPartUntilGrouneded.get(part);
		if (cued!=null) {
			boolean setX=Math.abs(cued.x()-velocityValueToIgnore)>1;
			boolean setY=Math.abs(cued.y()-velocityValueToIgnore)>1;
			if (facingRight)
				return new Vec(setX?cued.x():oldVelocity.x(), setY?cued.y():oldVelocity.y());
			else
				return new Vec(setX?-cued.x():oldVelocity.x(), setY?cued.y():oldVelocity.y());
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
		return partSprites.get(getPart());
	}
	
	private int countFrames() {
		int total=0;
		for (int i:partFrameLengths) total+=i;
		return total;
	}
	
	//gets which part of the animation we are in, or returns the last one if the animation is over
	private int getPart() {
		int framesLeft=frame;
		int partOn=0;
		while (framesLeft>=0&&partOn<partFrameLengths.size()) {
			if (framesLeft<partFrameLengths.get(partOn))
				return partOn;
			framesLeft-=partFrameLengths.get(partOn);
			partOn++;
		}
		return partFrameLengths.size()-1;
	}
	
	private void advanceToStartOfNextPart() {
		int currentPart=getPart();
		//the next part starts after this one ends
		int targetFrame=0;
		for (int i=0; i<=currentPart; i++)
			targetFrame+=partFrameLengths.get(i);
		frame=targetFrame;
	}
	
	public int getNoAttackAfterLength() {
		return noAttackAfterLength;
	}
	
	public void addBulletParticleFrame(int frame) {
		particleFrame.add(frame);
	}
	
	
}
