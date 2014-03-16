/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.kdtree;

import com.google.common.collect.MinMaxPriorityQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A slow, perfect implementation of k-d trees, where k is arbitrary.
 * @author Thomas Mercurio
 * @param <T> type of this tree; must extend Dimension
 * @param <U> type of the IDs of this trees points
 */
public class SlowTree<T extends Dimension<U>, U> implements KDTree<T, U> {
    
    private final KDNode<T> root;
    
    /**
     * Initializes a KDTree with the given points.
     * @param points a list of Dimension objects, used as points
     */
    public SlowTree(List<T> points) {
        this.root = buildTree(points, points.get(0).getDimension(), 0);
    }
    
    private KDNode<T> buildTree(List<T> point_list, int k, int depth) {
        // no more points
        if (point_list.isEmpty()) {
            return null;
        }
        // axis to split on
        final int axis = depth%k;
        // completely sort the points based on the axis
        Collections.sort(point_list, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                if (o1.getCoord(axis) < o2.getCoord(axis)) {
                    return -1;
                }
                else if (o1.getCoord(axis) == o2.getCoord(axis)) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
        });
        // the median index
        int median = point_list.size()/2;
        
        // new KDNode with the median point, and recursively build the child nodes
        return new KDNode<>(point_list.get(median), buildTree(point_list.subList(0, median), k, depth+1), buildTree(point_list.subList(median+1, point_list.size()), k, depth+1));
    }

    /**
     * Finds the n nearest neighbors to the specified point p
     * @param n the number of neighbors to return
     * @param includeGivenPoint whether or not to include the given point p as a neighbor
     * @param p the reference point
     * @return a list of n points' IDs, ordered from closest to farthest from p
     */
    @Override
    public List<U> findNNearestNeighbors(int n, boolean includeGivenPoint, T p) {
        // return nothing if n=0
        if (n == 0) {
            return new ArrayList<>();
        }
        // bounded priority queue to keep the current closest n neighbors
        MinMaxPriorityQueue<T> bpq = MinMaxPriorityQueue.orderedBy(new DimensionComparator<>(p)).maximumSize(n).<T>create();
        // recursively search the tree
        findNearestNeighborsRec(root, bpq, includeGivenPoint, p, n, root.getPoint().getDimension(), 0);
        // move the IDs of the contents of the queue into a list
        List<U> nearest = new ArrayList<>();
        int stop = bpq.size();
        for (int i=0; i<stop; i++) {
            nearest.add(bpq.pollFirst().getID());
        }
        return nearest;
    }
    
    @Override
    public U findNearestNeighbor(T p) {
        return findNNearestNeighbors(1, true, p).get(0);
    }
    
    private void findNearestNeighborsRec(KDNode<T> node, MinMaxPriorityQueue<T> bpq, boolean includeGivenPoint, T p, int n, int k, int depth) {
        // axis to split this layer on
        int axis = depth%k;
        // to see which tree was searched the first time
        boolean searchL = false;
        // as long as we are not at the end
        if (node != null) {
            // if we are told to include the given point, or if we aren't and it isn't the point in question, add it
            if (includeGivenPoint || node.getPoint().getID() != p.getID()) {
                bpq.add(node.getPoint());
            }
            // goal is smaller, search left subtree
            if (p.getCoord(axis) < node.getPointCoord(axis)) {
                searchL = true;
                findNearestNeighborsRec(node.left, bpq, includeGivenPoint, p, n, k, depth+1);
            }
            // goal is larger, search right subtree
            else {
                findNearestNeighborsRec(node.right, bpq, includeGivenPoint, p, n, k, depth+1);
            }
            // maybe check other, unsearched subtree
            if (bpq.size() != n || Math.abs(node.getPointCoord(axis)-p.getCoord(axis)) < Dimension.distance(p, bpq.peekLast())) {
                if (searchL) {
                    findNearestNeighborsRec(node.right, bpq, includeGivenPoint, p, n, k, depth+1);
                }
                else {
                    findNearestNeighborsRec(node.left, bpq, includeGivenPoint, p, n, k, depth+1);
                }
            }
        }
    }

    /**
     * Finds all points with radius r of the point p
     * @param r the radius within which to search
     * @param includeGivenPoint whether or not to include the given point p in the results
     * @param p the reference point
     * @return a list of points' IDs that are within r of p
     */
    @Override
    public List<U> findAllWithinRadius(double r, boolean includeGivenPoint, T p) {
        List<T> l = new ArrayList<>();
        findAllWithinRadiusRec(root, l, includeGivenPoint, p, r, root.getPoint().getDimension(), 0);
        Collections.sort(l, new DimensionComparator<>(p));
        List<U> within = new ArrayList<>();
        for (T point : l) {
            within.add(point.getID());
        }
        return within;
    }
    
    private void findAllWithinRadiusRec(KDNode<T> node, List<T> l, boolean includeGivenPoint, T p, double r, int k, int depth) {
        // axis to split this layer on
        int axis = depth%k;
        // to see which tree was searched the first time
        boolean searchL = false;
        // as long as we are not at the end
        if (node != null) {
            // if we are told to include the given point, or if we aren't and it isn't the point in question AND it is within range, add it
            if ((includeGivenPoint || node.getPoint().getID() != p.getID()) && Dimension.distance(p, node.getPoint()) <= Math.pow(r, 2.0)) {
                l.add(node.getPoint());
            }
            // goal is smaller, search left subtree
            if (p.getCoord(axis) < node.getPointCoord(axis)) {
                searchL = true;
                findAllWithinRadiusRec(node.left, l, includeGivenPoint, p, r, k, depth+1);
            }
            // goal is larger, search right subtree
            else {
                findAllWithinRadiusRec(node.right, l, includeGivenPoint, p, r, k, depth+1);
            }
            // maybe check other, unsearched subtree
            if (Math.abs(node.getPointCoord(axis)-p.getCoord(axis)) < r) {
                if (searchL) {
                    findAllWithinRadiusRec(node.right, l, includeGivenPoint, p, r, k, depth+1);
                }
                else {
                    findAllWithinRadiusRec(node.left, l, includeGivenPoint, p, r, k, depth+1);
                }
            }
        }
    }
}
