package game;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.scenes.Scene;
import game.scenes.TitleScene;
import game.scenes.TutorialScene;
import graphics.SpriteLoader;
import input.Input;
import input.Keyboard;

public class Game {
	
	public static final double ASPECT_RATIO=16.0/9;
	public static final int SCREEN_WIDTH=1000;
	public static final int SCREEN_HEIGHT=(int) (SCREEN_WIDTH/ASPECT_RATIO);
	public static int updatesPerSecond=120;

	private static final String GAME_NAME="Stick Smash";
	private static JFrame frame;
	private static JPanel mainPanel;
	
	private static ArrayList<Input> inputs=new ArrayList<>();
	private static Scene scene;
	
	
	public static void main(String[] args) {
		frame=new JFrame();
		mainPanel=new JPanel();
		mainPanel.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setTitle(GAME_NAME);
		frame.setVisible(true);
		SpriteLoader.loadSprites();
		
		Input player1Input=new Input(Keyboard.getInstance(true));
		scene=new TitleScene(player1Input);
		scene.init();

		runGameLoop();
	}
	
	public static void addKeyboard(Keyboard toAdd) {
		frame.addKeyListener(toAdd);
	}
	
	public static void addInput(Input input) {
		inputs.add(input);
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
		Scene newScene=scene.update();
		if (newScene!=scene) {
			scene=newScene;
			newScene.init();
		}
	}
	
	private static void updateListeners() {
		for (int i=0; i<inputs.size(); i++)
			inputs.get(i).onUpate();
	}
	
	private static void render() {
		BufferedImage result=scene.render();
		
		Graphics2D g=(Graphics2D) mainPanel.getGraphics();
		g.drawImage(result, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
		g.dispose();
	}
	
	public static Scene getScene() {
		return scene;
	}
	
}
