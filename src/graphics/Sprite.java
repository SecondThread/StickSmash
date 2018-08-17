package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import game.Game;
import math.Vec;

public class Sprite {
	
	private static int globalSpriteIndex;
	private static ArrayList<Sprite> allSprites=new ArrayList<>();
	
	private BufferedImage image;
	private int spriteIndex;
	
	public Sprite(String path) {
		path=path.replace("StickSmashArt", "");
		spriteIndex=globalSpriteIndex++;
		allSprites.add(this);
		try {
			URL resource=Game.class.getResource(path);
			System.out.println("null? "+resource==null);
			System.out.println("Path: "+path);
			image=ImageIO.read(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Vec pos, boolean facingRight) {
		Camera.getInstance().draw(image, facingRight?1:-1, 1, pos, 1, spriteIndex);
	}
	
	public void drawAlphaAndSize(Vec pos, double alpha, double xScale, double yScale) {
		Camera.getInstance().draw(image, xScale, yScale, pos, alpha, spriteIndex);
	}
	
	public void drawAlpha(Vec pos, boolean facingRight, double alpha) {
		Camera.getInstance().draw(image, facingRight?1:-1, 1, pos, alpha, spriteIndex);
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public static Sprite getSpriteFromIndex(int spriteIndex) {
		return allSprites.get(spriteIndex);
	}
}
