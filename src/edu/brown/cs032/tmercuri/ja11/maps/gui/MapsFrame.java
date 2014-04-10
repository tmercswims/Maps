package edu.brown.cs032.tmercuri.ja11.maps.gui;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * A JFrame that is a map.
 */
public class MapsFrame extends JFrame {
    
    static final long serialVersionUID = 69L;
    
    private MapData map;
    private final Map<String, MapWay> mapWays;
    private final Executor pool = Executors.newFixedThreadPool(8);
    private AutoCorrectedField one, two, three, four;
    MapPanel mainPanel;
    
    /**
     * A MapFrame with an initial map.
     * @param map
     */
    public MapsFrame(MapData map) {
        super("Maps");
        this.map = map;
        this.mapWays = new HashMap<>();
        mainPanel = new MapPanel(new BorderLayout(), pool);
        createAndShowGUI();
    }
    
    private void createAndShowGUI() {
        JPanel master = new JPanel(new BorderLayout());
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
                    pool.execute(new PathFinder(map, mainPanel, one.getText(), two.getText(), three.getText(), four.getText()));
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
        
        master.add(inputArea, BorderLayout.NORTH);
        //master.add(right, BorderLayout.EAST);
        
        mainPanel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON2) {
                    double latPointOne = mainPanel.getLatPointOne();
                    double lngPointOne = mainPanel.getLngPointOne();
                    double latPointTwo = mainPanel.getLatPointTwo();
                    double lngPointTwo = mainPanel.getLngPointTwo();
                    pool.execute(new PointPathFinder(map, mainPanel, latPointOne, lngPointOne, latPointTwo, lngPointTwo));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
        mainPanel.setVisible(true);
        master.add(mainPanel, BorderLayout.CENTER);
        
        add(master);
        pack();
        setSize(new Dimension(1400, 800));
        //setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        /*List<Image> icons = new ArrayList<>();
        URL iconURL = getClass().getResource("/edu/brown/cs032/tmercuri/ja11/maps/gui/icon16.png");
        icons.add(new ImageIcon(iconURL).getImage());
        iconURL = getClass().getResource("/edu/brown/cs032/tmercuri/ja11/maps/gui/icon32.png");
        icons.add(new ImageIcon(iconURL).getImage());
        iconURL = getClass().getResource("/edu/brown/cs032/tmercuri/ja11/maps/gui/icon64.png");
        icons.add(new ImageIcon(iconURL).getImage());
        iconURL = getClass().getResource("/edu/brown/cs032/tmercuri/ja11/maps/gui/icon128.png");
        icons.add(new ImageIcon(iconURL).getImage());
        setIconImages(icons);*/

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Changes the map that this frame uses.
     * @param newMap
     */
    public void setMap(MapData newMap) {
        System.out.println("setting map");
        this.map = newMap;
        one.setMap(newMap);
        one.setEnabled(true);
        two.setMap(newMap);
        two.setEnabled(true);
        three.setMap(newMap);
        three.setEnabled(true);
        four.setMap(newMap);
        four.setEnabled(true);
        mainPanel.setMap(newMap);
    }
}
