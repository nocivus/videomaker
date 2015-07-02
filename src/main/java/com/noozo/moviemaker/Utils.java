package com.noozo.moviemaker;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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

    public static boolean confirm(JFrame parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Confirma?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static BufferedImage overlayText(BufferedImage old, String text, Color color) {

        int w = old.getWidth();
        int h = old.getHeight();
        BufferedImage img = new BufferedImage(w, h, old.getType());
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(old, 0, 0, null);
        g2d.setPaint(color);
        g2d.setFont(new Font("Serif", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        int x = img.getWidth()/2 - fm.stringWidth(text)/2;
        int y = (int)(img.getHeight()/1.5f) - fm.stringWidth(text)/2;
        g2d.drawString(text, x, y);
        g2d.dispose();
        return img;
    }
}
