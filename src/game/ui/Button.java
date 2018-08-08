package game.ui;

import entities.Entity;
import graphics.Sprite;
import math.Lerp;
import math.Vec;

public class Button extends Entity {
	
	private static final double minXScale=.25, minYScale=.23, maxXScale=.3, maxYScale=.28;
	private static final double lerpSpeed=0.1;
	
	private Sprite sprite;
	private Vec position;
	private boolean selected;
	private double curXScale, curYScale;
	
	
	public Button(Sprite sprite, Vec position) {
		this.sprite=sprite;
		this.position=position;
		
	}
	
	public void setSelected(boolean selected) {
		this.selected=selected;
	}
	
	public void update() {
		if (selected) {
			curXScale=Lerp.lerp(curXScale, maxXScale, lerpSpeed);
			curYScale=Lerp.lerp(curYScale, maxYScale, lerpSpeed);
		}
		else {
			curXScale=Lerp.lerp(curXScale, minXScale, lerpSpeed);
			curYScale=Lerp.lerp(curYScale, minYScale, lerpSpeed);
		}
	}
	
	public void render() {
		sprite.drawAlphaAndSize(position, 1, curXScale, curYScale);
	}
	
	public Vec getPos() {
		return position;
	}
}
