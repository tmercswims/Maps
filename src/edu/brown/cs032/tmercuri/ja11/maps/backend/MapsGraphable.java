/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.brown.cs032.tmercuri.ja11.maps.backend;

import edu.brown.cs032.tmercuri.TSV.TSVBinarySearch;
import edu.brown.cs032.tmercuri.graph.Graphable;
import edu.brown.cs032.tmercuri.graph.RelationInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MapsGraphable implements Graphable<String> {
    
    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;

    private final TSVBinarySearch ways, nodes, index;
    
    public MapsGraphable(TSVBinarySearch ways, TSVBinarySearch nodes, TSVBinarySearch index) {
        this.ways = ways;
        this.nodes = nodes;
        this.index = index;
    }
    
    @Override
    public String getID(String name) throws IOException {
        return index.lookup(name, "name", "nodes");
    }
    
    @Override
    public List<String> getNodesOnStreet(String name) throws IOException {
        return Arrays.asList(index.lookup(name, "name", "nodes").split(","));
    }
    
    @Override
    public List<String> getWaysThatCrossNode(String ID) throws IOException {
        return Arrays.asList(nodes.lookup(ID, "id", "ways").split(","));
    }
    
    @Override
    public String getStartOfWay(String ID) throws IOException {
        return ways.lookup(ID, "id", "start");
    }
    
    @Override
    public String getEndOfWay(String ID) throws IOException {
        return ways.lookup(ID, "id", "end");
    }
    
    @Override
    public double getLat(String ID) throws IOException {
        return Double.parseDouble(nodes.lookup(ID, "id", "latitude"));
    }
    
    @Override
    public double getLng(String ID) throws IOException {
        return Double.parseDouble(nodes.lookup(ID, "id", "longitude"));
    }
    
    @Override
    public List<List<String>> getBetweenLats(Double topLat, Double botLat) throws IOException {
        return ways.getAllBetween("/w/"+topLat.toString().substring(0, 4), "/w/"+botLat.toString().substring(0, 4), "id");
    }

    @Override
    public String getStreetName(String ID) throws IOException {
        return ways.lookup(ID, "id", "name");
    }

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

    @Override
    public void close() throws IOException {
        ways.close();
        nodes.close();
        index.close();
    }
}
