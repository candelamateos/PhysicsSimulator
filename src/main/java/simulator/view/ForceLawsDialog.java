package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ForceLawsDialog extends JDialog implements SimulatorObserver {
	private DefaultComboBoxModel<String> _lawsModel;
	private DefaultComboBoxModel<String> _groupsModel;
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _forceLawsInfo;
	private String[] _headers = { "Key", "Value", "Description" };
	private Object _status;
// En caso de ser necesario, añadir los atributos aquí…

	ForceLawsDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		_ctrl = ctrl;
		_forceLawsInfo = _ctrl.getForceLawsInfo();
		initGUI();
		_ctrl.addObserver(this);
	}

	private void initGUI() {
		setTitle("Force Laws Selection");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

// help
		JLabel help = new JLabel(
				"<html><p>Select a force law and provide values for the parametes in the <b>Value column</b> (default values are used for parametes with no value).</p></html>");
		help.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(help);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

// Crea un JTable que use _dataTableModel, y anadirla al panel
		_dataTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
		};
		_dataTableModel.setColumnIdentifiers(_headers);
		JTable table = new JTable(_dataTableModel);

		JScrollPane tabelScroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(tabelScroll);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel inf = new JPanel();
		inf.setAlignmentX(CENTER_ALIGNMENT);

		// Combobox leyes
		_lawsModel = new DefaultComboBoxModel<>();
		for (JSONObject fl : _forceLawsInfo) {
			_lawsModel.addElement(fl.getString("desc"));
		}
		JComboBox<String> leyes = new JComboBox<String>(_lawsModel);
		inf.add(leyes);
		
		int _selectedLawsIndex = leyes.getSelectedIndex();
		leyes.addActionListener((e) -> initLeyes(_dataTableModel, leyes.getSelectedIndex()));

		// ComboBox grupos
		_groupsModel = new DefaultComboBoxModel<>();
		JComboBox<String> groups = new JComboBox<String>(_groupsModel);
		inf.add(groups);

		mainPanel.add(inf);

		// Crear los botones OK y Cancel y añadirlos al panel

		JPanel out = new JPanel();
		out.setAlignmentX(CENTER_ALIGNMENT);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ForceLawsDialog.this.setVisible(false);
			}
		});
		out.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JSONObject fl = new JSONObject("{\"type\":" + _forceLawsInfo.get(_selectedLawsIndex).getString("type")
						+ ", \"data\":" + getData() + "}");
				String gid = _groupsModel.getSelectedItem().toString();
				try {
					_ctrl.setForceLaws(gid, fl);
				} catch (Exception ex) {
					Utils.showErrorMsg("Something went wrong: " + ex.getLocalizedMessage());
				}
				
				ForceLawsDialog.this.setVisible(false);

			}
		});
		out.add(okButton);
		mainPanel.add(out);

		setPreferredSize(new Dimension(700, 400));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public String getData() {
		StringBuilder s = new StringBuilder();
		s.append('{');
		for (int i = 0; i < _dataTableModel.getRowCount(); i++) {
			String k = _dataTableModel.getValueAt(i, 0).toString();
			String v = _dataTableModel.getValueAt(i, 1).toString();
			if (!v.isEmpty()) {
				s.append('"');
				s.append(k);
				s.append('"');
				s.append(':');
				s.append(v);
				s.append(',');
			}
		}

		if (s.length() > 1)
			s.deleteCharAt(s.length() - 1);
		s.append('}');

		return s.toString();
	}
	

	public void open() {
		if (_groupsModel.getSize() == 0)
			return;
		pack();
		setVisible(true);
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		_groupsModel = new DefaultComboBoxModel<>();
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		for (String k : groups.keySet())
			_groupsModel.addElement(k);
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		_groupsModel.addElement(g.getId());

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
		_lawsModel.addElement(g.getForceLawsInfo());
	}

	public void initLeyes(DefaultTableModel model, int index) {

		JSONObject info = _forceLawsInfo.get(index);
		JSONObject data = info.getJSONObject("data");
		_dataTableModel.setNumRows(data.keySet().size());
		int i = 0;
		for (String key : data.keySet()) {
			_dataTableModel.setValueAt(key, i, 0);
			_dataTableModel.setValueAt("", i, 1);
			_dataTableModel.setValueAt(data.getString(key), i, 2);
			i++;
		}
	}

}
