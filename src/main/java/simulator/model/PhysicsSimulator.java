package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


public class PhysicsSimulator implements Observable<SimulatorObserver>{
	private ForceLaws forceLaws;
	private double delta;
	private double t;
	private Map<String, BodiesGroup> map;
	private Map<String, BodiesGroup> map_RO;
	private List<String> lista;
	private List<SimulatorObserver> observers;

	public PhysicsSimulator(ForceLaws forceLaws, double d) {
		if (forceLaws == null || d <= 0)
			throw new IllegalArgumentException();
		this.forceLaws = forceLaws;
		this.delta = d;
		this.t = 0.0;
		this.map = new HashMap<String, BodiesGroup>();
		this.lista = new ArrayList<String>();
		this.observers = new ArrayList<SimulatorObserver>();
		this.map_RO = Collections.unmodifiableMap(map);
	}

	public void setForceLaws(String id, ForceLaws forceLaws) {
    	if (!map.containsKey(id))
			throw new IllegalArgumentException();
		this.map.get(id).setForceLaws(forceLaws);
		
		for (SimulatorObserver o: observers) {
			o.onForceLawsChanged(this.map.get(id));;
		}
	}

	public void addGroup(String string) {
		if (map.containsKey(string) == false) {
			BodiesGroup addedGroup = new BodiesGroup(string, forceLaws);
			map.put(string, addedGroup);
			lista.add(string);
			
			for (SimulatorObserver o: observers) {
				o.onGroupAdded(map, addedGroup);
			}
		}
		else
			throw new IllegalArgumentException();
	}

	public void addBody(Body b1) {
		if (map.containsKey(b1.getgId())) {
			BodiesGroup bg = this.map.get(b1.getgId());
			bg.addBody(b1);
			
			for (SimulatorObserver o: observers) {
				o.onBodyAdded(map, b1);;
			}
		}
		else throw new IllegalArgumentException();
	}

	public void advance() {
		for (BodiesGroup bg : map.values()) {
			bg.advance(this.delta);
		}
		this.t += this.delta;
		
		for (SimulatorObserver o: observers) {
			o.onAdvance(map, t);
		}

	}	

	public Object getState() {
		JSONObject j = new JSONObject();
		JSONArray ja = new JSONArray();
		j.put("time", this.t);
		for (String id: lista) {
			ja.put(map.get(id).getState());
		}
		j.put("groups", ja);
		return j;
	}
	
	public String toString() {
		return getState().toString();
	}
	
	public void reset() {
		this.delta = 0.0;
		map.clear();
		lista.clear();
		
		for (SimulatorObserver o: observers) {
			o.onReset(map, t, delta);
		}
	}
	
	public void setDeltaTime(double dt) {
		if (dt <= 0) throw new IllegalArgumentException();
		this.delta = dt;
		
		for (SimulatorObserver o: observers) {
			o.onDeltaTimeChanged(dt);;
		}
	}

	@Override
	public void addObserver(SimulatorObserver o) {
		if (!observers.contains(o)) {
			observers.add(o);
		}
		o.onRegister(map, t, delta);
	}

	@Override
	public void removeObserver(SimulatorObserver o) {
		if (observers.contains(o)) {
			observers.remove(o);
		}
		
	}

}
