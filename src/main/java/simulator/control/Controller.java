package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {
	PhysicsSimulator fs;
	Factory<Body> fb;
	Factory<ForceLaws> fl;

	public Controller(PhysicsSimulator fs, Factory<Body> fb, Factory<ForceLaws> fl) {
		this.fs = fs;
		this.fb = fb;
		this.fl = fl;
	}

	public void loadData(InputStream in) {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));
		if (jsonInput.has("groups")) {
			JSONArray js = jsonInput.getJSONArray("groups");
			for (int i = 0; i < js.length(); i++) {
				fs.addGroup(js.getString(i));
			}
		} 

		if (jsonInput.has("laws")) {
			JSONArray list2 = jsonInput.getJSONArray("laws");
			for (int i = 0; i < list2.length(); i++) {
				ForceLaws forces = fl
						.createInstance(list2.getJSONObject(i).getJSONObject("laws"));
				this.fs.setForceLaws(list2.getJSONObject(i).getString("id"), forces);
			}
		}
		
		JSONArray list3 = jsonInput.getJSONArray("bodies");
		for (int i = 0; i < list3.length(); i++) {
			this.fs.addBody(fb.createInstance(list3.getJSONObject(i)));
		}
	}

	public void run(int n, OutputStream out) {		
		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println("\"states\": [");
		p.println(fs.getState());
		for (int i = 1; i <= n; i++) {
			fs.advance();
			p.print(",");
			p.println(fs.getState());
		}		
		p.println("]");
		p.println("}");
	}
	
	public void reset() {
		fs.reset();
	}
	
	public void setDeltaTime(double dt) {
		fs.setDeltaTime(dt);
	}
	
	public void addObserver(SimulatorObserver o) {
		fs.addObserver(o);
	}
	
	public void removeObserver(SimulatorObserver o) {
		fs.removeObserver(o);
	}
	
	public List<JSONObject> getForceLawsInfo(){
		return fl.getInfo();
	}
	
	public void setForceLaws(String gId, JSONObject info){
		ForceLaws force = fl.createInstance(info);
		fs.setForceLaws(gId, force);
	}
	
	public void run (int n) {
		
		for(int i=0; i<n; i++) 
			fs.advance();

	}
	

}
