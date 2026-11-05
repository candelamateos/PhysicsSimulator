package simulator.factories;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {
	private Map<String, Builder<T>> _builders;
	private List<JSONObject> _buildersInfo;

	public BuilderBasedFactory() {
		this._builders = new HashMap<String, Builder<T>>();
		this._buildersInfo = new LinkedList<JSONObject>();
	}

	public BuilderBasedFactory(List<Builder<T>> builders) {
		this();
		for (Builder<T> b : builders) addBuilder(b);
	}

	public void addBuilder(Builder b) {
		_builders.put(b.getTypeTag(), b);
		_buildersInfo.add(b.getInfo());
	}

	@Override
	public T createInstance(JSONObject info) {
		if (info == null) {
			throw new IllegalArgumentException("Invalid value for createInstance: null");
		}
		if(!_builders.containsKey(info.getString("type"))) 
			throw new IllegalArgumentException("Invalid value forcreateInstance: " + info.toString());
		else {
			if (!info.has("data")) return _builders.get(info.getString("type")).createInstance(new JSONObject());
			return _builders.get(info.getString("type")).createInstance(info.getJSONObject("data"));
		}
	}

	@Override
	public List<JSONObject> getInfo() {
		return Collections.unmodifiableList(_buildersInfo);
	}
}
