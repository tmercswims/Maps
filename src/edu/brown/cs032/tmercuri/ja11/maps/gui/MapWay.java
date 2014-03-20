/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.gui;

import java.awt.Color;

/**
 *
 * @author Thomas Mercurio
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
    
    public void setColor(Color c) {
        this.color = c;
    }
    
    public void setStartLat(double newLat) {
        this.startLat = newLat;
    }
    
    public void setStartLng(double newLng) {
        this.startLng = newLng;
    }
    
    public void setEndLat(double newLat) {
        this.endLat = newLat;
    }
    
    public void setEndLng(double newLng) {
        this.endLng = newLng;
    }
    
    public String getID() {
        return this.wayID;
    }
    
    public String getStartID() {
        return this.startNodeID;
    }
    
    public double getStartLat() {
        return this.startLat;
    }
    
    public double getStartLng() {
        return this.startLng;
    }
    
    public double getEndLat() {
        return this.endLat;
    }
    
    public double getEndLng() {
        return this.endLng;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public int getStartPixelX(){
    	return startPixelX;
    }
    
    public int getStartPixelY(){
    	return startPixelY;
    }
    
    public int getEndPixelX(){
    	return endPixelX;
    }
    
    public int getEndPixelY(){
    	return endPixelY;
    }
    
    public void convert(LatLngToPixel converter){
    	startPixelX = converter.LngToPixel(startLng);
    	startPixelY = converter.LatToPixel(startLat);
    	endPixelX = converter.LngToPixel(endLng);
    	endPixelY = converter.LatToPixel(endLat);
    }
    
}
