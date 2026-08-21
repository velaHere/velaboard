package org.velas.scoreboard.core.util;

import org.bukkit.Bukkit;

public final class VersionHelper {

    private static final int MAJOR_VERSION;
    private static final int MINOR_VERSION;
    private static final String VERSION_STRING;
    private static final String NMS_VERSION;

    static{
        try{
            String packageName = Bukkit.getServer().getClass().getPackage().getName();
            String [] parts = packageName.split("\\.");
            NMS_VERSION = parts[parts.length - 1];

            String versionPart = NMS_VERSION.substring(1);
            String [] versionParts = versionPart.split("_");
            MAJOR_VERSION = Integer.parseInt(versionParts[0]);
            MINOR_VERSION = Integer.parseInt(versionParts[1]);

            VERSION_STRING = MAJOR_VERSION + "." + MINOR_VERSION;
        }catch(Exception e){
            throw new RuntimeException("Could not detect Minecraft version", e);
        }
    }

    private VersionHelper(){}

    public static String getNmsVersion(){
        return NMS_VERSION;
    }

    public static int getMajorVersion(){
        return MAJOR_VERSION;
    }

    public static int getMinorVersion(){
        return MINOR_VERSION;
    }

    public static String getVersionString(){
        return VERSION_STRING;
    }

    public static boolean isVersion(int major, int minor){
        return MAJOR_VERSION==major && MINOR_VERSION==minor;
    }

    public static boolean isVersionOrHigher(int major, int minor){
        if(MAJOR_VERSION>major) return true;
        if(MAJOR_VERSION==major) return MINOR_VERSION>=minor;
        return false;
    }

    public static String getFullVersionString(){
        return String.format("Minecraft %s (NMS: %s)", VERSION_STRING, NMS_VERSION);
    }
}
