/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gives methods for a Graph to perform Dijkstra's algorithm, without actually constructing a graph wit Vertices and Nodes.
 * @author Thomas Mercurio
 * @param <T> the type of this Graphable
 */
public interface Graphable<T> {

    /**
     * Given a name, gives an ID.
     * @param name the name to look up
     * @return the ID of name
     * @throws IOException if the file was unable to be read
     */
    public T getID(T name) throws IOException;
    
    /**
     * Given an ID, gets a name.
     * @param ID the ID to lookup
     * @return the name of the ID
     * @throws IOException if the file was unable to be read
     */
    public T getStreetName(T ID) throws IOException;
    
    /**
     * Gets every node on a street called name.
     * @param name
     * @return
     * @throws IOException
     */
    public ArrayList<String> getNodesOnStreet(T name) throws IOException;
    
    /**
     * Gets every way that crosses this node.
     * @param ID
     * @return
     * @throws IOException
     */
    public List<String> getWaysThatCrossNode(T ID) throws IOException;
    
    /**
     * Gets the node of the start of a way.
     * @param ID
     * @return
     * @throws IOException
     */
    public String getStartOfWay(T ID) throws IOException;
    
    /**
     * Gets the node of the end of a way.
     * @param ID
     * @return
     * @throws IOException
     */
    public String getEndOfWay(T ID) throws IOException;
    
    /**
     * Gets the lat of this node.
     * @param ID
     * @return
     * @throws IOException
     */
    public double getLat(T ID) throws IOException;
    
    /**
     * Gets the long of this node.
     * @param ID
     * @return
     * @throws IOException
     */
    public double getLng(T ID) throws IOException;
    
    /**
     * Gets every way that has at least one end inside the box whose top left corner is (topLat, topLng) and bottom right corner is (botLat, botLng).
     * @param topLat
     * @param botLat
     * @param topLng
     * @param botLng
     * @return
     * @throws IOException
     */
    public List<List<String>> getBetween(Double topLat, Double topLng, Double botLat, Double botLng) throws IOException;
    
    /**
     * Gives all neighbors of the given name
     * @param ID the ID to find neighbors of
     * @return a List of RelationInfos, one for each neighbor
     * @throws IOException if the file was unable to be read
     */
    public List<RelationInfo<T>> getNeighbors(T ID) throws IOException;
    
    /**
     * Closes all files associated with this object. Object becomes unusable after this is called.
     * @throws IOException if the file was unable to be closed
     */
    public void close() throws IOException;
}
