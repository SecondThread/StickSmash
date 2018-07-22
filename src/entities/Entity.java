package entities;

import game.Game;

public abstract class Entity {

	public Entity() {
		Game.addEntity(this);
	}
	
	public int getRenderOrder() {
		return 0;
	}
	
	public void update() {
		
	}
	
	public void render() {
		
	}
	
	public final void destroy() {
		Game.destroyEntity(this);
	}
}
