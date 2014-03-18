/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

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
public class PathFinder implements Runnable {
    
    private final MapData map;
    private final String s1, cs1, s2, cs2;
    private final Runnable runAfter;
    
    public PathFinder(MapData map, String s1, String cs1, String s2, String cs2, Runnable runAfter) {
        this.map = map;
        this.s1 = s1;
        this.cs1 = cs1;
        this.s2 = s2;
        this.cs2 = cs2;
        this.runAfter = runAfter;
    }

    @Override
    public void run() {
        List<List<String>> path;
        try {
            path = map.getPath(s1, cs1, s2, cs2);
            List<String> ways = path.get(0);
            
            List<MapWay> mapWays = map.wayIDsToMapWays(ways);
            
            for (MapWay mw : mapWays) {
                mw.setColor(Color.blue);
            }
            
            SwingUtilities.invokeLater(runAfter);
        } catch (IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }
    }
}
