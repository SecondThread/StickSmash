package graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

import entities.Entity;
import game.Game;
import math.Lerp;
import math.Rect;
import math.Vec;

public class Camera extends Entity {
	
	private static Camera instance;
	private BufferedImage image;
	private ArrayList<DrawLogEntry> currentDrawLog;
	private ArrayList<DrawLogEntry> lastDrawLog;
	
	private Stack<Vec> positionsStack=new Stack<>();
	private Stack<Double> cameraWorldWidthStack=new Stack<>();
	
	private Vec position;
	private Graphics2D g;
	private double cameraWorldWidth=3000;
	private double moveLerpSpeed=0.03, screenLerpSpeed=0.1;
	private double maxScreenZoomin=4;
	private static double cameraPadding=600;
	
	public static Camera getInstance() {
		return instance==null?instance=new Camera():instance;
	}
	
	private Camera() {
		position=Vec.zero;
	}
	
	public void update() {
	}
	
	public void pushState() {
		positionsStack.push(position);
		cameraWorldWidthStack.push(cameraWorldWidth);
	}
	
	public void popState() {
		position=positionsStack.pop();
		cameraWorldWidth=cameraWorldWidthStack.pop();
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
		tryToMoveCamera();
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
	
	private void tryToMoveCamera() {
		ArrayList<Entity> allEntities=Game.getScene().getEntities();
		ArrayList<Vec> cameraTargets=new ArrayList<>();
		for (Entity e:allEntities) {
			Vec target=e.isCameraFocusable();
			if (target!=null)
				cameraTargets.add(target);
		}
		if (cameraTargets.isEmpty()) return;
		double minX=cameraTargets.get(0).x(), maxX=cameraTargets.get(0).x();
		double minY=cameraTargets.get(0).y(), maxY=cameraTargets.get(0).y();
		for (Vec v:cameraTargets) {
			minX=Math.min(minX, v.x());
			maxX=Math.max(maxX, v.x());
			minY=Math.min(minY, v.y());
			maxY=Math.max(maxY, v.y());
		}
		minX-=cameraPadding;
		minY-=cameraPadding;
		maxX+=cameraPadding;
		maxY+=cameraPadding;

		Rect boundingRect=Game.getScene().getBoundingBox();
		minX=Math.max(minX, boundingRect.getLeft());
		maxX=Math.min(maxX, boundingRect.getRight());
		minY=Math.max(minY, boundingRect.getBottom());
		maxY=Math.min(maxY, boundingRect.getTop());
		
		Vec newTargetCenter=new Vec(minX+maxX, minY+maxY).scale(0.5);
		position=Lerp.lerp(position, newTargetCenter, moveLerpSpeed);
		
		double newScreenWidth=0;
		newScreenWidth=Math.max(newScreenWidth, 2*Math.abs(position.x()-minX));
		newScreenWidth=Math.max(newScreenWidth, 2*Math.abs(position.x()-maxX));
		newScreenWidth=Math.max(newScreenWidth, 2*Math.abs(position.y()-minY));
		newScreenWidth=Math.max(newScreenWidth, 2*Math.abs(position.y()-maxY));
		
		
		
		if (newScreenWidth>cameraWorldWidth) {
			cameraWorldWidth=Lerp.lerp(cameraWorldWidth, newScreenWidth, screenLerpSpeed);
		}
		else {
			double newTarget=Lerp.lerp(cameraWorldWidth, newScreenWidth, screenLerpSpeed);
			if (cameraWorldWidth-newTarget>maxScreenZoomin) {
				cameraWorldWidth=cameraWorldWidth-maxScreenZoomin;
			}
			else {
				cameraWorldWidth=newTarget;
			}
		}
		cameraWorldWidth=Math.min(cameraWorldWidth, boundingRect.width());
		cameraWorldWidth=Math.min(cameraWorldWidth, boundingRect.height()*Game.SCREEN_WIDTH/Game.SCREEN_HEIGHT);
		
		double cameraXRad=cameraWorldWidth/2;
		double cameraYRad=cameraXRad*Game.SCREEN_HEIGHT/Game.SCREEN_WIDTH;
		position=new Vec(Math.min(position.x(), boundingRect.getRight()-cameraXRad), 
				Math.min(position.y(), boundingRect.getTop()-cameraYRad));
		position=new Vec(Math.max(position.x(), boundingRect.getLeft()+cameraXRad),
				Math.max(position.y(), boundingRect.getBottom()+cameraYRad));
	}
	
}
