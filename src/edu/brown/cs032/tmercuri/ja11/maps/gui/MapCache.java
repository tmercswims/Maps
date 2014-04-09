package edu.brown.cs032.tmercuri.ja11.maps.gui;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.brown.cs032.tmercuri.ja11.maps.backend.LatLng;
import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;

/**
 *
 * @author Thomas Mercurio
 */
public class MapCache {
	
	private List<List<MapWay>> cache;
	
	private MapData map;
	private MapPanel display;
	double leftX;

	double rightX;

	double topY;

	double bottomY;
	
	private Executor pool;
	
	private double width;
	private double height;
	private Point2D center;
	
	private LatLngToPixel converter;
	
	
	private class BlockFinder implements Runnable{
		int index; 
		
		public BlockFinder(int index){
			this.index = index;
		}
		public void run(){
			findInfoFor(index);
			display.addWays(cache.get(index));
			
		}
	}
	
	public MapCache(MapData map, MapPanel display){
		this.map = map;
		this.display = display;
		this.cache = new ArrayList<List<MapWay>>(9);
		for (int i = 1; i < 10; i++){
			cache.add(new ArrayList<MapWay>());
		}

		this.pool = Executors.newCachedThreadPool();
		this.converter = display.getConverter();
		this.width =2* display.getWidth();
		this.height = 2*display.getHeight();
		this.center =new Point2D.Double();
		center.setLocation(display.getCenter());
		leftX = 0;
		rightX = 0;
		topY = 0;
		bottomY = 0;
		resetLimits();
		try{
		LatLng point = map.getNearestPoint(41.827404, -71.399323);
        converter = new LatLngToPixel(point.getLat(), point.getLng());
       // center.setLocation(converter.LngToPixel(point.getLng()), converter.LatToPixel(point.getLat()));
		for (int i = 0; i < 9 ; i++){
			pool.execute(new BlockFinder(i));
		}
        //pool.execute(new BlockFinder(1));
        //pool.execute(new BlockFinder(4));
		}
		catch (IOException e){
			System.out.println("ERROR: in cache initalization the map acted dumb");
		}
	}
	
	private void resetLimits(){
		leftX = center.getX() - width;
		rightX = center.getX() + width;
		topY = center.getY()  - height;
	    bottomY = center.getY() + height;
	}

	
	public void monitor(){
		//System.out.println("X check:" + display.getCenter().getX()+ " vs "+ (center.getX() - width));
		//System.out.println("Y check:" + display.getCenter().getY()+ " vs "+ (center.getY() - height));
		double displayX = display.getCenter().getX();
		double displayY = display.getCenter().getY();

		if (displayX < leftX){
			System.out.println("moving left");
			moveLeft();
			//resetLimits();
		}
		else if (displayX > rightX){
			System.out.println("moving right");
			moveRight();
			//resetLimits();
		}
		else if (displayY < topY){
			System.out.println("moving up");
			moveUp();
			//resetLimits();
		}
		else if (displayY > bottomY){
			System.out.println("moving down");
			moveDown();
			//resetLimits();
		}
		
	}
	
