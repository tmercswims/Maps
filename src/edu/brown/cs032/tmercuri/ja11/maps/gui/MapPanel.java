package edu.brown.cs032.tmercuri.ja11.maps.gui;

import edu.brown.cs032.tmercuri.ja11.maps.backend.LatLng;
import java.awt.Color;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;



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
	private MapData mapData;
	private LatLngToPixel converter;
	private Collection<MapWay> toDisplay;
	private List<MapWay> pathWay;
	private boolean hasPath;
	
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
		toDisplay = new ArrayList<MapWay>();
		converter = new LatLngToPixel(40.15, -73.8);
		pathWay = new ArrayList<MapWay>();
		hasPath = false;
		
		Scroller scroller = new Scroller();
		addMouseListener(scroller);
		addMouseMotionListener(scroller);
		addMouseWheelListener(new Scaler());	
	}
	
	public void setMap (MapData mapData){
		this.mapData = mapData;

		try {
			LatLng point = mapData.getNearestPoint(41.827404, -71.399323);
			double initialLat = point.getLat();
			double initialLng = point.getLng();
            converter = new LatLngToPixel(point.getLat(), point.getLng());
            System.out.println("The span in lat is "+ converter.pixelToLatDistance(getHeight()));
            System.out.println("The span in lng is "+ converter.pixelToLngDistance(getWidth()));
            toDisplay.addAll(mapData.getAllBetween(initialLat + converter.pixelToLatDistance(getHeight()), 
						initialLng - converter.pixelToLngDistance(getWidth()), initialLat - converter.pixelToLatDistance(getHeight()), initialLng + converter.pixelToLngDistance(getWidth())));
            System.out.println("found all roads");
            centerOnPoint(converter.LngToPixel(initialLng), converter.LatToPixel(initialLat));
            System.out.println("there are " + toDisplay.size()+ " roads to show");
            repaint();
        } catch (IOException e) {
			System.out.println("ERROR: problem with IO");
        }
    }
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		AffineTransform transformer = new AffineTransform();
		transformer.translate(xOffset, yOffset);
		transformer.scale(scale, scale);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setTransform(transformer);
		g2d.setColor(Color.BLACK);
		for (MapWay way : toDisplay){
			drawMapWay(g2d, way);
		}
		g2d.setColor(Color.RED);
		if (PointOne!= null){
			g2d.drawOval((int)PointOne.getX(), (int)PointOne.getY(), 5, 5);
			if (PointTwo != null){
				g2d.setColor(Color.DARK_GRAY);
				g2d.drawOval((int) PointTwo.getX(), (int)PointTwo.getY(), 5, 5);
			}
		}
		if (hasPath){
			g2d.setColor(Color.MAGENTA);
			for (MapWay way: pathWay){
				drawMapWay(g2d, way);
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
	
	private void requestSquare(){
		try {
			toDisplay.addAll(mapData.getAllBetween(converter.pixelToLat((int) topLeft.getY()), converter.pixelToLng((int) topLeft.getX()), converter.pixelToLat((int)bottomRight.getY()), converter.pixelToLng((int) bottomRight.getX())));
			repaint();
		} catch (IOException e) {
			System.out.println("ERROR: reading map");
		} 
	}
	
	
	private void centerOnPoint(int x, int y){
		System.out.println("Centering on "+ x+ ", "+ y);
		int xSize = getWidth();
		int ySize = getHeight();
		xOffset = (xSize /2) + x;
		yOffset = (ySize / 2) + y;
		scale = 1;
		repaint();
	}
	
	private void drawMapWay(Graphics2D graphics, MapWay way){
		way.convert(converter);
		graphics.drawLine(way.getStartPixelX(), way.getStartPixelY(), way.getEndPixelX(), way.getEndPixelY());
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
								whichPoint = PointStatus.P1; 
								break;
							}
						}
					System.out.println("coordinates are "+ actualPos.getX()+ ", "+ actualPos.getY());
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
			requestSquare();
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
				scale -= (0.1* e.getWheelRotation());
				scale = Math.max(0.0000001, scale);
				repaint();
				requestSquare();
			}
		}
		
	}
	
	public double getLatPointOne(){
		 return  (converter.pixelToLat((int) PointOne.getY()));
	}
	
	public double getLngPointOne(){
		return  (converter.pixelToLng((int) PointOne.getY()));
	}
	
	public double getLatPointTwo(){
		 return  (converter.pixelToLat((int) PointTwo.getY()));
	}
	
	public double getLngPointTwo(){
		return  (converter.pixelToLng((int) PointTwo.getY()));
	}
	
	



}
