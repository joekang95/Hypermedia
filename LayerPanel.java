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
	public double x1 = 0, y1 = 0;
	public double sizeX = 60, sizeY = 60, pointSize = 10;
	private double x2 = x1 + sizeX, y2 = y1 + sizeY;
	private double offsetX, offsetY;
    private int pos = -1;
    private Rectangle2D[] points = new Rectangle2D[2];
    
    public LayerPanel(int x1, int y1, int width, int height) {

		this.x1 = x1;
		this.y1 = y1;
		x2 = x1 + sizeX;
		y2 = y1 + sizeY;
		
		points[0] = new Rectangle2D.Double(x1 - pointSize/2, y1 - pointSize/2, pointSize, pointSize);
		points[1] = new Rectangle2D.Double(x2 - pointSize/2, y2 - pointSize/2, pointSize, pointSize);
		square = new Rectangle2D.Double(x1, y1, sizeX, sizeY);
        this.setOpaque(false);
        this.setBounds(0, 5, width, height);
		color = new Color(255, 0, 0, alpha);
		setFocusable(true);
		addMouseMotionListener(this);
		addMouseListener(this);
		this.requestFocus();
    }

    public LayerPanel(int x1, int y1, int width, int height, int sizeX, int sizeY) {
    	
    	this.sizeX = sizeX;
    	this.sizeY = sizeY;
		this.x1 = x1;
		this.y1 = y1;
		x2 = x1 + sizeX;
		y2 = y1 + sizeY;
		
		points[0] = new Rectangle2D.Double(x1 - pointSize/2, y1 - pointSize/2, pointSize, pointSize);
		points[1] = new Rectangle2D.Double(x2 - pointSize/2, y2 - pointSize/2, pointSize, pointSize);
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
			Point p = e.getPoint();
		    if (pos == -1) {
                return;
            }
            points[pos].setRect(p.x, p.y, pointSize, pointSize);
            if(pos == 0){
			    if(points[0].getMaxX() > points[1].getMaxX()){
			    	points[0].setRect(points[1].getX(), points[0].getY(), pointSize, pointSize);
			    }
			    if(points[0].getMaxY() > points[1].getMaxY()){
			    	points[0].setRect(points[0].getX(), points[1].getY(), pointSize, pointSize);
			    }
            }
            else{
			    if(points[1].getMinX() < points[0].getMinX()){
			    	points[1].setRect(points[0].getX(), points[1].getY(), pointSize, pointSize);
			    }
			    if(points[1].getMinY() < points[0].getMinY()){
			    	points[1].setRect(points[1].getX(), points[0].getY(), pointSize, pointSize);
			    }
            }

		    x1 = points[0].getCenterX();
		    y1 = points[0].getCenterY();
		    x2 = points[1].getCenterX();
		    y2 = points[1].getCenterY();
	        sizeX = Math.abs(x2 - x1);
	        sizeY = Math.abs(y2 - y1);
	        offsetX = 0;
	        offsetY = 0;
	        square.setRect(x1, y1, sizeX,sizeY);
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
