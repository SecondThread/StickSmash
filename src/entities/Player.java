package entities;

import java.util.ArrayList;

import entities.attacks.Attack;
import entities.attacks.Damage;
import entities.attacks.GrabHitbox;
import entities.particles.Particle;
import game.Game;
import game.Ledge;
import graphics.Sprite;
import graphics.SpriteLoader;
import input.Input;
import math.Rect;
import math.Seg;
import math.Vec;

public class Player extends Entity {

	private Vec position;
	private Vec velocity=Vec.zero;
	private Rect collisionBox;
	private Rect hangBoxLeft, hangBoxRight;
	private Input input;

	//Constants
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
	
	
	//animation constants
	private static final int runningAnimLen=40;
	private static final int rollingAnimLen=50;
	private static final int spotDodgeAnimLen=50;
	private static final int airDodgeAnimLen=50;
	private static final int hangImmunityLen=50;
	private static final int framesBetweenHangs=60;
	private static final int grabAttackAnimLen=40;
	private static final int grabDamageFrame=35;

	//Attacks
	private Attack groundAttack1;
	private Attack groundAttack2;
	private Attack airAttack1;
	private Attack airAttack2;
	private Attack recoveryAttack;
	private Attack grabMissAttack;
	private Attack grabAttack;
	
	//Player states
	private boolean grounded=false;
	private boolean facingRight;
	private boolean hasDoubleJump=false;
	private boolean hasRecoveryMove=true;
	private PlayerState state=PlayerState.AIRBORN;
	private int animationCounter=0;
	private int jumpFramesLeft=0;
	private int shield=maxShield;
	private int stunCounter=0;
	private int framesUntilNextHang=0;
	private Ledge hangingOn=null;
	private Attack currentAttack;
	private int team;
	private double damagePercent;
	private int hitLagLeft=0;
	private int grabAttacksMade=0;
	private int grabFreeCounter=0;
	private int invincibiltyAfterDyingCounter=0;
	private Entity grabbing;
	private Entity grabbedBy;
	
	
	public Player(Input input, Vec position, int team) {
		this.position=position;
		facingRight=(position.x()<=0);
		this.team=team;
		collisionBox=new Rect(new Vec(-40, -70), new Vec(40, 70));
		double hangCloseX=20;
		double hangLowY=30;
		double hangFarX=105;
		double hangHighY=110;
		hangBoxRight=new Rect(new Vec(hangCloseX, hangLowY), new Vec(hangFarX, hangHighY));
		hangBoxLeft=new Rect(new Vec(-hangFarX, hangLowY), new Vec(-hangCloseX, hangHighY));
		position=new Vec(0, 500);
		this.input=input;
		createAttacks();
	}
	
