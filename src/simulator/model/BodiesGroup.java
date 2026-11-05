package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BodiesGroup implements Iterable<Body>{
	private String id;
	private ForceLaws fl;
	private ArrayList<Body> bodies;
	private List<Body> bodiesRO;
	
	BodiesGroup(String id, ForceLaws fl) {
		if (id == null || fl == null || id.trim().length() == 0)
			throw new IllegalArgumentException();
		else {
			this.id = id;
			this.fl = fl;
			this.bodies = new ArrayList<Body>();
			bodiesRO = Collections.unmodifiableList(bodies);
		}
	}

	public String getId() {
		return this.id;
	}

	void setForceLaws(ForceLaws fl) {
		if (fl == null)
			throw new IllegalArgumentException();
		else {
			this.fl = fl;
		}
	}

	void addBody(Body b) {
		if (b == null)
			throw new IllegalArgumentException();
		else {
			if (bodies.contains(b))
				throw new IllegalArgumentException();
			else {
				this.bodies.add(b);
			}
		}
	}

	void advance(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException();
		for (Body b : bodies) {
			b.resetForce();
		}
		fl.apply(bodies);
		for (Body b : bodies) {
			b.advance(dt);
		}
	}

	public JSONObject getState() {
		JSONObject j = new JSONObject();
		JSONArray ja = new JSONArray();
		j.put("id", this.id);
		for (Body b : bodies) {
			ja.put(b.getState());
		}
		j.put("bodies", ja);
		return j;
	}

	public String toString() {
		return getState().toString();
	}

	@Override

	public boolean equals(Object obj) {

		if (this == obj)

			return true;

		if (obj == null)

			return false;

		if (getClass() != obj.getClass())

			return false;

		BodiesGroup other = (BodiesGroup) obj;

		if (id == null) {

			if (other.id != null)

				return false;

		} else if (!id.equals(other.id))

			return false;

		return true;

	}

	@Override

	public int hashCode() {

		final int prime = 31;

		int result = 1;

		result = prime * result + ((id == null) ? 0 : id.hashCode());

		return result;

	}
	
	public String getForceLawsInfo() {
		return fl.toString(); 
	}

	@Override
	public Iterator<Body> iterator() {
		return bodiesRO.iterator();
	}

}
