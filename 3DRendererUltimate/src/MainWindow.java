import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


public class MainWindow extends JFrame {

	private static final long serialVersionUID = -3880026026104218593L;
	public MainWindow() {
		setResizable(false);
		setTitle("3D Renderer");
		setSize(600, 420);
		setLocationRelativeTo(null);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JSlider headingSlider = new JSlider(0, 360, 180);		
		headingSlider.setBounds(199, 354, 360, 26);
		getContentPane().add(headingSlider);
		
		JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
		pitchSlider.setBounds(559, 11, 20, 343);
		getContentPane().add(pitchSlider);
		ArrayList<Triangle> trg = new ArrayList<Triangle>();
		JPanel renderPanel = new JPanel() {

			private static final long serialVersionUID = 3121362272884517050L;
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, getWidth(), getHeight());
				double heading = Math.toRadians(headingSlider.getValue());
				double pitch = Math.toRadians(pitchSlider.getValue());
				Matrix3 xz = new Matrix3(new double[][] {{Math.cos(heading), 0, -Math.sin(heading)}, {0, 1, 0}, {Math.sin(heading), 0, Math.cos(heading)}});
				Matrix3 yz = new Matrix3(new double[][] {{1, 0, 0}, {0, Math.cos(pitch), Math.sin(pitch)}, {0, -Math.sin(pitch), Math.cos(pitch)}});				
				Matrix3 tr = xz.multiply(yz);
				BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				double[] zBuffer = new double[img.getWidth() * img.getHeight()];
				for (int q = 0; q < zBuffer.length; q++) {
				    zBuffer[q] = Double.NEGATIVE_INFINITY;
				}
				g2.translate(getWidth()/2, getHeight()/2);				
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
				    g2.drawImage(img, 0 - getWidth()/2, 0 - getHeight()/2, null);
				}
			}
		};
		renderPanel.setBounds(199, 11, 360, 343);
		getContentPane().add(renderPanel);
		
		JLabel cordA = new JLabel("A:");
		cordA.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cordA.setBounds(10, 11, 20, 20);
		getContentPane().add(cordA);
		
		JLabel cordB = new JLabel("B:");
		cordB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cordB.setBounds(10, 42, 20, 20);
		getContentPane().add(cordB);
		
		JLabel cordC = new JLabel("C:");
		cordC.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cordC.setBounds(10, 73, 20, 20);
		getContentPane().add(cordC);
		
		JLabel cordD = new JLabel("D:");
		cordD.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cordD.setBounds(10, 104, 20, 20);
		getContentPane().add(cordD);
		
		JTextField Ax = new JTextField();
		Ax.setColumns(10);
		Ax.setBounds(40, 11, 30, 20);
		getContentPane().add(Ax);
		
		JTextField Ay = new JTextField();
		Ay.setColumns(10);
		Ay.setBounds(75, 11, 30, 20);
		getContentPane().add(Ay);
		
		JTextField Az = new JTextField();
		Az.setColumns(10);
		Az.setBounds(110, 11, 30, 20);
		getContentPane().add(Az);
		
		JTextField Bx = new JTextField();
		Bx.setColumns(10);
		Bx.setBounds(40, 42, 30, 20);
		getContentPane().add(Bx);
		
		JTextField By = new JTextField();
		By.setColumns(10);
		By.setBounds(75, 42, 30, 20);
		getContentPane().add(By);
		
		JTextField Bz = new JTextField();
		Bz.setColumns(10);
		Bz.setBounds(110, 42, 30, 20);
		getContentPane().add(Bz);
		
		JTextField Cx = new JTextField();
		Cx.setColumns(10);
		Cx.setBounds(40, 73, 30, 20);
		getContentPane().add(Cx);
		
		JTextField Cy = new JTextField();
		Cy.setColumns(10);
		Cy.setBounds(75, 73, 30, 20);
		getContentPane().add(Cy);
		
		JTextField Cz = new JTextField();
		Cz.setColumns(10);
		Cz.setBounds(110, 73, 30, 20);
		getContentPane().add(Cz);
		
		JTextField Dx = new JTextField();
		Dx.setColumns(10);
		Dx.setBounds(40, 104, 30, 20);
		getContentPane().add(Dx);
		
		JTextField Dy = new JTextField();
		Dy.setColumns(10);
		Dy.setBounds(75, 104, 30, 20);
		getContentPane().add(Dy);
		
		JTextField Dz = new JTextField();
		Dz.setColumns(10);
		Dz.setBounds(110, 104, 30, 20);
		getContentPane().add(Dz);
		
		JButton rendbutton = new JButton("Render");
		rendbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vertex A = new Vertex(Double.parseDouble(Ax.getText()), Double.parseDouble(Ay.getText()), Double.parseDouble(Az.getText()));
				Vertex B = new Vertex(Double.parseDouble(Bx.getText()), Double.parseDouble(By.getText()), Double.parseDouble(Bz.getText()));
				Vertex C = new Vertex(Double.parseDouble(Cx.getText()), Double.parseDouble(Cy.getText()), Double.parseDouble(Cz.getText()));
				Vertex D = new Vertex(Double.parseDouble(Dx.getText()), Double.parseDouble(Dy.getText()), Double.parseDouble(Dz.getText()));
				trg.add(new Triangle(A, B, C, Color.WHITE));
				trg.add(new Triangle(A, C, D, Color.RED));
				trg.add(new Triangle(A, B, D, Color.GREEN));
				trg.add(new Triangle(B, C, D, Color.BLUE));	
				renderPanel.repaint();
			}
		});
		rendbutton.setBounds(51, 135, 89, 23);
		getContentPane().add(rendbutton);
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
	double[][] values;
	Matrix3(double[][] values) {
		this.values = values;
	}
	Matrix3 multiply(Matrix3 other) {
		double[][] result = new double[3][3];
		for(int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				for (int i = 0; i < 3; i++) {
					result[row][col] += this.values[row][i] * other.values[i][col];
				}
			}
		}
		return new Matrix3(result);
	}
	Vertex transform(Vertex in) {
		return new Vertex(this.values[0][0] * in.x + this.values[0][1] * in.y + this.values[0][2] * in.z, this.values[1][0] * in.x + this.values[1][1] * in.y + this.values[1][2] * in.z, this.values[2][0] * in.x + this.values[2][1] * in.y + this.values[2][2] * in.z);
		}
	}
}

