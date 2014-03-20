package edu.brown.cs032.tmercuri.ja11.maps.gui;


public class LatLngToPixel {
	
	private double latScale = 8000;
	private double lngScale = 11000;
	private double anchorLat;
	private double anchorLng;
	private double anchorX;
	private double anchorY;
	
	
	public LatLngToPixel (double anchorLat, double anchorLng){
		this.anchorLat = anchorLat;
		this.anchorLng = anchorLng;
		anchorX = 0;
		anchorY = 0;
	}
	
	public int LatToPixel (double lat){
		double distance = anchorLat - lat;
		int result =  (int) (distance * latScale);
		return result;
	}
	
	public int LngToPixel (double lng){
		double distance = lng - anchorLng;
		int result = (int) (distance *lngScale);
		return result;
	}
	
	public double pixelToLat(int pixel){
		double distance = pixel/ latScale;
		return (anchorLat - distance);
	}
	
	public double pixelToLatDistance(int pixel){
		return (pixel/latScale);
	}
	
	public double pixelToLng(int pixel){
		double distance = pixel/lngScale;
		return (anchorLng + distance);
	}
	
	public double pixelToLngDistance(int pixel){
		return (pixel/lngScale);
	}
	
	
}
