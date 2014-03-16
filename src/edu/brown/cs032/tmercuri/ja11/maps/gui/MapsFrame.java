/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.gui;

import edu.brown.cs032.tmercuri.ja11.maps.backend.Map;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    
    private Map map;
    
    public MapsFrame(Map map) {
        super("Maps");
        this.map = map;
        createAndShowGUI();
    }
    
    private void createAndShowGUI() {
        JPanel mainPanel = new MapPanel(new BorderLayout());
        
        JPanel inputArea = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel inputFields = new JPanel(new GridLayout(1, 4, 3, 3));
        
        final AutoCorrectedField one, two, three, four;
        one = new AutoCorrectedField();
        two = new AutoCorrectedField();
        three = new AutoCorrectedField();
        four = new AutoCorrectedField();
        
        ActionListener mapsListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!one.getText().equals("") && !two.getText().equals("") && !three.getText().equals("") && !four.getText().equals("")) {
                    System.out.println("*****THIS NEEDS TO BE THREADED STILL*****");
                    List<String>[] path;
                    try {
                        path = map.getPath(one.getText(), two.getText(), three.getText(), four.getText());
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                    }
                    //TODO: DO SOMETHING WITH THAT PATH
                    //      ALSO THREAD THIS SHIT UP YO
                }
            }
        };
        
        JButton goButton = new JButton("Go");
        
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
        
        mainPanel.add(inputArea, BorderLayout.NORTH);
        
        /*
         * HERE IS WHERE THE MAPS PANEL IS ADDED
         * ...
         * ...
        */
        
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
    
    public void setMap(Map newMap) {
        this.map = newMap;
    }
}
