/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.backend;

import edu.brown.cs032.tmercuri.TSV.TSVBinarySearch;
import edu.brown.cs032.tmercuri.graph.Graphable;
import edu.brown.cs032.tmercuri.graph.RelationInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Thomas Mercurio
 */
public class MapsGraphable implements Graphable<String> {

    private final TSVBinarySearch ways, nodes, index;
    
    /**
     *
     * @param ways
     * @param nodes
     * @param index
     */
    public MapsGraphable(TSVBinarySearch ways, TSVBinarySearch nodes, TSVBinarySearch index) {
        this.ways = ways;
        this.nodes = nodes;
        this.index = index;
    }
    
    /**
     * Given a name, gives an ID.
     * @param name the name to look up
     * @return the ID of name
     * @throws IOException if the file was unable to be read
     */
    @Override
    public String getID(String name) throws IOException {
        return index.lookup(name, "name", "nodes");
    }
    
    /**
     * Gets every node on a street called name.
     * @param name
     * @return
     * @throws IOException
     */
    @Override
    public ArrayList<String> getNodesOnStreet(String name) throws IOException {
        return new ArrayList<>(Arrays.asList(index.lookup(name, "name", "nodes").split(",")));
    }
    
    /**
     * Gets every way that crosses this node.
     * @param ID
     * @return
     * @throws IOException
     */
    @Override
    public List<String> getWaysThatCrossNode(String ID) throws IOException {
        return Arrays.asList(nodes.lookup(ID, "id", "ways").split(","));
    }
    
    /**
     * Gets the node of the start of a way.
     * @param ID
     * @return
     * @throws IOException
     */
    @Override
    public String getStartOfWay(String ID) throws IOException {
        return ways.lookup(ID, "id", "start");
    }
    
    /**
     * Gets the node of the end of a way.
     * @param ID
     * @return
     * @throws IOException
     */
    @Override
    public String getEndOfWay(String ID) throws IOException {
        return ways.lookup(ID, "id", "end");
    }
    
    /**
     * Gets the lat of this node.
     * @param ID
     * @return
     * @throws IOException
     */
    @Override
    public double getLat(String ID) throws IOException {
        return Double.parseDouble(nodes.lookup(ID, "id", "latitude"));
    }
    
    /**
     * Gets the long of this node.
     * @param ID
     * @return
     * @throws IOException
     */
    @Override
    public double getLng(String ID) throws IOException {
        return Double.parseDouble(nodes.lookup(ID, "id", "longitude"));
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
    @Override
    public List<List<String>> getBetween(Double topLat, Double botLat, Double topLng, Double botLng) throws IOException {
        return ways.getAllBetween("/w/"+botLat.toString().replaceAll("\\.", "").replaceAll("-", "").substring(0, 4)+"."+botLng.toString().replaceAll("\\.", "").replaceAll("-", "").substring(0, 4), "/w/"+topLat.toString().replaceAll("\\.", "").replaceAll("-", "").substring(0, 4)+"."+topLng.toString().replaceAll("\\.", "").replaceAll("-", "").substring(0, 4), "id");
    }

    /**
     * Given an ID, gets a name.
     * @param ID the ID to lookup
     * @return the name of the ID
     * @throws IOException if the file was unable to be read
     */
    @Override
    public String getStreetName(String ID) throws IOException {
        return ways.lookup(ID, "id", "name");
    }

    /**
     * Gives all neighbors of the given name
     * @param ID the ID to find neighbors of
     * @return a List of RelationInfos, one for each neighbor
     * @throws IOException if the file was unable to be read
     */
    @Override
    public List<RelationInfo<String>> getNeighbors(String ID) throws IOException {
        String[] waysFromNode = nodes.lookup(ID, "id", "ways").split(",");
        double srcLat = Double.parseDouble(nodes.lookup(ID, "id", "latitude"));
        double srcLng = Double.parseDouble(nodes.lookup(ID, "id", "longitude"));
        
        if (waysFromNode == null) {
            return null;
        }
        
        List<RelationInfo<String>> list = new ArrayList<>();
        for (String wayID : waysFromNode) {
            String startID = ways.lookup(wayID, "id", "start");
            String endID = ways.lookup(wayID, "id", "end");
            
            double neighLat, neighLng;
            String dest = "";
            if (startID.equals(ID)) {
                neighLat = Double.parseDouble(nodes.lookup(endID, "id", "latitude"));
                neighLng = Double.parseDouble(nodes.lookup(endID, "id", "longitude"));
                dest = endID;
            } else if (endID.equals(ID)) {
                neighLat = Double.parseDouble(nodes.lookup(startID, "id", "latitude"));
                neighLng = Double.parseDouble(nodes.lookup(startID, "id", "longitude"));
                dest = startID;
            } else {
                throw new RuntimeException("shouldn't happen");
            }
            
            list.add(new MapsRelationInfo(dest, wayID, Math.sqrt(Math.pow(srcLat-neighLat,2)+Math.pow(srcLng-neighLng,2))));
        }
        
        return list;
    }

    /**
     * Closes all files associated with this object. Object becomes unusable after this is called.
     * @throws IOException if the file was unable to be closed
     */
    @Override
    public void close() throws IOException {
        ways.close();
        nodes.close();
        index.close();
    }
}