	private void createAttacks() {
		Damage damage1, damage2;
		
		//GROUND ATTACK 1
		groundAttack1=new Attack(false);
		groundAttack1.addPart(20, SpriteLoader.stickFigureKick1);
		groundAttack1.addPart(20, SpriteLoader.stickFigureKick2);
		groundAttack1.addPart(20, SpriteLoader.stickFigureKick3);
		Rect groundAttack1Rect1=new Rect(new Vec(0, -80), new Vec(110, 70));
		Rect groundAttack1Rect2=new Rect(new Vec(-40, 0), new Vec(70, 100));
		damage1=new Damage(groundAttack1Rect1, 10, new Vec(15, 10), 40, team);
		damage2=new Damage(groundAttack1Rect2, 10, new Vec(-5, 10), 40, team);
		groundAttack1.addDamageFrame(35, damage1);
		groundAttack1.addDamageFrame(40, damage2);
		
		//GROUND ATTACK 2
		groundAttack2=new Attack(false);
		groundAttack2.addPart(40, SpriteLoader.stickFigureDab1);
		groundAttack2.addPart(30, SpriteLoader.stickFigureDab2);
		Rect groundAttack2Rect1=new Rect(new Vec(30, 0), new Vec(60, 30));
		Rect groundAttack2Rect2=new Rect(new Vec(-90, -30), new Vec(70, 90));
		damage1=new Damage(groundAttack2Rect1, 25, new Vec(20, 15), 100, team);
		damage2=new Damage(groundAttack2Rect2, 4, new Vec(-5, 5), 10, team);
		groundAttack2.addDamageFrame(54, damage2);
		groundAttack2.addDamageFrame(55, damage1);
		
		//AIR ATTACK 1
		airAttack1=new Attack(true);
		airAttack1.addPart(40, SpriteLoader.stickFigureAirSpike);
		Rect airAttack1Rect=new Rect(new Vec(-20, -100), new Vec(80, 30));
		damage1=new Damage(airAttack1Rect, 12, new Vec(5, -10), 60, team);
		airAttack1.addDamageFrame(10, damage1);
		airAttack1.addDamageFrame(30, damage1);
		
		//AIR ATTACK 2
		airAttack2=new Attack(true);
		airAttack2.addPart(25, SpriteLoader.stickFigureAirSlice1);
		airAttack2.addPart(25, SpriteLoader.stickFigureAirSlice2);
		Rect airAttack2Rect1=new Rect(new Vec(-80, 0), new Vec(80, 100));
		Rect airAttack2Rect2=new Rect(new Vec(-40, -60), new Vec(100, 60));
		damage1=new Damage(airAttack2Rect1, 10, new Vec(5, 10), 40, team);
		damage2=new Damage(airAttack2Rect2, 10, new Vec(10, -5), 40, team);
		airAttack2.addDamageFrame(25, damage1);
		airAttack2.addDamageFrame(36, damage2);
		
		//RECOVERY ATTACK
		recoveryAttack=new Attack(false);
		recoveryAttack.markAsRecoveryAttack();
		recoveryAttack.addPart(20, SpriteLoader.stickFigureJetpack2);
		recoveryAttack.addPart(40, SpriteLoader.stickFigureJetpack1);
		for (int i=20; i<40; i++)
			recoveryAttack.addVelocityCue(i, new Vec(5, doubleJumpPower));
		recoveryAttack.addPart(100, SpriteLoader.stickFigureJetpack2);
		for (int i=40; i<160; i++)
			recoveryAttack.addGrabCue(i);
		
		Rect recoveryDamageBox=new Rect(new Vec(-100, -100), new Vec(40, 60));
		damage1=new Damage(recoveryDamageBox, 6, new Vec(-10, -4), 40, team);
		recoveryAttack.addDamageFrame(20, damage1);
		recoveryAttack.addDamageFrame(40, damage1);
		recoveryAttack.addDamageFrame(60, damage1);
		
		//GRAB MISS ATTACK
		grabMissAttack=new Attack(false);
		grabMissAttack.addPart(60, SpriteLoader.stickFigureGrab);
		
		//GRAB ATTACK
		grabAttack=new Attack(false);
		grabAttack.addPart(grabAttackAnimLen, SpriteLoader.stickFigureGrabRelease);
		//damage updated when grab is released because it differs depending on
		//the number of times they hit the grab button
	}
	
	public void update() {
		applyGravity();
		applyFriction();
		checkForInputAndMovement();
		
		moveToCollision();
		updateGrounded();
		
		collisionBox.setDrawOffeset(position);
		hangBoxLeft.setDrawOffeset(position);
		hangBoxRight.setDrawOffeset(position);
	}
	
	private void updateGrounded() {
		grounded=wouldBeInGround(position.add(Vec.down.scale(0.1)));
		if (!hittingPlatform(position)&&hittingPlatform(position.add(Vec.down.scale(0.1))))
			grounded=true;
	}
	
	private void applyFriction() {
		if (grounded) {
			if (state==PlayerState.ATTACKING) {
				velocity=new Vec(velocity.x()*xAttackingFriction, velocity.y()*yFriction);
			}
			else {
				velocity=new Vec(velocity.x()*xGroundedFriction, velocity.y()*yFriction);
			}
		}
		else {
			velocity=new Vec(velocity.x()*xAirFriction, velocity.y()*yFriction);
		}
	}
	
	private void applyGravity() {
		if (!grounded) {
			if (input.downMovementHeld())
				velocity=velocity.add(fastGravity);
			else
				velocity=velocity.add(gravity);
		}
	}
	
