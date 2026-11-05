package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws {
	private double G;
	public NewtonUniversalGravitation(double G) {
		if (G <= 0) throw new IllegalArgumentException();
		else {
			this.G = G;
		}
	}
	
	@Override
	public void apply(List<Body> bs) {
		for (Body bi : bs) {
			for (Body bj : bs) {
				if (bi != bj) {
					bi.addForce(Fij(bi, bj));
				}
			}
		}
	}
	
	public Vector2D Fij (Body bi, Body bj) {
		Vector2D Fij = new Vector2D();
		Vector2D dij = new Vector2D();
		dij = bj.getPosition().minus(bi.getPosition());
		double fij;
		if (dij.magnitude() > 0) {
			fij = (this.G * bj.getMass() * bi.getMass());
			fij = fij / (dij.magnitude() * dij.magnitude());
		}
		else fij = 0.0;
		dij = dij.direction();
		Fij = dij.scale(fij);
		return Fij;
	}
	
	@Override
	public String toString() {
		return "Newton's Universal Gravitation with G=" + G;
	}


}
