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
 * A class for all the data, data structures, and operations that a map needs.
 */
public class MapData {
    
    private final KDTree<Dimension<String>, String> tree;
    private final Graph<String> graph;
    private final Autocorrecter autocorrecter;
    
    /**
     * A MapData for the given data files.
     * @param waysFilename
     * @param nodesFilename
     * @param indexFilename
     * @throws IOException
     */
    public MapData(String waysFilename, String nodesFilename, String indexFilename) throws IOException {
        this.tree = new SlowTree<>(getPointsForTree(nodesFilename));
        this.graph = new Graph<>(getGraphableForGraph(waysFilename, nodesFilename, indexFilename));
        this.autocorrecter = new Autocorrecter(indexFilename);
    }
    
    /**
     * Gets the path between the intersection of st1 and cst1 and the intersection of st2 and cst2.
     * @param st1
     * @param cst1
     * @param st2
     * @param cst2
     * @return
     * @throws IOException
     */
    public List<List<String>> getPath(String st1, String cst1, String st2, String cst2) throws IOException {
        String inter1 = graph.findIntersection(st1, cst1);
        System.out.println("inter1 is "+ inter1);
        String inter2 = graph.findIntersection(st2, cst2);
        System.out.println("inter2 is "+ inter2);
        return graph.computePathA(inter1, inter2);
    }
    
    /**
     * Gets the path between the points closest to (lat1, lng1) and (lat2, lng2).
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     * @throws IOException
     */
    public List<List<String>> getPath(double lat1, double lng1, double lat2, double lng2) throws IOException {
        String inter1 = getNearestPointTo(lat1, lng1);
        System.out.println("inter1 is "+ inter1);
        String inter2 = getNearestPointTo(lat2, lng2);
        System.out.println("inter2 is "+ inter2);
        return graph.computePathA(inter1, inter2);
    }
    
    /**
     * Gets every way that has at least one end inside the box whose top left corner is (topLat, topLng) and bottom right corner is (botLat, botLng).
     * @param topLat
     * @param topLng
     * @param botLat
     * @param botLng
     * @return
     * @throws IOException
     */
    public synchronized List<MapWay> getAllBetween(Double topLat, Double topLng, Double botLat, Double botLng) throws IOException {
        List<MapWay> mapWays = new ArrayList<>();
        
        List<List<String>> wayFileLines = graph.getBetween(botLat, topLat, topLng, botLng);
        int idIndex = wayFileLines.get(0).indexOf("id");
        int startIndex = wayFileLines.get(0).indexOf("start");
        int endIndex = wayFileLines.get(0).indexOf("end");
        int nameIndex = wayFileLines.get(0).indexOf("name");
        
        wayFileLines.remove(0);
        
        for (List<String> fileLine : wayFileLines) {
            Double startLng = graph.getLng(fileLine.get(startIndex));
            Double endLng = graph.getLng(fileLine.get(endIndex));
            mapWays.add(new MapWay(fileLine.get(idIndex), fileLine.get(startIndex), graph.getLat(fileLine.get(startIndex)), startLng, fileLine.get(endIndex), graph.getLat(fileLine.get(endIndex)), endLng, fileLine.get(nameIndex)));
        }
        
        return mapWays;
    }
    
    /**
     * Uses the kdtree to find the nearest point to (lat, lng).
     * @param lat
     * @param lng
     * @return
     * @throws IOException
     */
    public LatLng getNearestPoint(double lat, double lng) throws IOException {
        LatLng ref = new LatLng("", lat, lng);
        String nodeID = tree.findNearestNeighbor(ref);
        return new LatLng(nodeID, graph.getLat(nodeID), graph.getLng(nodeID));
    }
    
    /**
     * Turns a list of way IDs into a list of MapWays.
     * @param IDList
     * @return
     * @throws IOException
     */
    public List<MapWay> wayIDsToMapWays(List<String> IDList) throws IOException {
        List<MapWay> ways = new ArrayList<>();
        
        for (String way : IDList) {
            String start = graph.getStartOfWay(way);
            String end = graph.getEndOfWay(way);
            ways.add(new MapWay(way, start, graph.getLat(start), graph.getLng(start), end, graph.getLat(end), graph.getLng(end), graph.getWayName(way)));
        }
        
        return ways;
    }
    
    /**
     * Uses the autocorrect to get suggestions for a string.
     * @param input
     * @return
     */
    public List<String> getSuggestions(String input){
    	return autocorrecter.getResults(input);
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
    

}