	public List<MapWay> getCache(){
		List<MapWay> result = cache.get(4);
		for (int i = 0 ; i< 4; i++){
			result.addAll(cache.get(i));
		}
		for (int i = 5; i < 9; i++){
			result.addAll(cache.get(i));
		}
		return result;
	}

	

	
	private void findInfoFor(int blockIndex){
		// First, redefine so we can express everything in terms of its position to the top left of the central block
		double baseX = center.getX() - center.getX()/2;
		double baseY = center.getY() - center.getY()/2;
		double initialLat = converter.pixelToLat((int)baseY);
		double initialLng = converter.pixelToLng((int)baseX);
		double topLat, topLng, bottomLat, bottomLng;
		// Establish the two limiting corners of the block 
		switch (blockIndex){
		case 0:{
			topLat = initialLat + 3*converter.pixelToLatDistance((int) height);
			topLng = initialLng - 3*converter.pixelToLngDistance((int)width);
			bottomLat = initialLat + converter.pixelToLatDistance((int)height);
			bottomLng = initialLng - converter.pixelToLngDistance((int)width);
			break;
		}
		case 1: {
			topLat = initialLat + 3*converter.pixelToLatDistance((int)height);
			topLng = initialLng - converter.pixelToLngDistance((int)width);
			bottomLat = initialLat + converter.pixelToLatDistance((int)height);
			bottomLng = initialLng + converter.pixelToLngDistance((int)width);
			break;
		}
		case 2: {
			topLat = initialLat + 3*converter.pixelToLatDistance((int)height);
			topLng = initialLng + converter.pixelToLngDistance((int)width);
			bottomLat = initialLat + converter.pixelToLatDistance((int)height);
			bottomLng = initialLng + 3*converter.pixelToLngDistance((int)width);
			break;
		}
		case 3: {
			topLat = initialLat + converter.pixelToLatDistance((int)height);
			topLng = initialLng - 3*converter.pixelToLngDistance((int)width);
			bottomLat = initialLat -converter.pixelToLatDistance((int)height);
			bottomLng = initialLng - converter.pixelToLngDistance((int)width);
			break;
		}
		case 4: {
			topLat = initialLat + converter.pixelToLatDistance((int)height);
			topLng = initialLng - converter.pixelToLngDistance((int)width);
			bottomLat = initialLat - converter.pixelToLatDistance((int)height);
			bottomLng = initialLng + converter.pixelToLngDistance((int)width);
			break;
		}
		case 5: {
			topLat = initialLat + converter.pixelToLatDistance((int)height);
			topLng = initialLng + converter.pixelToLngDistance((int)width);
			bottomLat = initialLat - converter.pixelToLatDistance((int)height);
			bottomLng = initialLng + 3*converter.pixelToLngDistance((int)width);
			break;
		}
		case 6: {
			topLat = initialLat - converter.pixelToLatDistance((int)height);
			topLng = initialLng - 3*converter.pixelToLngDistance((int)width);
			bottomLat = initialLat - 3*converter.pixelToLatDistance((int)height);
			bottomLng = initialLng - converter.pixelToLngDistance((int)width);
			break;
		}
		case 7: {
			topLat = initialLat - converter.pixelToLatDistance((int)height);
			topLng = initialLng - converter.pixelToLngDistance((int)width);
			bottomLat = initialLat - 3*converter.pixelToLatDistance((int)height);
			bottomLng = initialLng + converter.pixelToLngDistance((int)width);
			break;
		}
		case 8: {
			topLat = initialLat + converter.pixelToLatDistance((int)height);
			topLng = initialLng + converter.pixelToLngDistance((int)width);
			bottomLat = initialLat - 3*converter.pixelToLatDistance((int)height);
			bottomLng = initialLng + 3*converter.pixelToLngDistance((int)width);
			break;
		}
		default: {
			topLat = initialLat - converter.pixelToLatDistance((int)height);
			topLng = initialLng + converter.pixelToLngDistance((int)width);
			bottomLat = initialLat - 2*converter.pixelToLatDistance((int)height);
			bottomLng = initialLng + 2*converter.pixelToLngDistance((int)width);
		}
		}
	//	try {
			cache.set(blockIndex, testFillBlock(topLat, topLng, bottomLat, bottomLng));
			//cache.set(blockIndex, map.getAllBetween(topLat, topLng, bottomLat, bottomLng));
	//	} catch (IOException e) {
	//		System.out.println("ERROR: In the cache, the map produced an IO error"+blockIndex);
	//	}
	}
	
	private List<MapWay> testFillBlock(double topLat, double topLng, double bottomLat, double bottomLng){
		List<MapWay> roads = new ArrayList<MapWay>();
		roads.add(new MapWay("top", null, topLat, topLng, null, topLat, bottomLng, null));
		roads.add(new MapWay("bottom", null, bottomLat, topLng, null, bottomLat, bottomLng, null));
		roads.add(new MapWay("left", null, topLat, topLng, null, bottomLat, topLng, null));
		roads.add(new MapWay("right", null, topLat, bottomLng, null, bottomLat, bottomLng, null));
		return roads;
	}

	
	private void moveUp(){
		center.setLocation(center.getX(), center.getY()- height);
		for (int i = 8; i > 2; i--){
			cache.set(i, cache.get(i-3));
		}
		for (int i = 0; i < 3; i++){
			pool.execute(new BlockFinder(i));
		}
		
	}
	
	private void moveDown(){
		center.setLocation(center.getX(), center.getY()+ height);
		for (int i = 0; i< 6; i++){
			cache.set(i, cache.get(i+3));
		}
		for (int i = 6; i < 9; i++){
			pool.execute(new BlockFinder(i));
		}
	}
	
	private void moveLeft(){

		center.setLocation(center.getX() - width, center.getY());
		for (int i = 0; i< 7; i+=3){
			cache.set(i, cache.get(i+1));
		}
		for (int i = 1; i< 8; i+=3){
			cache.set(i, cache.get(i+1));
		}
		for (int i = 2; i < 9; i+=3){
			pool.execute(new BlockFinder(i));
		}
	}
	
	private void moveRight(){

		center.setLocation(center.getX() + width, center.getY());
		for (int i = 2; i< 9; i+=3){
			cache.set(i, cache.get(i-1));
		}
		for (int i = 1; i< 8; i+=3){
			cache.set(i, cache.get(i-1));
		}
		for (int i = 0; i < 7; i+=3){
			pool.execute(new BlockFinder(i));
		}
	}
	
	

	
	
	
}
