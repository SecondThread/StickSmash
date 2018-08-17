package math;

import graphics.SpriteLoader;

public class Rect {

	private static final boolean drawRects=true;
	
	Vec bottomLeft, topRight;
	Vec drawOffset=Vec.zero;
	
	
	public Rect(Vec bottomLeft, Vec topRight) {
		this.bottomLeft=bottomLeft;
		this.topRight=topRight;
	}
	
	public Rect offsetBy(Vec offset) {
		return new Rect(bottomLeft.add(offset), topRight.add(offset));
	}

	public boolean contains(Vec o) {
		if (o.x()<bottomLeft.x()||o.x()>topRight.x()||o.y()<bottomLeft.y()||o.y()>topRight.y())
			return false;
		return true;
	}
	
	public boolean intersects(Rect o) {
		if (contains(o.topRight)||o.contains(topRight)) return true;
		Vec[] myCorners=corners(), oCorners=o.corners();
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				Seg mySeg=new Seg(myCorners[i], myCorners[(i+1)%4]);
				Seg oSeg=new Seg(oCorners[j], oCorners[(j+1)%4]);
				if (mySeg.intersects(oSeg)) return true;
			}
		}
		return false;
	}
	
	public boolean intersectsSeg(Seg s) {
		if (contains(s.from)||contains(s.to)) return true;
		Vec[] myCorners=corners();
		for (int i=0; i<4; i++) {
			Seg mySeg=new Seg(myCorners[i], myCorners[(i+1)%4]);
			if (mySeg.intersects(s)) return true;
		}
		return false;
	}
	
	public Vec[] corners() {
		return new Vec[] {bottomLeft, new Vec(topRight.x(), bottomLeft.y()), topRight, new Vec(bottomLeft.x(), topRight.y())};
	}
	
	public double width() {
		return Math.abs(topRight.sub(bottomLeft).x());
	}
	
	public double height() {
		return Math.abs(topRight.sub(bottomLeft).y());
	}
	
	public Vec center() {
		return topRight.add(bottomLeft).scale(0.5);
	}
	
	public void setDrawOffeset(Vec drawOffset) {
		this.drawOffset=drawOffset;
	}
	
	public Rect flipX() {
		return new Rect(new Vec(-topRight.x(), bottomLeft.y()), new Vec(-bottomLeft.x(), topRight.y()));
	}
	
	public void render() {
		if (drawRects)
			SpriteLoader.collisionSquare.drawAlphaAndSize(center().add(drawOffset), 0.3, width()/100, height()/100);
	}
	
	public double getLeft() {
		return bottomLeft.x();
	}
	
	public double getRight() {
		return topRight.x();
	}
	
	public double getBottom() {
		return bottomLeft.y();
	}
	
	public double getTop() {
		return topRight.y();
	}
}
