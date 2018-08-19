package entities;

import entities.attacks.Attack;
import entities.attacks.Damage;
import graphics.Sprite;
import graphics.SpriteLoader;
import math.Rect;
import math.Vec;

public class CarlosInstance extends PlayerInstance {
	
	//state
	private int team;
	double hangCloseX=40;
	double hangLowY=60;
	double hangFarX=125;
	double hangHighY=150;

	// Constants
	private static final Vec gravity=new Vec(0, -0.2), fastGravity=gravity.scale(2);
	private static final double moveGroundSpeed=1, moveAirSpeed=0.2;
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
	private static final int runningAnimLen=100;
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
	
	public CarlosInstance(int team) {
		this.team=team;
		createAttacks();
	}
	
	private void createAttacks() {
		Damage damage1, damage2;
		
		//GROUND ATTACK 1
		groundAttack1=new Attack(false, 20);
		groundAttack1.addPart(20, SpriteLoader.carlosSwordAttack1);
		groundAttack1.addPart(30, SpriteLoader.carlosSwordAttack2);
		groundAttack1.addPart(30, SpriteLoader.carlosSwordAttack3);
		Rect groundAttack1Rect1=new Rect(new Vec(0, -60), new Vec(170, 80));
		Rect groundAttack1Rect2=new Rect(new Vec(0, -80), new Vec(160, 60));
		damage1=new Damage(groundAttack1Rect1, 10, new Vec(0, 5), 31, team);
		damage2=new Damage(groundAttack1Rect2, 10, new Vec(15, 5), 40, team);
		groundAttack1.addDamageFrame(20, damage1);
		groundAttack1.addDamageFrame(50, damage2);
		
		//GROUND ATTACK 2
		groundAttack2=new Attack(false, 40);
		groundAttack2.addPart(70, SpriteLoader.carlosShooting1);
		groundAttack2.addPart(40, SpriteLoader.carlosShooting2);
		groundAttack2.addPart(20, SpriteLoader.carlosShooting1);
		groundAttack2.addBulletParticleFrame(100);
		Rect groundAttack2Rect1=new Rect(new Vec(30, 0), new Vec(2000, 10));
		damage1=new Damage(groundAttack2Rect1, 1, new Vec(1, 5), 60, team);
		groundAttack2.addDamageFrame(100, damage1);
		
		//AIR ATTACK 1
		airAttack1=new Attack(true, 20);
		airAttack1.addPart(30, SpriteLoader.carlosAirSlice1);
		airAttack1.addPart(30, SpriteLoader.carlosAirSlice2);
		Rect airAttack1Rect=new Rect(new Vec(0, -100), new Vec(160, 120));
		damage1=new Damage(airAttack1Rect, 12, new Vec(10, 5), 50, team);
		airAttack1.addDamageFrame(30, damage1);
		
		//AIR ATTACK 2
		airAttack2=new Attack(false, 25);
		airAttack2.markAsRecoveryAttack();
		airAttack2.addPart(20, SpriteLoader.carlosBouncingFish1);
		airAttack2.addPart(20, SpriteLoader.carlosBouncingFish2);
		airAttack2.addPart(30, SpriteLoader.carlosBouncingFish3);
		airAttack2.addPart(20, SpriteLoader.carlosBouncingFish2);
		airAttack2.addPart(20, SpriteLoader.carlosBouncingFish1);
		Rect airAttack2Rect1=new Rect(new Vec(0, -150), new Vec(130, -60));
		damage1=new Damage(airAttack2Rect1, 10, new Vec(5, -20), 80, team);
		airAttack2.addDamageFrame(50, damage2);
		airAttack2.addVelocityCue(10, new Vec(10, 12));
		airAttack2.addVelocityCue(30, new Vec(10, -2));
		airAttack2.addVelocityCue(60, new Vec(0, 0));
		airAttack2.addVelocityCue(70, new Vec(-10, 15));
		airAttack2.addVelocityCue(90, new Vec(-10, 15));
		
		//RECOVERY ATTACK
		recoveryAttack=new Attack(false, 0);
		recoveryAttack.addPart(40, SpriteLoader.carlosRecover1);
		recoveryAttack.addPart(20, SpriteLoader.carlosRecover2);
		for (int i=20; i<40; i++)
			recoveryAttack.addVelocityCue(i, new Vec(1, 1));
		for (int i=0; i<80; i++)
			recoveryAttack.addGrabCue(i);
		
		Rect recoveryDamageBox=new Rect(new Vec(200, 200), new Vec(300, 300));
		Rect recoveryGrabBox=new Rect(new Vec(100, 150), new Vec(460, 460));
		damage1=new Damage(recoveryDamageBox, 6, new Vec(0, 5), 40, team);
		recoveryAttack.addDamageFrame(50, damage1);
		recoveryAttack.addAdditionalGrab(50, recoveryGrabBox);
		
		//GRAB MISS ATTACK
		grabMissAttack=new Attack(false, 60);
		grabMissAttack.addPart(60, SpriteLoader.carlosGrab);
		
		//GRAB ATTACK
		grabAttack=new Attack(false, 10);
		grabAttack.addPart(grabAttackAnimLen, SpriteLoader.carlosGrabRelease);
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
//		return airAttack2;\
		return recoveryAttack;
	}

	Attack recoveryAttack() {
//		return recoveryAttack;
		return airAttack2;
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
			return SpriteLoader.carlosAirUp;
		}
		else {
			return SpriteLoader.carlosAirDown;
		}
	}
	
	Sprite getIdleSprite() {
		return SpriteLoader.carlosIdle;
	}
	
	Sprite getRunningSprite(int animationCounter) {
		if (animationCounter<runningAnimLen()/4)
			return SpriteLoader.carlosRunning1;
		else if (animationCounter<runningAnimLen()*2/4)
			return SpriteLoader.carlosRunning3;
		else if (animationCounter<runningAnimLen()*3/4)
			return SpriteLoader.carlosRunning2;
		else
			return SpriteLoader.carlosRunning3;
	}
	
	Sprite getRollingSprite(int animationCounter) {
		if (animationCounter<rollingAnimLen()/2) {
			return SpriteLoader.carlosRoll1;
		}
		else {
			return SpriteLoader.carlosRoll2;
		}
	}

	Sprite getHangingSprite() {
		return SpriteLoader.carlosHang;
	}

	Sprite getAirHitSprite() {
		return SpriteLoader.carlosAirHit;
	}

	Sprite getKnockedDownSprite() {
		return SpriteLoader.carlosKnockedDown;
	}

	Sprite getGrabbingSprite() {
		return SpriteLoader.carlosGrab;
	}

	Sprite getBeingGrabbedSprite() {
		return SpriteLoader.carlosGrabbed;
	}
	
	Rect getHitbox() {
		return new Rect(new Vec(-50, -90), new Vec(50, 100));
	}
	
	Rect getHang1() {
		return new Rect(new Vec(hangCloseX, hangLowY), new Vec(hangFarX, hangHighY));
	}
	
	Rect getHang2() {
		return new Rect(new Vec(hangCloseX, hangLowY), new Vec(hangFarX, hangHighY)).flipX();
	}
	
	public double getHitKnockbackMultiplier() {
		return 0.8;
	}
	
	Sprite getFaceSprite() {
		return SpriteLoader.carlosIconSprite;
	}
	
	Sprite getNameSprite() {
		return SpriteLoader.carlosText;
	}
	
}
