import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;


public class LayerPresent extends JPanel implements MouseListener {
	
	private int alpha = 127;
    private Color color;
    private boolean top = false;
	Graphics2D g2;
	Rectangle2D squareIn, squareOut;
	public double x1 = 0, y1 = 0;
	public double sizeX = 60, sizeY = 60, pointSize = 8;
	private double x2 = x1 + sizeX, y2 = y1 + sizeY;
	public boolean clickDetected = false;
	ArrayList<HyperLink> links;
	HyperLink click;
    
	public LayerPresent(int x1, int y1, int width, int height, int sizeX, int sizeY) {
    	
    	this.sizeX = sizeX;
    	this.sizeY = sizeY;
		this.x1 = x1;
		this.y1 = y1;

		x2 = x1 + sizeX;
		y2 = y1 + sizeY;
		
		squareIn = new Rectangle2D.Double(2, 2, sizeX - 4, sizeY - 4);
		squareOut = new Rectangle2D.Double(0, 0, sizeX, sizeY);
        this.setOpaque(false);
        this.setBounds(x1, y1, sizeX, sizeY);
		setFocusable(true);
		addMouseListener(this);
		this.requestFocus();
    }
	
	public LayerPresent(int x1, int y1, int sizeX, int sizeY, boolean top) {
    	
    	this.sizeX = sizeX;
    	this.sizeY = sizeY;
		this.x1 = x1;
		this.y1 = y1;
		this.top = top;
		
		x2 = x1 + sizeX;
		y2 = y1 + sizeY;
		
		squareIn = new Rectangle2D.Double(x1, y1, sizeX, sizeY);
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
		if(!top){
			g2.setStroke(new BasicStroke(1));
			g2.setColor(Color.WHITE);
			g2.draw(squareOut);
	    	color = new Color(255, 0, 0, alpha);
			g2.setColor(color);
			g2.fill(squareIn);
		}
		else{
	    	color = new Color(255, 255, 255, 0);
			g2.setColor(color);
			g2.fill(squareIn);
		}
    }

    public void setClickArea(ArrayList<HyperLink> links){
    	this.links = links;
    }
    
    
	@Override
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		clickDetected = false;
		for(HyperLink link : links){
			int x1 = link.getX();
			int x2 = x1 + link.getWidth();
			int y1 = link.getY();
			int y2 = y1 + link.getHeight();
			
			if(p.x >= x1 && p.x <= x2 && p.y >= y1 && p.y <= y2){
				this.click = link;
				clickDetected = true;
			}
		}
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
