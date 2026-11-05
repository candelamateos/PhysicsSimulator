package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.StationaryBody;

public class StationaryBodyBuilder extends Builder<Body> {

	public StationaryBodyBuilder() {
		super("st_body", "Stationary Body");
	}

	@Override
	protected Body createInstance(JSONObject data) {
		if (data == null || !data.has("id") || !data.has("gid") || !data.has("p") || !data.has("m"))
			throw new IllegalArgumentException();
		
		String id = data.getString("id");
		String idG = data.getString("gid");
		JSONArray posArray = data.getJSONArray("p");
		// buscamos que tenga dos posiciones para ser un vector
		if (posArray.length() != 2)
			throw new IllegalArgumentException();
		// lo convertimos a vector
		Vector2D pos = new Vector2D(posArray.getDouble(0), posArray.getDouble(1));
		double mass = data.getDouble("m");
		
		return new StationaryBody(id, idG, pos, mass);
	}

	@Override
	protected JSONObject fillInData() {
		// TODO Auto-generated method stub
		return null;
	}

}
