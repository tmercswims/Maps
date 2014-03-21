/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.backend;

import edu.brown.cs032.tmercuri.graph.RelationInfo;

/**
 *
 * @author Thomas Mercurio
 */
public class MapsRelationInfo implements RelationInfo<String> {
    
    private final String destinationNodeID, wayID;
    private final double weight;
    
    /**
     *
     * @param destinationNodeID
     * @param wayID
     * @param weight
     */
    public MapsRelationInfo(String destinationNodeID, String wayID, double weight) {
        this.destinationNodeID = destinationNodeID;
        this.wayID = wayID;
        this.weight = weight;
    }

    @Override
    public String getVID() {
        return this.destinationNodeID;
    }

    @Override
    public String getEdge() {
        return this.wayID;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }
    
}
