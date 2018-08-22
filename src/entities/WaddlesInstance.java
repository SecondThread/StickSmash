package entities;

import entities.attacks.Attack;
import entities.attacks.Damage;
import entities.particles.ProjectileType;
import graphics.Sprite;
import graphics.SpriteLoader;
import math.Rect;
import math.Vec;

public class WaddlesInstance extends PlayerInstance {
	
	//state
	private int team;
	double hangCloseX=40;
	double hangLowY=40;
	double hangFarX=125;
	double hangHighY=120;

	// Constants
	private static final Vec gravity=new Vec(0, -0.18), fastGravity=gravity.scale(2);
	private static final double moveGroundSpeed=1.2, moveAirSpeed=0.25;
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
	
	public WaddlesInstance(int team) {
		this.team=team;
		createAttacks();
	}
	
	private void createAttacks() {
		Damage damage1, damage2, damage3;
		
		//GROUND ATTACK 1
		groundAttack1=new Attack(false, 10);
		groundAttack1.addPart(15, SpriteLoader.waddlesPeck);
		Rect groundAttack1Rect1=new Rect(new Vec(20, -10), new Vec(120, 55));
		damage1=new Damage(groundAttack1Rect1, 8, new Vec(5, 3), 20, team);
		groundAttack1.addDamageFrame(5, damage1);
		
		//GROUND ATTACK 2
		groundAttack2=new Attack(false, 20);
		groundAttack2.addPart(60, SpriteLoader.waddlesSnowThrow);
		groundAttack2.addProjectileParticleFrame(5, ProjectileType.WaddlesSnowball);
		Rect groundAttack2Rect1=new Rect(new Vec(65, -20), new Vec(110, 30));
		Rect[] snowballRects=new Rect[40];
		int snowballBaseHeight = 60;
		int snowballDistanceIncrement = 40;
		for(int i = snowballRects.length-1; i >= 0; i--) {
			int currentRadius = (int) Math.round((snowballBaseHeight/2)*(i/(double)snowballRects.length));
			int currentDistance = snowballDistanceIncrement*(snowballRects.length-i);
			//System.out.println("dist: " + currentDistance + " Rad: " + currentRadius);
			snowballRects[(snowballRects.length-1)-i] = new Rect(new Vec(currentDistance, -currentRadius), new Vec(currentDistance+currentRadius,currentRadius));
		}
		Damage[] snowballDamage = new Damage[snowballRects.length];
		int snowballBaseDamage = 4;
		for(int i = 0; i < snowballDamage.length; i++) {
			snowballDamage[i] = new Damage(snowballRects[i], Math.round(snowballBaseDamage*(1-(i/(double)snowballDamage.length))), new Vec(0,0), 0, team);
		}
		damage1=new Damage(groundAttack2Rect1, 8, new Vec(5, 1), 30, team);
		int meleeDamageFrame = 15;
		groundAttack2.addDamageFrame(meleeDamageFrame, damage1);
		for(int i = 0; i < snowballDamage.length; i++) {
			if(i!=meleeDamageFrame) {
				groundAttack2.addDamageFrame(i, snowballDamage[i]);				
			}
		}
		
		//AIR ATTACK 1
		airAttack1=new Attack(true, 25);
		airAttack1.addPart(20, SpriteLoader.waddlesBackflip1);
		airAttack1.addPart(15, SpriteLoader.waddlesBackflip2);
		Rect airAttack1Rect=new Rect(new Vec(-80, -30), new Vec(120, 100));
		damage1=new Damage(airAttack1Rect, 12, new Vec(0, 2), 20, team);
		damage2=new Damage(airAttack1Rect, 12, new Vec(-10, 5), 40, team);
		airAttack1.addDamageFrame(10, damage1);
		airAttack1.addDamageFrame(25, damage2);
		
		//AIR ATTACK 2
		airAttack2=new Attack(true, 80);
		airAttack2.addPart(15, SpriteLoader.waddlesSpin1);
		airAttack2.addPart(15, SpriteLoader.waddlesSpin2);
		airAttack2.addPart(15, SpriteLoader.waddlesSpin3);
		airAttack2.addPart(15, SpriteLoader.waddlesSpin4);
		for(int i = 0; i < 60; i++)
			airAttack2.addVelocityCue(i, new Vec(10,0));
		Rect airAttack2Rect1=new Rect(new Vec(-20, -60), new Vec(130, 60));
		damage1=new Damage(airAttack2Rect1, 5, new Vec(5, 1), 14, team);
		damage2=new Damage(airAttack2Rect1, 20, new Vec(10, 0), 40, team);
		airAttack2.addDamageFrame(10, damage1);
		airAttack2.addDamageFrame(25, damage1);
		airAttack2.addDamageFrame(40, damage1);
		airAttack2.addDamageFrame(55, damage2);
		
		//RECOVERY ATTACK
		recoveryAttack=new Attack(false, 0);
		recoveryAttack.markAsRecoveryAttack();
		recoveryAttack.addPart(30, SpriteLoader.waddlesIce1);
		recoveryAttack.addPart(30, SpriteLoader.waddlesIce2);
		recoveryAttack.addPart(30, SpriteLoader.waddlesIce3);
		for (int i=0; i<50; i++)
			recoveryAttack.addVelocityCue(i, new Vec(Attack.velocityValueToIgnore, 5));
		recoveryAttack.addVelocityCue(60, new Vec(Attack.velocityValueToIgnore, 15));
		for (int i=20; i<80; i++)
			recoveryAttack.addGrabCue(i);
		
		Rect recoveryDamageBox=new Rect(new Vec(-80, 80), new Vec(80, 250));
		damage1=new Damage(recoveryDamageBox, 10, new Vec(0, 3), 40, team);
		damage2=new Damage(recoveryDamageBox, 20, new Vec(0, 10), 40, team);
		recoveryAttack.addDamageFrame(15, damage1);
		recoveryAttack.addDamageFrame(45, damage1);
		recoveryAttack.addDamageFrame(75, damage2);
		
		//GRAB MISS ATTACK
		grabMissAttack=new Attack(false, 60);
		grabMissAttack.addPart(60, SpriteLoader.waddlesGrab);
		
		//GRAB ATTACK
		grabAttack=new Attack(false, 10);
		grabAttack.addPart(grabAttackAnimLen, SpriteLoader.waddlesGrabAttack);
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
			return SpriteLoader.waddlesAirUp;
		}
		else {
			return SpriteLoader.waddlesAirDown;
		}
	}
	
	Sprite getIdleSprite() {
		return SpriteLoader.waddlesIdle;
	}
	
	Sprite getRunningSprite(int animationCounter) {
		if (animationCounter>runningAnimLen()/2)
			return SpriteLoader.waddlesIdle;
		else
			return SpriteLoader.waddlesIdle;
	}
	
	Sprite getRollingSprite(int animationCounter) {
		if (animationCounter>rollingAnimLen()/2)
			return SpriteLoader.waddlesSlide2;
		else
			return SpriteLoader.waddlesSlide1;
	}

	Sprite getHangingSprite() {
		return SpriteLoader.waddlesHang;
	}

	Sprite getAirHitSprite() {
		return SpriteLoader.waddlesAirHit;
	}

	Sprite getKnockedDownSprite() {
		return SpriteLoader.waddlesKnockedDown;
	}

	Sprite getGrabbingSprite() {
		return SpriteLoader.waddlesGrab;
	}

	Sprite getBeingGrabbedSprite() {
		return SpriteLoader.waddlesGrabbed;
	}
	
	Rect getHitbox() {
		return new Rect(new Vec(-65, -45), new Vec(65, 85));
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
	
	Sprite getFaceSprite() {
		return SpriteLoader.besiusIconSprite;
	}
	
	Sprite getNameSprite() {
		return SpriteLoader.besiusText;
	}
	
}
