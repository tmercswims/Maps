package edu.brown.cs032.tmercuri.ja11.maps.gui;

import java.util.List;

/**
 * A runnable that gives a path to the map.
 */
public class Painter implements Runnable {
    
    private final MapPanel mp;
    private final List<MapWay> mapWays;

    /**
     *
     * @param mp
     * @param mapWays
     */
    public Painter(MapPanel mp, List<MapWay> mapWays) {
        this.mp = mp;
        this.mapWays = mapWays;
    }
    
    @Override
    public void run() {
        mp.setPath(mapWays);
    }
    
}
