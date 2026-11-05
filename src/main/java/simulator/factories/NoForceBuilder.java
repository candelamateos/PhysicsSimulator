package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws> {

	public NoForceBuilder() {
		super("nf", "No Force");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) {
		if (data == null) throw new IllegalArgumentException();
		return new NoForce();
	}

	@Override
	protected JSONObject fillInData() {
		JSONObject j = new JSONObject();
		return j;
	}

}
