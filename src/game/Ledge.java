package game;

import math.Vec;

public class Ledge {
	
	private Vec pos;
	public boolean occupied=false;
	
	public Ledge(Vec pos) {
		this.pos=pos;
	}

	public Vec getPos() {
		return pos;
	}
	
	public void render() {
		pos.render();
	}
	

}
