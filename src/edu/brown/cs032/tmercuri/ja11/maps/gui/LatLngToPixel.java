package edu.brown.cs032.tmercuri.ja11.maps.gui;


public class LatLngToPixel {
	
	private double latScale = 8000;
	private double lngScale = 110000;
	private double highLat;
	private double lowLng;
	
	
	public LatLngToPixel (double highLat, double lowLng){
		this.highLat = highLat;
		this.lowLng = lowLng;
	}
	
	public int LatToPixel (double  lat){
		double distance = highLat - lat;
		int result =  (int) (distance * latScale);
		return result;
	}
	
	public int LngToPixel (double lng){
		double distance = lng - lowLng;
		int result = (int) (distance *lngScale);
		return result;
	}
	
	public double pixelToLat(int pixel){
		double distance = pixel/ latScale;
		return (highLat - distance);
	}
	
	public double pixelToLng(int pixel){
		double distance = pixel/lngScale;
		return (lowLng + distance);
	}
	
	
}
