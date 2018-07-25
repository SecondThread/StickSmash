package entities.attacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import graphics.Sprite;
import math.Vec;

public class Attack {

	private int frame=0;
	private ArrayList<Integer> partFrameLengths=new ArrayList<>();
	private ArrayList<Sprite> partSprites=new ArrayList<>();
	private HashMap<Integer, Vec> velocityAtFrame=new HashMap<>();
	private HashSet<Integer> canGrabAtFrame=new HashSet<>();
	private boolean airOnlyAttack;
	
	public Attack(boolean airOnlyAttack) {
		this.airOnlyAttack=airOnlyAttack;
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
	
	public void addGrabCue(int frame) {
		canGrabAtFrame.add(frame);
	}
	
	//------------------------------------------------------------
	//				Use
	public void start() {
		frame=0;
	}

	public void update(boolean isGrounded) {
		frame++;
		if (airOnlyAttack&&isGrounded) {
			frame=countFrames();
			return;
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
	
	public boolean canGrab() {
		return canGrabAtFrame.contains(frame);
	}
	
	public boolean isOver() {
		return frame>=countFrames();
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
	
	
}
