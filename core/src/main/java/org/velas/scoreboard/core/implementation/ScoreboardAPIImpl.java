package org.velas.scoreboard.core.implementation;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.api.ScoreboardAPI;
import org.velas.scoreboard.api.ScoreboardHandler;
import org.velas.scoreboard.core.nms.NMSHandlerFactory;
import org.velas.scoreboard.core.util.Logger;
import org.velas.scoreboard.core.util.VersionHelper;

public final class ScoreboardAPIImpl extends ScoreboardAPI {

    private static ScoreboardHandler handler;

    private ScoreboardAPIImpl(){
        super();
    }

    public static void init(@NotNull JavaPlugin plugin){
        if(isInitialized())
            throw new IllegalStateException("ScoreboardAPIImpl is already initialized");

        Logger.info("Initializing...");

        handler = NMSHandlerFactory.createHandler(plugin);
        new ScoreboardAPIImpl();
    }

    @Override
    protected @NotNull String getMinecraftVersionA() {
        return VersionHelper.getVersionString();
    }

    @Override
    protected @NotNull ScoreboardHandler getHandlerA() {
        checkInitialized();
        return handler;
    }

    private static void checkInitialized() {
        if (!isInitialized()) {
            throw new IllegalStateException(
                    "ScoreboardAPI not initialized.");
        }
    }
}
