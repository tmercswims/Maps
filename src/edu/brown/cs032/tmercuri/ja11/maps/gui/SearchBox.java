package edu.brown.cs032.tmercuri.ja11.maps.gui;

import java.io.IOException;
import java.util.Collection;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;

public class SearchBox implements Runnable {
	
	Collection<MapWay> toFill;
	int topX;
	int topY;
	int botX;
	int botY;
	MapData map;
	
	LatLngToPixel converter; 
	public SearchBox(MapData map, Collection<MapWay> toFill, int topX, int topY, int botX, int botY, LatLngToPixel converter){
		this.topX = topX;
		this.topY = topY;
		this.botX = botX;
		this.botY = botY;
		this.map = map;
	}
	@Override
	public void run() {
		double topLat = converter.pixelToLat(topY);
		double topLng = converter.pixelToLng(topX);
		double botLat = converter.pixelToLat(botY);
		double botLng = converter.pixelToLng(botX);
		try {
			toFill.addAll(map.getAllBetween(topLat, topLng, botLat, botLng));
		} catch (IOException e) {
			System.out.println("ERROR: something went wrong with acdcessing map");
		}

	}

}
