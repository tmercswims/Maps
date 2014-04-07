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
import java.util.List;

import javax.swing.JPanel;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;
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
	
	private final Point2D topLeft;
	private Point2D bottomRight;
	private double scale;
	private double xOffset;
	private double yOffset;
	private Point2D PointOne;
	private Point2D PointTwo;
	private PointStatus whichPoint;
	private MapData mapData;
	private LatLngToPixel converter;
	private final Collection<MapWay> toDisplay;
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
		topLeft = new Point2D.Double(0, 0);
		bottomRight = new Point2D.Double();
		scale = 1;
		xOffset  = 0;
		yOffset = 0;
		PointOne = null;
		PointTwo = null;
		whichPoint = PointStatus.P1;
		setDoubleBuffered(true);
		toDisplay = new ArrayList<>();
		converter = new LatLngToPixel(40.15, -73.8);
		pathWay = new ArrayList<>();
		hasPath = false;
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
            // Find the roads
            toDisplay.addAll(mapData.getAllBetween(initialLat + converter.pixelToLatDistance(getHeight()), 
						initialLng - converter.pixelToLngDistance(getWidth()), initialLat - converter.pixelToLatDistance(getHeight()), initialLng + converter.pixelToLngDistance(getWidth())));
           // Display roads
            centerOnPoint(converter.LngToPixel(initialLng), converter.LatToPixel(initialLat));
           repaint();
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
		transformer.translate(xOffset, yOffset);
		transformer.scale(scale, scale);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setTransform(transformer);
		// First draws the normal ways
		g2d.setColor(Color.LIGHT_GRAY);
		for (MapWay way : toDisplay){
			drawMapWay(g2d, way);
		}
		// Draws any points drawn by the user
		g2d.setColor(Color.RED);
		if (PointOne!= null){
			g2d.drawOval((int)PointOne.getX(), (int)PointOne.getY(), 8, 8);
			if (PointTwo != null){
				g2d.setColor(Color.BLUE);
				g2d.drawOval((int) PointTwo.getX(), (int)PointTwo.getY(), 8, 8);
			}
		}
		// Sketches out the path if there is one
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
	
	/**
	 * requestSquare(): updates the screen to visualize everything within the bounds of the panel
	 */
	private void requestSquare(){
        pool.execute(new SquareDrawer(this, mapData, converter.pixelToLat((int) topLeft.getY()), converter.pixelToLng((int) topLeft.getX()), converter.pixelToLat((int)bottomRight.getY()), converter.pixelToLng((int) bottomRight.getX())));
	}
    
    /**
     * Adds new ways to this map.
     * @param newWays 
     */
    public void addWays(List<MapWay> newWays) {
        this.toDisplay.addAll(newWays);
        repaint();
    }
	
	/**
	 * 
	 * @param x, the x-coordinate of the point to be in the middle of the panel
	 * @param y, the y-coordinate of the point to be in the middle of the panel
	 */
	private void centerOnPoint(int x, int y){
		System.out.println("Centering on "+ x+ ", "+ y);
		int xSize = getWidth();
		int ySize = getHeight();
		xOffset = (xSize /2) + x;
		yOffset = (ySize / 2) + y;
		scale = 1;
		repaint();
	}
	
	/**
	 * 
	 * @param graphics, the graphics of the panel
	 * @param way, the way to be drawn
	 */
	private void drawMapWay(Graphics2D graphics, MapWay way){
		way.convert(converter);
		graphics.setStroke(new BasicStroke(4));
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
				}
				System.out.println("P1 lat " + getLatPointOne() + " lng "+ getLngPointOne());
				System.out.println("P2 lat " + getLatPointTwo() + " lng " + getLngPointTwo());
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
        @Override
		public void mouseReleased(MouseEvent e) {
			requestSquare();
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
				requestSquare();
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
}
