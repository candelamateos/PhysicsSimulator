package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public abstract class Body {
	protected String id;
	protected String idG;
	protected Vector2D speed;
	protected Vector2D force;
	protected Vector2D pos;
	protected Double mass;

	public Body(String id, String idGroup, Vector2D pos, Vector2D speed, Double mass) {
		if (id == null || id == "" || idGroup == null || idGroup == "" || speed == null || pos == null || mass == null)
			throw new IllegalArgumentException();
		else if (id.trim().length() == 0 || idGroup.trim().length() == 0)
			throw new IllegalArgumentException();
		else if (mass <= 0)
			throw new IllegalArgumentException();
		else {
			this.id = id;
			this.idG = idGroup;
			this.speed = speed;
			this.force = new Vector2D();
			this.pos = pos;
			this.mass = mass;
		}

	}

	// devuelve el identificador del cuerpo.
	public String getId() {
		return id;
	}

	public String getgId() {
		return this.idG;
	}

	public Vector2D getVelocity() {
		return this.speed; // devuelve el vector de velocidad.
	}

	public Vector2D getForce() {
		return this.force; // devuelve el vector de fuerza.
	}

	public Vector2D getPosition() {
		return this.pos; // devuelve el vector de posiciÃ³n.
	}

	public double getMass() {
		return this.mass;// devuelve la masa del cuerpo.
	}

	// aÃ±ade la fuerza f al vector de fuerza del cuerpo (usando el mÃ©todo plus
	// de la clase Vector2D)
	public void addForce(Vector2D f) {
		this.force = this.force.plus(f);
	}

	void resetForce() {
		this.force = new Vector2D(); // pone el valor del vector de fuerza a (0, 0).
	}

	// mueve el cuerpo durante t segundos (las implementaciones estÃ¡n en las
	// sub-clases).
	public abstract void advance(double t);

	// devuelve la siguiente informaciÃ³n del cuerpo en formato JSON (como
	// JSONObject): { â€œid": id, "m": m, "p": p->, "v": v-> , "f": f-> }
	public JSONObject getState() {
		JSONObject j = new JSONObject();
		j.put("p", this.pos.asJSONArray());
		j.put("v", this.speed.asJSONArray());
		j.put("f", this.force.asJSONArray());
		j.put("id", this.id);
		j.put("m", this.mass);

		return j;
	}

	// devuelve getState().toString()
	public String toString() {
		return getState().toString();
	}

	@Override

	public boolean equals(Object obj) {

		if (this == obj)

			return true;

		if (obj == null)

			return false;

		if (!(obj instanceof Body))

			return false;

		Body other = (Body) obj;

		if (id == null || other.id == null) {

			return false;

		} else if (!id.equals(other.id))

			return false;

		return true;

	}
}
