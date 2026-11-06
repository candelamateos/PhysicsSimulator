package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;

import simulator.misc.Vector2D;
import simulator.model.BodiesGroup;
import simulator.model.Body;

@SuppressWarnings("serial")
class Viewer extends SimulationViewer {

	private static final int _WIDTH = 500;
	private static final int _HEIGHT = 500;

	private int _centerX;
	private int _centerY;
	private int _originX = 0;
	private int _originY = 0;
	private double _scale = 1.0;

	private boolean _showHelp = true;
	private boolean _showVectors = true;

	private List<Body> _bodies;
	private List<BodiesGroup> _groups;

	private ColorsGenerator _colorGen;
	private Map<String, Color> _gColor;

	private int _selectedGroupIdx = -1;
	private String _selectedGroup = null;

	// Lista de estrellas para el fondo espacial
	private List<Star> _stars;

	private static class Star {
		int x, y, size;
		Color color;

		Star(int x, int y, int size, Color color) {
			this.x = x;
			this.y = y;
			this.size = size;
			this.color = color;
		}
	}

	Viewer() {
		initGUI();
		initStarfield();
	}

	private void initStarfield() {
		_stars = new ArrayList<>();
		Random rand = new Random(42);
		int numStars = 250;

		for (int i = 0; i < numStars; i++) {
			int x = rand.nextInt(2500);
			int y = rand.nextInt(2500);
			int size = rand.nextInt(3) + 1;
			int alpha = rand.nextInt(155) + 100;

			Color[] starColors = {
				new Color(255, 255, 255, alpha),
				new Color(200, 220, 255, alpha),
				new Color(255, 240, 200, alpha)
			};
			_stars.add(new Star(x, y, size, starColors[rand.nextInt(starColors.length)]));
		}
	}

	private void initGUI() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		_colorGen = new ColorsGenerator();
		_gColor = new HashMap<>();

		_bodies = new ArrayList<>();
		_groups = new ArrayList<>();

		setMinimumSize(new Dimension(_WIDTH, _HEIGHT));
		setPreferredSize(new Dimension(_WIDTH, _HEIGHT));

		addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '-':
					_scale = _scale * 1.1;
					repaint();
					break;
				case '+':
					_scale = Math.max(1000.0, _scale / 1.1);
					repaint();
					break;
				case '=':
					autoScale();
					repaint();
					break;
				case 'k':
					_originX = 0;
					_originY = 0;
					repaint();
					break;
				case 'h':
					_showHelp = !_showHelp;
					repaint();
					break;
				case 'v':
					_showVectors = !_showVectors;
					repaint();
					break;
				case 'j':
					_originX -= 10;
					repaint();
					break;
				case 'l':
					_originX += 10;
					repaint();
					break;
				case 'i':
					_originY -= 10;
					repaint();
					break;
				case 'm':
					_originY += 10;
					repaint();
					break;
				case 'g':
					if (_groups.size() > 0) {
						if (_selectedGroupIdx == _groups.size() - 1) {
							_selectedGroupIdx = -1;
							_selectedGroup = null;
						} else {
							_selectedGroupIdx++;
							_selectedGroup = _groups.get(_selectedGroupIdx).getId();
						}
						autoScale();
					}
					repaint();
					break;
				default:
				}
			}
		});

		addMouseListener(new MouseListener() {
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) { requestFocus(); }
			@Override public void mouseClicked(MouseEvent e) {}
		});
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// 1. Fondo negro espacial
		gr.setColor(new Color(8, 8, 16));
		gr.fillRect(0, 0, getWidth(), getHeight());

		// 2. Dibujar estrellas
		if (_stars != null) {
			for (Star star : _stars) {
				gr.setColor(star.color);
				gr.fillOval(star.x % getWidth(), star.y % getHeight(), star.size, star.size);
			}
		}

		_centerX = getWidth() / 2 - _originX;
		_centerY = getHeight() / 2 - _originY;

		// Cruz central neón
		gr.setColor(new Color(255, 60, 60, 200));
		gr.drawLine(_centerX - 5, _centerY, _centerX + 5, _centerY);
		gr.drawLine(_centerX, _centerY - 5, _centerX, _centerY + 5);

		// Dibujar cuerpos
		drawBodies(gr);

		if (_showHelp) {
			showHelp(gr);
		}
	}

	private void showHelp(Graphics2D g) {
		g.setColor(new Color(220, 220, 220));
		g.drawString("h: toggle help, v: toggle vectors, +: zoom-in, -: zoom-out, =: fit", 10, 15);
		g.drawString("g: show next group", 10, 35);
		g.drawString("l: move right, j: move left, i: move up, m: move down: k: reset", 10, 55);
		g.drawString("Scaling ratio:  " + _scale, 10, 75);
		String s = _selectedGroup == null ? "all" : _selectedGroup;
		g.setColor(new Color(100, 190, 255));
		g.drawString("Selected Group: " + s, 10, 95);
	}

	private void drawBodies(Graphics2D g) {
		// Copia local para evitar modificacion concurrente durante el redibujado
		List<Body> bodiesCopy = new ArrayList<>(_bodies);

		for (Body b : bodiesCopy) {
			if (b != null && isVisible(b)) {
				Vector2D p = b.getPosition();
				if (p == null) continue;

				int x = _centerX + (int) (p.getX() / _scale);
				int y = _centerY - (int) (p.getY() / _scale);

				if (_showVectors) {
					
					if (b.getVelocity() != null && b.getVelocity().magnitude() > 0) {
						Vector2D v = b.getVelocity().direction().scale(20);
						int x2 = x + (int) v.getX();
						int y2 = y - (int) v.getY();
						drawLineWithArrow(g, x, y, x2, y2, 3, 3, new Color(50, 255, 120), new Color(50, 255, 120));
					}

					if (b.getForce() != null && b.getForce().magnitude() > 0) {
						Vector2D f = b.getForce().direction().scale(20);
						int x3 = x + (int) f.getX();
						int y3 = y - (int) f.getY();
						drawLineWithArrow(g, x, y, x3, y3, 3, 3, new Color(255, 70, 70), new Color(255, 70, 70));
					}
				}

				
				Color bodyColor = getBodyColor(b);
				g.setColor(bodyColor);
				g.fillOval(x - 5, y - 5, 11, 11);

				g.setColor(bodyColor.brighter());
				g.drawOval(x - 5, y - 5, 11, 11);

				if (b.getId() != null) {
					g.setColor(Color.WHITE);
					g.drawString(b.getId(), x + 7, y + 4);
				}
			}
		}
	}

	private Color getBodyColor(Body b) {
		if (b == null || b.getId() == null) {
			return new Color(0, 220, 255);
		}

		String id = b.getId().toLowerCase();

		if (id.contains("sun") || id.contains("sol") || id.contains("a3")) return new Color(255, 215, 0);
		if (id.contains("mercury") || id.contains("mercurio")) return new Color(180, 180, 180);
		if (id.contains("venus")) return new Color(230, 190, 130);
		if (id.contains("earth") || id.contains("tierra") || id.contains("a1")) return new Color(60, 140, 240);
		if (id.contains("mars") || id.contains("marte")) return new Color(230, 70, 50);
		if (id.contains("jupiter") || id.contains("a2")) return new Color(210, 130, 60);
		if (id.contains("saturn") || id.contains("saturno")) return new Color(220, 200, 120);
		if (id.contains("uranus") || id.contains("urano")) return new Color(100, 220, 220);
		if (id.contains("neptune") || id.contains("neptuno")) return new Color(40, 90, 220);

		if (b.getgId() != null && _gColor.containsKey(b.getgId())) {
			Color gCol = _gColor.get(b.getgId());
			if (gCol != null) return gCol;
		}

		return new Color(0, 220, 255);
	}

	private boolean isVisible(Body b) {
		return b != null && (_selectedGroup == null || _selectedGroup.equals(b.getgId()));
	}

	private void autoScale() {
		double max = 1.0;

		for (Body b : _bodies) {
			if (b != null && b.getPosition() != null) {
				Vector2D p = b.getPosition();
				max = Math.max(max, Math.abs(p.getX()));
				max = Math.max(max, Math.abs(p.getY()));
			}
		}

		double size = Math.max(1.0, Math.min(getWidth(), getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}

	@Override
	public void addGroup(BodiesGroup g) {
		if (g != null) {
			_groups.add(g);
			for (Body b : g) {
				if (b != null) _bodies.add(b);
			}
			_gColor.put(g.getId(), _colorGen.nextColor());
			autoScale();
			update();
		}
	}

	@Override
	public void addBody(Body b) {
		if (b != null) {
			_bodies.add(b);
			autoScale();
			update();
		}
	}

	@Override
	public void reset() {
		_groups.clear();
		_bodies.clear();
		_gColor.clear();
		_colorGen.reset();
		_selectedGroupIdx = -1;
		_selectedGroup = null;
		update();
	}

	@Override
	void update() {
		repaint();
	}

	private void drawLineWithArrow(Graphics g, int x1, int y1, int x2, int y2, int w, int h, Color lineColor, Color arrowColor) {
		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		if (D == 0 || Double.isNaN(D)) return;

		double xm = D - w, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;

		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;

		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;

		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };

		g.setColor(lineColor);
		g.drawLine(x1, y1, x2, y2);
		g.setColor(arrowColor);
		g.fillPolygon(xpoints, ypoints, 3);
	}
}
