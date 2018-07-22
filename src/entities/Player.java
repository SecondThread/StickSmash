package entities;

import game.Game;
import graphics.SpriteLoader;
import input.InputType;
import math.Rect;
import math.Vec;

public class Player extends Entity {

	private Vec position;
	private Vec velocity=Vec.zero;
	private Rect collisionBox;
	private InputType inputType;

	//Constants
	private static Vec gravity=new Vec(0, -0.2);
	private static double moveGroundSpeed=1, moveAirSpeed=0.3;
	private static double jumpPower=10;
	private static double xGroundedFriction=0.8, xAirFriction=0.95, yFriction=0.98;
	
	//Player states
	private boolean grounded=false;
	private PlayerState state=PlayerState.AIRBORN;
	
	public Player(InputType inputType) {
		collisionBox=new Rect(new Vec(-50, -50), new Vec(50, 50));
		position=new Vec(0, 500);
		this.inputType=inputType;
	}
	
	public void update() {
		applyGravity();
		applyFriction();
		checkForInputAndMovement();
		
		moveToCollision();
		updateGrounded();
	}
	
	private void updateGrounded() {
		grounded=wouldBeInGround(position.add(Vec.down.scale(0.1)));
	}
	
	private void applyFriction() {
		if (grounded) {
			velocity=new Vec(velocity.x()*xGroundedFriction, velocity.y()*yFriction);
		}
		else {
			velocity=new Vec(velocity.x()*xAirFriction, velocity.y()*yFriction);
		}
	}
	
	private void applyGravity() {
		if (!grounded) 
			velocity=velocity.add(gravity);
		else
			velocity=new Vec(velocity.x(), 0);
	}
	
	private void checkForInputAndMovement() {
		switch(state) {
			case AIRBORN:
				break;
			case IDLE:
				break;
			case RUNNING:
				break;
		}
		
		if (inputType.jumpMovementPressed()&&grounded) {
			velocity=velocity.add(Vec.up.scale(jumpPower));
		}
		if (inputType.leftMovementHeld()) {
			if (grounded)
				velocity=velocity.add(Vec.left.scale(moveGroundSpeed));
			else
				velocity=velocity.add(Vec.left.scale(moveAirSpeed));
		}
		if (inputType.rightMovementHeld()) {
			if (grounded)
				velocity=velocity.add(Vec.right.scale(moveGroundSpeed));
			else
				velocity=velocity.add(Vec.right.scale(moveAirSpeed));
		}
	}
	
	
	public boolean wouldBeInGround(Vec newPos) {
		Rect newCollisionBox=collisionBox.offsetBy(newPos);
		for (Rect r:Game.getCollisionBoxes()) {
			if (newCollisionBox.intersects(r))
				return true;
		}
		return false;
	}
	
	public void moveToCollision() {
		Vec toMove=velocity;
		while (toMove.mag()>Vec.EPS) {
			Vec nextStep;
			if (toMove.mag()>0.05) {
				nextStep=toMove.unit().scale(0.05);
			}
			else {
				nextStep=toMove;
			}
			
			if (wouldBeInGround(position.add(nextStep))) {
				toMove=Vec.zero;
			}
			else {
				position=position.add(nextStep);
				toMove=toMove.sub(nextStep);
			}
		}
	}
	
	public void render() {
		SpriteLoader.stickFigureIdle.draw(position, true);
	}
	
}
