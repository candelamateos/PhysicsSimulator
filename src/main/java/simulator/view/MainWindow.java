package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	private Controller _ctrl;
	private WindowListener windowListener;

	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);

// TODO crear ControlPanel y a�adirlo en PAGE_START de mainPanel
		JPanel controlPanel = new ControlPanel(_ctrl);
		mainPanel.add(controlPanel, BorderLayout.PAGE_START);
// TODO crear StatusBar y a�adirlo en PAGE_END de mainPanel
		JPanel statusBar = new StatusBar(_ctrl);
		mainPanel.add(statusBar, BorderLayout.PAGE_END);
// Definici�n del panel de tablas (usa un BoxLayout vertical)

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);

// TODO crear la tabla de grupos y a�adirla a contentPanel.
		
		InfoTable groupTable = new InfoTable("Groups", new GroupsTableModel(_ctrl));
		contentPanel.add(groupTable);
		
// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tama�o
		groupTable.setPreferredSize(new Dimension(500, 250));
		
// TODO crear la tabla de cuerpos y a�adirla a contentPanel.
		InfoTable bodiesTable = new InfoTable("Bodies", new BodiesTableModel(_ctrl));
		
// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tama�o
		bodiesTable.setPreferredSize(new Dimension(500, 250));
		
		contentPanel.add(bodiesTable);

		
// TODO llama a Utils.quit(MainWindow.this) en el m�todo windowClosing
		
		addWindowListener(new WindowListener () {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				Utils.quit(MainWindow.this);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}
}