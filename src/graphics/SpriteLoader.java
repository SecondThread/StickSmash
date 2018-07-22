package graphics;

public class SpriteLoader {

	public static Sprite backgroundSprite;
	
	//stick figure sprites
	public static Sprite stickFigureIdle;
	public static Sprite stickFigureRunning1;
	public static Sprite stickFigureRunning2;
	public static Sprite stickFigureAirUp;
	public static Sprite stickFigureAirDown;
	
	
	
	public static void loadSprites() {
		backgroundSprite=new Sprite("StickSmashArt/Art/Background/TestBackground.png");
		
		stickFigureIdle=new Sprite("StickSmashArt/Art/StickFigure/pngs/Idle.png");
		stickFigureRunning1=new Sprite("StickSmashArt/Art/StickFigure/pngs/Running1.png");
		stickFigureRunning2=new Sprite("StickSmashArt/Art/StickFigure/pngs/Running2.png");
		stickFigureAirUp=new Sprite("StickSmashArt/Art/StickFigure/pngs/AirUp.png");
		stickFigureAirDown=new Sprite("StickSmashArt/Art/StickFigure/pngs/AirDown.png");
	}
	
}
