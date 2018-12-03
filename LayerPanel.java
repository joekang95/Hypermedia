import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class LayerPanel extends JPanel implements MouseListener, MouseMotionListener {

    private int alpha = 127;
    private Color color;
	private boolean dragging = false;
	Graphics2D g2;
	Rectangle2D square;
	double x1 = 0, y1 = 0, x2, y2, size;
	double offsetX, offsetY;

    public LayerPanel(int width, int height) {

		size = 40.0;
		x2 = x1 + size;
		y2 = y1 + size;
		
		square = new Rectangle2D.Double(x1, y1, size, size);
        this.setOpaque(false);
        this.setBounds(0, 5, width, height);
		color = new Color(255, 0, 0, alpha);
	
		setFocusable(true);
		addMouseMotionListener(this);
		addMouseListener(this);
		this.requestFocus();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.fill(square);
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragging) {
		    double mx = e.getX();
		    double my = e.getY();
		    x1 = mx - offsetX;
		    y1 = my - offsetY;
		    x2 = x1 + size;
		    y2 = y1 + size;
			color = new Color(255, 0, 0, alpha);
		    square = new Rectangle2D.Double(x1, y1, size, size);
		    repaint(); 
	    }
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		double mx = e.getX();
		double my = e.getY();
		if (mx > x1 && mx < x2 && my > y1 && my < y2) {
		    dragging = true;
		    offsetX  = mx - x1;
		    offsetY = my - y1;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		dragging = false;
		color = new Color(130, 214, 255, alpha);
	    repaint(); 
	}
	@Override
	public void mouseClicked(MouseEvent e) {
//		double mx = e.getX();
//		double my = e.getY();
//		if (mx > x1 && mx < x2 && my > y1 && my < y2) {
//			color = new Color(255, 0, 0, alpha);
//		    repaint(); 
//		}
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}
	
}
