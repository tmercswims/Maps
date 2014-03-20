/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.gui;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author Thomas Mercurio
 */
public class MapsFrame extends JFrame {
    
    static final long serialVersionUID = 69L;
    
    private MapData map;
    private final Map<String, MapWay> mapWays;
    private final Executor pool = new ThreadPoolExecutor(4, 8, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    private AutoCorrectedField one, two, three, four;
    
    public MapsFrame(MapData map) {
        super("Maps");
        this.map = map;
        this.mapWays = new HashMap<>();
        createAndShowGUI();
    }
    
    private void createAndShowGUI() {
        JPanel mainPanel = new MapPanel(new BorderLayout());
        
        JPanel inputArea = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel inputFields = new JPanel(new GridLayout(1, 4, 3, 3));
        
        one = new AutoCorrectedField(map, "Street 1");
        two = new AutoCorrectedField(map, "Cross-Street 1");
        three = new AutoCorrectedField(map, "Street 2");
        four = new AutoCorrectedField(map, "Cross-Street 2");
        
        ActionListener mapsListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!one.getText().equals("") && !two.getText().equals("") && !three.getText().equals("") && !four.getText().equals("")) {
                    pool.execute(new PathFinder(map, one.getText(), two.getText(), three.getText(), four.getText(), null));
                }
            }
        };
        
        JButton goButton = new JButton("Go");
        goButton.setToolTipText("Calculate Route");
        
        one.addActionListener(mapsListener);
        two.addActionListener(mapsListener);
        three.addActionListener(mapsListener);
        four.addActionListener(mapsListener);
        goButton.addActionListener(mapsListener);
        
        inputFields.add(one);
        inputFields.add(two);
        inputFields.add(three);
        inputFields.add(four);
        
        inputFields.setOpaque(false);
        inputFields.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        inputArea.add(inputFields, c);
        c.anchor = GridBagConstraints.NORTHEAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridx = 1;
        inputArea.add(goButton, c);
        
        inputArea.setOpaque(false);
        inputArea.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        
        JPanel right = new JPanel(new BorderLayout());
        JPanel zoomPanel = new JPanel(new GridLayout(3, 1, 3, 3));
        JButton zoomIn = new JButton("+");
        zoomIn.setToolTipText("Zoom In");
        JButton zoomDefault = new JButton("o");
        zoomDefault.setToolTipText("Default Zoom");
        JButton zoomOut = new JButton("-");
        zoomOut.setToolTipText("Zoom Out");
        zoomPanel.setOpaque(false);
        
        zoomPanel.add(zoomIn);
        zoomPanel.add(zoomDefault);
        zoomPanel.add(zoomOut);
        right.setOpaque(false);
        
        right.add(zoomPanel, BorderLayout.SOUTH);
        right.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 3));
        
        mainPanel.add(inputArea, BorderLayout.NORTH);
        mainPanel.add(right, BorderLayout.EAST);
        
        add(mainPanel);
        pack();
        setSize(new Dimension(1400, 800));
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        List<Image> icons = new ArrayList<>();
        URL iconURL = getClass().getResource("/edu/brown/cs032/tmercuri/ja11/maps/gui/icon16.png");
        icons.add(new ImageIcon(iconURL).getImage());
        iconURL = getClass().getResource("/edu/brown/cs032/tmercuri/ja11/maps/gui/icon32.png");
        icons.add(new ImageIcon(iconURL).getImage());
        iconURL = getClass().getResource("/edu/brown/cs032/tmercuri/ja11/maps/gui/icon64.png");
        icons.add(new ImageIcon(iconURL).getImage());
        iconURL = getClass().getResource("/edu/brown/cs032/tmercuri/ja11/maps/gui/icon128.png");
        icons.add(new ImageIcon(iconURL).getImage());
        setIconImages(icons);

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void setMap(MapData newMap) {
        this.map = newMap;
        one.setMap(newMap);
        one.setEnabled(true);
        two.setMap(newMap);
        two.setEnabled(true);
        three.setMap(newMap);
        three.setEnabled(true);
        four.setMap(newMap);
        four.setEnabled(true);
    }
}
