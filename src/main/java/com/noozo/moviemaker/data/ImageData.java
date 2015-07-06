package com.noozo.moviemaker.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * void
 * 02/07/15.
 */
public class ImageData {

    public String filename;
    public String fullPath;
    public BufferedImage image;

    // Overlays
    public Text title = new Text();
    public Text title2 = new Text();

    public ImageData() { }
    
    public ImageData(String file) throws IOException {

        this.fullPath = file;
        this.image = ImageIO.read(new File(fullPath));
        this.filename = file.substring(file.lastIndexOf(File.separator)+1);
    }
}
