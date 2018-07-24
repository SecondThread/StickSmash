package graphics;

public class SpriteLoader {

	public static Sprite backgroundSprite;
	
	//stick figure sprites
	public static Sprite stickFigureIdle;
	public static Sprite stickFigureRunning1;
	public static Sprite stickFigureRunning2;
	public static Sprite stickFigureAirUp;
	public static Sprite stickFigureAirDown;
	public static Sprite stickFigureRolling1;
	public static Sprite stickFigureRolling2;
	public static Sprite stickFigureHang;
	
	//particle sprites
	public static Sprite doubleJumpParticleSprite;
	public static Sprite stunParticleSprite;
	public static Sprite runTurnParticleSprite;
	
	//UI sprites
	public static Sprite shieldSprite;
	public static Sprite collisionSquare;
	
	public static void loadSprites() {
		backgroundSprite=new Sprite("StickSmashArt/Art/Background/TestBackground.png");
		
		stickFigureIdle=new Sprite("StickSmashArt/Art/StickFigure/pngs/Idle.png");
		stickFigureRunning1=new Sprite("StickSmashArt/Art/StickFigure/pngs/Running1.png");
		stickFigureRunning2=new Sprite("StickSmashArt/Art/StickFigure/pngs/Running2.png");
		stickFigureAirUp=new Sprite("StickSmashArt/Art/StickFigure/pngs/AirUp.png");
		stickFigureAirDown=new Sprite("StickSmashArt/Art/StickFigure/pngs/AirDown.png");
		stickFigureRolling1=new Sprite("StickSmashArt/Art/StickFigure/pngs/Roll1.png");
		stickFigureRolling2=new Sprite("StickSmashArt/Art/StickFigure/pngs/Roll2.png");
		stickFigureHang=new Sprite("StickSmashArt/Art/StickFigure/pngs/Hang.png");
		
		doubleJumpParticleSprite=new Sprite("StickSmashArt/Art/Particles/DoubleJumpParticle.png");
		stunParticleSprite=new Sprite("StickSmashArt/Art/Particles/StunParticle.png");
		runTurnParticleSprite=new Sprite("StickSmashArt/Art/Particles/RunTurnParticle.png");
		
		shieldSprite=new Sprite("StickSmashArt/Art/UI/Shield.png");
		collisionSquare=new Sprite("StickSmashArt/Art/UI/Collision100x100Square.png");
		
	}
	
}
