	package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {

	public NewtonUniversalGravitationBuilder() {
		super("nlug", "Newton's law of universal gravitation");
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) {
		if (data == null) throw new IllegalArgumentException();
		if (data.has("G")) {
		    double G = data.getDouble("G");
		    return new NewtonUniversalGravitation(G);
		}
		else {
		    return new NewtonUniversalGravitation(6.67E-11);
	    }
	}

	@Override
	protected JSONObject fillInData() {
		JSONObject j = new JSONObject();
		j.put("G", "the gravitational constant (a number)");
		return j;
	}
	
	
}
