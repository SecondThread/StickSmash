package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import game.Game;
import javafx.scene.input.KeyCode;

public class Keyboard implements KeyListener, InputType {
	
	static int MAX_KEY_VALUE=1000;
	
	static Keyboard instanceP1, instanceP2;
	boolean[] pressed=new boolean[MAX_KEY_VALUE];
	boolean[] firstDown=new boolean[MAX_KEY_VALUE];
	boolean[] held=new boolean[MAX_KEY_VALUE];
	boolean[] released=new boolean[MAX_KEY_VALUE];
	
	int upCode;
	int downCode;
	int leftCode;
	int rightCode;
	int attack1Code;
	int attack2Code;
	int attackRecoverCode;
	int shieldCode;
	int grabCode;

	public static Keyboard getInstance(boolean player1Controls) {
		if (player1Controls)
			return instanceP1==null?instanceP1=new Keyboard(player1Controls):instanceP1;
		else
			return instanceP2==null?instanceP2=new Keyboard(player1Controls):instanceP2;
	}
	
	private Keyboard(boolean player1Controls) {
		Game.addKeyboard(this);
		System.out.println("Keyboard constructed");
		if (player1Controls) {
			upCode=KeyCode.W.getCode();
			leftCode=KeyCode.A.getCode();
			downCode=KeyCode.S.getCode();
			rightCode=KeyCode.D.getCode();
			attack1Code=KeyCode.J.getCode();
			attack2Code=KeyCode.K.getCode();
			attackRecoverCode=KeyCode.L.getCode();
			shieldCode=KeyCode.CONTROL.getCode();
			grabCode=KeyCode.SEMICOLON.getCode();
		}
		else {
			upCode=KeyCode.UP.getCode();
			leftCode=KeyCode.LEFT.getCode();
			downCode=KeyCode.DOWN.getCode();
			rightCode=KeyCode.RIGHT.getCode();
			attack1Code=KeyCode.Z.getCode();
			attack2Code=KeyCode.X.getCode();
			attackRecoverCode=KeyCode.C.getCode();
			shieldCode=KeyCode.NUMPAD0.getCode();
			grabCode=KeyCode.V.getCode();
		}
	}
	
	public void onUpate() {
		for (int key=0; key<MAX_KEY_VALUE; key++) {
			firstDown[key]=false;
			if (pressed[key]) {
				pressed[key]=false;
				firstDown[key]=true;
				held[key]=true;
			}
			if (released[key]) {
				released[key]=false;
				held[key]=false;
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		if (held[e.getKeyCode()]) 
			return;
		pressed[e.getKeyCode()]=true;
	}

	public void keyReleased(KeyEvent e) {
		released[e.getKeyCode()]=true;
	}

	public void keyTyped(KeyEvent e) {
	}

	public boolean jumpMovementPressed() {
		return firstDown[upCode];
	}
	
	public boolean jumpMovementHeld() {
		return held[upCode];
	}

	public boolean upMovementHeld() {
		return held[upCode];
	}

	public boolean downMovementHeld() {
		return held[downCode];
	}

	public boolean leftMovementHeld() {
		return held[leftCode];
	}

	public boolean rightMovementHeld() {
		return held[rightCode];
	}

	public boolean attack1Pressed() {
		return held[attack1Code];
	}

	public boolean attack2Pressed() {
		return held[attack2Code];
	}

	public boolean attackRecoverPressed() {
		return held[attackRecoverCode];
	}

	public boolean shieldHeld() {
		return held[shieldCode];
	}


	public boolean grabPressed() {
		return firstDown[grabCode];
	}

}
