import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

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
				double heading = Math.toRadians(headingSlider.getValue());
				Matrix3 headingTransform = new Matrix3(new double[] {Math.cos(heading), 0, -Math.sin(heading), 0, 1, 0, Math.sin(heading), 0, Math.cos(heading)});				 
				double pitch = Math.toRadians(pitchSlider.getValue()); 
				Matrix3 pitchTransform = new Matrix3(new double[] {1, 0, 0, 0, Math.cos(pitch), Math.sin(pitch), 0, -Math.sin(pitch), Math.cos(pitch)}); 
				Matrix3 tr = headingTransform.multiply(pitchTransform);
				g2.translate(getWidth()/2, getHeight()/2);
				g2.setColor(Color.WHITE);
				BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				double[] zBuffer = new double[img.getWidth() * img.getHeight()];
				for (int q = 0; q < zBuffer.length; q++) {
				    zBuffer[q] = Double.NEGATIVE_INFINITY;
				}
				for(Triangle t: trg) {
					Vertex v1 = tr.transform(t.v1);
					Vertex v2 = tr.transform(t.v2);
					Vertex v3 = tr.transform(t.v3);
					v1.x += getWidth() / 2;
				    v1.y += getHeight() / 2;
				    v2.x += getWidth() / 2;
				    v2.y += getHeight() / 2;
				    v3.x += getWidth() / 2;
				    v3.y += getHeight() / 2;
				    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
				    int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
				    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
				    int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
				    double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
				    for (int y = minY; y <= maxY; y++) {
				        for (int x = minX; x <= maxX; x++) {
				            double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
				            double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
				            double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
				            double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
				            int zIndex = y * img.getWidth() + x;
				            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
				            	if (zBuffer[zIndex] < depth) {
				                    img.setRGB(x, y, t.color.getRGB());
				                    zBuffer[zIndex] = depth;
				                }
				            }				           
				        }
				    }					
				}
				g2.drawImage(img, 0 - getWidth()/2, 0 - getHeight()/2, null);
			}
		};
		getContentPane().add(renderPanel, BorderLayout.CENTER);
		headingSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				renderPanel.repaint();
			}
		});
		pitchSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				renderPanel.repaint();
			}
		});
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
class Matrix3 {
	double[] values;
	Matrix3(double[] values) {
		this.values = values;
	}
	Matrix3 multiply(Matrix3 other) {
		double[] result = new double[9];
		for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                for (int i = 0; i < 3; i++) {
                    result[row * 3 + col] += this.values[row * 3 + i] * other.values[i * 3 + col];
                }
            }
        }
		return new Matrix3(result);
	}
	 Vertex transform(Vertex in) {
	        return new Vertex(in.x * values[0] + in.y * values[3] + in.z * values[6], in.x * values[1] + in.y * values[4] + in.z * values[7], in.x * values[2] + in.y * values[5] + in.z * values[8]);
	    }
}
