package org.velas.scoreboard.core.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.scoreboard.api.sidebar.PlayerSidebar;
import org.velas.scoreboard.api.team.Team;

import java.util.UUID;

public final class PlayerData {

    private final UUID playerUUID;
    @Nullable
    private PlayerSidebar sidebar;
    @Nullable
    private Team team;

    public PlayerData(UUID playerUUID){
        this.playerUUID=playerUUID;
    }

    @Nullable
    public PlayerSidebar getSidebar() {
        return sidebar;
    }

    public void setSidebar(@NotNull PlayerSidebar sidebar) {
        this.sidebar = sidebar;
    }

    public void removeSidebar(){
        sidebar=null;
    }

    @Nullable
    public Team getTeam() {
        return team;
    }

    public void setTeam(@NotNull Team team) {
        this.team = team;
    }

    public void removeTeam(){
        team=null;
    }

    public boolean hasTeam(){
        return team!=null;
    }

    public boolean hasSidebar(){
        return sidebar!=null;
    }
}
