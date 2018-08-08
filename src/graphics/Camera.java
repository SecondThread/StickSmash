package graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import game.Game;
import math.Vec;

public class Camera extends Entity {
	
	static Camera instance;
	static BufferedImage image;
	
	private Vec position;
	private Graphics2D g;
	private double cameraWorldWidth=3000;
	
	public static Camera getInstance() {
		return instance==null?instance=new Camera():instance;
	}
	
	private Camera() {
		position=Vec.zero;
		image=new BufferedImage(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
	}
	
	public void update() {
	}
	
	public void setPosition(Vec position) {
		this.position=position;
	}
	
	public void setWorldWidth(double cameraWorldWidth) {
		this.cameraWorldWidth=cameraWorldWidth;
	}
	
	public void preRender() {
		g=(Graphics2D) image.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
	}
	
	public BufferedImage postRender() {
		g.dispose();
		return image;
	}
	
	public void draw(BufferedImage image, double xScale, double yScale, Vec worldPos, double alpha) {
		double worldToScreenRatio=cameraWorldWidth/Game.SCREEN_WIDTH;
		Vec screenPos=worldPos.sub(position).scale(1/worldToScreenRatio);
		screenPos=new Vec(screenPos.x(), -screenPos.y());
		
		double width=image.getWidth()/worldToScreenRatio*xScale;
		double height=image.getHeight()/worldToScreenRatio*yScale;
		
		screenPos=screenPos.sub(new Vec(width, height).scale(.5));
		screenPos=screenPos.add(new Vec(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT).scale(.5));

		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)(alpha));
		g.setComposite(ac);
		g.drawImage(image, (int)screenPos.x(), (int)screenPos.y(), (int)width, (int)height,
				null);
	}
	
}
