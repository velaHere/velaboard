package org.velas.scoreboard.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.velas.scoreboard.core.implementation.ScoreboardAPIImpl;
import org.velas.scoreboard.core.util.Logger;
import org.velas.scoreboard.dev.TestCommand;

import java.util.Objects;

public class ScoreboardAPIPlugin extends JavaPlugin {

    private static final boolean DEV_MODE = false;

    @Override
    public void onEnable() {
        Logger.canDoLogs(false);
        ScoreboardAPIImpl.init(this);
        if(DEV_MODE){
            Objects.requireNonNull(getCommand("st")).setExecutor(new TestCommand());
        }


    }
}
