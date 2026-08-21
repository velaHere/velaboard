package org.velas.scoreboard.core.implementation;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.sidebar.PlayerSidebar;
import org.velas.scoreboard.api.sidebar.SharedSidebar;
import org.velas.scoreboard.api.sidebar.Sidebar;
import org.velas.scoreboard.api.team.Team;
import org.velas.scoreboard.core.data.PlayerData;
import org.velas.scoreboard.core.manager.ScoreboardManager;
import org.velas.scoreboard.core.manager.SidebarManager;
import org.velas.scoreboard.core.sidebar.SidebarFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class BukkitScoreboardImpl implements Scoreboard {

    private final org.bukkit.scoreboard.Scoreboard bukkitScoreboard;
    private final Map<String, Team> teamCache = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerData> players = new ConcurrentHashMap<>();
    private final SidebarManager sidebarManager;
    private final ScoreboardManager scoreboardManager;

    public BukkitScoreboardImpl(ScoreboardManager scoreboardManager, SidebarFactory sidebarFactory){
        bukkitScoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        this.sidebarManager = new SidebarManager(this, players, sidebarFactory);
        this.scoreboardManager=scoreboardManager;
    }

    @Override
    public @NotNull Set<UUID> getPlayers() {
        return new HashSet<>(players.keySet());
    }

    @Override
    public void addPlayer(@NotNull Player player) {
        addPlayer(player.getUniqueId());
    }

    @Override
    public void addPlayer(@NotNull UUID playerUUID) {
        if(this.players.containsKey(playerUUID)) return;
        Scoreboard scoreboard = scoreboardManager.getScoreboard(playerUUID);
        if(scoreboard!=null) scoreboard.removePlayer(playerUUID);
        players.put(playerUUID, new PlayerData(playerUUID));
        scoreboardManager.getScoreboards().put(playerUUID, this);
        Player player = Bukkit.getPlayer(playerUUID);
        if(player!=null) player.setScoreboard(bukkitScoreboard);
    }

    @Override
    public void removePlayer(@NotNull Player player) {
        removePlayer(player.getUniqueId());
    }

    @Override
    public void removePlayer(@NotNull UUID playerUUID){
        if(!this.players.containsKey(playerUUID)) return;

        //Remove Team
        removePlayerFromTeam(playerUUID);
        removePlayerFromSidebar(playerUUID);

        scoreboardManager.getScoreboards().remove(playerUUID);
        players.remove(playerUUID);
        Player player = Bukkit.getPlayer(playerUUID);
        if(player!=null) player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
    }

    @Override
    public @NotNull Team team(@NotNull String name) {
        if (teamCache.containsKey(name)) return teamCache.get(name);

        org.bukkit.scoreboard.Team bukkitTeam = bukkitScoreboard.getTeam(name);
        if (bukkitTeam == null) bukkitTeam = bukkitScoreboard.registerNewTeam(name);

        Team team = new BukkitTeamImpl(this, bukkitTeam);
        teamCache.put(name, team);
        return team;
    }

    @Override
    public void removeTeam(@NotNull String name) {
        org.bukkit.scoreboard.Team bukkitTeam = bukkitScoreboard.getTeam(name);
        if(bukkitTeam==null) return;

        Team team = teamCache.get(name);
        for(PlayerData data: players.values()){
            if(!data.hasTeam()) continue;
            if(data.getTeam() == team) data.removeTeam();
        }
        this.scoreboardManager.afterTeamDelete(team);
        bukkitTeam.unregister();
        teamCache.remove(name);
    }

    @Override
    public Team getTeam(@NotNull String name) {
        return teamCache.get(name);
    }

    @Override
    public @Nullable Team getTeamOf(@NotNull Player player) {
        return getTeamOf(player.getUniqueId());
    }

    @Override
    public @Nullable Team getTeamOf(@NotNull UUID uuid) {
        return teamCache.values().stream().filter(team ->
                team.hasMember(uuid)).findFirst().orElse(null);
    }

    @Override
    public boolean hasTeam(@NotNull String name) {
        return bukkitScoreboard.getTeam(name)!=null;
    }

    @Override
    public boolean hasTeam(@NotNull UUID playerUUID) {
        if(!Bukkit.getOfflinePlayer(playerUUID).hasPlayedBefore()) return false;
        return getTeamOf(playerUUID)!=null;
    }

    @Override
    public boolean hasTeam(@NotNull Player player) {
        return getTeamOf(player.getUniqueId())!=null;
    }

    @Override
    public void removePlayerFromTeam(@NotNull Player player) {
        removePlayerFromTeam(player.getUniqueId());
    }

    @Override
    public void removePlayerFromTeam(@NotNull UUID playerUUID) {
        Team team = getTeamOf(playerUUID);
        if(team==null) return;
        team.removePlayer(playerUUID);
    }

    @Override
    public void clearTeams() {
        for(PlayerData data: players.values()) data.removeTeam();
        new ArrayList<>(teamCache.keySet()).forEach(this::removeTeam);
    }

    @Override
    public @NotNull Set<Team> getTeams() {
        return new HashSet<>(this.teamCache.values());
    }

    @Override
    public @Nullable PlayerSidebar getPlayerSidebar(@NotNull Player player) {
        return getPlayerSidebar(player.getUniqueId());
    }

    @Override
    public @Nullable PlayerSidebar getPlayerSidebar(@NotNull UUID playerUUID) {
        return sidebarManager.getPlayerSidebar(playerUUID);
    }

    @Override
    public @Nullable SharedSidebar getSharedSidebar(@NotNull Player player) {
        return getSharedSidebar(player.getUniqueId());
    }

    @Override
    public @Nullable SharedSidebar getSharedSidebar(@NotNull UUID playerUUID) {
        return sidebarManager.getSharedSidebar(playerUUID);
    }

    @Override
    public @Nullable SharedSidebar getSharedSidebar(@NotNull String id) {
        return sidebarManager.getSharedSidebar(id);
    }

    @Override
    public @Nullable Sidebar getSidebar(@NotNull Player player) {
        PlayerSidebar playerSidebar = getPlayerSidebar(player);
        if(playerSidebar!=null) return playerSidebar;
        return getSharedSidebar(player);
    }

    @Override
    public @Nullable Sidebar getSidebar(@NotNull UUID playerUUID) {
        PlayerSidebar playerSidebar = getPlayerSidebar(playerUUID);
        if(playerSidebar!=null) return playerSidebar;
        return getSharedSidebar(playerUUID);
    }

    @Override
    public boolean hasSidebar(@NotNull Player player) {
        return getPlayerSidebar(player)!=null || getSharedSidebar(player)!=null;
    }

    @Override
    public boolean hasSidebar(@NotNull UUID playerUUID) {
        return getPlayerSidebar(playerUUID)!=null || getSharedSidebar(playerUUID)!=null;
    }

    @Override
    public void removePlayerFromSidebar(@NotNull Player player) {
        removePlayerFromSidebar(player.getUniqueId());
    }

    @Override
    public void removePlayerFromSidebar(@NotNull UUID playerUUID) {
        PlayerSidebar playerSidebar = getPlayerSidebar(playerUUID);
        if(playerSidebar!=null) playerSidebar.removePlayer();
        SharedSidebar sharedSidebar = getSharedSidebar(playerUUID);
        if(sharedSidebar!=null) sharedSidebar.removePlayer(playerUUID);
    }

    @Override
    public @NotNull PlayerSidebar createPlayerSidebar(@NotNull String title) {
        return sidebarManager.createPlayerSidebar(title);
    }

    @Override
    public @NotNull SharedSidebar createSharedSidebar(@NotNull String title) {
        return sidebarManager.createSharedSidebar(title);
    }

    @Override
    public @NotNull SharedSidebar createSharedSidebar(@NotNull String id, @NotNull String title) {
        return sidebarManager.createSharedSidebar(id, title);
    }

    @NotNull
    public org.bukkit.scoreboard.Scoreboard getBukkitScoreboard(){
        return this.bukkitScoreboard;
    }

    public SidebarManager getSidebarManager(){
        return this.sidebarManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public Map<UUID, PlayerData> getPlayersData(){
        return this.players;
    }
}
