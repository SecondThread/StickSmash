package entities;

import entities.attacks.Attack;
import graphics.Sprite;
import math.Rect;
import math.Vec;

public abstract class PlayerInstance {

	abstract int runningAnimLen();
	abstract int rollingAnimLen();
	abstract int spotDodgeAnimLen();
	abstract int airDodgeAnimLen();
	abstract int hangImmunityLen();
	abstract int framesBetweenHangs();
	abstract int grabAttackAnimLen();
	abstract int grabDamageFrame();

	abstract Attack groundAttack1();
	abstract Attack groundAttack2();
	abstract Attack airAttack1();
	abstract Attack airAttack2();
	abstract Attack recoveryAttack();
	abstract Attack grabMissAttack();
	abstract Attack grabAttack();

	// Constants
	abstract Vec gravity();
	abstract Vec fastGravity();
	abstract double moveGroundSpeed();
	abstract double moveAirSpeed();
	abstract double jumpPower();
	abstract double doubleJumpPower();
	abstract double xGroundedFriction();
	abstract double xAirFriction();
	abstract double yFriction();
	abstract double xAttackingFriction();
	abstract double minSpeedToRun();
	abstract int numJumpFrames();
	abstract double maxShieldScale();
	abstract int maxShield();
	abstract int stunFrameAfterBrokenShield();
	abstract double rollVelocity();
	abstract int fullGrabLength();
	abstract int grabMashSkipFrames();
	abstract int invincibilityAfterDying();
	abstract int framesBetweenGrabIconSwitch();
	abstract int noShieldAfterRollOrDodge();
	abstract Vec grabIconOffset();
	
	abstract Sprite getAirbornSprite(Vec velocity);
	abstract Sprite getIdleSprite();
	abstract Sprite getRunningSprite(int animationCounter);
	abstract Sprite getRollingSprite(int animationCounter);
	abstract Sprite getHangingSprite();
	abstract Sprite getAirHitSprite();
	abstract Sprite getKnockedDownSprite();
	abstract Sprite getGrabbingSprite();
	abstract Sprite getBeingGrabbedSprite();
	
	abstract Rect getHitbox();
	abstract Rect getHang1();
	abstract Rect getHang2();
	
	public double getHitKnockbackMultiplier() {
		return 1;
	}
	
}
