package org.velas.scoreboard.core.util;

import org.bukkit.Bukkit;

public final class Logger {

    private static boolean doLogging = false;

    private Logger(){}

    public static void canDoLogs(boolean can){
        doLogging=can;
    }

    public static void info(String text){
        if(!doLogging) return;
        Bukkit.getLogger().info(text);
    }

    public static void warning(String text){
        if(!doLogging) return;
        Bukkit.getLogger().warning(text);
    }

    public static void severe(String text){
        if(!doLogging) return;
        Bukkit.getLogger().severe(text);
    }
}
