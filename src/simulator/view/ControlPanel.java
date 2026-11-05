package simulator.view;

import simulator.control.Controller;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {
	private Controller _ctrl;
	private JToolBar _toolaBar;
	private JFileChooser _fc;
	private JSpinner _n;
	private boolean _stopped = true; // usado para botones run/stop
	private JButton _quitButton;
	private JButton _loadButton;
	private JButton _physicsButton;
	private JButton _runButton;
	private JButton _stopButton;
	private JButton _viewerButton;
	private ForceLawsDialog _fldialog;
	private JFrame parent;
	private JTextField _dt;

	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
		_ctrl.addObserver(this);
		parent = new JFrame();
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {

	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {

	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {

	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {

	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {

	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		String delta = Double.toString(dt);
		this._dt = new JTextField(delta);
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {

	}

	private void initGUI() {
		setLayout(new BorderLayout());
		_toolaBar = new JToolBar();
		add(_toolaBar, BorderLayout.PAGE_START);

// Boton load

// Crea el file chooser
		_fc = new JFileChooser();

		_toolaBar.addSeparator();
		_loadButton = new JButton();
		_loadButton.setToolTipText("Load");
		_loadButton.setIcon(loadImage("resources/icons/open.png"));
		_loadButton.addActionListener((e) -> openFile(this));
		_toolaBar.add(_loadButton);
		_toolaBar.addSeparator();

// Boton physics

		_toolaBar.addSeparator();
		_physicsButton = new JButton();
		_physicsButton.setToolTipText("Physics");
		_physicsButton.setIcon(loadImage("resources/icons/physics.png"));
		_physicsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_fldialog == null)
					_fldialog = new ForceLawsDialog(Utils.getWindow(ControlPanel.this), _ctrl);
				_fldialog.open();
			}
		});
		_toolaBar.add(_physicsButton);

// Boton viewer
		_viewerButton = new JButton();
		_viewerButton.setToolTipText("viewer");
		_viewerButton.setIcon(loadImage("resources/icons/viewer.png"));
		_viewerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new ViewerWindow((JFrame) SwingUtilities.getWindowAncestor(ControlPanel.this), _ctrl);
				} catch (Exception except) {
					Utils.showErrorMsg("No se pudo abrir la ventana");
				}
			}
		});
		_toolaBar.add(_viewerButton);

// Boton run
		_n = new JSpinner(new SpinnerNumberModel(1000, 1, 10000, 100));
		int n = (int) _n.getValue();
		_dt = new JTextField("1000");
		String sdt = _dt.getText();
		double dt = Double.parseDouble(sdt);

		_toolaBar.addSeparator();
		_runButton = new JButton();
		_runButton.setToolTipText("run");
		_runButton.setIcon(loadImage("resources/icons/run.png"));
		_runButton.addActionListener((e) -> run(n, dt));
		_toolaBar.add(_runButton);

// Boton stop
		_stopButton = new JButton();
		_stopButton.setToolTipText("stop");
		_stopButton.setIcon(loadImage("resources/icons/stop.png"));
		_stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					_stopped = true;
				} catch (Exception except) {
					Utils.showErrorMsg("Cause you are unstoppable");
				}
			}
		});
		_toolaBar.add(_stopButton);

//Barras
		JLabel label = new JLabel("Steps: ");
		JLabel label2 = new JLabel("Delta-Time: ");
		_toolaBar.add(label);
		_toolaBar.add(_n);
		_toolaBar.addSeparator();
		_toolaBar.add(label2);
		_toolaBar.add(_dt);
		_toolaBar.addSeparator();

// Boton quit
		_toolaBar.add(Box.createGlue()); // alinea el botón
		_toolaBar.addSeparator();
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit");
		_quitButton.setIcon(loadImage("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> Utils.quit(this));
		_toolaBar.add(_quitButton);

	}

	public void openFile(ControlPanel controlPanel) {
		_fc.showOpenDialog(Utils.getWindow(this));
		try {
			File f = _fc.getSelectedFile();
			_ctrl.reset();
			_ctrl.loadData(new FileInputStream(f));
		} catch (Exception exception) {
			Utils.showErrorMsg("No se ha podido cargar");
		}
	}

	private void run(int n, double dt) {
		_stopped = false;
		_quitButton.setEnabled(false);
		_loadButton.setEnabled(false);
		_physicsButton.setEnabled(false);
		_runButton.setEnabled(false);
		_viewerButton.setEnabled(false);

		try {
			_ctrl.setDeltaTime(dt);
			
		}catch (Exception exception){
			Utils.showErrorMsg("No se pudo realizar la operación");
		}

		run_sim(n);
	}

	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				Utils.showErrorMsg("No se puede ejecutar");
				_quitButton.setEnabled(true);
				_loadButton.setEnabled(true);
				_physicsButton.setEnabled(true);
				_runButton.setEnabled(true);
				_viewerButton.setEnabled(true);

				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater(() -> run_sim(n - 1));
		} else {
			_quitButton.setEnabled(true);
			_loadButton.setEnabled(true);
			_physicsButton.setEnabled(true);
			_runButton.setEnabled(true);
			_viewerButton.setEnabled(true);

			_stopped = true;
		}
	}

	protected ImageIcon loadImage(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(path));
	}
}
