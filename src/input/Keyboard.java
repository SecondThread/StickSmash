package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import game.Game;
import javafx.scene.input.KeyCode;

public class Keyboard implements KeyListener, InputType {
	
	static int MAX_KEY_VALUE=500;
	
	static Keyboard instance;
	static boolean[] pressed=new boolean[MAX_KEY_VALUE];
	static boolean[] firstDown=new boolean[MAX_KEY_VALUE];
	static boolean[] held=new boolean[MAX_KEY_VALUE];
	static boolean[] released=new boolean[MAX_KEY_VALUE];

	public static Keyboard getInstance() {
		return instance==null?instance=new Keyboard():instance;
	}
	
	private Keyboard() {
		Game.addKeyboard(this);
		System.out.println("Keyboard constructed");
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
		return firstDown[KeyCode.SPACE.getCode()]||firstDown[KeyCode.W.getCode()];
	}
	
	public boolean jumpMovementHeld() {
		return held[KeyCode.SPACE.getCode()]||held[KeyCode.W.getCode()];
	}

	public boolean upMovementHeld() {
		return held[KeyCode.W.getCode()];
	}

	public boolean downMovementHeld() {
		return held[KeyCode.S.getCode()];
	}

	public boolean leftMovementHeld() {
		return held[KeyCode.A.getCode()];
	}

	public boolean rightMovementHeld() {
		return held[KeyCode.D.getCode()];
	}

	public boolean attack1Pressed() {
		return held[KeyCode.J.getCode()];
	}

	public boolean attack2Pressed() {
		return held[KeyCode.K.getCode()];
	}

	public boolean attackRecoverPressed() {
		return held[KeyCode.L.getCode()];
	}

	public boolean shieldHeld() {
		return held[KeyCode.CONTROL.getCode()];
	}

	public boolean grabPressed() {
		return held[KeyCode.QUOTE.getCode()];
	}

}
