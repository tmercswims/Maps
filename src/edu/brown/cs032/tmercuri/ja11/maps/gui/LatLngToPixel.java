package edu.brown.cs032.tmercuri.ja11.maps.gui;

/***
 * LatLngToPixel deals with the conversions of lat/lng coordinates to pixel coordinates to be rendered
 * @author ja11
 *
 */
public class LatLngToPixel {
	
	private double latScale = 1100000;
	private double lngScale = 800000;
	private double anchorLat;
	private double anchorLng;
	private double anchorX;
	private double anchorY;
	
	/** 
	 * 
	 * @param anchorLat: a latitude which will be used as zero y-coordinate
	 * @param anchorLng: a longitude which will be used as zero x-coordinate
	 */
	public LatLngToPixel (double anchorLat, double anchorLng){
		this.anchorLat = anchorLat;
		this.anchorLng = anchorLng;
		anchorX = 0;
		anchorY = 0;
	}
	
	/**
	 * 
	 * @param lat: the latitude to be converted to pixels
	 * @return the y-coordinate on the pixel plane of lat
	 */
	public int LatToPixel (double lat){
		double distance = anchorLat - lat;
		int result =  (int) (distance * latScale);
		return result;
	}
	
	/**
	 * 
	 * @param lng: the longitude to be converted to pixels
	 * @return the x-coordinate on the pixel plane of lng
	 */
	public int LngToPixel (double lng){
		double distance = lng - anchorLng;
		int result = (int) (distance *lngScale);
		return result;
	}
	
	/**
	 * 
	 * @param pixel: the pixel coordinate to be converted to Latitude
	 * @return the latitude of the pixel
	 */
	public double pixelToLat(int pixel){
		double distance = pixel/ latScale;
		return (anchorLat - distance);
	}
	
	/**
	 * 
	 * @param pixel: the pixel coordinate of which we want the distance from the anchor in degrees
	 * @return the distance in degrees
	 */
	public double pixelToLatDistance(int pixel){
		return (pixel/latScale);
	}
	
	/** 
	 * 
	 * @param pixel: the pixel coordinate which we want returned in longitude degrees
	 * @return the longitude coordinate of this pixel
	 */
	public double pixelToLng(int pixel){
		double distance = pixel/lngScale;
		return (anchorLng + distance);
	}
	
	/**
	 * 
	 * @param pixel: the pixel coordinate of which we want the longitude distance from the anchor in degrees
	 * @return the distance in longitude degrees
	 */
	public double pixelToLngDistance(int pixel){
		return (pixel/lngScale);
	}
	
	
}
