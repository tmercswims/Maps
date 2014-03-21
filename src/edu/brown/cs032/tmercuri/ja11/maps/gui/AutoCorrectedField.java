package edu.brown.cs032.tmercuri.ja11.maps.gui;

import edu.brown.cs032.tmercuri.ja11.maps.backend.MapData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A panel for a field that is auto-complete/-corrected.
 */
public class AutoCorrectedField extends JPanel {
    
    static final long serialVersionUID = 42069L;
    
    private final JTextField field;
    private final JList<String> list;
    private MapData map;
    
    /**
     * Initial map, and the tooltip for the field.
     * @param map
     * @param fieldToolTip
     */
    public AutoCorrectedField(MapData map, String fieldToolTip) {
        super(new BorderLayout());
        this.field = new JTextField();
        field.setToolTipText(fieldToolTip);
        field.setEditable(true);
        this.list = new JList<>();
        this.map = map;
        buildPanel();
    }

    private void buildPanel() {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // generate suggestions for the text in the field
                List<String> results = map.getSuggestions(field.getText());
                // put them in an array
                String[] resultsArr = new String[results.size()];
                // set that array as the list data
                list.setListData(results.toArray(resultsArr));
                // if there are no suggestions, hide the list
                list.setVisible(results.size() > 0);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                List<String> results = map.getSuggestions(field.getText());  
                String[] resultsArr = new String[results.size()];
                list.setListData(results.toArray(resultsArr));
                list.setVisible(results.size() > 0);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                List<String> results = map.getSuggestions(field.getText());
                String[] resultsArr = new String[results.size()];
                list.setListData(results.toArray(resultsArr));
                list.setVisible(results.size() > 0);
            }
        });
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                field.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                list.setVisible(false);
            }
        });
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    // down goes into the list at the top...
                    case KeyEvent.VK_DOWN:
                        list.requestFocus();
                        list.setSelectedIndex(0);
                        break;
                    // and up goes into the list at the bottom
                    case KeyEvent.VK_UP:
                        list.requestFocus();
                        list.setSelectedIndex(list.getModel().getSize()-1);
                        break;
                }
            }
        });
        
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // if there is a single click...
                if (e.getClickCount() == 1) {
                    // ...put that item in the text field
                    String selectedItem = list.getSelectedValue();
                    field.setText(selectedItem);
                }
            }
        });
        list.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    // if the user presses enter, right, or space on an item...
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_SPACE:
                    case KeyEvent.VK_ENTER:
                        // ...put that item in the text field
                        String selectedItem = list.getSelectedValue();
                        field.setText(selectedItem);
                        field.requestFocus();
                        break;
                    // press up...
                    case KeyEvent.VK_UP:
                        // ...and at the top of the list...
                        if (list.getSelectedIndex() == 0) {
                            // ..go into the field
                            field.requestFocus();
                        }
                        break;
                    // press down...
                    case KeyEvent.VK_DOWN:
                        // ...and at the bottom of the list...
                        if (list.getSelectedIndex() == list.getModel().getSize()-1) {
                            // ...go into the field
                            field.requestFocus();
                        }
                        break;
                }
            }
        });
        
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        
        list.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.gray), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        //list.setFont(new Font("SansSerif", Font.PLAIN, list.getFont().getSize()));
        
        add(field, BorderLayout.NORTH);
        add(list, BorderLayout.CENTER);
        list.setListData(new String[]{" "," "," "," "," "});
        list.setFocusable(false);
        
        list.setVisible(false);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
        this.setEnabled(false);
    }
    
    /**
     * Gets the field's text.
     * @return
     */
    public String getText() {
        return field.getText();
    }
    
    /**
     * Adds an ActionListener to the field.
     * @param listener
     */
    public void addActionListener(ActionListener listener) {
        field.addActionListener(listener);
    }
    
    /**
     * Gives a new MapData to the field.
     * @param newMap
     */
    public void setMap(MapData newMap) {
        this.map = newMap;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        field.setEnabled(enabled);
    }
}
