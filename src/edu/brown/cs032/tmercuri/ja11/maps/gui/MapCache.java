package edu.brown.cs032.tmercuri.ja11.maps.gui;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
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
		this.width = display.getWidth();
		this.height = display.getHeight();
		this.center = display.getCenter();
		try{
		LatLng point = map.getNearestPoint(41.827404, -71.399323);
        converter = new LatLngToPixel(point.getLat(), point.getLng());
        center.setLocation(converter.LngToPixel(point.getLng()), converter.LatToPixel(point.getLat()));
		//for (int i = 0; i < 9 ; i++){
		//	pool.execute(new BlockFinder(i));
		//}
        pool.execute(new BlockFinder(3));
        //pool.execute(new BlockFinder(4));
		}
		catch (IOException e){
			System.out.println("ERROR: in cache initalization the map acted dumb");
		}
	}

	
	public void monitor(){
		if (display.getCenter().getX() < (center.getX() - width)){
			System.out.println("moving left");
			moveLeft();
		}
		if (display.getCenter().getX() > (center.getX()+ width)){
			System.out.println("moving right");
			moveRight();
		}
		if (display.getCenter().getY() < (center.getY() - height)){
			System.out.println("moving up");
			moveUp();
		}
		if (display.getCenter().getY() > (center.getY() + height)){
			System.out.println("moving down");
			moveDown();
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
			topLat = initialLat + 2*converter.pixelToLatDistance(display.getHeight());
			topLng = initialLng - 2*converter.pixelToLngDistance(display.getWidth());
			bottomLat = initialLat + converter.pixelToLatDistance(display.getHeight());
			bottomLng = initialLng - converter.pixelToLngDistance(display.getWidth());
			break;
		}
		case 1: {
			topLat = initialLat + 2*converter.pixelToLatDistance(display.getHeight());
			topLng = initialLng - converter.pixelToLngDistance(display.getWidth());
			bottomLat = initialLat + converter.pixelToLatDistance(display.getHeight());
			bottomLng = initialLng + converter.pixelToLngDistance(display.getWidth());
			break;
		}
		case 2: {
			topLat = initialLat + 2*converter.pixelToLatDistance(display.getHeight());
			topLng = initialLng + converter.pixelToLngDistance(display.getWidth());
			bottomLat = initialLat + converter.pixelToLatDistance(display.getHeight());
			bottomLng = initialLng + 2*converter.pixelToLngDistance(display.getWidth());
			break;
		}
		case 3: {
			System.out.println("in 3");
			topLat = initialLat + 7*converter.pixelToLatDistance(display.getHeight());
			topLng = initialLng - 5*converter.pixelToLngDistance(display.getWidth());
			bottomLat = initialLat + 4 *converter.pixelToLatDistance(display.getHeight());
			bottomLng = initialLng - 3* converter.pixelToLngDistance(display.getWidth());
			break;
		}
		case 4: {
			topLat = initialLat + converter.pixelToLatDistance(display.getHeight());
			topLng = initialLng - converter.pixelToLngDistance(display.getWidth());
			bottomLat = initialLat - converter.pixelToLatDistance(display.getHeight());
			bottomLng = initialLng + converter.pixelToLngDistance(display.getWidth());
			break;
		}
		case 5: {
			topLat = initialLat + converter.pixelToLatDistance(display.getHeight());
			topLng = initialLng - converter.pixelToLngDistance(display.getWidth());
			bottomLat = initialLat - converter.pixelToLatDistance(display.getHeight());
			bottomLng = initialLng + converter.pixelToLngDistance(display.getWidth());
			break;
		}
		case 6: {
			topLat = initialLat - converter.pixelToLatDistance(display.getHeight());
			topLng = initialLng - 2*converter.pixelToLngDistance(display.getWidth());
			bottomLat = initialLat - 2*converter.pixelToLatDistance(display.getHeight());
			bottomLng = initialLng - converter.pixelToLngDistance(display.getWidth());
			break;
		}
		case 7: {
			topLat = initialLat - converter.pixelToLatDistance(display.getHeight());
			topLng = initialLng - converter.pixelToLngDistance(display.getWidth());
			bottomLat = initialLat - 2*converter.pixelToLatDistance(display.getHeight());
			bottomLng = initialLng + converter.pixelToLngDistance(display.getWidth());
			break;
		}
		case 8: {
			topLat = initialLat + converter.pixelToLatDistance(display.getHeight());
			topLng = initialLng - converter.pixelToLngDistance(display.getWidth());
			bottomLat = initialLat - converter.pixelToLatDistance(display.getHeight());
			bottomLng = initialLng + converter.pixelToLngDistance(display.getWidth());
			break;
		}
		default: {
			topLat = initialLat - converter.pixelToLatDistance(display.getHeight());
			topLng = initialLng + converter.pixelToLngDistance(display.getWidth());
			bottomLat = initialLat - 2*converter.pixelToLatDistance(display.getHeight());
			bottomLng = initialLng + 2*converter.pixelToLngDistance(display.getWidth());
		}
		}
		try {
			cache.set(blockIndex, map.getAllBetween(topLat, topLng, bottomLat, bottomLng));
			System.out.println("done with"+ blockIndex);
		} catch (IOException e) {
			System.out.println("ERROR: In the cache, the map produced an IO error"+blockIndex);
		}
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
