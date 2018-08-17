package game.scenes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import entities.BesiusInstance;
import entities.CarlosInstance;
import entities.Entity;
import entities.Player;
import entities.SmashInstance;
import entities.StickFigureInstance;
import entities.backgrounds.MainBackground;
import game.Game;
import game.Ledge;
import graphics.Camera;
import graphics.SpriteLoader;
import input.Input;
import math.Rect;
import math.Seg;
import math.Vec;

public class MainScene extends Scene {

	private Input[] inputs;
	private int[] selectedCharacters;
	private static final int maxGamOverCounter=60;

	private boolean[] showHighlights;
	private Vec[] spawnPoints;
	private int gameOverCounter=0;
	private int oldUpdatesPerSecond;
	private boolean gameOver=false;
	private Scene oldChooseCharacterScene;
	
	public MainScene(Input[] inputs, boolean[] showHighlights, Scene oldChooseCharacterScene, int[] selectedCharacters) {
		this.inputs=inputs;
		this.showHighlights=showHighlights;
		this.oldChooseCharacterScene=oldChooseCharacterScene;
		this.selectedCharacters=selectedCharacters;
		Game.force120=true;
	}
	
	public void init() {
		new MainBackground();
		spawnPoints=new Vec[4];
		spawnPoints[0]=new Vec(-400, 300);
		spawnPoints[1]=new Vec(400, 300);
		spawnPoints[2]=new Vec(0, 500);
		spawnPoints[3]=new Vec(0, 100);
		int numPlayers=0;
		for (Input i:inputs)
			if (i!=null)
				numPlayers++;
		for (int i=0; i<inputs.length; i++) {
			if (inputs[i]==null) continue;
			double percentAcross=numPlayers==1?0.5:(i/(double)(numPlayers-1));
			if (selectedCharacters[i]==0||selectedCharacters[i]==4||selectedCharacters[i]==5)
				new Player(inputs[i], spawnPoints[i], i+1, percentAcross, showHighlights[i], new StickFigureInstance(i+1));
			if (selectedCharacters[i]==1)
				new Player(inputs[i], spawnPoints[i], i+1, percentAcross, showHighlights[i], new BesiusInstance(i+1));
			if (selectedCharacters[i]==2)
				new Player(inputs[i], spawnPoints[i], i+1, percentAcross, showHighlights[i], new SmashInstance(i+1));
			if (selectedCharacters[i]==3)
				new Player(inputs[i], spawnPoints[i], i+1, percentAcross, showHighlights[i], new CarlosInstance(i+1));
		}
		Camera.getInstance().setWorldWidth(3000);
		Camera.getInstance().setPosition(Vec.zero);
	}
	
	public Scene update() {
		updateEntities();
		int count=0;
		for (Entity e:getEntities()) {
			if (e.isAlive())
				count++;
		}
		if (!gameOver) {
			if (count<=1) {
				gameOver=true;
				oldUpdatesPerSecond=Game.updatesPerSecond;
				Game.updatesPerSecond=20;
			}
		}
		else {
			gameOverCounter++;
			if (gameOverCounter>=maxGamOverCounter) {
				Game.updatesPerSecond=oldUpdatesPerSecond;
				return oldChooseCharacterScene;
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
		renderEntites();
		cam.pushState();
		cam.setPosition(Vec.zero);
		cam.setWorldWidth(1000);
		if (gameOver) {
			SpriteLoader.gameOverText.drawAlphaAndSize(Vec.zero, 1, 0.5, 0.5);
		}
		cam.popState();
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
		
		//then render the UI
		for (Entity e:toRender)
			e.renderUI();
	}
	
	public Rect getBoundingBox() {
		return new Rect(new Vec(-1500, -1000), new Vec(1500, 1400));
	}
	
	public Vec[] getSpawnPoints() {
		return spawnPoints;
	}
	
}
