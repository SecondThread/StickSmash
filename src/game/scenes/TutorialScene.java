package game.scenes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import entities.Entity;
import entities.Player;
import entities.StickFigureInstance;
import entities.backgrounds.TutorialBackground;
import game.Game;
import game.Ledge;
import graphics.Camera;
import graphics.SpriteLoader;
import input.Input;
import input.Keyboard;
import math.Rect;
import math.Seg;
import math.Vec;

public class TutorialScene extends Scene {

	private static final int maxGamOverCounter=60;

	private Vec[] spawnPoints;
	private int gameOverCounter=0;
	private int oldUpdatesPerSecond;
	private boolean gameOver=false;
	private Player player;
	
	public void init() {
		spawnPoints=new Vec[] {new Vec(-1500, -100)};
		new TutorialBackground();
		player=new Player(new Input(Keyboard.getInstance(true)), spawnPoints[0], 1, 0.5, false, new StickFigureInstance(1));
		Camera.getInstance().setPosition(player.isCameraFocusable());
		Camera.getInstance().setWorldWidth(1000);
		player.setLives(1);
	}

	public Scene update() {
		updateEntities();
		int count=0;
		for (Entity e:getEntities()) {
			if (e.isAlive())
				count++;
		}
		if (!gameOver) {
			if (count<=0) {
				gameOver=true;
				oldUpdatesPerSecond=Game.updatesPerSecond;
				Game.updatesPerSecond=20;
			}
		}
		else {
			gameOverCounter++;
			if (gameOverCounter>=maxGamOverCounter) {
				Game.updatesPerSecond=oldUpdatesPerSecond;
				return new TitleScene(new Input(Keyboard.getInstance(true)));
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
	
	public Vec[] getSpawnPoints() {
		return spawnPoints;
	}
	
	public Rect getBoundingBox() {
		return new Rect(new Vec(-1800, -700), new Vec(1960, 750));
	}

}
