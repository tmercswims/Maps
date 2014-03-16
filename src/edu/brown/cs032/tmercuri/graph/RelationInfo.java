/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.graph;

/**
 * Contains information that relates two nodes.
 * @author Thomas Mercurio
 * @param <T> the type of this RelationInfo
 */
public interface RelationInfo<T> {
    
    /**
     * Gives the relative target ID.
     * @return this relative target ID
     */
    public T getVID();
    
    /**
     * Gives the edge between U and V.
     * @return this edge between U and V
     */
    public T getEdge();
    
    /**
     * Gives the weight of the edge.
     * @return the weight of this edge
     */
    public double getWeight();
}
