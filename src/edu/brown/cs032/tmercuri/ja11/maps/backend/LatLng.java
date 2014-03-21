/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.backend;

import edu.brown.cs032.tmercuri.kdtree.Dimension;
import java.util.ArrayList;

/**
 *
 * @author Thomas Mercurio
 */
public class LatLng extends Dimension<String> {
    
    /**
     *
     * @param _id
     * @param lat
     * @param lng
     */
    public LatLng(String _id, final double lat, final double lng) {
        this.coordinates = new ArrayList<>();
        coordinates.add(lat);
        coordinates.add(lng);
        this.id = _id;
        this.dimension = 2;
    }
    
    /**
     *
     * @return
     */
    public double getLat() {
        return this.getCoord(0);
    }
    
    /**
     *
     * @return
     */
    public double getLng() {
        return this.getCoord(1);
    }
}
