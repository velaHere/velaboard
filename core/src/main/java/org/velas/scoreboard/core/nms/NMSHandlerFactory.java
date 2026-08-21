package org.velas.scoreboard.core.nms;

import org.bukkit.plugin.java.JavaPlugin;
import org.velas.scoreboard.api.ScoreboardHandler;
import org.velas.scoreboard.core.exception.ScoreboardException;
import org.velas.scoreboard.core.util.Logger;
import org.velas.scoreboard.core.util.VersionHelper;

import java.lang.reflect.Constructor;

public final class NMSHandlerFactory {

    private static ScoreboardHandler instance;

    private NMSHandlerFactory(){}

    public static synchronized ScoreboardHandler createHandler(JavaPlugin plugin){
        if(instance!=null) return instance;

        String nmsVersion = VersionHelper.getNmsVersion();
        Logger.info("Version: " + nmsVersion);

        String handlerClassName = "org.velas.scoreboard.core.nms." + nmsVersion.toLowerCase() + ".ScoreboardHandler_" + nmsVersion;

        try{
            Class<?> handlerClass = Class.forName(handlerClassName);

            Constructor<?> constructor = handlerClass.getConstructor(JavaPlugin.class);
            instance= (ScoreboardHandler) constructor.newInstance(plugin);

            return instance;
        }catch(ClassNotFoundException e){
            throw new ScoreboardException("Scoreboard API does not support Minecraft version " +
                    VersionHelper.getFullVersionString() + ". " +
                    "Please update the plugin or report the issue."
            );
        }catch(Exception e){
            throw new ScoreboardException(
                    "Failed to initialize NMS handler for version " + nmsVersion, e
            );
        }
    }

    public static ScoreboardHandler getHandler(){
        return instance;
    }

    public static void reset(){
        instance=null;
    }
}
