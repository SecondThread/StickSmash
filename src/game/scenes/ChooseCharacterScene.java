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
		chooseCharacterButtons[0]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(-200, 150));
		chooseCharacterButtons[1]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(0, 150));
		chooseCharacterButtons[2]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(200, 150));
		chooseCharacterButtons[3]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(-200, 0));
		chooseCharacterButtons[4]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(0, 0));
		chooseCharacterButtons[5]=new Button(SpriteLoader.stickFigureIconSprite, new Vec(200, 0));
		
		server=new Server(3);
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
			
			for (int i=0; i<6; i++) {
				boolean selected=false;
				for (int p=0; p<numPlayers; p++)
					selected|=playerSelectedButton[p]==i;
				chooseCharacterButtons[i].setSelected(selected);
			}
			
			if (in.attack1Pressed()) {
				System.out.println("going to next screen now...");
				return new MainScene();
			}
			if (in.attack2Pressed()) {
				return new TitleScene(inputs[0]);
			}
		}
		return this;
	}

	public BufferedImage render() {
		Camera cam=Camera.getInstance();
		cam.preRender();
		renderEntites();
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

}
