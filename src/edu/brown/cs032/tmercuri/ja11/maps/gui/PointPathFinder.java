package edu.brown.cs032.tmercuri.ja11.maps.gui;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 *
 * @author Thomas Mercurio
 */
public class PointPathFinder implements Runnable {
    
    private final MapData map;
    private final MapPanel mp;
    private final double s1, cs1, s2, cs2;
    
    /**
     *
     * @param map
     * @param mp
     * @param s1
     * @param cs1
     * @param s2
     * @param cs2
     */
    public PointPathFinder(MapData map, MapPanel mp, double s1, double cs1, double s2, double cs2) {
        this.map = map;
        this.s1 = s1;
        this.cs1 = cs1;
        this.s2 = s2;
        this.cs2 = cs2;
        this.mp = mp;
    }

    @Override
    public void run() {
        List<List<String>> path;
        try {
            path = map.getPath(s1, cs1, s2, cs2);
            System.out.println(path);
            List<String> ways = path.get(0);
          //  System.out.println(ways);
            List<MapWay> mapWays = map.wayIDsToMapWays(ways.subList(1, ways.size()));
           // System.out.println(mapWays);
            for (MapWay mw : mapWays) {
                mw.setColor(Color.blue);
            }
            
            SwingUtilities.invokeLater(new Painter(mp, mapWays));
        } catch (IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }
    
}
