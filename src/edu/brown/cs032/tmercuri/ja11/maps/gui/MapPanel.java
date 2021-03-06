package edu.brown.cs032.tmercuri.ja11.maps.gui;

import edu.brown.cs032.tmercuri.ja11.maps.backend.LatLng;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;


/** 
 * MapPanel renders the map
 * @author ja11
 *
 */
public class MapPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 420L;
	
	public Point2D center;
	private double scale;
	private double xOffset;
	private double yOffset;
	private Point2D PointOne;
	private Point2D PointTwo;
	private PointStatus whichPoint;
	private MapData mapData;
	private LatLngToPixel converter;
	private MapCache cache;
	private final Map<MapWay, Double> toDisplay;
	private List<MapWay> pathWay;
	private boolean hasPath;
    private final Executor pool;
	
	private enum PointStatus{
		P1, P2;
	}
	
	/** Constructor
     * @param manager
     * @param pool **/
	
	public MapPanel(LayoutManager manager, Executor pool){
		super(manager);
		center = new Point2D.Double(0, 0);
		scale = 1;
		xOffset  = 0;
		yOffset = 0;
		PointOne = null;
		PointTwo = null;
		whichPoint = PointStatus.P1;
		setDoubleBuffered(true);
		toDisplay = new ConcurrentHashMap<MapWay, Double>();
		converter = new LatLngToPixel(40.15, -73.8);
		pathWay = new ArrayList<MapWay>();
		hasPath = false;
		this.cache = null;
        this.pool = pool;
        setBackground(Color.DARK_GRAY);
		
		Scroller scroller = new Scroller();
		addMouseListener(scroller);
		addMouseMotionListener(scroller);
		addMouseWheelListener(new Scaler());	

	}
	
	/**
	 * 
	 * @param mapData: the map to be used to get information for the map 
	 */
	public void setMap (MapData mapData){
		this.mapData = mapData;


		try {
			// Default: CIT
			LatLng point = mapData.getNearestPoint(41.827404, -71.399323);
			double initialLat = point.getLat();
			double initialLng = point.getLng();
	        converter = new LatLngToPixel(point.getLat(), point.getLng());
	        centerOnPoint(converter.LngToPixel(initialLng), converter.LatToPixel(initialLat));
            // Find the roads

			this.cache = new MapCache(mapData, this);         
			repaint();
			
			while (true){
				cache.monitor();
			}
        } catch (IOException e) {
			System.out.println("ERROR: problem with IO");
        }
    }
	
	/**
	 * paint
	 */
	@Override
	public void paint(Graphics g){
		super.paint(g);
		// Deals with the transformations by scrolling
		AffineTransform transformer = new AffineTransform();
		
		transformer.translate(getWidth()/2, getHeight()/2);
		transformer.scale(scale, scale);
		transformer.translate(xOffset, yOffset);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setTransform(transformer);
		// First draws the normal ways
		g2d.setColor(Color.LIGHT_GRAY);
		drawMapWays(g2d, toDisplay);
		// Draws any points drawn by the user
		g2d.setColor(Color.RED);
		if (PointOne!= null){
			g2d.drawOval((int)PointOne.getX(), (int)PointOne.getY(), 8, 8);
			if (PointTwo != null){
				g2d.setColor(Color.BLUE);
				g2d.drawOval((int) PointTwo.getX(), (int)PointTwo.getY(), 8, 8);
			}
		}
		g2d.setColor(Color.GREEN);
		g2d.drawOval((int) center.getX(), (int)center.getY(), 10, 10);
		// Sketches out the path if there is one
		if (hasPath){
			g2d.setColor(Color.MAGENTA);
			for (MapWay way: pathWay){
				drawPath(g2d, way);
			}
		}

		
		
	}
	
	
    /**
     * Adds new ways to this map.
     * @param newWays 
     */
    public void addWays(List<MapWay> newWays) {
        for (MapWay way : newWays){
        	if (!toDisplay.containsKey(way)){
        		toDisplay.put(way, 1.0);
        	}
        }
        repaint();
    }
    
    /** 
     * 
     */
	public void addTraffic(Collection<MapWay> newWays, double value){
		for (MapWay way: newWays){
			toDisplay.put(way, value);
		}
		repaint();
	}
	/**
	 * 
	 * @param x, the x-coordinate of the point to be in the middle of the panel
	 * @param y, the y-coordinate of the point to be in the middle of the panel
	 */
	private void centerOnPoint(int x, int y){
		xOffset = x;
		yOffset = y;
		scale = 1;
		center.setLocation(x, y);
		repaint();
	}
	
	/**
	 * 
	 * @param graphics, the graphics of the panel
	 * @param way, the way to be drawn
	 */
	private void drawMapWays(Graphics2D graphics, Map <MapWay, Double> ways){
		for (MapWay way : ways.keySet()){
		way.convert(converter);
		graphics.setStroke(new BasicStroke(4));
		double trafficVal = ways.get(way);
		if (trafficVal<= 1){
			graphics.setColor(Color.LIGHT_GRAY);
		}
		else if (trafficVal<= 2){
			graphics.setColor(Color.YELLOW);
		}
		else if (trafficVal <= 4){
			graphics.setColor(Color.ORANGE);
		}
		else if (trafficVal <= 5){
			graphics.setColor(Color.RED);
		}
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawLine(way.getStartPixelX(), way.getStartPixelY(), way.getEndPixelX(), way.getEndPixelY());
		}
		
	}
	
	private void drawPath (Graphics2D graphics, MapWay way){
		way.convert(converter);
		graphics.setStroke(new BasicStroke(4));
		graphics.setColor(Color.MAGENTA);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawLine(way.getStartPixelX(), way.getStartPixelY(), way.getEndPixelX(), way.getEndPixelY());
	}
	
	
	/***
	 * Scroller deals with the translation by mouseclicking
	 * @author ja11
	 *
	 */
	private class Scroller implements MouseListener, MouseMotionListener {
		private int initialX;
		private int initialY;
		
		
		public Scroller(){
			initialX = 0;
			initialY = 0;
		}
		
		/** mouseclicked allows users to select points
		 * 
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			Graphics2D g2d = (Graphics2D) getGraphics();

				AffineTransform transformNow = new AffineTransform();
				transformNow.translate(xOffset, yOffset);
				transformNow.scale(scale, scale);
				transformNow.transform(center, center);
				if (e.getClickCount() == 2){
					Point2D actualPos = getTranslatedPoint(e.getX(), e.getY());
					
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
				}
			
				repaint();

		}

		@Override
		public void mousePressed(MouseEvent e) {
			initialX = e.getX();
			initialY = e.getY();
			repaint();
		}



		@Override
		public void mouseDragged(MouseEvent e) {
			
			Point2D previousPoint = getTranslatedPoint(initialX, initialY);
			Point2D newPoint = getTranslatedPoint(e.getX(), e.getY());
			
			double newX = newPoint.getX() - previousPoint.getX();
			double newY = newPoint.getY() - previousPoint.getY();
			
			initialX = e.getX();
			initialY = e.getY();
			
			xOffset += newX;
			yOffset += newY;
			center.setLocation(center.getX()- newX, center.getY()-newY);
			
			repaint();
		}

		private Point2D getTranslatedPoint(double x, double y){
			AffineTransform currTransform = new AffineTransform();
			currTransform.translate(getWidth() /2 , getHeight()/2);
			currTransform.scale(scale, scale);
			currTransform.translate(xOffset, yOffset);
			
			try{
				return currTransform.inverseTransform(new Point2D.Double(x, y), null);
			}
			catch (NoninvertibleTransformException e){
				System.out.println("ERROR: the inverted ttransform in the mouse thing wasn't invertible");
				return null;
			}
		}
		//These methods do nothing
        @Override
		public void mouseReleased(MouseEvent e) {

        }
        @Override
		public void mouseEntered(MouseEvent e) {}
        @Override
		public void mouseExited(MouseEvent e) {}
        @Override
		public void mouseMoved(MouseEvent e) {}
		
	}
	
	/**
	 * Scaler lets the user zoom in and out of the panel
	 * @author ja11
	 *
	 */
	private class Scaler implements MouseWheelListener {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if(e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL){
				scale -= (0.001* e.getWheelRotation());
				scale = Math.max(0.00001, scale);
				repaint();
			}
		}
		
	}
	
	/** Some getters, for coordinates which have been already converted to lat and lng
     * @return  **/
	
	public double getLatPointOne(){
		if (PointOne!=null)
		 return  (converter.pixelToLat((int) PointOne.getY()));
		else return 0;
	}

    /**
     *
     * @return
     */
    public double getLngPointOne(){
    	if (PointOne != null)
		return  (converter.pixelToLng((int) PointOne.getY()));
    	else return 0;
	}

    /**
     *
     * @return
     */
    public double getLatPointTwo(){
    	if (PointTwo != null)
		 return  (converter.pixelToLat((int) PointTwo.getY()));
    	else return 0;
	}

    /**
     *
     * @return
     */
    public double getLngPointTwo(){
    	if (PointTwo != null)
		return  (converter.pixelToLng((int) PointTwo.getY()));
    	else return 0;
	}
    

    
    /**
     * Sets a path to display.
     * @param path
     */
    public void setPath(List<MapWay> path) {
    	System.out.println("has a pth");
        this.hasPath = true;
        this.pathWay = path;
        repaint();
    }

	public double getAnchorX() {
		return xOffset;
	}

	public double getAnchorY() {
		return yOffset;
	}
	
    public LatLngToPixel getConverter(){
    	return converter;
    }

	public Point2D getCenter() {
		return center;
	}
}
