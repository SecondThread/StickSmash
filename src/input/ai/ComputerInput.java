package input.ai;

import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Random;

import entities.Entity;
import entities.Player;
import entities.PlayerState;
import game.Game;
import game.Ledge;
import game.scenes.Scene;
import input.RawInputType;
import math.Rect;
import math.Vec;

public class ComputerInput implements RawInputType {

	private volatile boolean pressJump, pressDown, pressLeft, pressRight;
	private volatile boolean pressAttack1, pressAttack2, pressAttackRecover;
	private volatile boolean pressShield, pressGrab;
	private volatile Player player;
	private volatile Scene scenePlayingOn;
	
	private PlanTypes plan=PlanTypes.RECOVER;
	private Player playerTargetting;
	private Vec targetPosition=Vec.zero;
	private final Rect safeZone=new Rect(new Vec(-700, 0), new Vec(700, 700));
	
	public boolean newJump, newDown, newLeft, newRight;
	public boolean newAttack1, newAttack2, newAttackRecover, newShield, newGrab;
	private Random random=new Random();

	public ComputerInput() {
		scenePlayingOn=Game.getScene();
		Thread processThread=new Thread() {
			public void run() {
				while (Game.getScene()==scenePlayingOn) {
					if (player==null) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}
						continue;
					}
					
					for (int i=0; i<5; i++) {
						asyncExecutePlan();
						try {
							Thread.sleep(150);
						} catch (InterruptedException e) {
						}
					}
					asyncUpdatePlan();
				}
				System.out.println("Scene is over, AI is done processing...");
			}
		};
		processThread.start();
	}

	public void setPlayer(Player player) {
		this.player=player;
		targetPosition=player.isCameraFocusable();
	}

	public void asyncExecutePlan() {
		if (!safeZone.contains(player.isCameraFocusable())&&player.isCameraFocusable().y()<150) {
			plan=PlanTypes.RECOVER;
		}
		newLeft=newRight=newJump=newDown=newAttack1=newAttack2=newAttackRecover=newGrab=newShield=false;
		switch(plan) {
		case DEFEND:
			if (playerTargetting==null) {
				newShield=true;
				break;
			}
			if (player.getState()==PlayerState.BEING_GRABBED) {
				newGrab=!pressGrab;
				break;
			}
			if (playerTargetting.getState()==PlayerState.AIR_ATTACKING||playerTargetting.getState()==PlayerState.ATTACKING) {
				if (player.getVelocity().mag()<player.getMinSpeedToRun()) {
					newShield=true;
					if (player.getGrounded()) {
						switch(random.nextInt(3)) {
						case 0:
							newLeft=true;
							break;
						case 1:
							newDown=true;
							break;
						case 2:
							newRight=true;
							break;
						}
					}
				}
			}
			else {
				if (random.nextInt(10)<3) {
					Vec deltaMovement=player.isCameraFocusable().sub(playerTargetting.isCameraFocusable()).unit().scale(500);
					targetPosition=playerTargetting.isCameraFocusable().add(deltaMovement);
					moveToTargetPosition(true, false);
				}
				else {
					newShield=true;
				}
			}
			break;
		case GRAB:
			if (player.getState()==PlayerState.GRABBING) {
				newGrab=!pressGrab;
				break;
			}
			if (playerTargetting==null) {
				break;
			}
			Vec grabVec=player.getGrabCenter();
			if (!player.getFacingRight()) grabVec=new Vec(-grabVec.x(), grabVec.y());
			
			targetPosition=playerTargetting.isCameraFocusable().sub(grabVec);
			moveToTargetPosition(false, false);
			if (player.isCameraFocusable().dist(targetPosition)<grabVec.mag()+3&&player.getGrounded()) {
				newGrab=true;
			}
			break;
		case RECOVER:
			if (player.getState()==PlayerState.HANGING) {
				newShield=true;
			}
			else {
				pressShield=false;
				Vec playerPosition=player.isCameraFocusable();
				
				if (safeZone.contains(playerPosition)) {
					break;
				}
				
				double maxPlayerY=playerPosition.y();
				if (player.getHasDoubleJump())
					maxPlayerY+=200;
				if (player.hasRecovery())
					maxPlayerY+=300;
				
				if (maxPlayerY<350) {
					//then tryToHang
					ArrayList<Ledge> hangPositions=Game.getScene().getHangPositions();
					Vec closest=null;
					for (Ledge l:hangPositions) {
						if (closest==null||l.getPos().dist(playerPosition)<closest.dist(playerPosition))
							closest=l.getPos();
					}
					targetPosition=closest.add(new Vec((closest.x()>0?1:-1)*player.getHangXOffset(), -player.getHangYOffset()));
				}
				else {
					//go to center
					targetPosition=new Vec(0, 400);
				}
				moveToTargetPosition(true, true);
			}
			break;
		case USE_ATTACK1:
			Vec attack1Center=player.getAttack1().getCenterOfAttack();
			if (!player.getFacingRight()) 
				attack1Center=new Vec(-attack1Center.x(), attack1Center.y());
			targetPosition=playerTargetting.isCameraFocusable().sub(attack1Center);
			moveToTargetPosition(false, false);
			if (random.nextInt(1+(int)(targetPosition.sub(player.isCameraFocusable()).mag()))<70) {
				newAttack1=true;
			}
			break;
		case USE_ATTACK2:
			Vec attack2Center=player.getAttack2().getCenterOfAttack();
			if (!player.getFacingRight()) 
				attack2Center=new Vec(-attack2Center.x(), attack2Center.y());
			targetPosition=playerTargetting.isCameraFocusable().sub(attack2Center);
			moveToTargetPosition(false, false);
			if (random.nextInt(1+(int)(targetPosition.sub(player.isCameraFocusable()).mag()))<70) {
				newAttack2=true;
			}
			break;
		default:
			throw new Error("Unknown plan type!");
		}
		pressLeft=newLeft;
		pressRight=newRight;
		pressJump=newJump;
		pressDown=newDown;
		pressAttack1=newAttack1;
		pressAttack2=newAttack2;
		pressAttackRecover=newAttackRecover;
		pressShield=newShield;
		pressGrab=newGrab;
	}
	
	public void moveToTargetPosition(boolean ySensative, boolean useRecoveryToGetToPosition) {
		Vec currentPos=player.isCameraFocusable();
		if (targetPosition.x()<currentPos.x()) {
			newLeft=true;
		}
		else {
			newRight=true;
		}
		
		Vec currentVel=player.getVelocity();
		if (!ySensative && Math.abs(currentPos.y()-targetPosition.y())<100) {
			return;
		}
		if (currentPos.y()>targetPosition.y()) {
			//too high
			if (currentPos.y()-targetPosition.y()>Math.abs(currentPos.x()-targetPosition.x()) && currentPos.sub(targetPosition).mag()>100) {
				//fastfall if significantly too high
				newDown=true;
			}
		}
		else {
			if (Math.abs(currentPos.y()-targetPosition.y())>5) {
				//too low
				if (currentVel.y()>0) {
					newJump=currentVel.y()>0.9*player.getJumpPower();
					newAttackRecover=false;
				}
				else if (player.getHasDoubleJump()) {
					//use double jump if we have it
					newJump=random.nextBoolean();
					System.out.println("Trying to jump");
				}
				else {
					//no double jump, use recovery
					if (useRecoveryToGetToPosition&&player.hasRecovery()) {
						System.out.println("Trying to use recovery!");
						newAttackRecover=true;
					}
				}
			}
		}
	}

	public void asyncUpdatePlan() {
		playerTargetting=null;
		ArrayList<Entity> entities=Game.getScene().getEntities();
		ArrayList<Player> players=new ArrayList<>();
		for (Entity e:entities) {
			if (!(e instanceof Player)||e==player)
				continue;
			players.add((Player)e);
		}
		if (playerTargetting==null||random.nextInt(4)==0&&players.size()>0) {
			playerTargetting=players.get(random.nextInt(players.size()));
		}
		
		if (safeZone.contains(player.isCameraFocusable())) {
			if (plan==PlanTypes.RECOVER||random.nextInt(3)==0) {
				switch (random.nextInt(4)) {
				case 0:
					plan=PlanTypes.DEFEND;
					break;
				case 1:
					plan=PlanTypes.GRAB;
					break;
				case 2:
					plan=PlanTypes.USE_ATTACK1;
					break;
				case 3:
					plan=PlanTypes.USE_ATTACK2;
					break;
				}
			}
		}
		else {
			plan=PlanTypes.RECOVER;
		}
		if (player.getState()==PlayerState.BEING_GRABBED) {
			plan=PlanTypes.DEFEND;
		}
		System.out.println("Updated plan to be "+plan);
	}

	public boolean jumpMovementDown() {
		return pressJump;
	}

	public boolean upMovementDown() {
		return pressJump;
	}

	public boolean downMovementDown() {
		return pressDown;
	}

	public boolean leftMovementDown() {
		return pressLeft;
	}

	public boolean rightMovementDown() {
		return pressRight;
	}

	public boolean attack1Down() {
		return pressAttack1;
	}

	public boolean attack2Down() {
		return pressAttack2;
	}

	public boolean attackRecoverDown() {
		return pressAttackRecover;
	}

	public boolean shieldDown() {
		return pressShield;
	}

	public boolean grabDown() {
		return pressGrab;
	}

}
