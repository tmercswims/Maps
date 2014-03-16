/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.backend;

import edu.brown.cs032.tmercuri.kdtree.Dimension;
import java.util.ArrayList;


public class LatLng extends Dimension<String> {
    
    public LatLng(String _id, final double lat, final double lng) {
        this.coordinates = new ArrayList<Double>(){{add(lat);add(lng);}};
        this.id = _id;
        this.dimension = 2;
    }
    
    public double getLat() {
        return this.getCoord(0);
    }
    
    public double getLng() {
        return this.getCoord(1);
    }
}