	private void checkForInputAndMovement() {
		if (state!=PlayerState.STUNNED&&state!=PlayerState.ROLLING&&state!=PlayerState.SPOT_DODGING&&state!=PlayerState.HANGING&&!(state==PlayerState.AIR_HIT&&hitLagLeft>0)&&
				state!=PlayerState.ATTACKING&&state!=PlayerState.GRABBING&&state!=PlayerState.BEING_GRABBED&&
				input.jumpMovementPressed()&&grounded) {
			velocity=new Vec(velocity.x(), jumpPower);
			jumpFramesLeft=numJumpFrames;
			setAnimation(PlayerState.AIRBORN);
		}
		if (state!=PlayerState.HANGING&&state!=PlayerState.GRABBING&&state!=PlayerState.BEING_GRABBED&&
				input.jumpMovementHeld()) {
			if (!grounded&&jumpFramesLeft>0) {
				jumpFramesLeft--;
				velocity=new Vec(velocity.x(), jumpPower);
			}
		}
		else {
			jumpFramesLeft=0;
		}
		
		//double jump
		if (state!=PlayerState.AIR_DODGING&&!grounded&&state!=PlayerState.HANGING&&state!=PlayerState.AIR_ATTACKING&&input.jumpMovementPressed()
				&&(!(state==PlayerState.AIR_HIT&&hitLagLeft>0))&&state!=PlayerState.GRABBING&&state!=PlayerState.BEING_GRABBED&&
				hasDoubleJump) {
			hasDoubleJump=false;
			velocity=new Vec(velocity.x(), doubleJumpPower);
			Particle.createDoubleJumpParticle(position);
			setAnimation(PlayerState.AIRBORN);
		}
		
		//movement
		if (state!=PlayerState.STUNNED&&state!=PlayerState.SHIELDING&&state!=PlayerState.ROLLING&&state!=PlayerState.HANGING&&state!=PlayerState.ATTACKING
				&&!(state==PlayerState.AIR_HIT&&hitLagLeft>0)&&state!=PlayerState.KNOCKED_DOWN&&state!=PlayerState.GRABBING&&state!=PlayerState.BEING_GRABBED) {
			if (input.leftMovementHeld()) {
				if (grounded) {
					if (state!=PlayerState.SPOT_DODGING)
						velocity=velocity.add(Vec.left.scale(moveGroundSpeed));
				}
				else
					velocity=velocity.add(Vec.left.scale(moveAirSpeed));
			}
			if (input.rightMovementHeld()) {
				if (grounded) {
					if (state!=PlayerState.SPOT_DODGING)
						velocity=velocity.add(Vec.right.scale(moveGroundSpeed));
				}
				else
					velocity=velocity.add(Vec.right.scale(moveAirSpeed));
			}
		}

		animationCounter++;
		if (state!=PlayerState.SHIELDING) {
			shield++;
			shield=Math.min(shield, maxShield);
		}
		framesUntilNextHang=Math.max(0, framesUntilNextHang-1);
		hitLagLeft=Math.max(0, hitLagLeft-1);
		invincibiltyAfterDyingCounter=Math.max(0, invincibiltyAfterDyingCounter-1);
		switch(state) {
			case AIRBORN:
				if (grounded) {
					onLand();
				}
				else if (input.shieldHeld())
					setAnimation(PlayerState.AIR_DODGING);
				else if (tryToAttack())
					;
				else {
					if (framesUntilNextHang<=0)
						tryToHang();
				}
				break;
			case IDLE:
				if (!grounded)
					setAnimation(PlayerState.AIRBORN);
				else if (Math.abs(velocity.x())>=minSpeedToRun)
					setAnimation(PlayerState.RUNNING);
				else if (input.shieldHeld())
					setAnimation(PlayerState.SHIELDING);
				else if (tryToAttack())
					;
				break;
			case RUNNING:
				if (animationCounter>=runningAnimLen)
					animationCounter=0;
				
				if (Math.abs(velocity.x())<minSpeedToRun)
					setAnimation(PlayerState.IDLE);
				else if (!grounded)
					setAnimation(PlayerState.AIRBORN);
				else if (tryToAttack())
					;
				else {
					boolean oldFacingRight=facingRight;
					facingRight=velocity.x()>0;
					if (oldFacingRight^facingRight)
						createRunTurnParticle();
				}
				break;
			case SHIELDING:
				if (!grounded) {
					setAnimation(PlayerState.AIRBORN);
					break;
				}
				shield=Math.max(0, shield-3);
				if (shield==0) {
					setAnimation(PlayerState.STUNNED);
					stunCounter=stunFrameAfterBrokenShield;
				}
				else if (!input.shieldHeld()) {
					setAnimation(PlayerState.IDLE);
				}
				else if (input.leftMovementHeld()) {
					facingRight=false;
					setAnimation(PlayerState.ROLLING);
				}
				else if (input.rightMovementHeld()) {
					facingRight=true;
					setAnimation(PlayerState.ROLLING);
				}
				else if (input.downMovementHeld()) {
					setAnimation(PlayerState.SPOT_DODGING);
				}
				break;
			case STUNNED:
				stunCounter--;
				if (stunCounter<=0) {
					setAnimation(PlayerState.IDLE);
				}
				else if (stunCounter%10==0) {
					Vec offset=new Vec((Math.random()*2-1)*40, (Math.random()*2-1)*50+15);
					Particle.createStunParticle(position.add(offset));
				}
				break;
			case ROLLING:
				velocity=(facingRight?Vec.right:Vec.left).scale(rollVelocity);
				if (animationCounter>rollingAnimLen)
					state=PlayerState.IDLE;
				break;
			case SPOT_DODGING:
				if (animationCounter>=spotDodgeAnimLen) {
					setAnimation(PlayerState.IDLE);
				}
				break;
			case AIR_DODGING:
				if (grounded) {
					onLand();
				}
				else if (animationCounter>=airDodgeAnimLen) {
					setAnimation(PlayerState.AIRBORN);
				}
				break;
			case HANGING:
				velocity=Vec.zero;
				framesUntilNextHang=framesBetweenHangs;
				if (input.downMovementHeld()||(input.leftMovementHeld()&&facingRight)||(input.rightMovementHeld()&&!facingRight)) {
					//drop from the ledge
					hasDoubleJump=true;
					hasRecoveryMove=true;
					setAnimation(PlayerState.AIRBORN);
					hangingOn.occupied=false;
					hangingOn=null;
					if (facingRight)
						velocity=velocity.add(Vec.left.scale(3.5));
					else
						velocity=velocity.add(Vec.right.scale(3.5));
				}
				else if (input.jumpMovementPressed()) {
					//jump up
					hasDoubleJump=true;
					hasRecoveryMove=true;
					setAnimation(PlayerState.AIRBORN);
					hangingOn.occupied=false;
					hangingOn=null;
					velocity=Vec.up.scale(jumpPower*2);
					if (facingRight) {
						position=position.add(Vec.left.scale(30));
						velocity=velocity.add(Vec.right.scale(6));
					}
					else {
						position=position.add(Vec.right.scale(30));
						velocity=velocity.add(Vec.left.scale(6));
					}
				}
				else if (input.shieldHeld()) {
					//roll
					setAnimation(PlayerState.ROLLING);
					if (facingRight) {
						position=hangingOn.getPos().sub(collisionBox.corners()[1]).add(new Vec(50, .1));
					}
					else {
						position=hangingOn.getPos().sub(collisionBox.corners()[0]).add(new Vec(-50, .1));
					}
					hangingOn.occupied=false;
					hangingOn=null;
				}
				else if (animationCounter>=hangImmunityLen&&(input.leftMovementHeld()||input.rightMovementHeld())) {
					//just climb up
					setAnimation(PlayerState.IDLE);
					if (facingRight) {
						position=hangingOn.getPos().sub(collisionBox.corners()[1]).add(new Vec(50, .1));
					}
					else {
						position=hangingOn.getPos().sub(collisionBox.corners()[0]).add(new Vec(-50, .1));
					}
					hangingOn.occupied=false;
					hangingOn=null;
				}
				else {
					//otherwise keep hanging on
					if (facingRight)
						position=hangingOn.getPos().sub(hangBoxRight.center());
					else
						position=hangingOn.getPos().sub(hangBoxLeft.center());
				}
				break;
			case ATTACKING:
				currentAttack.update(grounded, facingRight, position);
				if (currentAttack.isOver())
					setAnimation(PlayerState.IDLE);
				else {
					if (!grounded)
						setAnimation(PlayerState.AIR_ATTACKING);
					Vec newVel=currentAttack.getVelocity(facingRight);
					if (newVel!=null) {
						velocity=newVel;
					}
				}
				break;
			case AIR_ATTACKING:
				currentAttack.update(grounded, facingRight, position);
				if (currentAttack.isRecoveryAttack()) hasRecoveryMove=false;
				if (currentAttack.isOver())
					setAnimation(PlayerState.AIRBORN);
				if (currentAttack.isOver())
					setAnimation(PlayerState.IDLE);
				else {
					Vec newVel=currentAttack.getVelocity(facingRight);
					if (newVel!=null)
						velocity=newVel;
					if (currentAttack.canGrab() && framesUntilNextHang<=0)
						tryToHang();
				}
				break;
			case AIR_HIT:
				if (hitLagLeft>0) {
					if (grounded) {
						onLand();
						setAnimation(PlayerState.KNOCKED_DOWN);
					}
				}
				else {
					if (grounded) {
						onLand();
						setAnimation(PlayerState.KNOCKED_DOWN);
					}
					else if (input.shieldHeld())
						setAnimation(PlayerState.AIR_DODGING);
					else 
						tryToHang();
				}
				break;
			case KNOCKED_DOWN:
				if (hitLagLeft>0) {
					if (!grounded)
						setAnimation(PlayerState.AIR_HIT);
				}
				else {
					if (input.leftMovementHeld()) {
						facingRight=false;
						setAnimation(PlayerState.ROLLING);
					}
					else if (input.rightMovementHeld()) {
						facingRight=true;
						setAnimation(PlayerState.ROLLING);
					}
					else if (input.downMovementHeld()) {
						setAnimation(PlayerState.IDLE);
					}
					else if (tryToAttack()) {
						
					}
					else if (input.shieldHeld()) {
						setAnimation(PlayerState.SHIELDING);
					}
				}
				break;
			case GRABBING:
				if (input.grabPressed())
					grabAttacksMade++;
				break;
			case BEING_GRABBED:
				grabFreeCounter++;
				if (input.grabPressed()) {
					grabFreeCounter+=grabMashSkipFrames;
				}
				if (grabFreeCounter>=fullGrabLength||!grounded) {
					grabbedBy.releaseGrabbedEntityRequest();
					grabFreeCounter=Integer.MIN_VALUE;
				}
				break;
		}
	}
	
