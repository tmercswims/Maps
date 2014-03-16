/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.ja11.maps.gui;

import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Thomas Mercurio
 */
public class MyCellRenderer extends DefaultListCellRenderer {
     @Override
     public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         Rectangle bounds = c.getBounds();
         bounds.width -= 2;
         c.setBounds(bounds);
         return c;
     }
}
