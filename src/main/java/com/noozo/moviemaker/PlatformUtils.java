package com.noozo.moviemaker;

/**
 * void
 * 02/07/15.
 */
public class PlatformUtils {

    public static final int OS_WINDOWS = 0;
    public static final int OS_OSX = 1;
    public static final int OS_UNIX = 2;

    public static int OS = getOSType();

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
}
