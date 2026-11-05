package simulator.model;

import simulator.misc.Vector2D;

public class MovingBody extends Body {

	public MovingBody(String id, String idGroup, Vector2D pos, Vector2D speed, Double mass) {
		super(id, idGroup, pos, speed, mass);
	}

	@Override
	void advance(double t) {
		Vector2D a = new Vector2D();
		if (this.mass != 0)
			a = this.force.scale(1.0/this.mass);
		
		pos = pos.plus(speed.scale(t).plus(a.scale(0.5 * t * t)));

		speed = a.scale(t).plus(speed);

	}
}
