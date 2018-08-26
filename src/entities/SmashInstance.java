package entities;

import entities.attacks.Attack;
import entities.attacks.Damage;
import graphics.Sprite;
import graphics.SpriteLoader;
import math.Rect;
import math.Vec;

public class SmashInstance extends PlayerInstance {
	
	//state
	private int team;
	double hangCloseX=20;
	double hangLowY=50;
	double hangFarX=105;
	double hangHighY=120;

	// Constants
	private static final Vec gravity=new Vec(0, -0.3), fastGravity=gravity.scale(1.6);
	private static final double moveGroundSpeed=.9, moveAirSpeed=0.2;
	private static final double jumpPower=9, doubleJumpPower=13;
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
	
	public SmashInstance(int team) {
		this.team=team;
		createAttacks();
	}
	
	private void createAttacks() {
		Damage damage1, damage2;
		
		//GROUND ATTACK 1
		groundAttack1=new Attack(false, 20);
		groundAttack1.addPart(30, SpriteLoader.smashDownSwing1);
		groundAttack1.addPart(30, SpriteLoader.smashDownSwing2);
		Rect groundAttack1Rect1=new Rect(new Vec(-50, 0), new Vec(160, 130));
		Rect groundAttack1Rect2=new Rect(new Vec(0, -120), new Vec(150, 0));
		damage1=new Damage(groundAttack1Rect1, 14, new Vec(15, 5), 40, team);
		damage2=new Damage(groundAttack1Rect2, 11, new Vec(5, -9), 40, team);
		groundAttack1.addDamageFrame(30, damage1);
		groundAttack1.addDamageFrame(45, damage2);
		
		//GROUND ATTACK 2
		groundAttack2=new Attack(false, 40);
		groundAttack2.addPart(40, SpriteLoader.smashKick1);
		groundAttack2.addPart(40, SpriteLoader.smashKick2);
		Rect groundAttack2Rect1=new Rect(new Vec(0, -100), new Vec(140, 30));
		Rect groundAttack2Rect2=new Rect(new Vec(-140, -100), new Vec(0, 30));
		damage1=new Damage(groundAttack2Rect1, 8, new Vec(8, 7), 100, team);
		damage2=new Damage(groundAttack2Rect2, 22, new Vec(-15, 12), 100, team);
		groundAttack2.addDamageFrame(10, damage1);
		groundAttack2.addDamageFrame(55, damage2);
		
		//AIR ATTACK 1
		airAttack1=new Attack(true, 25);
		airAttack1.addPart(20, SpriteLoader.smashAirSwing1);
		airAttack1.addPart(35, SpriteLoader.smashAirSwing2);
		Rect airAttack1Rect1=new Rect(new Vec(-20, -100), new Vec(160, 100));
		Rect airAttack1Rect2=new Rect(new Vec(-70, 0), new Vec(70, 135));
		damage1=new Damage(airAttack1Rect1, 10, new Vec(5, 10), 40, team);
		damage2=new Damage(airAttack1Rect2, 18, new Vec(-15, 3), 60, team);
		airAttack1.addDamageFrame(25, damage1);
		airAttack1.addDamageFrame(35, damage2);
		
		//AIR ATTACK 2
		airAttack2=new Attack(true, 5);
		airAttack2.addPart(20, SpriteLoader.smashStomp1);
		airAttack2.addPart(25, SpriteLoader.smashStomp2);
		Rect airAttack2Rect1=new Rect(new Vec(-80, -185), new Vec(80, -20));
		damage1=new Damage(airAttack2Rect1, 14, new Vec(0, -10), 50, team);
		airAttack2.addVelocityCue(35, Vec.up.scale(3));
		airAttack2.addDamageFrame(25, damage1);
		airAttack2.addDamageFrame(40, damage1);
		
		//RECOVERY ATTACK
		recoveryAttack=new Attack(false, 50);
		recoveryAttack.markAsRecoveryAttack();
		int jumpUpLength=90;
		int jumpUpPowerLength=45;
		Vec upwardsVelocity=new Vec(Attack.velocityValueToIgnore, 12);
		recoveryAttack.addPart(jumpUpLength, SpriteLoader.smashRecover1);
		for (int i=0; i<jumpUpPowerLength; i++)
			recoveryAttack.addVelocityCue(i, upwardsVelocity);
		for (int i=0; i<jumpUpLength+Attack.partUntilGroundedMaxLength; i++)
			recoveryAttack.addGrabCue(i);
		
		recoveryAttack.addPartUntilGrounded(SpriteLoader.smashRecover2, null, null);

		recoveryAttack.addPart(80, SpriteLoader.smashRecover3);
		Rect finalDamageBoxLeft=new Rect(new Vec(-100, -120), new Vec(-10, 50));
		Rect finalDamageBoxRight=new Rect(new Vec(20, -120), new Vec(170, 50));
		damage1=new Damage(finalDamageBoxLeft, 10, new Vec(-6, 9), 40, team);
		damage2=new Damage(finalDamageBoxRight, 9, new Vec(7, 10), 65, team);
		recoveryAttack.addDamageFrame(jumpUpLength+Attack.partUntilGroundedMaxLength+10, damage1);
		recoveryAttack.addDamageFrame(jumpUpLength+Attack.partUntilGroundedMaxLength+11, damage2);
		
		//GRAB MISS ATTACK
		grabMissAttack=new Attack(false, 60);
		grabMissAttack.addPart(60, SpriteLoader.smashGrab);
		
		//GRAB ATTACK
		grabAttack=new Attack(false, 10);
		grabAttack.addPart(grabAttackAnimLen, SpriteLoader.smashGrabRelease);
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
			return SpriteLoader.smashAirUp;
		}
		else {
			return SpriteLoader.smashAirDown;
		}
	}
	
	Sprite getIdleSprite() {
		return SpriteLoader.smashIdle;
	}
	
	Sprite getRunningSprite(int animationCounter) {
		if (animationCounter>runningAnimLen()/2)
			return SpriteLoader.smashRunning1;
		else
			return SpriteLoader.smashRunning2;
	}
	
	Sprite getRollingSprite(int animationCounter) {
		return SpriteLoader.smashSliding;
	}

	Sprite getHangingSprite() {
		return SpriteLoader.smashHang;
	}

	Sprite getAirHitSprite() {
		return SpriteLoader.smashAirHit;
	}

	Sprite getKnockedDownSprite() {
		return SpriteLoader.smashKnockedDown;
	}

	Sprite getGrabbingSprite() {
		return SpriteLoader.smashGrab;
	}

	Sprite getBeingGrabbedSprite() {
		return SpriteLoader.smashGrabbed;
	}
	
	Rect getHitbox() {
		return new Rect(new Vec(-40, -70), new Vec(40, 70));
	}
	
	Rect getHang1() {
		return new Rect(new Vec(hangCloseX, hangLowY), new Vec(hangFarX, hangHighY));
	}
	
	Rect getHang2() {
		return new Rect(new Vec(hangCloseX, hangLowY), new Vec(hangFarX, hangHighY)).flipX();
	}
	
	public double getHitKnockbackMultiplier() {
		return 0.85;
	}
	
	Sprite getFaceSprite() {
		return SpriteLoader.smashIconSprite;
	}
	
	Sprite getNameSprite() {
		return SpriteLoader.smashText;
	}
	
}
