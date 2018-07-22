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
		Camera.instance.draw(image, facingRight?1:-1, 1, pos);
	}
	
}
