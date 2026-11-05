package simulator.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {
	String[] _header = { "Id", "gId", "Mass", "Velocity", "Position", "Force" };
	List<Body> _bodies;

	BodiesTableModel(Controller ctrl) {
		_bodies = new ArrayList<>();
		ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return _bodies.size();
	}

	@Override
	public int getColumnCount() {
		return _header.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return _header[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String r = "";
		
		Body b =_bodies.get(rowIndex);
		if (columnIndex == 0) r = b.getId();
		else if (columnIndex == 1) r = b.getgId();
		else if (columnIndex == 2)
			r = Double.toString(b.getMass());
		else if (columnIndex == 3)
			r = b.getVelocity().toString();
		else if (columnIndex == 4)
			r = b.getPosition().toString();
		else r = b.getForce().toString();
		return r;
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		fireTableDataChanged();

	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		_bodies = new ArrayList<Body>();
		fireTableStructureChanged();
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		for (BodiesGroup bg : groups.values())
			for (Body b : bg)
				_bodies.add(b);
		fireTableStructureChanged();
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		for (Body b : g) {
			_bodies.add(b);
		}
		fireTableStructureChanged();
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		_bodies.add(b);
		fireTableStructureChanged();
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
	
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		
	}
}