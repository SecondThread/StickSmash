package input;

public interface InputType {

	boolean jumpMovementPressed();
	boolean jumpMovementHeld();
	
	boolean upMovementPressed();
	boolean upMovementHeld();
	boolean downMovementPressed();
	boolean downMovementHeld();
	boolean leftMovementPressed();
	boolean leftMovementHeld();
	boolean rightMovementPressed();
	boolean rightMovementHeld();
	
	boolean attack1Pressed();
	boolean attack2Pressed();
	boolean attackRecoverPressed();
	boolean shieldHeld();
	boolean grabPressed();
	
	void onUpate();
	
	
}
