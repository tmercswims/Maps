/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.kdtree;

import java.util.List;

/**
 * Interface for k-d trees.
 * @author Thomas Mercurio
 * @param <T> type of this tree; must extend Dimension
 * @param <U> type of the IDs of this tree
 */
public interface KDTree<T extends Dimension<U>, U> {
    
    /**
     * Finds the n nearest neighbors to the specified point p
     * @param n the number of neighbors to return
     * @param includeGivenPoint whether or not to include the given point p as a neighbor
     * @param p the reference point
     * @return a list of n points' IDs, ordered from closest to farthest from p
     */
    List<U> findNNearestNeighbors(int n, boolean includeGivenPoint, T p);
    
    U findNearestNeighbor(T p);
    
    /**
     * Finds all points with radius r of the point p
     * @param r the radius within which to search
     * @param includeGivenPoint whether or not to include the given point p in the results
     * @param p the reference point
     * @return a list of points' IDs that are within r of p
     */
    List<U> findAllWithinRadius(double r, boolean includeGivenPoint, T p);
}
