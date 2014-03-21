package edu.brown.cs032.tmercuri.ja11.maps.gui;

import java.awt.Color;

/**
 * A class that keeps everything needed to know about a way that needs to be drawn.
 */
public class MapWay {
    
    private final String wayID;
    private final String startNodeID;
    private double startLat;
    private double startLng;
    private final String endNodeID;
    private double endLat;
    private double endLng;
    private final String name;
    private Color color;
    private int startPixelX;
    private int startPixelY;
    private int endPixelX;
    private int endPixelY;
    
    /**
     *
     * @param wayID
     * @param startNodeID
     * @param startLat
     * @param startLng
     * @param endNodeID
     * @param endLat
     * @param endLng
     * @param name
     */
    public MapWay(String wayID, String startNodeID, double startLat, double startLng, String endNodeID, double endLat, double endLng, String name) {
        this.wayID = wayID;
        this.startNodeID = startNodeID;
        this.startLat = startLat;
        this.startLng = startLng;
        this.endLat = endLat;
        this.endLng = endLng;
        this.endNodeID = endNodeID;
        this.name = name;
        this.color = Color.white;
    }
    
    /**
     *
     * @param c
     */
    public void setColor(Color c) {
        this.color = c;
    }
    
    /**
     *
     * @param newLat
     */
    public void setStartLat(double newLat) {
        this.startLat = newLat;
    }
    
    /**
     *
     * @param newLng
     */
    public void setStartLng(double newLng) {
        this.startLng = newLng;
    }
    
    /**
     *
     * @param newLat
     */
    public void setEndLat(double newLat) {
        this.endLat = newLat;
    }
    
    /**
     *
     * @param newLng
     */
    public void setEndLng(double newLng) {
        this.endLng = newLng;
    }
    
    /**
     *
     * @return
     */
    public String getID() {
        return this.wayID;
    }
    
    /**
     *
     * @return
     */
    public String getStartID() {
        return this.startNodeID;
    }
    
    /**
     *
     * @return
     */
    public double getStartLat() {
        return this.startLat;
    }
    
    /**
     *
     * @return
     */
    public double getStartLng() {
        return this.startLng;
    }
    
    /**
     *
     * @return
     */
    public double getEndLat() {
        return this.endLat;
    }
    
    /**
     *
     * @return
     */
    public double getEndLng() {
        return this.endLng;
    }
    
    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }
    
    /**
     *
     * @return
     */
    public Color getColor() {
        return this.color;
    }
    
    /**
     *
     * @return
     */
    public int getStartPixelX(){
    	return startPixelX;
    }
    
    /**
     *
     * @return
     */
    public int getStartPixelY(){
    	return startPixelY;
    }
    
    /**
     *
     * @return
     */
    public int getEndPixelX(){
    	return endPixelX;
    }
    
    /**
     *
     * @return
     */
    public int getEndPixelY(){
    	return endPixelY;
    }
    
    /**
     *
     * @param converter
     */
    public void convert(LatLngToPixel converter){
    	startPixelX = converter.LngToPixel(startLng);
    	startPixelY = converter.LatToPixel(startLat);
    	endPixelX = converter.LngToPixel(endLng);
    	endPixelY = converter.LatToPixel(endLat);
    }
    
}
