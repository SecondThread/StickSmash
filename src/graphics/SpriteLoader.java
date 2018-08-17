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
	
	//Besius sprites
	public static Sprite besiusIdle;
	public static Sprite besiusRunning1;
	public static Sprite besiusRunning2;
	public static Sprite besiusAirUp;
	public static Sprite besiusAirDown;
	public static Sprite besiusSliding;
	public static Sprite besiusHang;
	public static Sprite besiusAirHit;
	public static Sprite besiusKnockedDown;
	
	public static Sprite besiusFlipKick1;
	public static Sprite besiusFlipKick2;
	public static Sprite besiusKnee;
	public static Sprite besiusFury1;
	public static Sprite besiusFury2;
	public static Sprite besiusFury3;
	public static Sprite besiusGrab;
	public static Sprite besiusGrabbed;
	public static Sprite besiusGrabRelease;
	public static Sprite besiusRecover1;
	public static Sprite besiusRecover2;
	public static Sprite besiusUppercut1;
	public static Sprite besiusUppercut2;
	
	//Smash sprites
	public static Sprite smashIdle;
	public static Sprite smashRunning1;
	public static Sprite smashRunning2;
	public static Sprite smashAirUp;
	public static Sprite smashAirDown;
	public static Sprite smashSliding;
	public static Sprite smashHang;
	public static Sprite smashAirHit;
	public static Sprite smashKnockedDown;
	
	public static Sprite smashStomp1;
	public static Sprite smashStomp2;
	public static Sprite smashAirSwing1;
	public static Sprite smashAirSwing2;
	public static Sprite smashDownSwing1;
	public static Sprite smashDownSwing2;
	public static Sprite smashGrab;
	public static Sprite smashGrabbed;
	public static Sprite smashGrabRelease;
	public static Sprite smashRecover1;
	public static Sprite smashRecover2;
	public static Sprite smashRecover3;
	public static Sprite smashKick1;
	public static Sprite smashKick2;
	
	//Carlos sprites
	public static Sprite carlosIdle;
	public static Sprite carlosRunning1;
	public static Sprite carlosRunning2;
	public static Sprite carlosRunning3;
	public static Sprite carlosAirUp;
	public static Sprite carlosAirDown;
	public static Sprite carlosRoll1;
	public static Sprite carlosRoll2;
	public static Sprite carlosHang;
	public static Sprite carlosAirHit;
	public static Sprite carlosKnockedDown;
	
	public static Sprite carlosAirSlice1;
	public static Sprite carlosAirSlice2;
	public static Sprite carlosBouncingFish1;
	public static Sprite carlosBouncingFish2;
	public static Sprite carlosBouncingFish3;
	public static Sprite carlosGrab;
	public static Sprite carlosGrabbed;
	public static Sprite carlosGrabRelease;
	public static Sprite carlosRecover1;
	public static Sprite carlosRecover2;
	public static Sprite carlosShooting1;
	public static Sprite carlosShooting2;
	public static Sprite carlosSwordAttack1;
	public static Sprite carlosSwordAttack2;
	public static Sprite carlosSwordAttack3;
	public static Sprite carlosBullet;
	
	
	//particle sprites
	public static Sprite doubleJumpParticleSprite;
	public static Sprite stunParticleSprite;
	public static Sprite runTurnParticleSprite;
	public static Sprite[] airSmokeSprites;
	public static Sprite explosionRoundedSprite;
	public static Sprite explosionRoundedVerticalSprite;
	public static Sprite keyPress1Sprite;
	public static Sprite keyPress2Sprite;
	public static Sprite keyPressedIndicatorSprite;
	
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
	public static Sprite besiusIconSprite;
	public static Sprite smashIconSprite;
	public static Sprite carlosIconSprite;
	public static Sprite emptySelectionIcon;
	public static Sprite readyIcon;
	public static Sprite pressJToStart;
	public static Sprite cueballText;
	public static Sprite besiusText;
	public static Sprite smashText;
	public static Sprite carlosText;
	
	public static Sprite redCharacterBackground;
	public static Sprite blueCharacterBackground;
	public static Sprite greenCharacterBackground;
	public static Sprite yellowCharacterBackground;
	public static Sprite redSelector;
	public static Sprite blueSelector;
	public static Sprite greenSelector;
	public static Sprite yellowSelector;
	public static Sprite greySelector;
	public static Sprite redNameBackground;
	public static Sprite blueNameBackground;
	public static Sprite greenNameBackground;
	public static Sprite yellowNameBackground;
	public static Sprite greyNameBackground;
	
	public static Sprite redDot;
	public static Sprite blueDot;
	public static Sprite greenDot;
	public static Sprite yellowDot;
	public static Sprite greyDot;
	public static Sprite blackDot;
	
	public static Sprite red0;
	public static Sprite red1;
	public static Sprite red2;
	public static Sprite red3;
	public static Sprite red4;
	public static Sprite red5;
	public static Sprite red6;
	public static Sprite red7;
	public static Sprite red8;
	public static Sprite red9;
	public static Sprite redPercent;
	public static Sprite gameOverText;
	
	//tutorial
	public static Sprite tutorialBackgroundSprite;
	
	public static void loadSprites() {
		backgroundSprite=new Sprite("StickSmashArt/Art/Background/TestBackground.png");
		
		//stick figure
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
		
		//besius
		besiusIdle=new Sprite("StickSmashArt/Art/Besius/Idle.png");
		besiusRunning1=new Sprite("StickSmashArt/Art/Besius/Running1.png");
		besiusRunning2=new Sprite("StickSmashArt/Art/Besius/Running2.png");
		besiusAirUp=new Sprite("StickSmashArt/Art/Besius/AirUp.png");
		besiusAirDown=new Sprite("StickSmashArt/Art/Besius/AirDown.png");
		besiusSliding=new Sprite("StickSmashArt/Art/Besius/Sliding.png");
		besiusHang=new Sprite("StickSmashArt/Art/Besius/hanging.png");
		besiusAirHit=new Sprite("StickSmashArt/Art/Besius/AirHit.png");
		besiusKnockedDown=new Sprite("StickSmashArt/Art/Besius/KnockedDown.png");
		
		besiusFlipKick1=new Sprite("StickSmashArt/Art/Besius/Attacks/FlipKick1.png");
		besiusFlipKick2=new Sprite("StickSmashArt/Art/Besius/Attacks/FlipKick2.png");
		besiusKnee=new Sprite("StickSmashArt/Art/Besius/Attacks/Knee.png");
		besiusFury1=new Sprite("StickSmashArt/Art/Besius/Attacks/Fury1.png");
		besiusFury2=new Sprite("StickSmashArt/Art/Besius/Attacks/Fury2.png");
		besiusFury3=new Sprite("StickSmashArt/Art/Besius/Attacks/Fury3.png");
		besiusGrab=new Sprite("StickSmashArt/Art/Besius/Attacks/Grab.png");
		besiusGrabbed=new Sprite("StickSmashArt/Art/Besius/Attacks/Grabbed.png");
		besiusGrabRelease=new Sprite("StickSmashArt/Art/Besius/Attacks/GrabAttack.png");
		besiusRecover1=new Sprite("StickSmashArt/Art/Besius/Attacks/AttackRecover1.png");
		besiusRecover2=new Sprite("StickSmashArt/Art/Besius/Attacks/AttackRecover2.png");
		besiusUppercut1=new Sprite("StickSmashArt/Art/Besius/Attacks/Uppercut1.png");
		besiusUppercut2=new Sprite("StickSmashArt/Art/Besius/Attacks/Uppercut2.png");
		
		//smash
		smashIdle=new Sprite("StickSmashArt/Art/Smash/Idle.png");
		smashRunning1=new Sprite("StickSmashArt/Art/Smash/running1.png");
		smashRunning2=new Sprite("StickSmashArt/Art/Smash/running2.png");
		smashAirUp=new Sprite("StickSmashArt/Art/Smash/AirUp.png");
		smashAirDown=new Sprite("StickSmashArt/Art/Smash/AirDown.png");
		smashSliding=new Sprite("StickSmashArt/Art/Smash/Slide.png");
		smashHang=new Sprite("StickSmashArt/Art/Smash/Hanging.png");
		smashAirHit=new Sprite("StickSmashArt/Art/Smash/AirHit.png");
		smashKnockedDown=new Sprite("StickSmashArt/Art/Smash/KnockedDown.png");
		
		smashStomp1=new Sprite("StickSmashArt/Art/Smash/Attacks/Stomp1.png");
		smashStomp2=new Sprite("StickSmashArt/Art/Smash/Attacks/Stomp2.png");
		smashAirSwing1=new Sprite("StickSmashArt/Art/Smash/Attacks/AirSwing1.png");
		smashAirSwing2=new Sprite("StickSmashArt/Art/Smash/Attacks/AirSwing2.png");
		smashDownSwing1=new Sprite("StickSmashArt/Art/Smash/Attacks/DownSwing1.png");
		smashDownSwing2=new Sprite("StickSmashArt/Art/Smash/Attacks/DownSwing2.png");
		smashGrab=new Sprite("StickSmashArt/Art/Smash/Attacks/Grabbing.png");
		smashGrabbed=new Sprite("StickSmashArt/Art/Smash/Attacks/BeingGrabbed.png");
		smashGrabRelease=new Sprite("StickSmashArt/Art/Smash/Attacks/GrabAttack.png");
		smashRecover1=new Sprite("StickSmashArt/Art/Smash/Attacks/AttackRecover1.png");
		smashRecover2=new Sprite("StickSmashArt/Art/Smash/Attacks/AirRecover2.png");
		smashRecover3=new Sprite("StickSmashArt/Art/Smash/Attacks/RecoverAttack3.png");
		smashKick1=new Sprite("StickSmashArt/Art/Smash/Attacks/Kick1.png");
		smashKick2=new Sprite("StickSmashArt/Art/Smash/Attacks/Kick2.png");
		
		//Carlos
		carlosIdle=new Sprite("StickSmashArt/Art/Carlos/IdleReal.png");
		carlosRunning1=new Sprite("StickSmashArt/Art/Carlos/Running1.png");
		carlosRunning2=new Sprite("StickSmashArt/Art/Carlos/Running2.png");
		carlosRunning3=new Sprite("StickSmashArt/Art/Carlos/Running3.png");
		carlosAirUp=new Sprite("StickSmashArt/Art/Carlos/AirUp.png");
		carlosAirDown=new Sprite("StickSmashArt/Art/Carlos/AirDown.png");
		carlosRoll1=new Sprite("StickSmashArt/Art/Carlos/Roll1.png");
		carlosRoll2=new Sprite("StickSmashArt/Art/Carlos/Roll2.png");
		carlosHang=new Sprite("StickSmashArt/Art/Carlos/Hanging.png");
		carlosAirHit=new Sprite("StickSmashArt/Art/Carlos/AirHit.png");
		carlosKnockedDown=new Sprite("StickSmashArt/Art/Carlos/KnockedDown.png");
		
		carlosAirSlice1=new Sprite("StickSmashArt/Art/Carlos/Attacks/AirSlice1.png");
		carlosAirSlice2=new Sprite("StickSmashArt/Art/Carlos/Attacks/AirSlice2.png");
		carlosBouncingFish1=new Sprite("StickSmashArt/Art/Carlos/Attacks/BouncingFish1.png");
		carlosBouncingFish2=new Sprite("StickSmashArt/Art/Carlos/Attacks/BouncingFish2.png");
		carlosBouncingFish3=new Sprite("StickSmashArt/Art/Carlos/Attacks/BouncingFish3.png");
		carlosGrab=new Sprite("StickSmashArt/Art/Carlos/Attacks/Grab.png");
		carlosGrabbed=new Sprite("StickSmashArt/Art/Carlos/Attacks/Grabbed.png");
		carlosGrabRelease=new Sprite("StickSmashArt/Art/Carlos/Attacks/GrabAttack.png");
		carlosRecover1=new Sprite("StickSmashArt/Art/Carlos/Attacks/Recover1.png");
		carlosRecover2=new Sprite("StickSmashArt/Art/Carlos/Attacks/Recover2.png");
		carlosShooting1=new Sprite("StickSmashArt/Art/Carlos/Attacks/Shooting1.png");
		carlosShooting2=new Sprite("StickSmashArt/Art/Carlos/Attacks/Shooting2.png");
		carlosSwordAttack1=new Sprite("StickSmashArt/Art/Carlos/Attacks/SwordAttack1.png");
		carlosSwordAttack2=new Sprite("StickSmashArt/Art/Carlos/Attacks/SwordAttack2.png");
		carlosSwordAttack3=new Sprite("StickSmashArt/Art/Carlos/Attacks/SwordAttack3.png");
		carlosBullet=new Sprite("StickSmashArt/Art/Carlos/Attacks/Bullet.png");
		
		/*public static Sprite carlosIdle;
		public static Sprite carlosRunning1;
		public static Sprite carlosRunning2;
		public static Sprite carlosAirUp;
		public static Sprite carlosAirDown;
		public static Sprite carlosRoll1;
		public static Sprite carlosRoll2;
		public static Sprite carlosHang;
		public static Sprite carlosAirHit;
		public static Sprite carlosKnockedDown;
		
		public static Sprite carlosAirSlick1;
		public static Sprite carlosAirSlick2;
		public static Sprite carlosBouncingFish1;
		public static Sprite carlosBouncingFish2;
		public static Sprite carlosBouncingFish3;
		public static Sprite carlosGrab;
		public static Sprite carlosGrabbed;
		public static Sprite carlosGrabRelease;
		public static Sprite carlosRecover1;
		public static Sprite carlosRecover2;
		public static Sprite carlosShooting1;
		public static Sprite carlosShooting2;
		public static Sprite carlosSwordAttack1;
		public static Sprite carlosSwordAttack2;
		public static Sprite carlosSwordAttack3;
		public static Sprite carlosBullet;*/
		
		
		
		
		doubleJumpParticleSprite=new Sprite("StickSmashArt/Art/Particles/DoubleJumpParticle.png");
		stunParticleSprite=new Sprite("StickSmashArt/Art/Particles/StunParticle.png");
		runTurnParticleSprite=new Sprite("StickSmashArt/Art/Particles/RunTurnParticle.png");
		airSmokeSprites=new Sprite[4];
		airSmokeSprites[0]=new Sprite("StickSmashArt/Art/Particles/AirSmoke1.png");
		airSmokeSprites[1]=new Sprite("StickSmashArt/Art/Particles/AirSmoke2.png");
		airSmokeSprites[2]=new Sprite("StickSmashArt/Art/Particles/AirSmoke3.png");
		airSmokeSprites[3]=new Sprite("StickSmashArt/Art/Particles/AirSmoke4.png");
		explosionRoundedSprite=new Sprite("StickSmashArt/Art/Particles/ExplosionRounded.png");
		explosionRoundedVerticalSprite=new Sprite("StickSmashArt/Art/Particles/ExplosionRoundedVertical.png");
		keyPress1Sprite=new Sprite("StickSmashArt/Art/Particles/Button1Icon.png");
		keyPress2Sprite=new Sprite("StickSmashArt/Art/Particles/Button2Icon.png");
		keyPressedIndicatorSprite=new Sprite("StickSmashArt/Art/Particles/KeyPressedParticle.png");
		
		shieldSprite=new Sprite("StickSmashArt/Art/UI/Shield.png");
		collisionSquare=new Sprite("StickSmashArt/Art/UI/Collision100x100Square.png");
		UIBackground=new Sprite("StickSmashArt/Art/UI/UIBackground.png");
		playButton=new Sprite("StickSmashArt/Art/UI/MenuScreen/PlayButton.png");
		teamsButton=new Sprite("StickSmashArt/Art/UI/MenuScreen/TeamsButton.png");
		joinGameButton=new Sprite("StickSmashArt/Art/UI/MenuScreen/JoinGameButton.png");
		tutorialButton=new Sprite("StickSmashArt/Art/UI/MenuScreen/TutorialButton.png");
		selectedButtonIndicatorSprite=new Sprite("StickSmashArt/Art/UI/MenuScreen/SelectedButtonIndicator.png");
		stickFigureIconSprite=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/StickFigureIcon.png");
		besiusIconSprite=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/BesiusSelectionIcon.png");
		smashIconSprite=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/SmashSelectionIcon.png");
		carlosIconSprite=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/CarlosSelectionIcon.png");
		emptySelectionIcon=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/EmptySelectionIcon.png");
		readyIcon=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/ReadyIcon.png");
		pressJToStart=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/PressJToStart.png");
		cueballText=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/CueballText.png");
		besiusText=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/BesiusText.png");
		smashText=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/SmashText.png");
		carlosText=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/CarlosText.png");
		
		redCharacterBackground=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/RedCharacterBackground.png");
		blueCharacterBackground=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/BlueCharacterBackground.png");
		greenCharacterBackground=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/GreenCharacterBackground.png");
		yellowCharacterBackground=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/YellowCharacterBackground.png");
		redSelector=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/RedSelector.png");
		blueSelector=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/BlueSelector.png");
		greenSelector=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/GreenSelector.png");
		yellowSelector=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/YellowSelector.png");
		greySelector=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/GreySelector.png");
		redNameBackground=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/RedNameBackground.png");
		blueNameBackground=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/BlueNameBackground.png");
		greenNameBackground=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/GreenNameBackground.png");
		yellowNameBackground=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/YellowNameBackground.png");
		greyNameBackground=new Sprite("StickSmashArt/Art/UI/ChooseCharacterScreen/GreyNameBackground.png");
		
		redDot=new Sprite("StickSmashArt/Art/UI/GameHUD/RedDot.png");
		blueDot=new Sprite("StickSmashArt/Art/UI/GameHUD/BlueDot.png");
		greenDot=new Sprite("StickSmashArt/Art/UI/GameHUD/GreenDot.png");
		yellowDot=new Sprite("StickSmashArt/Art/UI/GameHUD/YellowDot.png");
		greyDot=new Sprite("StickSmashArt/Art/UI/GameHUD/GreyDot.png");
		blackDot=new Sprite("StickSmashArt/Art/UI/GameHUD/BlackDot.png");
		red0=new Sprite("StickSmashArt/Art/UI/GameHUD/text/red/0.png");
		red1=new Sprite("StickSmashArt/Art/UI/GameHUD/text/red/1.png");
		red2=new Sprite("StickSmashArt/Art/UI/GameHUD/text/red/2.png");
		red3=new Sprite("StickSmashArt/Art/UI/GameHUD/text/red/3.png");
		red4=new Sprite("StickSmashArt/Art/UI/GameHUD/text/red/4.png");
		red5=new Sprite("StickSmashArt/Art/UI/GameHUD/text/red/5.png");
		red6=new Sprite("StickSmashArt/Art/UI/GameHUD/text/red/6.png");
		red7=new Sprite("StickSmashArt/Art/UI/GameHUD/text/red/7.png");
		red8=new Sprite("StickSmashArt/Art/UI/GameHUD/text/red/8.png");
		red9=new Sprite("StickSmashArt/Art/UI/GameHUD/text/red/9.png");
		redPercent=new Sprite("StickSmashArt/Art/UI/GameHUD/text/red/Percent.png");
		gameOverText=new Sprite("StickSmashArt/Art/UI/GameHUD/text/GameText.png");
		
		tutorialBackgroundSprite=new Sprite("StickSmashArt/Art/Tutorial/TutorialBackground.png");
	}
	
}
