package input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import game.Game;

public class Keyboard implements KeyListener, FocusListener, RawInputType {
	
	static int MAX_KEY_VALUE=1000;
	
	static Keyboard instanceP1, instanceP2;
	boolean[] down=new boolean[MAX_KEY_VALUE];
	
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
			upCode=87;//KeyCode.W.getCode();
			leftCode=65;//KeyCode.A.getCode();
			downCode=83;//KeyCode.S.getCode();
			rightCode=68;//KeyCode.D.getCode();
			attack1Code=74;//KeyCode.J.getCode();
			attack2Code=75;//KeyCode.K.getCode();
			attackRecoverCode=76;//KeyCode.L.getCode();
			shieldCode=17;//KeyCode.CONTROL.getCode();
			grabCode=59;//KeyCode.SEMICOLON.getCode();
		}
		else {
			/*upCode=KeyCode.UP.getCode();
			leftCode=KeyCode.LEFT.getCode();
			downCode=KeyCode.DOWN.getCode();
			rightCode=KeyCode.RIGHT.getCode();
			attack1Code=KeyCode.Z.getCode();
			attack2Code=KeyCode.X.getCode();
			attackRecoverCode=KeyCode.C.getCode();
			shieldCode=KeyCode.NUMPAD0.getCode();
			grabCode=KeyCode.V.getCode();*/
		}
	}

	public void keyPressed(KeyEvent e) {
		down[e.getKeyCode()]=true;
	}

	public void keyReleased(KeyEvent e) {
		down[e.getKeyCode()]=false;
	}

	public void keyTyped(KeyEvent e) {
	}

	public boolean jumpMovementDown() {
		return down[upCode];
	}

	public boolean upMovementDown() {
		return down[upCode];
	}

	public boolean downMovementDown() {
		return down[downCode];
	}

	public boolean leftMovementDown() {
		return down[leftCode];
	}

	public boolean rightMovementDown() {
		return down[rightCode];
	}

	public boolean attack1Down() {
		return down[attack1Code];
	}

	public boolean attack2Down() {
		return down[attack2Code];
	}

	public boolean attackRecoverDown() {
		return down[attackRecoverCode];
	}

	public boolean shieldDown() {
		return down[shieldCode];
	}

	public boolean grabDown() {
		return down[grabCode];
	}

	public void focusGained(FocusEvent arg0) {
		System.out.println("Event called");
		for (int i=0; i<MAX_KEY_VALUE; i++)
			down[i]=false;
	}

	public void focusLost(FocusEvent arg0) {
		System.out.println("Event called");
		for (int i=0; i<MAX_KEY_VALUE; i++)
			down[i]=false;
	}

}
