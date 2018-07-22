package input;

public interface InputType {

	boolean jumpMovementPressed();
	
	boolean upMovementHeld();
	boolean downMovementHeld();
	boolean leftMovementHeld();
	boolean rightMovementHeld();
	
	boolean attack1Pressed();
	boolean attack2Pressed();
	boolean attackRecoverPressed();
	boolean shieldPressed();
	boolean grabPressed();
	
	void onUpate();
	
	
}
