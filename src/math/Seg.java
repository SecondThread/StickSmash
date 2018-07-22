package math;

public class Seg {
	
	Vec from, to;
	
	public Seg(Vec from, Vec to) {
		this.from=from;
		this.to=to;
	}
	
	public boolean intersects(Seg o) {
		if (otherCrosses(o)&o.otherCrosses(this)) return true;
		return false;
	}
	
	private boolean otherCrosses(Seg o) {
		Vec toFrom=o.from.sub(from), toTo=o.to.sub(from);
		Vec dir=to.sub(from);
		return Math.signum(dir.cross(toFrom))!=Math.signum(dir.cross(toTo));
	}
	
}
