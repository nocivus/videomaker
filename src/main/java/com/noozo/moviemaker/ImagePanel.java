package com.noozo.moviemaker;

import org.imgscalr.Scalr;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private BufferedImage image;

    public void setImage(BufferedImage image) {

        setImage(image, getWidth(), getHeight());
    }

    public void setImage(BufferedImage image, int width, int height) {

        defineSizes(width, height);

        if (image != null) {
            this.image = Scalr.resize(image, width, height);
        }

        // Force repaint
        this.validate();
        this.repaint();
    }

    public void clear() {

        setImage(null, 0, 0);
    }

    private void defineSizes(int width, int height) {

        // Define the minimum and preferred sizes for the component
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
    }

    /**
     * (Re)paints the component (ie: draws the preview image)
     * 
     * @param mGraphics
     *            The graphics device where to draw
     * 
     * @return void
     **/
    protected void paintComponent(Graphics mGraphics) {

        super.paintComponent(mGraphics);

        if (this.image != null) {

            int x = (int) ((this.getWidth() / 2F) - (this.image.getWidth() / 2F));
            int y = (int) ((this.getHeight() / 2F) - (this.image.getHeight() / 2F));
            mGraphics.drawImage(this.image, x, y, this);
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