	private boolean wouldBeInGround(Vec newPos) {
		Rect newCollisionBox=collisionBox.offsetBy(newPos);
		for (Rect r:Game.getScene().getCollisionBoxes()) {
			if (newCollisionBox.intersects(r))
				return true;
		}
		return false;
	}
	
	private boolean hittingPlatform(Vec newPos) {
		if (input.downMovementHeld()) return false;
		Rect newCollusionBox=collisionBox.offsetBy(newPos);
		for (Seg s:Game.getScene().getPlatforms()) {
			if (newCollusionBox.intersectsSeg(s))
				return true;
		}
		return false;
	}
	
	private void moveToCollision() {
		//move y
		Vec toMove=Vec.j.scale(Vec.j.dot(velocity));
		while (toMove.mag()>Vec.EPS) {
			Vec nextStep;
			if (toMove.mag()>0.05)
				nextStep=toMove.unit().scale(0.05);
			else
				nextStep=toMove;
			
			//also, don't go through ground
			boolean tooFar=wouldBeInGround(position.add(nextStep));
			if (!hittingPlatform(position)&&hittingPlatform(position.add(nextStep))&&toMove.y()<0)
				tooFar=true;
			if (tooFar) {
				toMove=Vec.zero;
				velocity=new Vec(velocity.x(), 0);
			}
			else {
				position=position.add(nextStep);
				toMove=toMove.sub(nextStep);
			}
		}
		
		//move x
		toMove=Vec.i.scale(Vec.i.dot(velocity));
		while (toMove.mag()>Vec.EPS) {
			Vec nextStep;
			if (toMove.mag()>0.05)
				nextStep=toMove.unit().scale(0.05);
			else
				nextStep=toMove;
			
			if (wouldBeInGround(position.add(nextStep))) {
				toMove=Vec.zero;
				velocity=new Vec(0, velocity.y());
			}
			else {
				position=position.add(nextStep);
				toMove=toMove.sub(nextStep);
			}
		}
	}
	
