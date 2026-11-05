package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MovingBody;

public class MovingBodyBuilder extends Builder<Body> {

	public MovingBodyBuilder() {
		super("mv_body", "Moving Body");
	}

	@Override
	protected Body createInstance(JSONObject data) {
		if (data == null || !data.has("id") || !data.has("gid") || !data.has("v") || !data.has("p") || !data.has("m"))
			throw new IllegalArgumentException();
		
		String id = data.getString("id");
		String idG = data.getString("gid");
		
		JSONArray speedArray = data.getJSONArray("v");
		JSONArray posArray = data.getJSONArray("p");
		// buscamos que tengan dos posiciones para ser un vector
		if (speedArray.length() != 2 || posArray.length() != 2)
			throw new IllegalArgumentException();
		// los convertimos a vectores
		Vector2D speed = new Vector2D (speedArray.getDouble(0), speedArray.getDouble(1));
		Vector2D pos = new Vector2D(posArray.getDouble(0), posArray.getDouble(1));
		double mass = data.getDouble("m");
		
	return new MovingBody(id, idG, pos, speed,  mass);
	}

	@Override
	protected JSONObject fillInData() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
