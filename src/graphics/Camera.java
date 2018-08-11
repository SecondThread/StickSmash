package graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Entity;
import game.Game;
import math.Vec;

public class Camera extends Entity {
	
	private static Camera instance;
	private BufferedImage image;
	private ArrayList<DrawLogEntry> currentDrawLog;
	private ArrayList<DrawLogEntry> lastDrawLog;
	
	private Vec position;
	private Graphics2D g;
	private double cameraWorldWidth=3000;
	
	public static Camera getInstance() {
		return instance==null?instance=new Camera():instance;
	}
	
	private Camera() {
		position=Vec.zero;
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
		image=new BufferedImage(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		g=(Graphics2D) image.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		currentDrawLog=new ArrayList<>();
	}
	
	public BufferedImage postRender() {
		g.dispose();
		lastDrawLog=currentDrawLog;
		return image;
	}
	
	public ArrayList<DrawLogEntry> getLastDrawLog() {
		return lastDrawLog;
	}
	
	public void draw(BufferedImage image, double xScale, double yScale, Vec worldPos, double alpha, int imageIndex) {
		currentDrawLog.add(new DrawLogEntry(xScale, yScale, worldPos, alpha, imageIndex, cameraWorldWidth, position));
		
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
	
	public void draw(DrawLogEntry oldEntry) {
		BufferedImage image=Sprite.getSpriteFromIndex(oldEntry.getSpriteIndex()).getImage();
		cameraWorldWidth=oldEntry.getWorldWidth();
		position=oldEntry.getCameraPos();
		draw(image, oldEntry.getXScale(), oldEntry.getYScale(), oldEntry.getWorldPos(), oldEntry.getAlpha(), oldEntry.getSpriteIndex());
	}
	
}