	private void createRunTurnParticle() {
		Particle.createRunTurnSprite(position.add(Vec.down.scale(45)).add((facingRight?Vec.left:Vec.right).scale(10)), facingRight);
	}
	
	private void setAnimation(PlayerState newAnimation) {
		animationCounter=0;
		state=newAnimation;
	}
	
	private void startAttack(Attack toUse) {
		toUse.start();
		currentAttack=toUse;
		setAnimation(PlayerState.ATTACKING);
	}
	
	private void onLand() {
		hasDoubleJump=true;
		hasRecoveryMove=true;
		if (Math.abs(velocity.x())>=minSpeedToRun)
			setAnimation(PlayerState.RUNNING);
		else
			setAnimation(PlayerState.IDLE);
		if (Math.abs(velocity.x())>=minSpeedToRun&&hitLagLeft<=0)
			facingRight=velocity.x()>0;
		createRunTurnParticle();
		facingRight^=true;
		createRunTurnParticle();
		facingRight^=true;
	}
	
	private void tryToHang() {
		ArrayList<Ledge> hangPositions=Game.getScene().getHangPositions();
		for (Ledge l:hangPositions) {
			if (l.occupied) continue;
			
			if (hangBoxRight.offsetBy(position).contains(l.getPos())) {
				//hang to right
				setAnimation(PlayerState.HANGING);
				hangingOn=l;
				l.occupied=true;
				facingRight=true;
				position=l.getPos().sub(hangBoxRight.center());
			}
			else if (hangBoxLeft.offsetBy(position).contains(l.getPos())) {
				//hang to left
				setAnimation(PlayerState.HANGING);
				hangingOn=l;
				l.occupied=true;
				facingRight=false;
				position=l.getPos().sub(hangBoxLeft.center());
			}
		}
	}
	
