/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.backend;

import edu.brown.cs032.ja11.autocorrect.frontend.Autocorrecter;
import edu.brown.cs032.tmercuri.TSV.TSVBinarySearch;
import edu.brown.cs032.tmercuri.TSV.TSVReader;
import edu.brown.cs032.tmercuri.graph.Graph;
import edu.brown.cs032.tmercuri.graph.Graphable;
import edu.brown.cs032.tmercuri.ja11.maps.gui.MapWay;
import edu.brown.cs032.tmercuri.kdtree.Dimension;
import edu.brown.cs032.tmercuri.kdtree.KDTree;
import edu.brown.cs032.tmercuri.kdtree.SlowTree;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Mercurio
 */
public class MapData {
    
    private final KDTree<Dimension<String>, String> tree;
    private final Graph<String> graph;
    private final Autocorrecter autocorrecter;
    
    public MapData(String waysFilename, String nodesFilename, String indexFilename) throws IOException {
        this.tree = new SlowTree<>(getPointsForTree(nodesFilename));
        this.graph = new Graph<>(getGraphableForGraph(waysFilename, nodesFilename, indexFilename));
        this.autocorrecter = new Autocorrecter(indexFilename);
    }
    
    public List<List<String>> getPath(String st1, String cst1, String st2, String cst2) throws IOException {
        String inter1 = graph.findIntersection(st1, cst1);
        String inter2 = graph.findIntersection(st2, cst2);
        graph.computePathA(inter1, inter2);
        return graph.returnShortestPathToFromA(inter1, inter2);
    }
    
    public List<List<String>> getPath(double lat1, double lng1, double lat2, double lng2) throws IOException {
        String inter1 = getNearestPointTo(lat1, lng1);
        String inter2 = getNearestPointTo(lat2, lng2);
        System.out.println("inter1 "+inter1);
        System.out.println("inter2 "+inter2);
        graph.computePathA(inter1, inter2);
        return graph.returnShortestPathToFromA(inter2, inter1);
    }
    
    public List<MapWay> getAllWithinRadius(int r, String refID) throws IOException {
        List<String> nodeIDs = tree.findAllWithinRadius(r, true, new LatLng("", graph.getLat(refID), graph.getLng(refID)));
        List<MapWay> ways = new ArrayList<>();
        
        for (String node : nodeIDs) {
            List<String> waysOnNode = graph.getWaysThatCrossNode(node);
            for (String way : waysOnNode) {
                String start = graph.getStartOfWay(way);
                String end = graph.getEndOfWay(way);
                ways.add(new MapWay(way, start, graph.getLat(start), graph.getLng(start), end, graph.getLat(end), graph.getLng(end), graph.getWayName(way)));
            }
        }
        
        return ways;
    }
    
    public List<MapWay> wayIDsToMapWays(List<String> IDList) throws IOException {
        List<MapWay> ways = new ArrayList<>();
        
        for (String way : IDList) {
            String start = graph.getStartOfWay(way);
            String end = graph.getEndOfWay(way);
            ways.add(new MapWay(way, start, graph.getLat(start), graph.getLng(start), end, graph.getLat(end), graph.getLng(end), graph.getWayName(way)));
        }
        
        return ways;
    }
    
    private String getNearestPointTo(double lat, double lng) {
        LatLng ref = new LatLng("", lat, lng);
        return tree.findNearestNeighbor(ref);
    }
    
    
    private List<Dimension<String>> getPointsForTree(String nodesFilename) throws IOException {
        List<Dimension<String>> list = new ArrayList<>();
        
        try (TSVReader t = new TSVReader(nodesFilename)) {
            List<String> IDs = t.getAllEntriesInColumn("id");
            List<String> lats = t.getAllEntriesInColumn("latitude");
            List<String> lngs = t.getAllEntriesInColumn("longitude");
            
            // ensure that all lists are the same size (they should be)
            if (!(IDs.size() == lats.size()) || !(IDs.size() == lngs.size()) || !(lats.size() == lngs.size())) {
                throw new RuntimeException("weird lists, maybe a bad nodes TSV file?");
            }
            
            for (int i=0; i<IDs.size(); i++) {
                list.add(new LatLng(IDs.get(i), Double.parseDouble(lats.get(i)), Double.parseDouble(lngs.get(i))));
            }
        }
        
        return list;
    }
    
    private Graphable<String> getGraphableForGraph(String waysFilename, String nodesFilename, String indexFilename) throws IOException {
        return new MapsGraphable(new TSVBinarySearch(waysFilename), new TSVBinarySearch(nodesFilename), new TSVBinarySearch(indexFilename));
    }
    
    public List<String> getSuggestions(String input){
    	return autocorrecter.getResults(input);
    }
    
}
