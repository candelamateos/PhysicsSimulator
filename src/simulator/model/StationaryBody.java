package simulator.model;

import simulator.misc.Vector2D;

public class StationaryBody extends Body {

	public StationaryBody(String id, String idGroup, Vector2D pos, Double mass) {
		super(id, idGroup, pos, new Vector2D(), mass);
	}

	@Override
	void advance(double t) {
		// TODO Auto-generated method stub
		
	}

}
