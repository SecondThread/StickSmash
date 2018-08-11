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
	public static Sprite stickFigureAirHit;
	public static Sprite stickFigureKnockedDown;
	
	public static Sprite stickFigureAirSlice1;
	public static Sprite stickFigureAirSlice2;
	public static Sprite stickFigureAirSpike;
	public static Sprite stickFigureDab1;
	public static Sprite stickFigureDab2;
	public static Sprite stickFigureGrab;
	public static Sprite stickFigureGrabbed;
	public static Sprite stickFigureGrabRelease;
	public static Sprite stickFigureJetpack1;
	public static Sprite stickFigureJetpack2;
	public static Sprite stickFigureKick1;
	public static Sprite stickFigureKick2;
	public static Sprite stickFigureKick3;
	
	
	//particle sprites
	public static Sprite doubleJumpParticleSprite;
	public static Sprite stunParticleSprite;
	public static Sprite runTurnParticleSprite;
	
	//UI sprites
	public static Sprite shieldSprite;
	public static Sprite collisionSquare;
	public static Sprite UIBackground;
	public static Sprite playButton;
	public static Sprite joinGameButton;
	public static Sprite tutorialButton;
	public static Sprite teamsButton;
	public static Sprite selectedButtonIndicatorSprite;
	public static Sprite stickFigureIconSprite;
	
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
		stickFigureAirHit=new Sprite("StickSmashArt/Art/StickFigure/pngs/AirHit.png");
		stickFigureKnockedDown=new Sprite("StickSmashArt/Art/StickFigure/pngs/KnockedDown.png");
		
		stickFigureAirSlice1=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/AirSlice1.png");
		stickFigureAirSlice2=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/AirSlice2.png");
		stickFigureAirSpike=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/AirSpike.png");
		stickFigureDab1=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/Dab1.png");
		stickFigureDab2=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/Dab2.png");
		stickFigureGrab=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/grab.png");
		stickFigureGrabbed=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/Grabbed.png");
		stickFigureGrabRelease=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/GrabRelease.png");
		stickFigureJetpack1=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/Jetpack1.png");
		stickFigureJetpack2=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/Jetpack2.png");
		stickFigureKick1=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/Kick1.png");
		stickFigureKick2=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/Kick2.png");
		stickFigureKick3=new Sprite("StickSmashArt/Art/StickFigure/attacks/pngs/Kick3.png");
		
		doubleJumpParticleSprite=new Sprite("StickSmashArt/Art/Particles/DoubleJumpParticle.png");
		stunParticleSprite=new Sprite("StickSmashArt/Art/Particles/StunParticle.png");
		runTurnParticleSprite=new Sprite("StickSmashArt/Art/Particles/RunTurnParticle.png");
		
		shieldSprite=new Sprite("StickSmashArt/Art/UI/Shield.png");
		collisionSquare=new Sprite("StickSmashArt/Art/UI/Collision100x100Square.png");
		UIBackground=new Sprite("StickSmashArt/Art/UI/UIBackground.png");
		playButton=new Sprite("StickSmashArt/Art/UI/MenuScreen/PlayButton.png");
		teamsButton=new Sprite("StickSmashArt/Art/UI/MenuScreen/TeamsButton.png");
		joinGameButton=new Sprite("StickSmashArt/Art/UI/MenuScreen/JoinGameButton.png");
		tutorialButton=new Sprite("StickSmashArt/Art/UI/MenuScreen/TutorialButton.png");
		selectedButtonIndicatorSprite=new Sprite("StickSmashArt/Art/UI/MenuScreen/SelectedButtonIndicator.png");
		stickFigureIconSprite=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/StickFigureIcon.png");
		
	}
	
}
