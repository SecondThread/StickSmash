package game.scenes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Entity;
import game.Ledge;
import math.Rect;
import math.Seg;

public abstract class Scene {
	
	private ArrayList<Entity> entities=new ArrayList<>();
	private ArrayList<Rect> universalCollitionBoxes=new ArrayList<>();
	private ArrayList<Seg> universalPlatforms=new ArrayList<>();
	private ArrayList<Ledge> hangPositions=new ArrayList<>();
	
	//called once this is set to the active scene in Game. Do all construction of entities here
	public abstract void init();
	
	public abstract Scene update();
	
	public abstract BufferedImage render();
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	public void destroyEntity(Entity e) {
		entities.remove(e);
	}
	
	public void addCollitionBox(Rect toAdd) {
		universalCollitionBoxes.add(toAdd);
	}
	
	public void destroyCollisionBox(Rect toDestroy) {
		universalCollitionBoxes.remove(toDestroy);
	}
	
	public void addPlatform(Seg toAdd) {
		universalPlatforms.add(toAdd);
	}
	
	public void destroyPlatform(Seg toDestroy) {
		universalPlatforms.remove(toDestroy);
	}
	
	public void addHangPos(Ledge toAdd) {
		hangPositions.add(toAdd);
	}
	
	public void destroyHangPosition(Ledge toDestroy) {
		hangPositions.remove(toDestroy);
	}
	
	public ArrayList<Entity> getEntities() {
		ArrayList<Entity> toReturn=new ArrayList<>();
		toReturn.addAll(entities);
		return toReturn;
	}

	public ArrayList<Rect> getCollisionBoxes() {
		ArrayList<Rect> toReturn=new ArrayList<>();
		toReturn.addAll(universalCollitionBoxes);
		return toReturn;
	}
	
	public ArrayList<Seg> getPlatforms() {
		ArrayList<Seg> toReturn=new ArrayList<>();
		toReturn.addAll(universalPlatforms);
		return toReturn;
	}
	
	public ArrayList<Ledge> getHangPositions() {
		ArrayList<Ledge> toReturn=new ArrayList<>();
		toReturn.addAll(hangPositions);
		return toReturn;
	}
}
