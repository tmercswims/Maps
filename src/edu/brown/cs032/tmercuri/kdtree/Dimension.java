/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.kdtree;

import java.util.List;

/**
 * Interface for objects that have dimensions and coordinates.
 * @author Thomas Mercurio
 * @param <U> the type of the IDs of this dimension
 */
public abstract class Dimension<U> {
    
    /**
     *
     */
    public int dimension;

    /**
     *
     */
    public List<Double> coordinates;

    /**
     *
     */
    public U id;

    /**
     * Gives the number of dimensions that this representation has.
     * @return the number of dimensions
     */
    public int getDimension() {
        return this.dimension;
    }
    
    /**
     * Gives the cth coordinate of this representation.
     * @param c the coordinate to get, zero-indexed
     * @return the cth coordinate
     * @throws IndexOutOfBoundsException if c is less than zero or c is greater than the number of dimensions this representation has
     */
    public double getCoord(int c) throws IndexOutOfBoundsException {
        if (c > this.getDimension()-1 || c < 0) {
            throw new IndexOutOfBoundsException("No coordinate " + c);
        }
        else return this.coordinates.get(c);
    }
    
    /**
     * Gives the ID of this representation
     * @return this representation's ID
     */
    public U getID() {
        return this.id;
    }
    
    /**
     * Calculates the (squared) distance between the 2 given hyperpoints.
     * @param d1 the first hyperpoint
     * @param d2 the second hyperpoint
     * @return the squared distance between d1 and d2
     * @throws IllegalArgumentException if d1 and d2 are of different dimension
     */
    public static double distance(Dimension<?> d1, Dimension<?> d2) throws IllegalArgumentException {
        if (d1.getDimension() != d2.getDimension()) {
            throw new IllegalArgumentException("Points are of different dimensions: " + d1.getDimension() + ", " + d2.getDimension());
        }
        double dist = 0.0;
        for (int i=0; i<d1.getDimension(); i++) {
            dist += Math.pow(d2.getCoord(i)-d1.getCoord(i), 2.0);
        }
        return dist;
    }
}
