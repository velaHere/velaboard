package org.velas.scoreboard.core.nms.v1_21_r6;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.ScoreboardHandler;
import org.velas.scoreboard.core.manager.ScoreboardManager;

public class ScoreboardHandler_v1_21_R6 implements ScoreboardHandler {

    private final ScoreboardManager scoreboardManager;

    public ScoreboardHandler_v1_21_R6(JavaPlugin plugin){
        this.scoreboardManager=new ScoreboardManager(plugin, new SidebarFactory_v1_21_R6());
    }

    @Override
    public @Nullable Scoreboard getScoreboard(@NotNull Player player) {
        return scoreboardManager.getScoreboard(player);
    }

    @Override
    public @NotNull Scoreboard createScoreboard() {
        return scoreboardManager.createScoreboard();
    }
}
