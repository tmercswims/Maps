/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * A "Graph," that does not use any Nodes or Vertices.
 * @author Thomas Mercurio
 * @param <T> the type of this graph
 */
public class Graph<T> {
    
    private final Graphable<T> g;
    private final Map<T,Double> minDistance;
    private final Map<T,T> minEdge, previous;
    
    /**
     * A new Graph.
     * @param g a Graphable to use for looking up things
     */
    public Graph(Graphable<T> g) {
        this.g = g;
        this.minDistance = new HashMap<>();
        this.minEdge = new HashMap<>();
        this.previous = new HashMap<>();
    }
    
    public double getLat(T p) throws IOException {
        return g.getLat(p);
    }
    
    public double getLng(T p) throws IOException {
        return g.getLng(p);
    }
    
    public String findIntersection(T street, T crossStreet) throws IOException {
        List<String> nodesOnStreet = g.getNodesOnStreet(street);
        List<String> nodesOnCrossStreet = g.getNodesOnStreet(crossStreet);
        
        // intersect the two lists
        for (int i = nodesOnStreet.size() - 1; i > -1; --i){
            String str = nodesOnStreet.get(i);
            if (!nodesOnCrossStreet.remove(str)) {
                nodesOnStreet.remove(str);
            }
        }
        
        System.out.println("Number of intersections: " + nodesOnStreet.size());
        
        return nodesOnStreet.get(0);
    }
    
    /**
     * Uses a modified A* algorithm to find to shortest path from n1 to nN.
     * @param sourceID the source
     * @param targetID the target
     * @throws IOException if any file cannot be read
     */
    public void computePathA(T sourceID, T targetID) throws IOException {
        final double targetLat = g.getLat(targetID);
        final double targetLng = g.getLng(targetID);
        
        // if one is null, it is not in the graph
        if (sourceID == null) {
            throw new IllegalArgumentException("source '" + sourceID + "' not present");
        } if (targetID == null) {
            throw new IllegalArgumentException("target '" + targetID + "' not present");
        }
        
        // min distance to the source is 0
        minDistance.put(sourceID, 0.0);
        // priority queue for faster looping
        Queue<T> open = new PriorityQueue<>(512, new Comparator<T>() {
            @Override
            // compares based on the min distance to this node from the source
            public int compare(T o1, T o2) {
                double distToo1, distToo2;
                distToo1 = distToo2 = 0.0;
                try {
                    distToo1 = ((minDistance.get(o1) != null) ? minDistance.get(o1) : Double.POSITIVE_INFINITY) + heuristic(o1, targetLat, targetLng);
                    distToo2 = ((minDistance.get(o2) != null) ? minDistance.get(o2) : Double.POSITIVE_INFINITY) + heuristic(o2, targetLat, targetLng);
                } catch (IOException ex) {
                    System.err.println("ERROR: " + ex.getMessage());
                }
                return Double.compare(distToo1, distToo2);
            }
        });
        Set<T> closed = new HashSet<>();
        
        // add the source to open
        open.add(sourceID);
        
        while (!open.isEmpty() && !open.peek().equals(targetID)) {
            T current = open.poll();
            closed.add(current);
            
            for (RelationInfo<T> neighbor : g.getNeighbors(current)) {
                double cost = minDistance.get(current) + neighbor.getWeight();
                
                if (open.contains(neighbor.getVID()) && cost < ((minDistance.get(neighbor.getVID()) != null) ? minDistance.get(neighbor.getVID()) : Double.POSITIVE_INFINITY)) {
                    open.remove(neighbor.getVID());
                } else if (closed.contains(neighbor.getVID()) && cost < ((minDistance.get(neighbor.getVID()) != null) ? minDistance.get(neighbor.getVID()) : Double.POSITIVE_INFINITY)) {
                    closed.remove(neighbor.getVID());
                } else if (!open.contains(neighbor.getVID()) && !closed.contains(neighbor.getVID())) {
                    minDistance.put(neighbor.getVID(), cost);
                    open.add(neighbor.getVID());
                    previous.put(neighbor.getVID(), current);
                    minEdge.put(neighbor.getVID(), neighbor.getEdge());
                }
            }
        }
    }
    
    private double heuristic(T from, double targetLat, double targetLng) throws IOException {
        double dx = g.getLat(from) - targetLat;
        double dy = g.getLng(from) - targetLng;
        
        return Math.sqrt((dx*dx)+(dy*dy));
    }
    
    /**
     * Prints the path from targetName to sourceName
     * @param targetID
     * @param sourceID
     * @throws IOException 
     */
    public void printShortestPathToFromA(T targetID, T sourceID) throws IOException {
        // Lists for edges and nodes on the path
        List<T> edges = new ArrayList<>();
        List<T> nodes = new ArrayList<>();
        
        // contruct the path, starting form the target and following the previouses
        for (T n=targetID; n!=null; n=previous.get(n)) {
            nodes.add(n);
            edges.add(minEdge.get(n));
        }
        // the Lists need to be reversed
        Collections.reverse(nodes);
        Collections.reverse(edges);
        
        previous.clear();
        minDistance.clear();
        minEdge.clear();
        
        // if the first node is not the given source or the target and source are the same, then a path doesn't exist
        if (!nodes.get(0).equals(sourceID) || targetID.equals(sourceID)) {
            System.out.println(sourceID + " -/- " + targetID);
        } // print the path, one connection at a time
        else {
            for (int i=1; i<nodes.size(); i++) {
                System.out.println(nodes.get(i-1) + " -> " + nodes.get(i) + " : " + edges.get(i));
            }
        }
    }
    
    public List<List<T>> returnShortestPathToFromA(final T targetID, final T sourceID) throws IOException {
        final List<T> edges = new ArrayList<>();
        final List<T> nodes = new ArrayList<>();
        final List<T> endpoints = new ArrayList<>();
        endpoints.add(sourceID);
        endpoints.add(targetID);
        
        // contruct the path, starting form the target and following the previouses
        for (T n=targetID; n!=null; n=previous.get(n)) {
            nodes.add(n);
            edges.add(minEdge.get(n));
        }
        // the Lists need to be reversed
        Collections.reverse(nodes);
        Collections.reverse(edges);
        
        previous.clear();
        minDistance.clear();
        minEdge.clear();
        
        List<List<T>> ret = new ArrayList<>();
        ret.add(edges);
        ret.add(nodes);
        ret.add(endpoints);
        
        return ret;
    }
    
    /**
     * Closes the files associated with this object. Object becomes useless after this is called.
     * @throws IOException if any file cannot be accessed
     */
    public void close() throws IOException {
        g.close();
    }
}