	private boolean tryToAttack() {
		if (input.attack1Pressed())
			startAttack(grounded?groundAttack1:airAttack1);
		else if (input.attack2Pressed())
			startAttack(grounded?groundAttack2:airAttack2);
		else if (input.attackRecoverPressed()&&hasRecoveryMove)
			startAttack(recoveryAttack);
		else if (tryToGrab())
			;
		else 
			return false;
		return true;
	}
	
	private boolean tryToGrab() {
		if (grounded&&input.grabPressed()) {
			grabAttacksMade=0;
			Rect hitboxPos=new Rect(new Vec(20, -30), new Vec(120, 30));
			if (!facingRight)
				hitboxPos=hitboxPos.flipX();
			hitboxPos=hitboxPos.offsetBy(position);
			Vec grabPosition=position.add(facingRight?Vec.right.scale(120):Vec.left.scale(120));
			GrabHitbox hitbox=new GrabHitbox(this, hitboxPos, team, facingRight, grabPosition);
			Entity hit=hitbox.runScan();
			if (hit==null) {
				startAttack(grabMissAttack);
			}
			else {
				setAnimation(PlayerState.GRABBING);
				grabbing=hit;
			}
			return true;
			
		}
		return false;
	}
	
	public void processDamage(Damage damage) {
		if (damage.getTeam()==team)
			return;
		
		//if I am immune, ignore the damage
		if (isInvincible())
			return;
		
		if (state==PlayerState.SHIELDING) {
			shield-=15*damage.getPercentDamage();
			return;
		}
		
		if (damage.getHitbox().intersects(collisionBox.offsetBy(position))) {
			damagePercent+=damage.getPercentDamage();
			velocity=damage.getHitVelocity().scale(1+damagePercent/100.0);
			hitLagLeft=damage.getHitLagFrames();
			setAnimation(PlayerState.AIR_HIT);
			facingRight=!(velocity.x()>=0);
			hasRecoveryMove=true;
		}
	}
	
