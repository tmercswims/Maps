package edu.brown.cs032.tmercuri.ja11.maps.gui;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 *
 */
public class SquareDrawer implements Runnable {
    
    private final MapPanel mp;
    private final MapData map;
    private final double tlLat, tlLng, brLat, brLng;

    public SquareDrawer(MapPanel mp, MapData map, double tlLat, double tlLng, double brLat, double brLng) {
        this.mp = mp;
        this.map = map;
        this.brLat = brLat;
        this.brLng = brLng;
        this.tlLat = tlLat;
        this.tlLng = tlLng;
    }
    
    @Override
    public void run() {
        List<MapWay> newWays = null;
        try {
            newWays = map.getAllBetween(tlLat, tlLng, brLat, brLng);
        } catch (IOException ex) {
            System.err.println("ERROR: in the Squaredrawer" + ex.getMessage());
        }
        SwingUtilities.invokeLater(new PointAdder(mp, newWays));
    }
    
}
