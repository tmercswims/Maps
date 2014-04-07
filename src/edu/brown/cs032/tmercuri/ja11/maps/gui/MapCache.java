package edu.brown.cs032.tmercuri.ja11.maps.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
	private double pixelWidth;
	private double pixelHeight;
	
	public MapCache(MapData map, MapPanel display){
		this.map = map;
		this.display = display;
		this.cache = new ArrayList<List<MapWay>>(9);
		this.pool = new ThreadPoolExecutor(9, 100, 3000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		this.pixelHeight = display.getAnchorX();
		this.pixelWidth = display.getAnchorY();
		
		
	}
	
	public moveUp(){
		
	}
	
	
	
}
