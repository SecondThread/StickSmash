package entities;

import entities.attacks.Attack;
import entities.attacks.Damage;
import graphics.Sprite;
import graphics.SpriteLoader;
import math.Rect;
import math.Vec;

public class BesiusInstance extends PlayerInstance {
	
	//state
	private int team;
	double hangCloseX=20;
	double hangLowY=10;
	double hangFarX=105;
	double hangHighY=90;

	// Constants
	private static final Vec gravity=new Vec(0, -0.18), fastGravity=gravity.scale(2);
	private static final double moveGroundSpeed=1.4, moveAirSpeed=0.25;
	private static final double jumpPower=10, doubleJumpPower=15;
	private static final double xGroundedFriction=0.8, xAirFriction=0.95, yFriction=0.98, xAttackingFriction=0.98;
	private static final double minSpeedToRun=0.1;
	private static final int numJumpFrames=30;
	private static final double maxShieldScale=0.8;
	private static final int maxShield=120*5*3;
	private static final int stunFrameAfterBrokenShield=240;
	private static final double rollVelocity=6;
	private static final int fullGrabLength=120*3;
	private static final int grabMashSkipFrames=15;
	private static final int invincibilityAfterDying=300;
	private static final int framesBetweenGrabIconSwitch=30;
	private static final int noShieldAfterRollOrDodge=40;
	private static final Vec grabIconOffset=Vec.up.scale(120);

	// animation constants
	private static final int runningAnimLen=40;
	private static final int rollingAnimLen=50;
	private static final int spotDodgeAnimLen=50;
	private static final int airDodgeAnimLen=50;
	private static final int hangImmunityLen=50;
	private static final int framesBetweenHangs=60;
	private static final int grabAttackAnimLen=40;
	private static final int grabDamageFrame=35;

	// Attacks
	private Attack groundAttack1;
	private Attack groundAttack2;
	private Attack airAttack1;
	private Attack airAttack2;
	private Attack recoveryAttack;
	private Attack grabMissAttack;
	private Attack grabAttack;
	
	public BesiusInstance(int team) {
		this.team=team;
		createAttacks();
	}
	
	private void createAttacks() {
		Damage damage1, damage2, damage3;
		
		//GROUND ATTACK 1
		groundAttack1=new Attack(false, 20);
		groundAttack1.addPart(20, SpriteLoader.besiusUppercut1);
		groundAttack1.addPart(25, SpriteLoader.besiusUppercut2);
		Rect groundAttack1Rect1=new Rect(new Vec(20, -30), new Vec(90, 35));
		damage1=new Damage(groundAttack1Rect1, 10, new Vec(5, 10), 40, team);
		groundAttack1.addDamageFrame(30, damage1);
		
		//GROUND ATTACK 2
		groundAttack2=new Attack(false, 40);
		groundAttack2.addPart(30, SpriteLoader.besiusFury1);
		groundAttack2.addPart(30, SpriteLoader.besiusFury2);
		groundAttack2.addPart(45, SpriteLoader.besiusFury3);
		Rect groundAttack2Rect1=new Rect(new Vec(0, -20), new Vec(90, 30));
		Rect groundAttack2Rect2=new Rect(new Vec(0, -20), new Vec(110, 30));
		Rect groundAttack2Rect3=new Rect(new Vec(0, -60), new Vec(130, 50));
		damage1=new Damage(groundAttack2Rect1, 8, new Vec(0, 3), 30, team);
		damage2=new Damage(groundAttack2Rect2, 8, new Vec(5, 2), 30, team);
		damage3=new Damage(groundAttack2Rect3, 12, new Vec(15, 5), 30, team);
		groundAttack2.addDamageFrame(10, damage1);
		groundAttack2.addDamageFrame(30, damage2);
		groundAttack2.addDamageFrame(50, damage3);
		
		//AIR ATTACK 1
		airAttack1=new Attack(true, 25);
		airAttack1.addPart(25, SpriteLoader.besiusFlipKick1);
		airAttack1.addPart(30, SpriteLoader.besiusFlipKick2);
		Rect airAttack1Rect=new Rect(new Vec(-80, -30), new Vec(120, 100));
		damage1=new Damage(airAttack1Rect, 12, new Vec(-5, 10), 80, team);
		airAttack1.addDamageFrame(10, damage1);
		airAttack1.addDamageFrame(30, damage1);
		
		//AIR ATTACK 2
		airAttack2=new Attack(true, 40);
		airAttack2.addPart(40, SpriteLoader.besiusKnee);
		Rect airAttack2Rect1=new Rect(new Vec(30, -30), new Vec(60, 0));
		Rect airAttack2Rect2=new Rect(new Vec(-30, -60), new Vec(90, 30));
		damage1=new Damage(airAttack2Rect1, 10, new Vec(0, 20), 80, team);
		damage2=new Damage(airAttack2Rect2, 10, new Vec(0, 5), 40, team);
		airAttack2.addDamageFrame(21, damage1);
		airAttack2.addDamageFrame(20, damage2);
		
		//RECOVERY ATTACK
		recoveryAttack=new Attack(false, 0);
		recoveryAttack.markAsRecoveryAttack();
		recoveryAttack.addPart(20, SpriteLoader.besiusRecover1);
		recoveryAttack.addPart(60, SpriteLoader.besiusRecover2);
		for (int i=0; i<30; i++)
			recoveryAttack.addVelocityCue(i, new Vec(4, 8));
		for (int i=20; i<80; i++)
			recoveryAttack.addGrabCue(i);
		
		Rect recoveryDamageBox=new Rect(new Vec(0, 0), new Vec(130, 110));
		damage1=new Damage(recoveryDamageBox, 10, new Vec(10, 5), 40, team);
		recoveryAttack.addDamageFrame(5, damage1);
		recoveryAttack.addDamageFrame(25, damage1);
		recoveryAttack.addDamageFrame(45, damage1);
		
		//GRAB MISS ATTACK
		grabMissAttack=new Attack(false, 60);
		grabMissAttack.addPart(60, SpriteLoader.besiusGrab);
		
		//GRAB ATTACK
		grabAttack=new Attack(false, 10);
		grabAttack.addPart(grabAttackAnimLen, SpriteLoader.besiusGrabRelease);
		//damage updated when grab is released because it differs depending on
		//the number of times they hit the grab button
	}

