/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.gui;

import edu.brown.cs032.tmercuri.ja11.maps.backend.Map;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Thomas Mercurio
 */
public class AutoCorrectedField extends JPanel {
    
    static final long serialVersionUID = 42069L;
    
    private final JTextField field;
    private final JList<String> list;
    private final Map map;
    
    public AutoCorrectedField(Map map) {
        super(new BorderLayout());
        this.field = new JTextField();
        this.list = new JList<>();
        this.map = map;
        buildPanel();
    }

    private void buildPanel() {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // generate suggestions for the text in the field
                List<String> results = new ArrayList<>(); // <- THIS LIST SHOULD CONTAIN THE AUTOCORRECTED THINGS, ie map.getSuggestions()
                // put them in an array
                String[] resultsArr = new String[results.size()];
                // set that array as the list data
                list.setListData(results.toArray(resultsArr));
                // if there are no suggestions, hide the list
                list.setVisible(results.size() > 0);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                List<String> results = new ArrayList<>();  // <- THIS LIST SHOULD CONTAIN THE AUTOCORRECTED THINGS, ie map.getSuggestions()
                String[] resultsArr = new String[results.size()];
                list.setListData(results.toArray(resultsArr));
                list.setVisible(results.size() > 0);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                List<String> results = new ArrayList<>();  // <- THIS LIST SHOULD CONTAIN THE AUTOCORRECTED THINGS, ie map.getSuggestions()
                String[] resultsArr = new String[results.size()];
                list.setListData(results.toArray(resultsArr));
                list.setVisible(results.size() > 0);
            }
        });
        
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        
        list.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.gray), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        //list.setFont(new Font("SansSerif", Font.PLAIN, list.getFont().getSize()));
        
        add(field, BorderLayout.NORTH);
        add(list, BorderLayout.CENTER);
        list.setListData(new String[]{" "," "," "," "," "});
        
        
        
        list.setVisible(false);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
    }
    
    public void setListVisible(boolean visible) {
        list.setVisible(visible);
    }
    
    public void setListData(String[] newListData) {
        list.setListData(newListData);
    }
    
    public String getText() {
        return field.getText();
    }
    
    public void addActionListener(ActionListener listener) {
        field.addActionListener(listener);
    }
    
    
}
