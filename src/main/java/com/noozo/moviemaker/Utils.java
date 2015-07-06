package com.noozo.moviemaker;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.noozo.moviemaker.data.Text;

/**
 * void
 * 02/07/15.
 */
public class Utils {

    public static final int OS_WINDOWS = 0;
    public static final int OS_OSX = 1;
    public static final int OS_UNIX = 2;

    public static int OS = getOSType();

    /**
     * Window alignment X center and Y center
     */
    public static final int CENTER_CENTER = 0;
    /**
     * Window alignment X left and Y center
     */
    public static final int LEFT_CENTER   = 1;

    /**
     * A method to align a window.
     *
     * @param mWindow The window to align
     * @param mAlign  The type of alignment
     */
    public static void alignWindow(java.awt.Window mWindow, int mAlign) {

        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = mWindow.getSize().width;
        int h = mWindow.getSize().height;
        int x, y;

        // Determine the new location of the window
        switch (mAlign) {
            default:
            case CENTER_CENTER: {
                x = (dim.width - w) / 2;
                y = (dim.height - h) / 2;
            }
            break;

            case LEFT_CENTER: {
                x = 0;
                y = (dim.height - h) / 2;
            }
            break;
        }

        // Move the window
        mWindow.setLocation(x, y);
    }

    public static void centerWindow(Window window) {

        alignWindow(window, CENTER_CENTER);
    }

    private static int getOSType() {

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows")) {

            return OS_WINDOWS;

        } else if (osName.contains("os x")) {

            return OS_OSX;
        }
        return OS_UNIX;
    }

    public static String getOsName() {

        switch (OS) {
            case OS_WINDOWS: return "Windows";
            case OS_OSX: return "Mac OSX";
        }
        return "Unix/Linux";
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Erro!", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message, "Erro!", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(JFrame parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Confirma?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static BufferedImage overlayText(BufferedImage old, Text ... data) {

    	int w = old.getWidth();
        int h = old.getHeight();
        BufferedImage img = new BufferedImage(w, h, old.getType());
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(old, 0, 0, null);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        
        // Title
        for (Text t : data) {
        
        	int textSize = calcTextSize(old.getHeight(), VideoMaker.VIDEO_HEIGHT, t.fontSize);
        	Point position = calcTextPosition(old.getWidth(), old.getHeight(), VideoMaker.VIDEO_WIDTH, VideoMaker.VIDEO_HEIGHT, t.position);
        	g2d.setPaint(t.color);
        	g2d.setFont(new Font("Verdana", Font.BOLD, textSize));        
        	g2d.drawString(t.text, position.x, position.y + g2d.getFont().getSize());
        }
        
        g2d.dispose();

//        if (centered) {
//            FontMetrics fm = g2d.getFontMetrics();
//            x = img.getWidth() / 2 - fm.stringWidth(text) / 2;
//            y = (int) (img.getHeight() / 1.5f) - fm.stringWidth(text) / 2;
//        }

        return img;
    }
    
    public static Point calcTextPosition(int imageWidth, int imageHeight, int videoWidth, int videoHeight, Point position) {
    	
    	return new Point((int) (((float)imageWidth*(float)position.x) / (float)videoWidth), (int) (((float)imageHeight*(float)position.y) / (float)videoHeight));
    }

	public static int calcTextSize(int imageHeight, int videoHeight, int textSize) {
		
		return (int) (((float)imageHeight*(float)textSize) / (float)videoHeight);
	}
	
	public static String input(String question) {

        return input(question, null);
    }

    public static String input(String question, String defaultValue) {

        return JOptionPane.showInputDialog(question, defaultValue);
    }
	
	public static Integer integerInput(String question, Integer defaultValue) {

        return decimalInput(question, defaultValue == null ? null : new BigDecimal(defaultValue)).intValue();
    }

    public static BigDecimal decimalInput(String question) {

        return decimalInput(question, null);
    }

    public static BigDecimal decimalInput(String question, BigDecimal defaultValue) {

        try {

            Object value = defaultValue != null ? input(question, defaultValue.toString()) : input(question);
            return value == null ? null : new BigDecimal(value.toString());

        } catch (Exception exc) {

            JOptionPane.showMessageDialog(null, "Número inválido"); //$NON-NLS-1$
            return decimalInput(question, defaultValue);
        }
    }
    
    /**
     * Asks the user to select a target file to save to, of a certain type
     *
     * @param filter The filter for the accepted files. Example "xml" or "jpg".
     * @return The selected file or null
     */
    public static File askForTarget(final String filter, Component parent) {

        JFileChooser fc = new JFileChooser("."); //$NON-NLS-1$

        fc.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {

                return filter + " files"; //$NON-NLS-1$
            }

            @Override
            public boolean accept(File f) {

                return f.getName().toLowerCase().endsWith(filter.toLowerCase()) || f.isDirectory();
            }
        });
        fc.showSaveDialog(parent);

        return fc.getSelectedFile();
    }
    
    public static void enabledDisableChildren(JComponent comp, boolean enabled) {

        try {
            if (comp != null) {
                comp.setEnabled(enabled);
            }
        } catch (Exception ignored) {
        }
    }
}
