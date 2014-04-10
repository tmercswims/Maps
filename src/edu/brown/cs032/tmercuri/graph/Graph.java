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
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * A "Graph," that does not use any Nodes or Vertices.
 * @author Thomas Mercurio
 * @param <T> the type of this graph
 */
public class Graph<T> {
    
    private final Graphable<T> g;
    
    /**
     * A new Graph.
     * @param g a Graphable to use for looking up things
     */
    public Graph(Graphable<T> g) {
        this.g = g;
    }
    
    /**
     * Gets the latitude of this point.
     * @param p
     * @return
     * @throws IOException
     */
    public double getLat(T p) throws IOException {
        return g.getLat(p);
    }
    
    /**
     * Gets the longitude of this node.
     * @param p
     * @return
     * @throws IOException
     */
    public double getLng(T p) throws IOException {
        return g.getLng(p);
    }
    
    /**
     * Gets the ways that cross this node.
     * @param ID
     * @return
     * @throws IOException
     */
    public List<String> getWaysThatCrossNode(T ID) throws IOException {
        return g.getWaysThatCrossNode(ID);
    }
    
    /**
     * Gets the start node of a way.
     * @param ID
     * @return
     * @throws IOException
     */
    public String getStartOfWay(T ID) throws IOException {
        return g.getStartOfWay(ID);
    }
    
    /**
     * Gets the end node of way.
     * @param ID
     * @return
     * @throws IOException
     */
    public String getEndOfWay(T ID) throws IOException {
        return g.getEndOfWay(ID);
    }
    
    /**
     * Gets the name of street.
     * @param ID
     * @return
     * @throws IOException
     */
    public T getWayName(T ID) throws IOException {
        return g.getStreetName(ID);
    }
    
    /**
     * Gets every way that has at least one end inside the box whose top left corner is (topLat, topLng) and bottom right corner is (botLat, botLng).
     * @param topLat
     * @param botLat
     * @param topLng
     * @param botLng
     * @return
     * @throws IOException
     */
    public List<List<String>> getBetween(Double topLat, Double botLat, Double topLng, Double botLng) throws IOException {
        return g.getBetween(topLat, botLat, topLng, botLng);
    }
    
    /**
     * Finds the node of the intersection of street and crossStreet.
     * @param street
     * @param crossStreet
     * @return
     * @throws IOException
     */
    public String findIntersection(T street, T crossStreet) throws IOException {
        List<String> nodesOnStreet = g.getNodesOnStreet(street);
        List<String> nodesOnCrossStreet = g.getNodesOnStreet(crossStreet);

        
        // intersect the two lists
        for (int i = nodesOnStreet.size() - 1; i > -1; i--) {
            String str = nodesOnStreet.get(i);
            if (!nodesOnCrossStreet.remove(str)) {
                nodesOnStreet.remove(str);
            }
        }
        return nodesOnStreet.get(0);
    }
    
    /**
     * Uses a modified Dijkstra's algorithm to find to shortest path from n1 to nN. Stops when nN has been reached in the shortest way possible.
     * @param sourceID the source
     * @param targetID the target
     * @return the path
     * @throws IOException if any file cannot be read
     */
    public synchronized List<List<T>> computePath(T sourceID, T targetID) throws IOException {
        final Map<T,Double> minDistance = new HashMap<>();
        Map<T,T> minEdge = new HashMap<>();
        Map<T,T> previous = new HashMap<>();
        
        // get the ID versions of the names
        
        // if one is null, it is not in the graph
        if (sourceID == null) {
            throw new IllegalArgumentException("source '" + sourceID + "' not present");
        } if (targetID == null) {
            throw new IllegalArgumentException("target '" + targetID + "' not present");
        }
        
        // min distance to the source is 0
        minDistance.put(sourceID, 0.0);
        // priority queue for faster looping
        Queue<T> q = new PriorityQueue<>(512, new Comparator<T>() {
            @Override
            // compares based on the min distance to this node from the source
            public int compare(T o1, T o2) {
                double distToo1 = ((minDistance.get(o1) != null) ? minDistance.get(o1) : Double.POSITIVE_INFINITY);
                double distToo2 = ((minDistance.get(o2) != null) ? minDistance.get(o2) : Double.POSITIVE_INFINITY);
                return Double.compare(distToo1, distToo2);
            }
        });
        // add the source
        q.add(sourceID);
        
        boolean done = false;
        // as long as the queue isn't empty
        while (!q.isEmpty() && !done) {
            // pop the closest thing
            T u = q.poll();
            // if it is what we want, then we're done
            if (u.equals(targetID)) {
                done = true;
            }
            
            // for every neighbor...
            for (RelationInfo<T> v : g.getNeighbors(u)) {
                // extract info from the RelationInfo
                T edge = v.getEdge();
                double weight = v.getWeight();
                // possible alt path
                double distViaU = minDistance.get(u) + weight;
                
                // if the nalt path is shorter, replace the information stored with this new path
                if (distViaU < ((minDistance.get(v.getVID()) != null) ? minDistance.get(v.getVID()) : Double.POSITIVE_INFINITY)) {
                    q.remove(v.getVID());
                    minEdge.put(v.getVID(), edge);
                    minDistance.put(v.getVID(), distViaU);
                    previous.put(v.getVID(), u);
                    q.add(v.getVID());
                    
                }
            }
        }
        return returnShortestPathToFrom(targetID, sourceID, minDistance, minEdge, previous);
    }
    
    private double heuristic(T from, double targetLat, double targetLng) throws IOException {
        double dx = g.getLat(from) - targetLat;
        double dy = g.getLng(from) - targetLng;
        
        return Math.sqrt((dx*dx)+(dy*dy));
    }
    
    private List<List<T>> returnShortestPathToFrom(final T targetID, final T sourceID, final Map<T,Double> minDistance, Map<T,T> minEdge, Map<T,T> previous) throws IOException {
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
