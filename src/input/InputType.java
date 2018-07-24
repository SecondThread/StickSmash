package input;

public interface InputType {

	boolean jumpMovementPressed();
	boolean jumpMovementHeld();
	
	boolean upMovementHeld();
	boolean downMovementHeld();
	boolean leftMovementHeld();
	boolean rightMovementHeld();
	
	boolean attack1Pressed();
	boolean attack2Pressed();
	boolean attackRecoverPressed();
	boolean shieldHeld();
	boolean grabPressed();
	
	void onUpate();
	
	
}
