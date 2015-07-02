package com.noozo.moviemaker;

import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Locale;

public class VideoMaker {

    private static final String black = "templates/black.jpg";
    private static final String ffmpeg = Utils.OS == Utils.OS_OSX ? "ffmpeg/./ffmpeg" : "ffmpeg/ffmpeg.exe";

    List<BufferedImage> images = Lists.newArrayList();

    public VideoMaker(int width, int height, String ... imageFiles) throws Exception {

        // Make all processed images the same size
        System.out.println("Making all images the same size...");
        for (int i=0; i<imageFiles.length; i++) {
            String command = ffmpeg + " -i " + imageFiles[i] + " -vf scale=" + width + ":" + height + " " + (i+1) + "_processed.jpg";
            System.out.println(command);
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
        }

        // Overlay text on them
        System.out.println("Overlaying text...");
        for (int i=0; i<imageFiles.length; i++) {
            String filename = (i+1) + "_processed.jpg";
            images.add(Utils.overlayText(ImageIO.read(new File(filename)), "Teste", Color.black));
        }
        // Add the last one twice, so it gets its air time
        String filename = imageFiles.length + "_processed.jpg";
        images.add(Utils.overlayText(ImageIO.read(new File(filename)), "Teste", Color.black));

        // Write them back to file
        System.out.println("Saving them back to file...");
        for (int i=0; i<imageFiles.length; i++) {
            ImageIO.write(images.get(i), "jpg", new File((i+1) + "_processed.jpg"));
        }
    }

    private void create(String movieFile, int secondsPerImage) throws Exception {

        System.out.println("Running command...");
        String command = ffmpeg + " -framerate 1/" + secondsPerImage + " -i %d_processed.jpg -c:v libx264 -r 30 -pix_fmt yuv420p -y " + movieFile;
        System.out.println(command);
        Process p = Runtime.getRuntime().exec("chmod +x ffmpeg");
        p.waitFor();
        p = Runtime.getRuntime().exec(command);
        p.waitFor();

        System.out.println("Deleting processed images...");
        for (int i=1; i<images.size()+1; i++) {
           new File(i + "_processed.jpg").delete();
        }
    }

    public static void main(String[] args) throws Exception {

        //VideoMaker maker = new VideoMaker(1024, 768, black, "test/1.jpg", black);//, "test/2.jpg", "test/3.jpg");
        //maker.create("out.mp4", 2);

        Locale.setDefault(new Locale("pt", "PT"));

        VideoMakerGUI gui = new VideoMakerGUI();
        gui.setVisible(true);
    }
}