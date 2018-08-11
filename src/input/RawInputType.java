package input;

public interface RawInputType {

	boolean jumpMovementDown();
	
	boolean upMovementDown();
	boolean downMovementDown();
	boolean leftMovementDown();
	boolean rightMovementDown();
	
	boolean attack1Down();
	boolean attack2Down();
	boolean attackRecoverDown();
	boolean shieldDown();
	boolean grabDown();
	
}
