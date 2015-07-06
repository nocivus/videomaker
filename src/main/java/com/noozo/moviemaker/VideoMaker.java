package com.noozo.moviemaker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import com.google.common.collect.Lists;
import com.noozo.moviemaker.data.ImageData;

public class VideoMaker {

	public static final int VIDEO_WIDTH = 1024;
	public static final int VIDEO_HEIGHT = 768;
	public static final String BLACK = "templates/black.jpg";

	private static final String ffmpeg = Utils.OS == Utils.OS_OSX ? "ffmpeg/./ffmpeg" : "ffmpeg/ffmpeg.exe";

    List<BufferedImage> images = Lists.newArrayList();

    public void process(List<ImageData> imageFiles) throws Exception {

        // Make all processed images the same size
        System.out.println("Making all images the same size...");
        for (int i=0; i<imageFiles.size(); i++) {
        	
        	// If exists, delete the processed file
        	if (new File((i+1) + "_processed.jpg").exists()) {
        		new File((i+1) + "_processed.jpg").delete();
        	}
        	
            String command = ffmpeg + " -i " + imageFiles.get(i).fullPath + " -vf scale=" + VIDEO_WIDTH + ":" + VIDEO_HEIGHT + " " + (i+1) + "_processed.jpg";
            System.out.println(command);
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
        }

        // Overlay text on them
        System.out.println("Overlaying text...");
        for (int i=0; i<imageFiles.size(); i++) {
            String filename = (i+1) + "_processed.jpg";
            ImageData data = imageFiles.get(i);
            images.add(Utils.overlayText(ImageIO.read(new File(filename)), data.title, data.title2));
        }
        // Add the last one twice, so it gets its air time
        String filename = imageFiles.size() + "_processed.jpg";
        ImageData data = imageFiles.get(imageFiles.size()-1);
        images.add(Utils.overlayText(ImageIO.read(new File(filename)), data.title, data.title2));

        // Write them back to file
        System.out.println("Saving them back to file...");
        for (int i=0; i<imageFiles.size(); i++) {
            ImageIO.write(images.get(i), "jpg", new File((i+1) + "_processed.jpg"));
        }
    }

    public void create(String movieFile, int secondsPerImage) throws Exception {

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

        Locale.setDefault(new Locale("pt", "PT"));

        VideoMakerGUI gui = new VideoMakerGUI();
        gui.setVisible(true);
    }
}