	int runningAnimLen() {
		return runningAnimLen;
	}

	int rollingAnimLen() {
		return rollingAnimLen;
	}

	int spotDodgeAnimLen() {
		return spotDodgeAnimLen;
	}

	int airDodgeAnimLen() {
		return airDodgeAnimLen;
	}

	int hangImmunityLen() {
		return hangImmunityLen;
	}

	int framesBetweenHangs() {
		return framesBetweenHangs;
	}

	int grabAttackAnimLen() {
		return grabAttackAnimLen;
	}

	int grabDamageFrame() {
		return grabDamageFrame;
	}

	Attack groundAttack1() {
		return groundAttack1;
	}

	Attack groundAttack2() {
		return groundAttack2;
	}

	Attack airAttack1() {
		return airAttack1;
	}

	Attack airAttack2() {
		return airAttack2;
	}

	Attack recoveryAttack() {
		return recoveryAttack;
	}

	Attack grabMissAttack() {
		return grabMissAttack;
	}

	Attack grabAttack() {
		return grabAttack;
	}

	Vec gravity() {
		return gravity;
	}

	Vec fastGravity() {
		return fastGravity;
	}

	double moveGroundSpeed() {
		return moveGroundSpeed;
	}

	double moveAirSpeed() {
		return moveAirSpeed;
	}

	double jumpPower() {
		return jumpPower;
	}

	double doubleJumpPower() {
		return doubleJumpPower;
	}

	double xGroundedFriction() {
		return xGroundedFriction;
	}

	double xAirFriction() {
		return xAirFriction;
	}

	double yFriction() {
		return yFriction;
	}

	double xAttackingFriction() {
		return xAttackingFriction;
	}

	double minSpeedToRun() {
		return minSpeedToRun;
	}

	int numJumpFrames() {
		return numJumpFrames;
	}

	double maxShieldScale() {
		return maxShieldScale;
	}

	int maxShield() {
		return maxShield;
	}

	int stunFrameAfterBrokenShield() {
		return stunFrameAfterBrokenShield;
	}

	double rollVelocity() {
		return rollVelocity;
	}

	int fullGrabLength() {
		return fullGrabLength;
	}

	int grabMashSkipFrames() {
		return grabMashSkipFrames;
	}

	int invincibilityAfterDying() {
		return invincibilityAfterDying;
	}

	int framesBetweenGrabIconSwitch() {
		return framesBetweenGrabIconSwitch;
	}

	int noShieldAfterRollOrDodge() {
		return noShieldAfterRollOrDodge;
	}

	Vec grabIconOffset() {
		return grabIconOffset;
	}

	Sprite getAirbornSprite(Vec velocity) {
		if (velocity.y()>=0) {
			return SpriteLoader.besiusAirUp;
		}
		else {
			return SpriteLoader.besiusAirDown;
		}
	}
	
	Sprite getIdleSprite() {
		return SpriteLoader.besiusIdle;
	}
	
	Sprite getRunningSprite(int animationCounter) {
		if (animationCounter>runningAnimLen()/2)
			return SpriteLoader.besiusRunning1;
		else
			return SpriteLoader.besiusRunning2;
	}
	
	Sprite getRollingSprite(int animationCounter) {
		return SpriteLoader.besiusSliding;
	}

	Sprite getHangingSprite() {
		return SpriteLoader.besiusHang;
	}

	Sprite getAirHitSprite() {
		return SpriteLoader.besiusAirHit;
	}

	Sprite getKnockedDownSprite() {
		return SpriteLoader.besiusKnockedDown;
	}

	Sprite getGrabbingSprite() {
		return SpriteLoader.besiusGrab;
	}

	Sprite getBeingGrabbedSprite() {
		return SpriteLoader.besiusGrabbed;
	}
	
	Rect getHitbox() {
		return new Rect(new Vec(-35, -60), new Vec(35, 60));
	}
	
	Rect getHang1() {
		return new Rect(new Vec(hangCloseX, hangLowY), new Vec(hangFarX, hangHighY));
	}
	
	Rect getHang2() {
		return new Rect(new Vec(hangCloseX, hangLowY), new Vec(hangFarX, hangHighY)).flipX();
	}
	
	public double getHitKnockbackMultiplier() {
		return 1.3;
	}
	
}
