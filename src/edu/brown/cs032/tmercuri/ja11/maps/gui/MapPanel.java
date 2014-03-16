/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.gui;

import java.awt.Color;
import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 *
 * @author Thomas Mercurio
 */
public class MapPanel extends JPanel {
    
    static final long serialVersionUID = 420L;
    
    public MapPanel(LayoutManager manager) {
        super(manager);
        setBackground(Color.yellow);
    }
}
