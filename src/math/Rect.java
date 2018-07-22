package math;

public class Rect {
	
	Vec bottomLeft, topRight;
	
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
				Seg oSeg=new Seg(oCorners[i], oCorners[(i+1)%4]);
				if (mySeg.intersects(oSeg)) return true;
			}
		}
		return false;
	}
	
	public Vec[] corners() {
		return new Vec[] {bottomLeft, new Vec(topRight.x(), bottomLeft.y()), topRight, new Vec(bottomLeft.x(), topRight.y())};
	}
	
}
