package org.velas.scoreboard.core.manager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.scoreboard.api.sidebar.PlayerSidebar;
import org.velas.scoreboard.api.sidebar.SharedSidebar;
import org.velas.scoreboard.api.sidebar.Sidebar;
import org.velas.scoreboard.core.data.PlayerData;
import org.velas.scoreboard.core.exception.ScoreboardException;
import org.velas.scoreboard.core.implementation.BukkitScoreboardImpl;
import org.velas.scoreboard.core.sidebar.SidebarFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SidebarManager{

    private final BukkitScoreboardImpl scoreboard;
    private final Map<UUID, PlayerData> players;
    private final Map<String, SharedSidebar> sharedSidebars = new ConcurrentHashMap<>();
    private final SidebarFactory sidebarFactory;

    public SidebarManager(BukkitScoreboardImpl scoreboard, Map<UUID, PlayerData> players, SidebarFactory sidebarFactory){
        this.scoreboard=scoreboard;
        this.players=players;
        this.sidebarFactory=sidebarFactory;
    }

    @Nullable
    public PlayerSidebar getPlayerSidebar(@NotNull UUID playerUUID){
        if(!players.containsKey(playerUUID)) return null;
        if(players.get(playerUUID)==null) return null;
        return players.get(playerUUID).getSidebar();
    }

    @Nullable
    public SharedSidebar getSharedSidebar(@NotNull UUID playerUUID){
        return sharedSidebars.values().stream()
                .filter(sidebar -> sidebar.hasPlayer(playerUUID))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public SharedSidebar getSharedSidebar(@NotNull String id){
        return sharedSidebars.get(id);
    }

    @NotNull
    public PlayerSidebar createPlayerSidebar(@NotNull String title){
        return sidebarFactory.createPlayerSidebar(this.scoreboard, title);
    }

    @NotNull
    public SharedSidebar createSharedSidebar(@NotNull String title){
        String id = "ss_"+System.currentTimeMillis();
        return createSharedSidebar(id, title);
    }

    @NotNull
    public SharedSidebar createSharedSidebar(@NotNull String id, @NotNull String title){
        if(sharedSidebars.containsKey(id))
            throw new ScoreboardException("Shared sidebar with the id " + id + " already exists");

        SharedSidebar sidebar = sidebarFactory.createSharedSidebar(id, this.scoreboard, title);
        this.sharedSidebars.put(id, sidebar);
        return sidebar;
    }

    public void updateSidebarEntry(@NotNull UUID playerUUID, @NotNull Sidebar sidebar){
        if(!players.containsKey(playerUUID)) return;

        if(scoreboard.hasSidebar(playerUUID)){
            PlayerSidebar playerSidebar = scoreboard.getPlayerSidebar(playerUUID);
            if(playerSidebar!=null) playerSidebar.removePlayer();
            SharedSidebar sharedSidebar = scoreboard.getSharedSidebar(playerUUID);
            if(sharedSidebar!=null) sharedSidebar.removePlayer(playerUUID);
        }

        if(!(sidebar instanceof PlayerSidebar playerSidebar)) return;
        players.get(playerUUID).setSidebar(playerSidebar);
    }

    public void onSharedSidebarRemoved(SharedSidebar sidebar){
        sharedSidebars.remove(sidebar.getId(), sidebar);
    }
}
