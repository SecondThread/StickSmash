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
import input.networking.RawNetworkInput;
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
	private boolean initialized=false;
	private boolean isTeamMode;
	private int[] playerBackgroundColor;
	
	private Server server;
	
	public ChooseCharacterScene(Input player1Input, boolean isTeamMode) {
		inputs=new Input[numPlayers];
		inputs[0]=player1Input;
		this.isTeamMode=isTeamMode;
	}
	
	public void init() {
		Camera.getInstance().setPosition(Vec.zero);
		Camera.getInstance().setWorldWidth(1000);
		if (initialized) {
			for (int i=0; i<numPlayers; i++)
				if (!isCPU[i])
					isReady[i]=false;
			return;
		}
		initialized=true;
		
		playerSelectedButton=new int[numPlayers];
		Arrays.fill(playerSelectedButton, -1);
		playerSelectedButton[0]=0;

		Camera.getInstance().setPosition(Vec.zero);
		Camera.getInstance().setWorldWidth(1000);
		
		new UIBackground();
		
		chooseCharacterButtons=new Button[6];
		chooseCharacterButtons[0]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(-170, 180));
		chooseCharacterButtons[1]=new Button(SpriteLoader.besiusIconSprite, new Vec(0, 180));
		chooseCharacterButtons[2]=new Button(SpriteLoader.smashIconSprite, new Vec(170, 180));
		chooseCharacterButtons[3]=new Button(SpriteLoader.carlosIconSprite, new Vec(-170, 60));
		chooseCharacterButtons[4]=new Button(SpriteLoader.emptySelectionIcon, new Vec(0, 60));
		chooseCharacterButtons[5]=new Button(SpriteLoader.emptySelectionIcon, new Vec(170, 60));
		
		server=new Server(numPlayers-1);
		showColorBackground=new boolean[numPlayers];
		Arrays.fill(showColorBackground, true);
		isReady=new boolean[numPlayers];
		isCPU=new boolean[numPlayers];
		characterChoicePositions=new Vec[numPlayers];
		characterChoicePositions[0]=new Vec(-300, -150);
		characterChoicePositions[1]=new Vec(-100, -150);
		characterChoicePositions[2]=new Vec(100, -150);
		characterChoicePositions[3]=new Vec(300, -150);
		playerBackgroundColor=new int[numPlayers];
		for (int i=0; i<numPlayers; i++)
			playerBackgroundColor[i]=i;
	}
	
	public Scene update() {
		ArrayList<Entity> toUpdate=getEntities();
		for (Entity e:toUpdate)
			e.update();
		
		for (int playerIndex=0; playerIndex<numPlayers; playerIndex++) {
			Input in=inputs[playerIndex];
			if (in==null) continue;
			
			int playerControlling;
			if (isReady[playerIndex])
				playerControlling=getFirstUnreadyCPU();
			else
				playerControlling=playerIndex;
			
			if (playerControlling!=-1)	
				handlePlayerButtonMovement(playerControlling, in);
			
			if (in.attack1Pressed()) {
				if (playerControlling==-1) {
					int[] teams=new int[numPlayers];
					for (int i=0; i<numPlayers; i++) 
						teams[i]=playerBackgroundColor[i]+1;
					return new MainScene(inputs, showColorBackground, this, playerSelectedButton, isCPU, teams);
				}
				else {
					isReady[playerControlling]=true;
				}
			}
			if (in.attack2Pressed()) {
				if (!isReady[playerIndex]) {
					//if it isn't the host who tries to close this, then just ignore it
					if (playerIndex==0) {
						server.closeServer();
						return new TitleScene(inputs[0]);
					}
				}
				else {
					//if there are any unselected cpu's, remove and unselect the previous cpu
					//if there are only selected cpu's, unselect the last cpu
					//if there are not cpu's, unselect myself
					boolean areCPUs=false;
					for (boolean b:isCPU) if (b) areCPUs=true;
					if (!areCPUs) {
						isReady[playerIndex]=false;
					}
					else {
						
						int unreadyCPU=getFirstUnreadyCPU();
						if (unreadyCPU!=-1) {
							isCPU[unreadyCPU]=false;
							playerSelectedButton[unreadyCPU]=-1;
						}
						int lastCPU=-1;
						for (int i=0; i<numPlayers; i++) if (isCPU[i]) lastCPU=i;
						if (lastCPU!=-1)
							isReady[lastCPU]=false;
						else
							isReady[playerIndex]=false;
					}
				}
			}
			if (in.shieldPressed()) {
				int toChangeColorOf=playerControlling==-1?playerIndex:playerControlling;
				if (isTeamMode)
					playerBackgroundColor[toChangeColorOf]=(playerBackgroundColor[toChangeColorOf]+1)%4;
				else 
					showColorBackground[toChangeColorOf]^=true;
			}
			if (in.attackRecoverPressed()) {
				if (everyoneReady()) {
					for (int i=0; i<numPlayers; i++) {
						if (playerSelectedButton[i]==-1) {
							System.out.println("Adding CPU as player "+i);
							playerSelectedButton[i]=0;
							isCPU[i]=true;
							isReady[i]=false;
							break;
						}
					}
				}
			}
			
		}
		for (int i=0; i<6; i++) {
			boolean selected=false;
			for (int p=0; p<numPlayers; p++)
				selected|=(playerSelectedButton[p]==i&&!isReady[p]);
			chooseCharacterButtons[i].setSelected(selected);
		}
		RawNetworkInput playerWhoLeft=server.getPlayerWhoLeft();
		while (playerWhoLeft!=null) {
			removePlayer(playerWhoLeft);
			playerWhoLeft=server.getPlayerWhoLeft();
		}
		RawNetworkInput playerWhoJoined=server.getPlayerWhoJoined();
		while (playerWhoJoined!=null) {
			System.out.println("Someone joined!");
			addPlayer(playerWhoJoined);
			playerWhoJoined=server.getPlayerWhoJoined();
		}
		return this;
	}

	public BufferedImage render() {
		Camera cam=Camera.getInstance();
		cam.preRender();
		renderEntites();

		Sprite[] selectors= {SpriteLoader.redSelector, SpriteLoader.blueSelector, SpriteLoader.greenSelector, SpriteLoader.yellowSelector};
		Sprite[] nameBackgrounds= {SpriteLoader.redNameBackground, SpriteLoader.blueNameBackground, SpriteLoader.greenNameBackground, 
				SpriteLoader.yellowNameBackground};
		Sprite[] namesOfCharacters= {SpriteLoader.cueballText, SpriteLoader.besiusText, SpriteLoader.smashText, SpriteLoader.carlosText, 
				SpriteLoader.cueballText, SpriteLoader.cueballText};
		Sprite[] characterIcons= {SpriteLoader.stickFigureIconSprite, SpriteLoader.besiusIconSprite, SpriteLoader.smashIconSprite,
				SpriteLoader.carlosIconSprite, SpriteLoader.emptySelectionIcon, SpriteLoader.emptySelectionIcon};
		Sprite[] coloredBackgrounds= {SpriteLoader.redCharacterBackground, SpriteLoader.blueCharacterBackground, SpriteLoader.greenCharacterBackground,
				SpriteLoader.yellowCharacterBackground};
		
		for (int i=0; i<numPlayers; i++) {
			if (playerSelectedButton[i]==-1) 
				continue;

			if (showColorBackground[i])
				coloredBackgrounds[playerBackgroundColor[i]].drawAlphaAndSize(characterChoicePositions[i], 1, 0.5, 0.5);
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
				nameBackgrounds[playerBackgroundColor[i]].drawAlphaAndSize(nameBackgroundPos, 1, 0.4, 0.4);
			
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
	
	private int getFirstUnreadyCPU() {
		for (int i=0; i<numPlayers; i++) {
			if (!isCPU[i]||isReady[i])
				continue;
			return i;
		}
		return -1;
	}
	
	private void addPlayer(RawNetworkInput rawInput) {
		//add to an empty spot, but if there are not, add to a cpu spot
		int emptySpot=-1;
		for (int i=0; i<numPlayers; i++) {
			if (playerSelectedButton[i]==-1) {
				emptySpot=i;
				break;
			}
		}
		
		if (emptySpot==-1) {
			for (int i=0; i<numPlayers; i++) {
				if (isCPU[i]) {
					emptySpot=i;
					break;
				}
			}
		}
		
		playerSelectedButton[emptySpot]=0;
		isCPU[emptySpot]=false;
		inputs[emptySpot]=new Input(rawInput);
	}
	
	private void removePlayer(RawNetworkInput input) {
		for (int i=0; i<numPlayers; i++) {
			if (inputs[i]!=null&&inputs[i].hasMatchingRawInput(input)) {
				inputs[i]=null;
				isCPU[i]=false;
				playerSelectedButton[i]=-1;
			}
		}
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
