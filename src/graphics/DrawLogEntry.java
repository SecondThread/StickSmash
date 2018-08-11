package graphics;

import java.text.DecimalFormat;

import math.Vec;

public class DrawLogEntry {

	private double xScale, yScale;
	private Vec worldPos;
	private double alpha;
	private int spriteIndex;
	private double worldWidth;
	private Vec cameraPos;
	
	public DrawLogEntry(String s) {
		String[] parts=s.split(" ");
		xScale=Double.parseDouble(parts[0]);
		yScale=Double.parseDouble(parts[1]);
		worldPos=new Vec(Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
		alpha=Double.parseDouble(parts[4]);
		spriteIndex=Integer.parseInt(parts[5]);
		worldWidth=Double.parseDouble(parts[6]);
		cameraPos=new Vec(Double.parseDouble(parts[7]), Double.parseDouble(parts[8]));
	}
	
	public DrawLogEntry(double xScale, double yScale, Vec worldPos, double alpha, int spriteIndex, double worldWidth, Vec cameraPos) {
		this.xScale=xScale;
		this.yScale=yScale;
		this.worldPos=worldPos;
		this.alpha=alpha;
		this.spriteIndex=spriteIndex;
		this.worldWidth=worldWidth;
		this.cameraPos=cameraPos;
	}
	
	public double getXScale() {
		return xScale;
	}
	
	public double getYScale() {
		return yScale;
	}
	
	public Vec getWorldPos() {
		return worldPos;
	}
	
	public double getAlpha() {
		return alpha;
	}
	
	public int getSpriteIndex() {
		return spriteIndex;
	}
	
	public double getWorldWidth() {
		return worldWidth;
	}
	
	public Vec getCameraPos() {
		return cameraPos;
	}
	
	public String toString() {
		DecimalFormat df=new DecimalFormat("#.##");
		StringBuilder sb=new StringBuilder("");

		sb.append(df.format(xScale)+" ");
		sb.append(df.format(yScale)+" ");
		sb.append(df.format(worldPos.x())+" ");
		sb.append(df.format(worldPos.y())+" ");
		sb.append(df.format(alpha)+" ");
		sb.append(spriteIndex+" ");
		sb.append(df.format(worldWidth)+" ");
		sb.append(df.format(cameraPos.x())+" ");
		sb.append(df.format(cameraPos.y())+" ");
		
		
		return sb.toString();
	}
	
}
