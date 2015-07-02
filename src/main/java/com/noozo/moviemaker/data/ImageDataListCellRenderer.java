package com.noozo.moviemaker.data;

import com.noozo.moviemaker.data.ImageData;

import javax.swing.*;
import java.awt.*;

/**
 * void
 * 02/07/15.
 */
public class ImageDataListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        ImageData data = (ImageData) value;
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        label.setText(value != null ? data.filename : "null");

        return label;
    }
}
