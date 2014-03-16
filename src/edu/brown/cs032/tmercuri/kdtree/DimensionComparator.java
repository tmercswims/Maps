/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.kdtree;

import java.util.Comparator;

/**
 *
 * @author Thomas Mercurio
 * @param <T> type of this comparator; must extend Dimension
 */
public class DimensionComparator<T extends Dimension<?>> implements Comparator<T> {
    
    private final T p;
    
    /**
     * New Dimension Comparator, using the given point p as the reference.
     * @param _p the point that compared points will be measured from to determine their ordering
     */
    public DimensionComparator(T _p) {
        this.p = _p;
    }

    @Override
    public int compare(T p1, T p2) {
        // distance from p1 to the reference
        double distToP1 = Dimension.distance(p, p1);
        // distance from p2 to the reference
        double distToP2 = Dimension.distance(p, p2);
        
        if (distToP1 > distToP2) {
            return 1;
        }
        else if (distToP1 == distToP2) {
            return 0;
        }
        else {
            return -1;
        }
    }
}
