package entities;

import java.util.ArrayList;
import java.util.Random;

import entities.attacks.Attack;
import entities.attacks.Damage;
import entities.attacks.GrabHitbox;
import entities.particles.Particle;
import game.Game;
import game.Ledge;
import graphics.Camera;
import graphics.Sprite;
import graphics.SpriteLoader;
import input.Input;
import math.Lerp;
import math.Rect;
import math.Seg;
import math.Vec;

public class Player extends Entity {

	private Vec position;
	private Vec velocity=Vec.zero;
	private Rect collisionBox;
	private Rect hangBoxLeft, hangBoxRight;
	private Input input;
	
	private PlayerInstance instance;

	//Player states
	private boolean grounded=false;
	private boolean facingRight;
	private boolean hasDoubleJump=false;
	private boolean hasRecoveryMove=true;
	private PlayerState state=PlayerState.AIRBORN;
	private int animationCounter=0;
	private int jumpFramesLeft=0;
	private int shield;
	private int stunCounter=0;
	private int framesUntilNextHang=0;
	private int framesUntilNextAttack=0;
	private int framesUntilNextShield=0;
	private Ledge hangingOn=null;
	private Attack currentAttack;
	private int team;
	private double damagePercent;
	private int hitLagLeft=0;
	private int grabAttacksMade=0;
	private int grabFreeCounter=0;
	private int grabIconCounter=0;
	private int invincibilityAfterDyingCounter=0;
	private Entity grabbing;
	private Entity grabbedBy;
	int livesLeft=3;
	private double percentAcrossScreenToRenderUI;
	private boolean showHighlight;
	
	//team colors: 0: Grey, 1-4: red, blue, green, yellow
	public Player(Input input, Vec position, int team, double percentAcrossScreenToRenderUI, boolean showHighlight) {
		this.position=position;
		this.percentAcrossScreenToRenderUI=percentAcrossScreenToRenderUI;
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
		this.showHighlight=showHighlight;
		instance=new StickFigureInstance(team);
		shield=instance.maxShield();
	}
	
	
	public void update() {
		if (!isAlive())
			return;
		
		applyGravity();
		applyFriction();
		checkForInputAndMovement();
		
		moveToCollision();
		updateGrounded();
		checkIfDead();
		
		collisionBox.setDrawOffeset(position);
		hangBoxLeft.setDrawOffeset(position);
		hangBoxRight.setDrawOffeset(position);
	}
	
	private void updateGrounded() {
		grounded=wouldBeInGround(position.add(Vec.down.scale(0.1)));
		if (!hittingPlatform(position)&&hittingPlatform(position.add(Vec.down.scale(0.1))))
			grounded=true;
		if (grounded)
			hasRecoveryMove=true;
	}
	
	private void applyFriction() {
		if (grounded) {
			if (state==PlayerState.ATTACKING) {
				velocity=new Vec(velocity.x()*instance.xAttackingFriction(), velocity.y()*instance.yFriction());
			}
			else {
				velocity=new Vec(velocity.x()*instance.xGroundedFriction(), velocity.y()*instance.yFriction());
			}
		}
		else {
			velocity=new Vec(velocity.x()*instance.xAirFriction(), velocity.y()*instance.yFriction());
		}
	}
	
	private void applyGravity() {
		if (!grounded) {
			if (input.downMovementHeld())
				velocity=velocity.add(instance.fastGravity());
			else
				velocity=velocity.add(instance.gravity());
		}
	}
	
