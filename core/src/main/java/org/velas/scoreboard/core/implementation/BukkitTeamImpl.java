package org.velas.scoreboard.core.implementation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.api.event.AddMemberToTeamEvent;
import org.velas.scoreboard.api.team.Team;
import org.velas.scoreboard.api.team.TeamCollisionRule;
import org.velas.scoreboard.api.team.TeamColor;
import org.velas.scoreboard.api.team.TeamNameTagVisibility;
import org.velas.scoreboard.core.data.PlayerData;
import org.velas.scoreboard.core.exception.ScoreboardException;

import java.util.*;


public final class BukkitTeamImpl implements Team {

    private final BukkitScoreboardImpl scoreboard;
    private final org.bukkit.scoreboard.Team bukkitTeam;
    private boolean entitiesFriendlyFire;
    private final List<UUID> players = new ArrayList<>();
    private final List<UUID> entities = new ArrayList<>();

    public BukkitTeamImpl(BukkitScoreboardImpl scoreboard, org.bukkit.scoreboard.Team bukkitTeam) {
        this.scoreboard=scoreboard;
        this.bukkitTeam = bukkitTeam;
        this.entitiesFriendlyFire=true;
    }

    @Override
    public @NotNull String getName() {
        return bukkitTeam.getName();
    }

    @Override
    public @NotNull Team setColor(@NotNull TeamColor color) {
        bukkitTeam.setColor(color.getChatColor());
        return this;
    }

    @Override
    public @NotNull TeamColor getColor() {
        return TeamColor.fromChatColor(bukkitTeam.getColor());
    }

    @Override
    public @NotNull ChatColor getChatColor() {
        return bukkitTeam.getColor();
    }

    @Override
    public @NotNull Team setPrefix(@NotNull String prefix) {
        bukkitTeam.setPrefix(prefix);
        return this;
    }

    @Override
    public @NotNull String getPrefix() {
        return bukkitTeam.getPrefix();
    }

    @Override
    public @NotNull Team setSuffix(@NotNull String suffix) {
        bukkitTeam.setSuffix(suffix);
        return this;
    }

    @Override
    public @NotNull String getSuffix() {
        return bukkitTeam.getSuffix();
    }

    @Override
    public @NotNull Team setFriendlyFire(boolean enabled) {
        bukkitTeam.setAllowFriendlyFire(enabled);
        return this;
    }

    @Override
    public @NotNull Team setEntitiesFriendlyFire(boolean enabled) {
        this.entitiesFriendlyFire=enabled;
        return this;
    }

    @Override
    public boolean isFriendlyFireEnabled() {
        return bukkitTeam.allowFriendlyFire();
    }

    @Override
    public boolean isEntitiesFriendlyFireEnabled() {
        return entitiesFriendlyFire;
    }

    @Override
    public @NotNull Team setCanSeeFriendlyInvisibilities(boolean enabled) {
        bukkitTeam.setCanSeeFriendlyInvisibles(enabled);
        return this;
    }

    @Override
    public boolean canSeeFriendlyInvisibilities() {
        return bukkitTeam.canSeeFriendlyInvisibles();
    }

    @Override
    public @NotNull Team setTeamCollisionRule(@NotNull TeamCollisionRule rule) {
        bukkitTeam.setOption(org.bukkit.scoreboard.Team.Option.COLLISION_RULE, rule.toBukkitStatus());
        return this;
    }

    @Override
    public @NotNull TeamCollisionRule getCollisionRule() {
        org.bukkit.scoreboard.Team.OptionStatus status =
                bukkitTeam.getOption(org.bukkit.scoreboard.Team.Option.COLLISION_RULE);
        return TeamCollisionRule.fromBukkitStatus(status);
    }

    @Override
    public @NotNull Team setNameTagVisibility(@NotNull TeamNameTagVisibility visibility) {
        bukkitTeam.setOption(
                org.bukkit.scoreboard.Team.Option.NAME_TAG_VISIBILITY, visibility.toBukkitStatus()
        );
        return this;
    }

    @Override
    public @NotNull TeamNameTagVisibility getNameTagVisibility() {
        org.bukkit.scoreboard.Team.OptionStatus status =
                bukkitTeam.getOption(org.bukkit.scoreboard.Team.Option.NAME_TAG_VISIBILITY);
        return TeamNameTagVisibility.fromBukkitStatus(status);
    }

    @Override
    public @NotNull Team addPlayer(@NotNull Player player) {
        return addPlayer(player, AddMemberToTeamEvent.Reason.PLAYER_ADD);
    }

    @Override
    public @NotNull Team addPlayer(@NotNull UUID playerUUID) {
        Player onlinePlayer = Bukkit.getPlayer(playerUUID);
        if(onlinePlayer!=null) addPlayer(onlinePlayer);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
        if(!offlinePlayer.hasPlayedBefore()) return this;
        String playerName = offlinePlayer.getName();
        if(playerName==null) return this;

        return addPlayer(playerName, playerUUID, AddMemberToTeamEvent.Reason.PLAYER_ADD);
    }

