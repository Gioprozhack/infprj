import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PRWindow extends JFrame {
	private static final long serialVersionUID = -7358671128081952153L;
	public PRWindow() {
		setTitle("Test");
		setSize(1600, 900);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = -3723787926154172133L;
			public void  paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(new Color(0, 127, 255));
				g2.fillRect(0, 0, getWidth(), getHeight());				
				int y[] = new int[getWidth()];
				for (int x = 0; x < getWidth(); x++) {
					y[x] = (int) Math.round(Math.sin(x/(Math.random() * 15 + 5)) * (Math.random() * 5 + 5) + Math.sin(x/(Math.random() * 5 + 5)) * 5) + getHeight() * 3 / 4;
				}
				for (int x = 0; x < getWidth() - 4; x += 4) {
					int ymax = Math.max(y[x], Math.max(y[x+1], Math.max(y[x+2], y[x+3])));
					int ymin = Math.min(y[x], Math.min(y[x+1], Math.min(y[x+2], y[x+3])));
					g2.setColor(new Color(0, 190, 0));
					g2.fillRect(x, (ymin + ymax)/2 - 19, 4, 20);
					g2.setColor(new Color(127, 63, 63));
					g2.fillRect(x, (ymin + ymax)/2, 4, getHeight() - (ymin + ymax)/2);
				}
			}
		};
		panel.setBounds(0, 0, 1594, 868);
		getContentPane().add(panel);
	}
}