	private void checkForInputAndMovement() {
		if (state!=PlayerState.STUNNED&&state!=PlayerState.ROLLING&&state!=PlayerState.SPOT_DODGING&&state!=PlayerState.HANGING&&!(state==PlayerState.AIR_HIT&&hitLagLeft>0)&&
				state!=PlayerState.ATTACKING&&state!=PlayerState.GRABBING&&state!=PlayerState.BEING_GRABBED&&
				input.jumpMovementPressed()&&grounded) {
			velocity=new Vec(velocity.x(), instance.jumpPower());
			jumpFramesLeft=instance.numJumpFrames();
			setAnimation(PlayerState.AIRBORN);
		}
		if (state!=PlayerState.HANGING&&state!=PlayerState.GRABBING&&state!=PlayerState.BEING_GRABBED&&
				input.jumpMovementHeld()) {
			if (!grounded&&jumpFramesLeft>0) {
				jumpFramesLeft--;
				velocity=new Vec(velocity.x(), instance.jumpPower());
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
			velocity=new Vec(velocity.x(), instance.doubleJumpPower());
			Particle.createDoubleJumpParticle(position);
			setAnimation(PlayerState.AIRBORN);
		}
		
		//movement
		if (state!=PlayerState.STUNNED&&state!=PlayerState.SHIELDING&&state!=PlayerState.ROLLING&&state!=PlayerState.HANGING&&state!=PlayerState.ATTACKING
				&&!(state==PlayerState.AIR_HIT&&hitLagLeft>0)&&state!=PlayerState.KNOCKED_DOWN&&state!=PlayerState.GRABBING&&state!=PlayerState.BEING_GRABBED) {
			if (input.leftMovementHeld()) {
				if (grounded) {
					if (state!=PlayerState.SPOT_DODGING)
						velocity=velocity.add(Vec.left.scale(instance.moveGroundSpeed()));
				}
				else
					velocity=velocity.add(Vec.left.scale(instance.moveAirSpeed()));
			}
			if (input.rightMovementHeld()) {
				if (grounded) {
					if (state!=PlayerState.SPOT_DODGING)
						velocity=velocity.add(Vec.right.scale(instance.moveGroundSpeed()));
				}
				else
					velocity=velocity.add(Vec.right.scale(instance.moveAirSpeed()));
			}
		}

		animationCounter++;
		if (state!=PlayerState.SHIELDING) {
			shield++;
			shield=Math.min(shield, instance.maxShield());
		}
		framesUntilNextHang=Math.max(0, framesUntilNextHang-1);
		hitLagLeft=Math.max(0, hitLagLeft-1);
		invincibilityAfterDyingCounter=Math.max(0, invincibilityAfterDyingCounter-1);
		framesUntilNextAttack=Math.max(0, framesUntilNextAttack-1);
		framesUntilNextShield=Math.max(0, framesUntilNextShield-1);
		switch(state) {
			case AIRBORN:
				if (grounded) {
					onLand();
				}
				else if (input.shieldHeld()&&framesUntilNextShield==0) {
					setAnimation(PlayerState.AIR_DODGING);
					framesUntilNextShield=instance.airDodgeAnimLen()+instance.noShieldAfterRollOrDodge();
				}
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
				else if (Math.abs(velocity.x())>=instance.minSpeedToRun())
					setAnimation(PlayerState.RUNNING);
				else if (input.shieldHeld()&&framesUntilNextShield==0)
					setAnimation(PlayerState.SHIELDING);
				else if (tryToAttack())
					;
				break;
			case RUNNING:
				if (animationCounter>=instance.runningAnimLen())
					animationCounter=0;
				
				if (Math.abs(velocity.x())<instance.minSpeedToRun())
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
					stunCounter=instance.stunFrameAfterBrokenShield();
				}
				else if (!input.shieldHeld()) {
					setAnimation(PlayerState.IDLE);
				}
				else if (input.leftMovementHeld()) {
					facingRight=false;
					setAnimation(PlayerState.ROLLING);
					framesUntilNextShield=instance.rollingAnimLen()+instance.noShieldAfterRollOrDodge();
				}
				else if (input.rightMovementHeld()) {
					facingRight=true;
					setAnimation(PlayerState.ROLLING);
					framesUntilNextShield=instance.rollingAnimLen()+instance.noShieldAfterRollOrDodge();
				}
				else if (input.downMovementHeld()) {
					setAnimation(PlayerState.SPOT_DODGING);
					framesUntilNextShield=instance.spotDodgeAnimLen()+instance.noShieldAfterRollOrDodge();
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
				velocity=(facingRight?Vec.right:Vec.left).scale(instance.rollVelocity());
				if (animationCounter>instance.rollingAnimLen())
					state=PlayerState.IDLE;
				break;
			case SPOT_DODGING:
				if (animationCounter>=instance.spotDodgeAnimLen()) {
					setAnimation(PlayerState.IDLE);
				}
				break;
			case AIR_DODGING:
				if (grounded) {
					onLand();
				}
				else if (animationCounter>=instance.airDodgeAnimLen()) {
					setAnimation(PlayerState.AIRBORN);
				}
				break;
			case HANGING:
				velocity=Vec.zero;
				framesUntilNextHang=instance.framesBetweenHangs();
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
					velocity=Vec.up.scale(instance.jumpPower()*2);
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
				else if (animationCounter>=instance.hangImmunityLen()&&(input.leftMovementHeld()||input.rightMovementHeld())) {
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
				if (currentAttack.isOver()) {
					framesUntilNextAttack=currentAttack.getNoAttackAfterLenght();
					setAnimation(PlayerState.IDLE);
				}
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
				if (currentAttack.isOver()) {
					framesUntilNextAttack=currentAttack.getNoAttackAfterLenght();
					setAnimation(PlayerState.AIRBORN);
				}
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
					else {
						if (velocity.mag()>7&&hitLagLeft%5==1)
							Particle.createSmokeParticle(position);
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
				grabIconCounter++;
				if (input.grabPressed()) {
					grabAttacksMade++;
					Particle.createKeyPressedParticle(position.add(instance.grabIconOffset()));
				}
				break;
			case BEING_GRABBED:
				grabFreeCounter++;
				grabIconCounter++;
				if (input.grabPressed()) {
					grabFreeCounter+=instance.grabMashSkipFrames();
					Particle.createKeyPressedParticle(position.add(instance.grabIconOffset()));
				}
				if (grabFreeCounter>=instance.fullGrabLength()||!grounded) {
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
		if (Math.abs(velocity.x())>=instance.minSpeedToRun())
			setAnimation(PlayerState.RUNNING);
		else
			setAnimation(PlayerState.IDLE);
		if (Math.abs(velocity.x())>=instance.minSpeedToRun()&&hitLagLeft<=0)
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
		if (framesUntilNextAttack>0)
			return false;
		
		if (input.attack1Pressed())
			startAttack(grounded?instance.groundAttack1():instance.airAttack1());
		else if (input.attack2Pressed())
			startAttack(grounded?instance.groundAttack2():instance.airAttack2());
		else if (input.attackRecoverPressed()&&hasRecoveryMove)
			startAttack(instance.recoveryAttack());
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
			grabIconCounter=0;
			Entity hit=hitbox.runScan();
			if (hit==null) {
				startAttack(instance.grabMissAttack());
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
		if (state==PlayerState.AIR_DODGING || state==PlayerState.SPOT_DODGING || state==PlayerState.ROLLING || (state==PlayerState.HANGING&&animationCounter<instance.hangImmunityLen())
				||state==PlayerState.BEING_GRABBED)
			return false;
		if (grab.getHitbox().intersects(collisionBox.offsetBy(position))) {
			grabFreeCounter=0;
			setAnimation(PlayerState.BEING_GRABBED);
			grabbedBy=grab.getGrabber();
			facingRight=grab.getGrabberFacingRight();
			position=grab.getGrabPosition();
			grabIconCounter=0;
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
			instance.grabAttack().addDamageFrame(instance.grabDamageFrame(), damage1);
			startAttack(instance.grabAttack());
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
		if ((state==PlayerState.HANGING&&animationCounter<instance.hangImmunityLen()) || invincibilityAfterDyingCounter>0)
			return true;
		return false;
	}

	private void checkIfDead() {
		Rect sceneBox=Game.getScene().getBoundingBox();
		if (!sceneBox.contains(position)) {
			if (position.x()<=sceneBox.getLeft()) {
				Particle.createHorizontalExplosionParticle(position, false);
			}
			if (position.x()>=sceneBox.getRight()) {
				Particle.createHorizontalExplosionParticle(position, true);
			}
			if (position.y()<=sceneBox.getBottom()) {
				Particle.createVerticleExplosionParticle(position, false);
			}
			if (position.y()>=sceneBox.getTop()) {
				Particle.createVerticleExplosionParticle(position, true);
			}
			livesLeft--;
				
			if (isAlive()) {
				Vec[] spawnPoints=Game.getScene().getSpawnPoints();
				Random r=new Random();
				position=spawnPoints[r.nextInt(spawnPoints.length)];
				velocity=Vec.zero;
				invincibilityAfterDyingCounter=instance.invincibilityAfterDying();
				setAnimation(PlayerState.IDLE);
			}
		}
	}

	public Vec isCameraFocusable() {
		return position;
	}
	
	public boolean isAlive() {
		return livesLeft>0;
	}
	
	public void setLives(int livesLeft) {
		this.livesLeft=livesLeft;
	}
	
	public void renderUI() {
		Camera.getInstance().pushState();
		Camera.getInstance().setWorldWidth(1000);
		Camera.getInstance().setPosition(Vec.zero);
		
		Sprite characterFace=SpriteLoader.stickFigureIconSprite;
		Vec centerUIPosition=Lerp.lerp(new Vec(-300, -220), new Vec(300, -220), percentAcrossScreenToRenderUI);
		centerUIPosition=centerUIPosition.add(new Vec(-50, 0));
		characterFace.drawAlphaAndSize(centerUIPosition, isAlive()?1:0.25, 0.2, 0.2);
		
		Sprite[] textBackground= {SpriteLoader.greyNameBackground, SpriteLoader.redNameBackground, SpriteLoader.blueNameBackground, 
				SpriteLoader.greenNameBackground, SpriteLoader.yellowNameBackground};
		Vec nameOffset=new Vec(80, -30);
		textBackground[team].drawAlphaAndSize(centerUIPosition.add(nameOffset), 1, 0.3, 0.3);
		SpriteLoader.cueballText.drawAlphaAndSize(centerUIPosition.add(nameOffset.add(Vec.down.scale(10))), 1, 0.2, 0.2);
		
		Sprite[] lifeDot= {SpriteLoader.greyDot, SpriteLoader.redDot, SpriteLoader.blueDot, SpriteLoader.greenDot, SpriteLoader.yellowDot};
		double lifeDotOffset=23;
		Vec startDrawingDotsPos=centerUIPosition.add(new Vec(-lifeDotOffset, -50));
		for (int i=0; i<livesLeft; i++) {
			SpriteLoader.blackDot.drawAlphaAndSize(startDrawingDotsPos, 1, .2, .2);
			lifeDot[team].drawAlphaAndSize(startDrawingDotsPos, 1, .25, .25);
			startDrawingDotsPos=startDrawingDotsPos.add(Vec.right.scale(lifeDotOffset));
		}
		
		if (isAlive()) {
			Vec numbersPosition=centerUIPosition.add(new Vec(60, 10));
			double textOffset=35;
			Sprite[] numbers= {
				SpriteLoader.red0, 
				SpriteLoader.red1, 
				SpriteLoader.red2, 
				SpriteLoader.red3, 
				SpriteLoader.red4, 
				SpriteLoader.red5, 
				SpriteLoader.red6, 
				SpriteLoader.red7, 
				SpriteLoader.red8, 
				SpriteLoader.red9, 
				};
			for (char c:(""+((int)damagePercent)).toCharArray()) {
				Sprite toDraw=numbers[c-'0'];
				toDraw.drawAlphaAndSize(numbersPosition, 1, 0.2, 0.2);
				numbersPosition=numbersPosition.add(Vec.right.scale(textOffset));
			}
			SpriteLoader.redPercent.drawAlphaAndSize(numbersPosition, 1, 0.2, 0.2);
		}
		
		Camera.getInstance().popState();
	}
	
	public void render() {
		if (!isAlive()) 
			return;
		
		Sprite[] highlight= {SpriteLoader.redCharacterBackground, SpriteLoader.redCharacterBackground, SpriteLoader.blueCharacterBackground, 
				SpriteLoader.greenCharacterBackground, SpriteLoader.yellowCharacterBackground};
		if (showHighlight) {
			highlight[team].drawAlphaAndSize(position, 0.8, 0.6, 0.6);
		}
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
				if (animationCounter>=instance.runningAnimLen()/2)
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
				if (animationCounter<instance.rollingAnimLen()/2)
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
				if ((grabIconCounter/instance.framesBetweenGrabIconSwitch())%2==0)
					SpriteLoader.keyPress1Sprite.drawAlphaAndSize(position.add(instance.grabIconOffset()), 1, 0.4, 0.4);
				else
					SpriteLoader.keyPress2Sprite.drawAlphaAndSize(position.add(instance.grabIconOffset()), 1, 0.4, 0.4);
				break;
			case BEING_GRABBED:
				toDraw=SpriteLoader.stickFigureGrabbed;
				if ((grabIconCounter/instance.framesBetweenGrabIconSwitch())%2==0)
					SpriteLoader.keyPress1Sprite.drawAlphaAndSize(position.add(instance.grabIconOffset()), 1, 0.4, 0.4);
				else
					SpriteLoader.keyPress2Sprite.drawAlphaAndSize(position.add(instance.grabIconOffset()), 1, 0.4, 0.4);
				break;
			default:
				throw new Error("invalid render state: "+state);	
		}
		collisionBox.render();
		hangBoxLeft.render();
		hangBoxRight.render();
		toDraw.drawAlpha(position, facingRight, drawAtFullAlpha?1:0.2);
		
		if (state==PlayerState.SHIELDING) {
			double shieldScale=instance.maxShieldScale()*(shield/(double)instance.maxShield());
			SpriteLoader.shieldSprite.drawAlphaAndSize(position, 0.6, shieldScale, shieldScale);
		}
	}
	
	
}
