import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LayerPanel extends JPanel implements MouseListener, MouseMotionListener {

    private int alpha = 127;
    private Color color;
	private boolean dragging = false, dragPoint = false;
	Graphics2D g2;
	Rectangle2D square;
	double x1 = 0, y1 = 0;
	double sizeX = 60, sizeY = 60, pointSize = 10;
	double x2 = x1 + sizeX, y2 = y1 + sizeY;
	double offsetX, offsetY;
    private int pos = -1;
    private Rectangle2D[] points = {
    		new Rectangle2D.Double(x1 - pointSize/2, y1 - pointSize/2, pointSize, pointSize), 
    		new Rectangle2D.Double(x2 - pointSize/2, y2 - pointSize/2, pointSize, pointSize)
    		};

    public LayerPanel(int x, int y, int width, int height) {

		x1 = x;
		y1 = y;
		x2 = x1 + sizeX;
		y2 = y1 + sizeY;
		
		square = new Rectangle2D.Double(x1, y1, sizeX, sizeY);
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
		g2.setColor(Color.WHITE);
		g2.fill(points[0]);
		g2.fill(points[1]);
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		if(dragging) {
		    double mx = e.getX();
		    double my = e.getY();
		    x1 = mx - offsetX;
		    y1 = my - offsetY;
		    x2 = x1 + sizeX;
		    y2 = y1 + sizeY;
			color = new Color(255, 0, 0, alpha);
		    points[0] = new Rectangle2D.Double(x1 - pointSize/2, y1 - pointSize/2, pointSize, pointSize);
		    points[1] = new Rectangle2D.Double(x2 - pointSize/2, y2 - pointSize/2, pointSize, pointSize);
	        square.setRect(points[0].getCenterX(), points[0].getCenterY(),
	                Math.abs(points[1].getCenterX() - points[0].getCenterX()),
	                Math.abs(points[1].getCenterY() - points[0].getCenterY()));
		    repaint(); 	
	    }
		else if(dragPoint) {
		    if (pos == -1) {
                return;
            }
            points[pos].setRect(e.getPoint().x, e.getPoint().y, points[pos].getWidth(), points[pos].getHeight());	
	        square.setRect(points[0].getCenterX(), points[0].getCenterY(),
	                Math.abs(points[1].getCenterX() - points[0].getCenterX()),
	                Math.abs(points[1].getCenterY() - points[0].getCenterY()));
	        sizeX = Math.abs(points[1].getCenterX() - points[0].getCenterX());
	        sizeY = Math.abs(points[1].getCenterY() - points[0].getCenterY());
            repaint(); 	
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		double mx = e.getX();
		double my = e.getY();
		Point p = e.getPoint();
		for (int i = 0; i < points.length; i++) {
            if (points[i].contains(p)) {
        		dragPoint = true;
                pos = i;
                return;
            }
        }
		if(square.contains(p) && !dragPoint){
		    dragging = true;
		    offsetX  = mx - x1;
		    offsetY = my - y1;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		pos = -1;
		dragPoint = false;
		dragging = false;
		color = new Color(130, 214, 255, alpha);
	    repaint(); 
	}
	@Override
	public void mouseClicked(MouseEvent e) {

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
