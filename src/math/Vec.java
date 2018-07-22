package math;

import java.text.DecimalFormat;

public class Vec {
	
	public static Vec zero=new Vec(0, 0);
	public static Vec i=new Vec(1, 0);
	public static Vec j=new Vec(0, 1);
	public static Vec up=j, down=j.scale(-1), left=i.scale(-1), right=i;
	
	public static final double EPS=1e-6;

	private double x, y;
	
	public Vec(double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	public Vec add(Vec o) {
		return new Vec(x+o.x, y+o.y);
	}
	
	public Vec scale(double s) {
		return new Vec(x*s, y*s);
	}
	
	public Vec sub(Vec o) {
		return add(o.scale(-1));
	}
	
	public double dot(Vec o) {
		return x*o.x+y*o.y;
	}

	public double cross(Vec o) {
		return x*o.y-y*o.x;
	}
	
	public double mag2() {
		return dot(this);
	}
	
	public double mag() {
		return Math.sqrt(mag2());
	}
	
	public double dist(Vec o) {
		return sub(o).mag();
	}
	
	public Vec flipX() {
		return new Vec(-x, y);
	}
	
	public Vec flipY() {
		return new Vec(x, -y);
	}
	
	public double x() {
		return x;
	}
	
	public double y() {
		return y;
	}
	
	public boolean equals(Vec o) {
		Vec d=sub(o);
		return Math.abs(d.x)<EPS&&Math.abs(d.y)<EPS;
	}
	
	public Vec unit() {
		if (equals(Vec.zero))
			return Vec.i;
		return scale(1/mag());
	}
	
	public Vec rotate(double theta) {
		double newX=x*Math.cos(theta)+y*Math.cos(theta+Math.PI/2);
		double newY=x*Math.sin(theta)+y*Math.sin(theta+Math.PI/2);
		return new Vec(newX, newY);
	}
	
	public String toString() {
		DecimalFormat df=new DecimalFormat("#.##");
		return "("+df.format(x)+", "+df.format(y)+")";
	}
	

}

