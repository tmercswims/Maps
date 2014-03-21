/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.gui;

import java.util.List;

/**
 *
 * @author Thomas Mercurio
 */
public class PointAdder implements Runnable {
    
    private final MapPanel mp;
    private final List<MapWay> newWays;
    
    public PointAdder(MapPanel mp, List<MapWay> newWays) {
        this.mp = mp;
        this.newWays = newWays;
    }

    @Override
    public void run() {
        mp.addWays(newWays);
    }
}
