package input;

import game.Game;

public class Input {
	
	private RawInputType rawInput;
	
	boolean lastJump, nowJump, jumpFirst;
	boolean lastUp, nowUp, upFirst;
	boolean lastDown, nowDown, downFirst;
	boolean lastLeft, nowLeft, leftFirst;
	boolean lastRight, nowRight, rightFirst;
	boolean lastAttack1, nowAttack1, attack1First;
	boolean lastAttack2, nowAttack2, attack2First;
	boolean lastAttackRecover, nowAttackRecover, attackRecoverFirst;
	boolean lastShield, nowShield, shieldFirst;
	boolean lastGrab, nowGrab, grabFirst;
	
	public Input(RawInputType rawInput) {
		this.rawInput=rawInput;
		Game.addInput(this);
	}
	

	public void onUpdate() {
		nowJump=rawInput.jumpMovementDown();
		jumpFirst=nowJump&&!lastJump;
		lastJump=nowJump;
		
		nowUp=rawInput.upMovementDown();
		upFirst=nowUp&&!lastUp;
		lastUp=nowUp;
		
		nowDown=rawInput.downMovementDown();
		downFirst=nowDown&&!lastDown;
		lastDown=nowDown;

		nowLeft=rawInput.leftMovementDown();
		leftFirst=nowLeft&&!lastLeft;
		lastLeft=nowLeft;
		
		nowRight=rawInput.rightMovementDown();
		rightFirst=nowRight&&!lastRight;
		lastRight=nowRight;
		
		nowAttack1=rawInput.attack1Down();
		attack1First=nowAttack1&&!lastAttack1;
		lastAttack1=nowAttack1;
		
		nowAttack2=rawInput.attack2Down();
		attack2First=nowAttack2&&!lastAttack2;
		lastAttack2=nowAttack2;
		
		nowAttackRecover=rawInput.attackRecoverDown();
		attackRecoverFirst=nowAttackRecover&&!lastAttackRecover;
		lastAttackRecover=nowAttackRecover;
		
		nowShield=rawInput.shieldDown();
		shieldFirst=nowShield&&!lastShield;
		lastShield=nowShield;
		
		nowGrab=rawInput.grabDown();
		grabFirst=nowGrab&&!lastGrab;
		lastGrab=nowGrab;
	}

	public boolean jumpMovementPressed() {
		return jumpFirst;
	}
	public boolean jumpMovementHeld() {
		return nowJump;
	}
	public boolean upMovementPressed() {
		return upFirst;
	}
	public boolean upMovementHeld() {
		return nowUp;
	}
	public boolean downMovementPressed() {
		return downFirst;
	}
	public boolean downMovementHeld() {
		return nowDown;
	}
	public boolean leftMovementPressed() {
		return leftFirst;
	}
	public boolean leftMovementHeld() {
		return nowLeft;
	}
	public boolean rightMovementPressed() {
		return rightFirst;
	}
	public boolean rightMovementHeld() {
		return nowRight;
	}
	
	public boolean attack1Pressed() {
		return attack1First;
	}
	public boolean attack1Held() {
		return nowAttack1;
	}
	public boolean attack2Pressed() {
		return attack2First;
	}
	public boolean attack2Held() {
		return nowAttack2;
	}
	public boolean attackRecoverPressed() {
		return attackRecoverFirst;
	}
	public boolean attackRecoverHeld()  {
		return nowAttackRecover;
	}

	public boolean shieldPressed() {
		return shieldFirst;
	}
	public boolean shieldHeld() {
		return nowShield;
	}
	public boolean grabPressed() {
		return grabFirst;
	}
	public boolean grabHeld()  {
		return nowGrab;
	}
	
	public boolean hasMatchingRawInput(RawInputType toMatch) {
		return rawInput==toMatch;
	}

}
