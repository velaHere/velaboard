package org.velas.scoreboard.core.sidebar;

import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.api.sidebar.PlayerSidebar;
import org.velas.scoreboard.api.sidebar.SharedSidebar;
import org.velas.scoreboard.core.implementation.BukkitScoreboardImpl;

public interface SidebarFactory {
    @NotNull PlayerSidebar createPlayerSidebar(@NotNull BukkitScoreboardImpl scoreboard, @NotNull String title);
    @NotNull SharedSidebar createSharedSidebar(@NotNull String id, @NotNull BukkitScoreboardImpl scoreboard, @NotNull String title);
}
