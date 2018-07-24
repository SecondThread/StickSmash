package math;

import java.awt.Color;

public class Lerp {
	
	public static double lerp(double a, double b, double time) {
		return a*(1-time)+b*time;
	}
	
	public static Vec lerp(Vec a, Vec b, double time) {
		return a.scale(1-time).add(b.scale(time));
	}
	
	public static Color lerp(Color a, Color b, double time) {
		double rr=lerp(a.getRed(), b.getRed(), time);
		double gg=lerp(a.getGreen(), b.getGreen(), time);
		double bb=lerp(a.getBlue(), b.getBlue(), time);
		double alpha=lerp(a.getAlpha(), b.getAlpha(), time);
		return new Color((int)rr, (int)gg, (int)bb, (int)alpha);
	}
}
