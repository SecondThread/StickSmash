package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import math.Vec;

public class Sprite {
	
	private static int globalSpriteIndex;
	private static ArrayList<Sprite> allSprites=new ArrayList<>();
	
	private BufferedImage image;
	private int spriteIndex;
	
	public Sprite(String path) {
		spriteIndex=globalSpriteIndex++;
		allSprites.add(this);
		try {
			image=ImageIO.read(new File(path));
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
