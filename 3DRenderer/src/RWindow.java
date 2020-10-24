import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class RWindow extends JFrame {
	
	private static final long serialVersionUID = 1954202317255135998L;

	public RWindow() {
		setSize(400, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSlider headingSlider = new JSlider(0, 360, 180);
		getContentPane().add(headingSlider, BorderLayout.SOUTH);
		
		JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
		getContentPane().add(pitchSlider, BorderLayout.EAST);
		
		JPanel renderPanel = new JPanel() {
			
			private static final long serialVersionUID = -4274033785321941459L;

			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, getWidth(), getHeight());	
				ArrayList<Triangle> trg = new ArrayList<Triangle>();
				trg.add(new Triangle(new Vertex(100, 100, 100), new Vertex(-100, -100, 100), new Vertex(-100, 100, -100), Color.WHITE));
				trg.add(new Triangle(new Vertex(100, 100, 100), new Vertex(-100, -100, 100), new Vertex(100, -100, -100), Color.RED));
				trg.add(new Triangle(new Vertex(-100, 100, -100), new Vertex(100, -100, -100), new Vertex(100, 100, 100), Color.GREEN));
				trg.add(new Triangle(new Vertex(-100, 100, -100), new Vertex(100, -100, -100), new Vertex(-100, -100, 100), Color.BLUE));
			}
		};
		getContentPane().add(renderPanel, BorderLayout.CENTER);
	}
}
class Vertex {
	double x, y, z;	
	Vertex(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
class Triangle {
	Vertex v1, v2, v3;
	Color color;
	Triangle(Vertex v1, Vertex v2, Vertex v3, Color color) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = color;
	}
}
	

