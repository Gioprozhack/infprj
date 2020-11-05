import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PRWindow extends JFrame {
	private static final long serialVersionUID = -7358671128081952153L;
	public PRWindow() {
		setTitle("Test");
		setSize(1600, 900);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = -3723787926154172133L;
			public void  paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.setColor(Color.WHITE);
				g2.translate(0, getHeight() * 3 / 4);
				Path2D path = new Path2D.Double();	
				path.moveTo(0, 0);
				for (int x = 0; x < getWidth(); x ++) {
					path.lineTo(x, Math.sin(x/200) * 25);
					path.moveTo(x, Math.sin(x/200) * 25);
				}			
				path.closePath();	
				g2.draw(path);				
			}
		};
		panel.setBounds(0, 0, 1594, 867);
		getContentPane().add(panel);
	}
}
