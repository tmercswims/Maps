package edu.brown.cs032.tmercuri.ja11.maps.gui;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;

public class MapCache {
	private Executor pool;
	private Collection<MapWay> topLeft, topCenter, topRight, middleLeft, middleCenter, middleRight, bottomLeft, bottomCenter, bottomRight;
	private MapData map;
	
	
	public static enum Side{
		UP, DOWN, LEFT, RIGHT;
	}
	public MapCache(MapData map){
		this.map = map;
		pool = new ThreadPoolExecutor(5, 9, 3000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	
	}
	
	public void setMap(MapData map){
		this.map = map;
	}
	
	public void searchBlock(int topX, int topY, int botX, int botY, LatLngToPixel converter ){
		double 
	}
	
}
