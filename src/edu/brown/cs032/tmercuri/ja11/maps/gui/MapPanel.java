package edu.brown.cs032.tmercuri.ja11.maps.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.JPanel;



public class MapPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 420L;
	
	private Point2D topLeft;
	private Point2D bottomRight;
	private double scale;
	private double xOffset;
	private double yOffset;
	private Point2D PointOne;
	private Point2D PointTwo;
	private PointStatus whichPoint;
	
	private enum PointStatus{
		P1, P2;
	}
	
	public MapPanel(LayoutManager manager){
		super(manager);
		topLeft = new Point2D.Double(0, 0);
		bottomRight = new Point2D.Double();
		scale = 1;
		xOffset  = 0;
		yOffset = 0;
		PointOne = null;
		PointTwo = null;
		whichPoint = PointStatus.P1;
		setDoubleBuffered(true);
		
		Scroller scroller = new Scroller();
		addMouseListener(scroller);
		addMouseMotionListener(scroller);
		addMouseWheelListener(new Scaler());	
	}
	
	@Override
	public void paint(Graphics g){
		AffineTransform transformer = new AffineTransform();
		transformer.translate(xOffset, yOffset);
		transformer.scale(scale, scale);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setTransform(transformer);
		g2d.setColor(Color.PINK);
		g2d.fillRect(100, 300, 50, 200);
		g2d.setColor(Color.RED);
		if (PointOne!= null){
			g2d.drawOval((int)PointOne.getX(), (int)PointOne.getY(), 5, 5);
			if (PointTwo != null){
				g2d.setColor(Color.DARK_GRAY);
				g2d.drawOval((int) PointTwo.getX(), (int)PointTwo.getY(), 5, 5);
			}
		}
		try {
			AffineTransform inverted = transformer.createInverse();
			bottomRight = new Point2D.Double(getWidth(), getHeight());
			inverted.transform(topLeft, topLeft);
			inverted.transform(bottomRight, bottomRight);
		} catch (NoninvertibleTransformException e) {
			System.out.println("ERROR: Noninvertible");
		}
		
		
	}
	
	
	
	private class Scroller implements MouseListener, MouseMotionListener{
		private int initialX;
		private int initialY;
		
		
		public Scroller(){
			initialX = 0;
			initialY = 0;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			Graphics2D g2d = (Graphics2D) getGraphics();
			try {
				AffineTransform transformNow = new AffineTransform();
				transformNow.translate(xOffset, yOffset);
				transformNow.scale(scale, scale);
				AffineTransform inverse = transformNow.createInverse();
				if (e.getClickCount() == 2){
					Point2D mousePos = new Point2D.Double(e.getX(), e.getY());
					Point2D actualPos = new Point2D.Double();
					inverse.transform(mousePos, actualPos);
					switch (whichPoint){
						case P1: { 
								PointOne = actualPos;
								whichPoint = PointStatus.P2;
								break;
								}
					
						case P2: {
								PointTwo = actualPos;
								whichPoint = PointStatus.P2; 
								break;
							}
						}
					}
				repaint();
			} catch (NoninvertibleTransformException e1) {
				System.out.println("ERROR: NonInvertible");
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			initialX = e.getX();
			initialY = e.getY();
			repaint();
		}



		@Override
		public void mouseDragged(MouseEvent e) {
			int newX = e.getX() - initialX;
			int newY = e.getY() - initialY;
			
			initialX += newX;
			initialY += newY;
			
			xOffset += newX;
			yOffset += newY;
			
			repaint();	
		}

		//These methods do nothing
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseMoved(MouseEvent e) {}
		
	}
	
	private class Scaler implements MouseWheelListener{

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if(e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL){
				scale += (0.1* e.getWheelRotation());
				scale = Math.max(0.00001, scale);
				repaint();
			}
		}
		
	}



}
