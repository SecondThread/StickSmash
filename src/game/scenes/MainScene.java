package game.scenes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import entities.Entity;
import entities.Player;
import entities.backgrounds.MainBackground;
import game.Ledge;
import graphics.Camera;
import input.Keyboard;
import math.Rect;
import math.Seg;
import math.Vec;

public class MainScene extends Scene {

	public void init() {
		new MainBackground();
		new Player(Keyboard.getInstance(true), new Vec(-400, 300), 1);
		new Player(Keyboard.getInstance(false), new Vec(400, 300), 2);
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
	
	
	
}
