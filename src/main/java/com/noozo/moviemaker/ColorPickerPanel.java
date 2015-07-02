package com.noozo.moviemaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * void
 * 02/07/15.
 */
public class ColorPickerPanel extends JPanel {

    interface ColorChangeListener {
        void onColorChanged();
    }
    private Color color;

    public ColorPickerPanel(int width, int height, final ColorChangeListener listener) {

        defineSizes(width, height);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                super.mouseClicked(e);

                if (!isEnabled()) {
                    return;
                }

                setColor(JColorChooser.showDialog(ColorPickerPanel.this, "Escolha a cor", color));
                listener.onColorChanged();
            }
        });
    }

    public void setColor(Color color) {

        this.color = color;

        // Force repaint
        this.validate();
        this.repaint();
    }

    public void clear() {

        setColor(Color.white);
    }

    private void defineSizes(int width, int height) {

        // Define the minimum and preferred sizes for the component
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
    }

    protected void paintComponent(Graphics mGraphics) {

        super.paintComponent(mGraphics);

        if (color != null) {

            mGraphics.setColor(color);
            mGraphics.fillRect(2, 2, this.getPreferredSize().width-4, this.getPreferredSize().height-4);
        }
        mGraphics.setColor(Color.white);
        mGraphics.drawRect(1, 1, this.getPreferredSize().width-2, this.getPreferredSize().height-2);
        mGraphics.setColor(Color.black);
        mGraphics.drawRect(0, 0, this.getPreferredSize().width-1, this.getPreferredSize().height-1);
    }

    public Color getColor() {
        return color;
    }
}