    public @NotNull Team addPlayer(@NotNull Player player, @NotNull AddMemberToTeamEvent.Reason reason){
        return this.addPlayer(player.getName(), player.getUniqueId(), reason);
    }

    private @NotNull Team addPlayer(@NotNull String playerName, @NotNull UUID playerUUID, @NotNull AddMemberToTeamEvent.Reason reason){
        if(reason.equals(AddMemberToTeamEvent.Reason.ENTITY_ADD))
            throw new ScoreboardException("ENTITY_ADD reason for adding the player in a team?? Think for it");
        if(!scoreboard.getPlayers().contains(playerUUID)) return this;
        AddMemberToTeamEvent event = new AddMemberToTeamEvent(
                playerUUID, this, reason
        );
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) return this;
        PlayerData playerData = this.scoreboard.getPlayersData().get(playerUUID);
        if(playerData==null) return this;
        if(playerData.hasTeam()) {
            Team team = playerData.getTeam();
            assert team != null;
            team.removePlayer(playerUUID);
        }
        bukkitTeam.addEntry(playerName);
        this.players.add(playerUUID);
        playerData.setTeam(this);
        this.scoreboard.getScoreboardManager().setTeam(playerUUID, this);
        return this;
    }

    @Override
    public @NotNull Team removePlayer(@NotNull Player player) {
        return removePlayer(player.getUniqueId());
    }

    @Override
    public @NotNull Team removePlayer(@NotNull UUID playerUUID) {
        if(!scoreboard.getPlayers().contains(playerUUID)) return this;
        if(!this.players.contains(playerUUID)) return this;
        PlayerData playerData = this.scoreboard.getPlayersData().get(playerUUID);
        if(playerData==null) return this;
        this.scoreboard.getScoreboardManager().removeTeam(playerUUID, this);
        playerData.removeTeam();
        this.players.remove(playerUUID);
        Player player = Bukkit.getPlayer(playerUUID);
        if(player!=null) {
            bukkitTeam.removeEntry(player.getName());
            return this;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
        if(offlinePlayer.hasPlayedBefore()) {
            String playerName = offlinePlayer.getName();
            assert playerName!=null;
            bukkitTeam.removeEntry(playerName);
        }
        return this;
    }

    @Override
    public @NotNull Team addEntity(@NotNull UUID entityUUID) {
        if(Bukkit.getOfflinePlayer(entityUUID).hasPlayedBefore()) return this;

        AddMemberToTeamEvent event = new AddMemberToTeamEvent(
                entityUUID, this, AddMemberToTeamEvent.Reason.ENTITY_ADD
        );

        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) return this;
        bukkitTeam.addEntry(entityUUID.toString());
        this.entities.add(entityUUID);
        this.scoreboard.getScoreboardManager().setTeam(entityUUID, this);
        return this;
    }

    @Override
    public @NotNull Team addEntity(@NotNull Entity entity) {
        if(entity instanceof Player)
            throw new ScoreboardException("Cannot add player in a team using addEntity() method");
        return addEntity(entity.getUniqueId());
    }

    @Override
    public @NotNull Team removeEntity(@NotNull Entity entity) {
        return removeEntity(entity.getUniqueId());
    }

    @Override
    public @NotNull Team removeEntity(@NotNull UUID entityUUID) {
        if(!this.entities.contains(entityUUID)) return this;
        this.scoreboard.getScoreboardManager().removeTeam(entityUUID, this);
        this.entities.remove(entityUUID);
        bukkitTeam.removeEntry(entityUUID.toString());
        return this;
    }

    @Override
    public boolean hasPlayer(@NotNull Player player) {
        return hasPlayer(player.getUniqueId());
    }

    @Override
    public boolean hasPlayer(@NotNull UUID playerUUID) {
        return players.contains(playerUUID);
    }

    @Override
    public boolean hasEntity(@NotNull Entity entity) {
        return hasEntity(entity.getUniqueId());
    }

    @Override
    public boolean hasEntity(@NotNull UUID entityUUID) {
        return this.entities.contains(entityUUID);
    }

    @Override
    public boolean hasMember(@NotNull UUID uuid) {
        return hasPlayer(uuid) || hasEntity(uuid);
    }

    @Override
    public @NotNull Set<UUID> getPlayers() {
        return new HashSet<>(this.players);
    }

    @Override
    public @NotNull Set<UUID> getEntities() {
        return new HashSet<>(this.entities);
    }

    @Override
    public @NotNull Set<UUID> getMembers() {
        Set<UUID> combined = new HashSet<>(this.players);
        combined.addAll(entities);
        return combined;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        players.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(message));
    }
}
