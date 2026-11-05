package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class StatusBar extends JPanel implements SimulatorObserver {
	private JLabel _currTime;
	private JLabel _numOfGroups;

	StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));

		_currTime = new JLabel("0.0");
		_currTime.setPreferredSize(new Dimension(100, 20));
		_numOfGroups = new JLabel("0.0");
		_numOfGroups.setPreferredSize(new Dimension(50, 20));

		JSeparator sep_1 = new JSeparator(JSeparator.VERTICAL);
		sep_1.setPreferredSize(new Dimension(10, 20));

		JSeparator sep_2 = new JSeparator(JSeparator.VERTICAL);
		sep_2.setPreferredSize(new Dimension(10, 20));

		this.add(new JLabel("Time: "));
		this.add(_currTime);
		this.add(sep_1);
		this.add(new JLabel("Groups: "));
		this.add(_numOfGroups);
		this.add(sep_2);
	}

	// TODO el resto de métodos van aquí…
	
	private void updateTime(double time) {
		_currTime.setText("" + time);
	}

	private void updateNumGroups(int n) {
		_numOfGroups.setText("" + n);
	}
	
	
	//Simulator observer methods:
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		updateTime(time);
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		updateTime(time);
		updateNumGroups(groups.keySet().size());
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		updateTime(time);
		updateNumGroups(groups.keySet().size());
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		updateNumGroups(groups.keySet().size());
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		// TODO Auto-generated method stub

	}

}
