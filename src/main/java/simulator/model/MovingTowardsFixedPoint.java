package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws {
	
	private Vector2D c;
	private double g;

	public MovingTowardsFixedPoint(Vector2D c, double g) {
		if (c == null || g <= 0) throw new IllegalArgumentException();
		else {
			this.c = c;
			this.g = g;
		}
	}
	
	private Vector2D Fi(Body bi, Vector2D c, double g) {
		Vector2D Fi = new Vector2D();
		Vector2D di = new Vector2D();
		di = (c.minus(bi.getPosition())).direction();
		Fi = di.scale(bi.getMass()*g);
		return Fi;
	}


	@Override
	public void apply(List<Body> bs) {
		for (Body bi : bs) {
			bi.addForce(Fi(bi, c, g));
		}
	}

	public String toString() {
		return "Moving towards" + c + "with constant acceleration" + g;
	}
	
}
