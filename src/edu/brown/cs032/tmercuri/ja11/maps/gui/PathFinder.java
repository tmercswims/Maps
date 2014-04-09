package edu.brown.cs032.tmercuri.ja11.maps.gui;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 * A runnable that can find a path, and then tells the map to display it.
 */
public class PathFinder implements Runnable {
    
    private final MapData map;
    private final MapPanel mp;
    private final String s1, cs1, s2, cs2;
    
    /**
     *
     * @param map
     * @param mp
     * @param s1
     * @param cs1
     * @param s2
     * @param cs2
     */
    public PathFinder(MapData map, MapPanel mp, String s1, String cs1, String s2, String cs2) {
        this.map = map;
        this.mp = mp;
        this.s1 = s1;
        this.cs1 = cs1;
        this.s2 = s2;
        this.cs2 = cs2;
    }

    @Override
    public void run() {
        List<List<String>> path;
        try {
            path = map.getPath(s1, cs1, s2, cs2);
            List<String> ways = path.get(0);
            List<MapWay> mapWays = map.wayIDsToMapWays(ways.subList(1, ways.size()));
            
            for (MapWay mw : mapWays) {  
                mw.setColor(Color.blue);
            }
            
            SwingUtilities.invokeLater(new Painter(mp, mapWays));
        } catch (IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }
}
