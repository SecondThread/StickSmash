package input.networking;

import input.RawInputType;

public class RawNetworkInput implements RawInputType {
	
	boolean jump, up, down, left, right, attack1, attack2, attackRecover, shield, grab;
	
	public void setValues(String inputString) {
		jump=inputString.charAt(0)=='1';
		up=inputString.charAt(0)=='1';
		down=inputString.charAt(0)=='1';
		left=inputString.charAt(0)=='1';
		right=inputString.charAt(0)=='1';
		attack1=inputString.charAt(0)=='1';
		attack2=inputString.charAt(0)=='1';
		attackRecover=inputString.charAt(0)=='1';
		shield=inputString.charAt(0)=='1';
		grab=inputString.charAt(0)=='1';
	}

	public boolean jumpMovementDown() {
		return jump;
	}

	public boolean upMovementDown() {
		return up;
	}

	public boolean downMovementDown() {
		return down;
	}

	public boolean leftMovementDown() {
		return left;
	}

	public boolean rightMovementDown() {
		return right;
	}

	public boolean attack1Down() {
		return attack1;
	}

	public boolean attack2Down() {
		return attack2;
	}

	public boolean attackRecoverDown() {
		return attackRecover;
	}

	public boolean shieldDown() {
		return shield;
	}

	public boolean grabDown() {
		return grab;
	}
	
}
