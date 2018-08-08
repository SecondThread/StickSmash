package entities.backgrounds;

import entities.Entity;
import graphics.SpriteLoader;
import math.Vec;

public class UIBackground extends Entity {

	public UIBackground() {
		
	}
	
	public void update() {
	}
	
	public void render() {
		SpriteLoader.UIBackground.draw(Vec.zero, true);
	}
	
	public int getRenderOrder() {
		return -100;
	}
}
