package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {

	public MovingTowardsFixedPointBuilder() {
		super("mtfp", "Moving towards a fixed point");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) {
		if(data == null) throw new IllegalArgumentException();
		else {
			Vector2D c = new Vector2D();
			double g = 9.81;
			if (data.has("c")) {
				JSONArray cArray = data.getJSONArray("c");
				if (cArray.length() != 2) throw new IllegalArgumentException();
				c = new Vector2D (cArray.getDouble(0),cArray.getDouble(1));
			}
			if (data.has("g")) g = data.getDouble("g");
			return new MovingTowardsFixedPoint(c,g);
		}
	}

	@Override
	protected JSONObject fillInData() {
		JSONObject j = new JSONObject();
		j.put("c", "the point towards which bodies move (e.g., [100.0,50.0])");
		j.put("g", "the length of the acceleration vector (a number)");
		return j;
	}
	}