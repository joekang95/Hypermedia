import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;


public class LayerPresent extends JPanel implements MouseListener {
	
	private int alpha = 127;
    private Color color;
	Graphics2D g2;
	Rectangle2D squareIn, squareOut;
	public double x1 = 0, y1 = 0;
	public double sizeX = 60, sizeY = 60, pointSize = 8;
	private double x2 = x1 + sizeX, y2 = y1 + sizeY;
    
	public LayerPresent(int x1, int y1, int width, int height, int sizeX, int sizeY) {
    	
    	this.sizeX = sizeX;
    	this.sizeY = sizeY;
		this.x1 = x1;
		this.y1 = y1;

		x2 = x1 + sizeX;
		y2 = y1 + sizeY;
		
//		squareIn = new Rectangle2D.Double(x1 + 2, y1 + 2, sizeX - 2, sizeY - 2);
//		squareOut = new Rectangle2D.Double(x1, y1, sizeX, sizeY);
		squareIn = new Rectangle2D.Double(2, 2, sizeX - 4, sizeY - 4);
		squareOut = new Rectangle2D.Double(0, 0, sizeX, sizeY);
        this.setOpaque(false);
        this.setBounds(x1, y1, sizeX, sizeY);
		setFocusable(true);
		addMouseListener(this);
		this.requestFocus();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.WHITE);
		g2.draw(squareOut);
    	color = new Color(255, 0, 0, alpha);
		g2.setColor(color);
		g2.fill(squareIn);
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("LL");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
