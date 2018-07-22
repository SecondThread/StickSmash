package entities.backgrounds;

import entities.Entity;
import game.Game;
import graphics.SpriteLoader;
import math.Rect;
import math.Vec;

public class MainBackground extends Entity {

	private static Rect colisionBox=new Rect(new Vec(-1000, -100), new Vec(1000, 100));
	
	public MainBackground() {
		Game.addCollitionBox(colisionBox);
	}
	
	public int getRenderOrder() {
		return -100;
	}
	
	public void render() {
		SpriteLoader.backgroundSprite.draw(Vec.zero, true);
	}
	
}
