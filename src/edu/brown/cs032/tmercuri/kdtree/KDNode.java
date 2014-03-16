/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.kdtree;

/**
 *
 * @author Thomas Mercurio
 * @param <T> type of this node; must extend Dimension
 */
public class KDNode<T extends Dimension<?>> {
        
    private final T point;
    KDNode<T> left;
    KDNode<T> right;

    KDNode(T _point, KDNode<T> _left, KDNode<T> _right) {
        this.point = _point;
        this.left = _left;
        this.right = _right;
    }
    
    /**
     * Gives the cth coordinate of this node's point.
     * @param c the coordinate to get
     * @return this node's point's cth coordinate
     */
    public double getPointCoord(int c) {
        return point.getCoord(c);
    }
    
    /**
     * Gives this node's point.
     * @return the node's point
     */
    public T getPoint() {
        return this.point;
    }
}
