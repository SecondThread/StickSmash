package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import math.Vec;

public class Sprite {
	
	public BufferedImage image;
	
	public Sprite(String path) {
		try {
			image=ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Vec pos, boolean facingRight) {
		Camera.instance.draw(image, facingRight?1:-1, 1, pos, 1);
	}
	
	public void drawAlphaAndSize(Vec pos, double alpha, double xScale, double yScale) {
		Camera.instance.draw(image, xScale, yScale, pos, alpha);
	}
	
	public void drawAlpha(Vec pos, boolean facingRight, double alpha) {
		Camera.instance.draw(image, facingRight?1:-1, 1, pos, alpha);
	}
}
