package game.scenes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import entities.Entity;
import entities.Player;
import entities.backgrounds.MainBackground;
import game.Ledge;
import graphics.Camera;
import input.Input;
import input.Keyboard;
import math.Rect;
import math.Seg;
import math.Vec;

public class MainScene extends Scene {

	private Input[] inputs;
	private Vec[] spawnPoints;
	
	public MainScene(Input[] inputs) {
		this.inputs=inputs;
	}
	
	public void init() {
		new MainBackground();
		spawnPoints=new Vec[4];
		spawnPoints[0]=new Vec(-400, 300);
		spawnPoints[1]=new Vec(400, 300);
		spawnPoints[2]=new Vec(0, 500);
		spawnPoints[3]=new Vec(0, 100);
		int nextSpawnPoint=0;
		int team=1;
		for (Input i:inputs) {
			if (i==null) continue;
			new Player(i, spawnPoints[nextSpawnPoint++], team++);
		}
		Camera.getInstance().setWorldWidth(3000);
		Camera.getInstance().setPosition(Vec.zero);
	}
	
	public Scene update() {
		updateEntities();
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
	
	public Rect getBoundingBox() {
		return new Rect(new Vec(-1500, -1000), new Vec(1500, 1400));
	}
	
}
