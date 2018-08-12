package entities.backgrounds;

import entities.Entity;
import game.Game;
import game.Ledge;
import graphics.SpriteLoader;
import math.Rect;
import math.Seg;
import math.Vec;

public class TutorialBackground extends Entity {
	private static Rect bottomCollisionBox=new Rect(new Vec(-3000, -1000), new Vec(830, -620));
	private static Rect leftFloorCollisionBox=new Rect(new Vec(-3000, -1000), new Vec(-500, -440));
	private static Rect lowJumpBox=new Rect(new Vec(-850, -1000), new Vec(-500, -150));
	private static Rect highJumpBox=new Rect(new Vec(-640, -1000), new Vec(-417, 405));
	private static Rect ceiling=new Rect(new Vec(-196, -50), new Vec(58, 10000));
	private static Rect superHighBox=new Rect(new Vec(408, -3000), new Vec(590, 460));
	private static Rect closeAfterHighLand=new Rect(new Vec(570, -3000), new Vec(1395, -300));
	private static Rect farAfterHighLand=new Rect(new Vec(1670, -3000), new Vec(3000, -300));
	private static Rect farWall=new Rect(new Vec(1947, -1000), new Vec(3000, 1000));
	private static Rect ceil=new Rect(new Vec(-3000, 750), new Vec(3000, 800));
	private static Rect left=new Rect(new Vec(-3000, -3000), new Vec(-1730, 800));
	private static Seg platform=new Seg(new Vec(-350, 405), new Vec(0, 405));
	private static Vec firstHangPlatform=new Vec(408, 460);
	private static Vec secondHangPlatform=new Vec(590, 460);
	private static Vec thirdHangPlatform=new Vec(1395, -300);
	private static Vec forthHangPlatform=new Vec(1670, -300);
	
	public TutorialBackground() {
		Game.getScene().addCollitionBox(bottomCollisionBox);
		Game.getScene().addCollitionBox(leftFloorCollisionBox);
		Game.getScene().addCollitionBox(lowJumpBox);
		Game.getScene().addCollitionBox(highJumpBox);
		Game.getScene().addCollitionBox(ceiling);
		Game.getScene().addCollitionBox(superHighBox);
		Game.getScene().addCollitionBox(closeAfterHighLand);
		Game.getScene().addCollitionBox(farAfterHighLand);
		Game.getScene().addCollitionBox(farWall);
		Game.getScene().addCollitionBox(ceil);
		Game.getScene().addCollitionBox(left);
		Game.getScene().addPlatform(platform);
		Game.getScene().addHangPos(new Ledge(firstHangPlatform));
		Game.getScene().addHangPos(new Ledge(secondHangPlatform));
		Game.getScene().addHangPos(new Ledge(thirdHangPlatform));
		Game.getScene().addHangPos(new Ledge(forthHangPlatform));
		
	}
	
	public int getRenderOrder() {
		return -100;
	}
	
	public void render() {
		SpriteLoader.tutorialBackgroundSprite.drawAlphaAndSize(Vec.zero, 1, 1, 1.5);
	}
}
