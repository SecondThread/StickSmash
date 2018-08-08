package game.scenes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import entities.Entity;
import entities.backgrounds.UIBackground;
import game.ui.Button;
import graphics.Camera;
import graphics.SpriteLoader;
import input.InputType;
import math.Vec;

public class TitleScene extends Scene {

	private InputType inputType;
	private Button[] buttons;
	private int selectedButtonIndex=0;
	private int selectedButtonCounter=0;
	private final int maxSelectedButtonCounter=120;
	
	public TitleScene(InputType inputType) {
		this.inputType=inputType;
	}
	
	public void init() {
		new UIBackground();
		buttons=new Button[4];
		buttons[0]=new Button(SpriteLoader.playButton, new Vec(-300, 200));
		buttons[1]=new Button(SpriteLoader.teamsButton, new Vec(-300, 66.6));
		buttons[2]=new Button(SpriteLoader.joinGameButton, new Vec(-300, -66.6));
		buttons[3]=new Button(SpriteLoader.tutorialButton, new Vec(-300, -200));
		Camera.getInstance().setPosition(Vec.zero);
		Camera.getInstance().setWorldWidth(1000);
	}

	public Scene update() {
		updateEntities();
		selectedButtonCounter++;
		if (selectedButtonCounter>=maxSelectedButtonCounter)
			selectedButtonCounter=0;
		if (inputType.upMovementPressed())
			selectedButtonIndex=Math.max(0, selectedButtonIndex-1);
		if (inputType.downMovementPressed())
			selectedButtonIndex=Math.min(buttons.length-1, selectedButtonIndex+1);
		for (int i=0; i<buttons.length; i++)
			buttons[i].setSelected(selectedButtonIndex==i);
		if (inputType.attack1Pressed()) {
			switch(selectedButtonIndex) {
				case 0:
					return new MainScene();
				case 1:
				case 2:
				case 3:
			}
		}
		return this;
	}
	
	private void updateEntities() {
		ArrayList<Entity> toUpdate=getEntities();
		for (Entity e:toUpdate)
			e.update();
	}
	
	public BufferedImage render() {
		Camera cam=Camera.getInstance();
		cam.preRender();
		ArrayList<Entity> toRender=getEntities();
		Collections.sort(toRender, (a, b)->{return Integer.compare(a.getRenderOrder(), b.getRenderOrder());});
		for (Entity e:toRender)
			e.render();
		double alpha=Math.abs(selectedButtonCounter-maxSelectedButtonCounter/2)/(maxSelectedButtonCounter/2.0);
		alpha=Math.pow(alpha, 0.75);
		SpriteLoader.selectedButtonIndicatorSprite.drawAlphaAndSize(buttons[selectedButtonIndex].getPos(), alpha, 0.35, 0.35);
		BufferedImage result=cam.postRender();
		return result;
	}

	
}
