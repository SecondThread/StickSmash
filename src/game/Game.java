package game;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

import entities.Entity;
import entities.Player;
import entities.backgrounds.MainBackground;
import graphics.Camera;
import graphics.SpriteLoader;
import input.InputType;
import input.Keyboard;
import math.Rect;
import math.Seg;
import math.Vec;

public class Game {
	
	public static final double ASPECT_RATIO=16.0/9;
	public static final int SCREEN_WIDTH=1000;
	public static final int SCREEN_HEIGHT=(int) (SCREEN_WIDTH/ASPECT_RATIO);
	public static int updatesPerSecond=120;

	private static final String GAME_NAME="Stick Smash";
	private static JFrame frame;
	private static JPanel mainPanel;
	
	private static ArrayList<InputType> inputTypes=new ArrayList<>();
	private static ArrayList<Entity> entities=new ArrayList<>();
	private static ArrayList<Rect> universalCollitionBoxes=new ArrayList<>();
	private static ArrayList<Seg> universalPlatforms=new ArrayList<>();
	private static ArrayList<Ledge> hangPositions=new ArrayList<>();
	
	public static void main(String[] args) {
		frame=new JFrame();
		mainPanel=new JPanel();
		mainPanel.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setTitle(GAME_NAME);
		frame.setVisible(true);
		
		Keyboard.getInstance();
		SpriteLoader.loadSprites();
		
		new MainBackground();
		new Player(Keyboard.getInstance());
		
		runGameLoop();
	}
	
	public static void addKeyboard(Keyboard toAdd) {
		inputTypes.add(toAdd);
		frame.addKeyListener(toAdd);
	}
	
	public static void addEntity(Entity e) {
		entities.add(e);
	}
	
	public static void destroyEntity(Entity e) {
		entities.remove(e);
	}
	
	public static void addCollitionBox(Rect toAdd) {
		universalCollitionBoxes.add(toAdd);
	}
	
	public static void destroyCollisionBox(Rect toDestroy) {
		universalCollitionBoxes.remove(toDestroy);
	}
	
	public static void addPlatform(Seg toAdd) {
		universalPlatforms.add(toAdd);
	}
	
	public static void destroyPlatform(Seg toDestroy) {
		universalPlatforms.remove(toDestroy);
	}
	
	public static void addHangPos(Ledge toAdd) {
		hangPositions.add(toAdd);
	}
	
	public static void destroyHangPosition(Ledge toDestroy) {
		hangPositions.remove(toDestroy);
	}
	
	private static void runGameLoop() {
		int updates=0, frames=0;
		long nextSecond=System.currentTimeMillis();
		long nextUpdateTime=System.nanoTime();
		long nanoSecsPerSec=1_000_000_000;
		while (true) {
			if (System.currentTimeMillis()>=nextSecond) {
				frame.setTitle(GAME_NAME+"                     Updates: "+updates+"   FPS: "+frames);
				updates=0;
				frames=0;
				nextSecond+=1000;
				
				//lag reduction when computer is shut
				if (nextSecond-System.currentTimeMillis()>10000)
					nextSecond=System.currentTimeMillis()+1000;
			}
			while (nextUpdateTime<System.nanoTime()) {
				update();
				updates++;
	
				//lag reduction when computer is shut
				nextUpdateTime+=nanoSecsPerSec/updatesPerSecond;
				if (System.nanoTime()-nextUpdateTime>nanoSecsPerSec)
					nextUpdateTime=nanoSecsPerSec;
			}
			render();
			frames++;
			
		}
	}
	
	private static void update() {
		updateListeners();
		updateEntities();
	}
	
	private static void updateListeners() {
		for (int i=0; i<inputTypes.size(); i++)
			inputTypes.get(i).onUpate();
	}
	
	private static void updateEntities() {
		ArrayList<Entity> toUpdate=new ArrayList<>();
		for (Entity e:entities) 
			toUpdate.add(e);
		for (Entity e:toUpdate)
			e.update();
	}
	
	private static void render() {
		Camera cam=Camera.getInstance();
		cam.preRender();
		renderEntites();
		BufferedImage result=cam.postRender();

		Graphics2D g=(Graphics2D) mainPanel.getGraphics();
		g.drawImage(result, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
		g.dispose();
	}
	
	private static void renderEntites() {
		ArrayList<Entity> toRender=new ArrayList<>();
		for (Entity e:entities) 
			toRender.add(e);
		Collections.sort(toRender, (a, b)->{return Integer.compare(a.getRenderOrder(), b.getRenderOrder());});
		for (Entity e:toRender)
			e.render();
		for (Rect rect:universalCollitionBoxes)
			rect.render();
		for (Seg s:universalPlatforms)
			s.render();
		for (Ledge v:hangPositions)
			v.render();
	}
	
	public static ArrayList<Rect> getCollisionBoxes() {
		ArrayList<Rect> toReturn=new ArrayList<>();
		toReturn.addAll(universalCollitionBoxes);
		return toReturn;
	}
	
	public static ArrayList<Seg> getPlatforms() {
		ArrayList<Seg> toReturn=new ArrayList<>();
		toReturn.addAll(universalPlatforms);
		return toReturn;
	}
	
	public static ArrayList<Ledge> getHangPositions() {
		ArrayList<Ledge> toReturn=new ArrayList<>();
		toReturn.addAll(hangPositions);
		return toReturn;
	}

}