	public boolean processGrab(GrabHitbox grab) {
		if (grab.getTeam()==team)
			return false;
		
		//if I am immune, ignore the damage
		if (state==PlayerState.AIR_DODGING || state==PlayerState.SPOT_DODGING || state==PlayerState.ROLLING || (state==PlayerState.HANGING&&animationCounter<hangImmunityLen)
				||state==PlayerState.BEING_GRABBED)
			return false;
		if (grab.getHitbox().intersects(collisionBox.offsetBy(position))) {
			grabFreeCounter=0;
			setAnimation(PlayerState.BEING_GRABBED);
			grabbedBy=grab.getGrabber();
			facingRight=grab.getGrabberFacingRight();
			position=grab.getGrabPosition();
			return true;
		}
		return false;
	}
	
	public void releaseGrabbedEntityRequest() {
		if (grabAttacksMade==0) {
			setAnimation(PlayerState.IDLE);
			grabbing.onReleasedFromGrab();
		}
		else {
			Rect grabAttackRect=new Rect(new Vec(60, -50), new Vec(140, 40));
			Damage damage1=new Damage(grabAttackRect, 4+grabAttacksMade, new Vec(5+grabAttacksMade*1.75, 1+grabAttacksMade), 80, team);
			grabAttack.addDamageFrame(grabDamageFrame, damage1);
			startAttack(grabAttack);
		}
	}
	
	public void onReleasedFromGrab() {
		facingRight^=true;
		velocity=new Vec(facingRight?-5:5, 10);
		setAnimation(PlayerState.AIRBORN);
	}
	
	private boolean isInvincible() {
		if (state==PlayerState.AIR_DODGING || state==PlayerState.SPOT_DODGING || state==PlayerState.ROLLING)
			return true;
		if ((state==PlayerState.HANGING&&animationCounter<hangImmunityLen) || invincibiltyAfterDyingCounter>0)
			return true;
		return false;
	}

	public Vec isCameraFocusable() {
		return position;
	}
	
	public void render() {
		Sprite toDraw=null;
		boolean drawAtFullAlpha=!isInvincible();
		switch(state) {
			case AIRBORN:
				if (velocity.y()>=0)
					toDraw=SpriteLoader.stickFigureAirUp;
				else
					toDraw=SpriteLoader.stickFigureAirDown;
				break;
			case IDLE:
				toDraw=SpriteLoader.stickFigureIdle;
				break;
			case RUNNING:
				if (animationCounter>=runningAnimLen/2)
					toDraw=SpriteLoader.stickFigureRunning1;
				else
					toDraw=SpriteLoader.stickFigureRunning2;
				break;
			case SHIELDING:
				toDraw=SpriteLoader.stickFigureIdle;
				break;
			case STUNNED:
				toDraw=SpriteLoader.stickFigureIdle;
				break;
			case ROLLING:
				if (animationCounter<rollingAnimLen/2)
					toDraw=SpriteLoader.stickFigureRolling1;
				else
					toDraw=SpriteLoader.stickFigureRolling2;
				break;
			case SPOT_DODGING:
				toDraw=SpriteLoader.stickFigureIdle;
				break;
			case AIR_DODGING:
				if (velocity.y()>=0)
					toDraw=SpriteLoader.stickFigureAirUp;
				else
					toDraw=SpriteLoader.stickFigureAirDown;
				break;
			case HANGING:
				toDraw=SpriteLoader.stickFigureHang;
				break;
			case ATTACKING:
				toDraw=currentAttack.getCurrentSprite();
				break;
			case AIR_ATTACKING:
				toDraw=currentAttack.getCurrentSprite();
				break;
			case AIR_HIT:
				toDraw=SpriteLoader.stickFigureAirHit;
				break;
			case KNOCKED_DOWN:
				toDraw=SpriteLoader.stickFigureKnockedDown;
				break;
			case GRABBING:
				toDraw=SpriteLoader.stickFigureGrab;
				break;
			case BEING_GRABBED:
				toDraw=SpriteLoader.stickFigureGrabbed;
				break;
			default:
				throw new Error("invalid render state: "+state);	
		}
		collisionBox.render();
		hangBoxLeft.render();
		hangBoxRight.render();
		toDraw.drawAlpha(position, facingRight, drawAtFullAlpha?1:0.2);
		
		if (state==PlayerState.SHIELDING) {
			double shieldScale=maxShieldScale*(shield/(double)maxShield);
			SpriteLoader.shieldSprite.drawAlphaAndSize(position, 0.6, shieldScale, shieldScale);
		}
	}
	
	
}
