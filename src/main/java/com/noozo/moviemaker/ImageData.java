package com.noozo.moviemaker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * void
 * 02/07/15.
 */
public class ImageData {

    public String filename;
    public String fullPath;
    public BufferedImage image;

    // Overlays
    public String title;
    public Color titleColor = Color.white;

    public ImageData(String file) throws IOException {

        this.fullPath = file;
        this.image = ImageIO.read(new File(fullPath));
        this.filename = file.substring(file.lastIndexOf(File.separator)+1);
    }
}
