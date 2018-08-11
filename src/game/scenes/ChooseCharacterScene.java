package game.scenes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import entities.Entity;
import entities.backgrounds.UIBackground;
import game.Ledge;
import game.ui.Button;
import graphics.Camera;
import graphics.Sprite;
import graphics.SpriteLoader;
import input.Input;
import input.networking.Server;
import math.Rect;
import math.Seg;
import math.Vec;

public class ChooseCharacterScene extends Scene {

	private static final int numPlayers=4;
	
	private Button[] chooseCharacterButtons;
	private int[] playerSelectedButton;
	private Input[] inputs;
	private boolean[] showColorBackground;
	private boolean[] isReady;
	private boolean[] isCPU;
	private Vec[] characterChoicePositions;
	
	private Server server;
	
	public ChooseCharacterScene(Input player1Input) {
		inputs=new Input[numPlayers];
		inputs[0]=player1Input;
	}
	
	public void init() {
		playerSelectedButton=new int[numPlayers];
		Arrays.fill(playerSelectedButton, -1);
		playerSelectedButton[0]=0;

		Camera.getInstance().setPosition(Vec.zero);
		Camera.getInstance().setWorldWidth(1000);
		
		new UIBackground();
		
		chooseCharacterButtons=new Button[6];
		chooseCharacterButtons[0]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(-170, 180));
		chooseCharacterButtons[1]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(0, 180));
		chooseCharacterButtons[2]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(170, 180));
		chooseCharacterButtons[3]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(-170, 60));
		chooseCharacterButtons[4]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(0, 60));
		chooseCharacterButtons[5]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(170, 60));
		
		server=new Server(numPlayers-1);
		showColorBackground=new boolean[numPlayers];
		isReady=new boolean[numPlayers];
		isCPU=new boolean[numPlayers];
		characterChoicePositions=new Vec[numPlayers];
		characterChoicePositions[0]=new Vec(-300, -150);
		characterChoicePositions[1]=new Vec(-100, -150);
		characterChoicePositions[2]=new Vec(100, -150);
		characterChoicePositions[3]=new Vec(300, -150);
	}
	
	public Scene update() {
		ArrayList<Entity> toUpdate=getEntities();
		for (Entity e:toUpdate)
			e.update();
		
		for (int playerIndex=0; playerIndex<numPlayers; playerIndex++) {
			if (inputs[playerIndex]==null) { 
				playerSelectedButton[playerIndex]=-1;
				continue;
			}

			Input in=inputs[playerIndex];
			
			if (!isReady[playerIndex])
				handlePlayerButtonMovement(playerIndex, in);
			
			
			if (in.attack1Pressed()) {
				isReady[playerIndex]=true;
			}
			if (in.attack2Pressed()) {
				if (isReady[playerIndex]) {
					isReady[playerIndex]=false;
				}
				else {
					server.closeServer();
					return new TitleScene(inputs[0]);
				}
			}
			if (in.shieldPressed()) {
				showColorBackground[playerIndex]^=true;
			}
			if (in.attackRecoverPressed()) {
				if (everyoneReady()) {
					//create CPU
				}
				else {
					//ignore it...
				}
			}
		}
		for (int i=0; i<6; i++) {
			boolean selected=false;
			for (int p=0; p<numPlayers; p++)
				selected|=(playerSelectedButton[p]==i&&!isReady[p]);
			chooseCharacterButtons[i].setSelected(selected);
		}
		return this;
	}

	public BufferedImage render() {
		Camera cam=Camera.getInstance();
		cam.preRender();
		renderEntites();

		Sprite[] selectors= {SpriteLoader.redSelector, SpriteLoader.blueSelector, SpriteLoader.greenSelector, SpriteLoader.yellowSelector};
		Sprite[] nameBackgrounds= {SpriteLoader.redNameBackground, SpriteLoader.blueNameBackground, SpriteLoader.greenCharacterBackground, 
				SpriteLoader.yellowNameBackground};
		Sprite[] namesOfCharacters= {SpriteLoader.cueballText, SpriteLoader.cueballText, SpriteLoader.cueballText, SpriteLoader.cueballText, 
				SpriteLoader.cueballText, SpriteLoader.cueballText};
		Sprite[] characterIcons= {SpriteLoader.stickFigureIconSprite, SpriteLoader.emptySelectionIcon, SpriteLoader.emptySelectionIcon,
				SpriteLoader.emptySelectionIcon, SpriteLoader.emptySelectionIcon, SpriteLoader.emptySelectionIcon};
		Sprite[] coloredBackgrounds= {SpriteLoader.redCharacterBackground, SpriteLoader.blueCharacterBackground, SpriteLoader.greenCharacterBackground,
				SpriteLoader.yellowCharacterBackground};
		
		for (int i=0; i<numPlayers; i++) {
			if (playerSelectedButton[i]==-1) 
				continue;

			if (showColorBackground[i]&&!isCPU[i])
				coloredBackgrounds[i].drawAlphaAndSize(characterChoicePositions[i], 1, 0.5, 0.5);
			characterIcons[playerSelectedButton[i]].drawAlphaAndSize(characterChoicePositions[i], 1, 0.3, 0.3);
			
			if (isReady[i])
				SpriteLoader.readyIcon.drawAlphaAndSize(characterChoicePositions[i], 0.8, 0.15, 0.2);
			else {
				if (isCPU[i])
					SpriteLoader.greySelector.drawAlphaAndSize(chooseCharacterButtons[playerSelectedButton[i]].getPos(), 1, 0.36, 0.35);
				else
					selectors[i].drawAlphaAndSize(chooseCharacterButtons[playerSelectedButton[i]].getPos(), 1, 0.36, 0.35);
			}

			Vec nameBackgroundPos=characterChoicePositions[i].add(Vec.down.scale(80));
			if (isCPU[i])
				SpriteLoader.greyNameBackground.drawAlphaAndSize(nameBackgroundPos, 1, 0.4, 0.4);
			else
				nameBackgrounds[i].drawAlphaAndSize(nameBackgroundPos, 1, 0.4, 0.4);
			
			namesOfCharacters[playerSelectedButton[i]].drawAlphaAndSize(nameBackgroundPos.add(Vec.down.scale(10)), 1, 0.3, 0.3);
		}
		
		if (everyoneReady())
			SpriteLoader.pressJToStart.drawAlphaAndSize(Vec.zero, 1, .6, .4);
		BufferedImage result=cam.postRender();
		return result;
	}
	
	private void renderEntites() {
		ArrayList<Entity> toRender=getEntities();
		Collections.sort(toRender, (a, b)->{return Integer.compare(a.getRenderOrder(), b.getRenderOrder());});
		for (Entity e:toRender)
			e.render();
		for (Rect rect:getCollisionBoxes())
			rect.render();
		for (Seg s:getPlatforms())
			s.render();
		for (Ledge v:getHangPositions())
			v.render();
	}
	
	private boolean everyoneReady() {
		for (int i=0; i<numPlayers; i++) {
			if (!(isReady[i]||playerSelectedButton[i]==-1))
				return false;
		}
		return true;
	}
	
	private void handlePlayerButtonMovement(int playerIndex, Input in) {
		if (in.leftMovementPressed()) {
			switch (playerSelectedButton[playerIndex]) {
			case 0:
				break;
			case 1:
				playerSelectedButton[playerIndex]=0;
				break;
			case 2:
				playerSelectedButton[playerIndex]=1;
				break;
			case 3:
				break;
			case 4:
				playerSelectedButton[playerIndex]=3;
				break;
			case 5:
				playerSelectedButton[playerIndex]=4;
				break;
			}
		}
		if (in.rightMovementPressed()) {
			switch (playerSelectedButton[playerIndex]) {
			case 0:
				playerSelectedButton[playerIndex]=1;
				break;
			case 1:
				playerSelectedButton[playerIndex]=2;
				break;
			case 2:
				break;
			case 3:
				playerSelectedButton[playerIndex]=4;
				break;
			case 4:
				playerSelectedButton[playerIndex]=5;
				break;
			case 5:
				break;
			}
		}
		if (in.upMovementPressed()) {
			switch (playerSelectedButton[playerIndex]) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				playerSelectedButton[playerIndex]=0;
				break;
			case 4:
				playerSelectedButton[playerIndex]=1;
				break;
			case 5:
				playerSelectedButton[playerIndex]=2;
				break;
			}
		}
		if (in.downMovementPressed()) {
			switch (playerSelectedButton[playerIndex]) {
			case 0:
				playerSelectedButton[playerIndex]=3;
				break;
			case 1:
				playerSelectedButton[playerIndex]=4;
				break;
			case 2:
				playerSelectedButton[playerIndex]=5;
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			}
		}
	}

}
