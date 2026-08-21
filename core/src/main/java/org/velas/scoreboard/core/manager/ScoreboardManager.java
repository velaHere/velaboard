package org.velas.scoreboard.core.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.event.AddMemberToTeamEvent;
import org.velas.scoreboard.api.event.SidebarShowEvent;
import org.velas.scoreboard.api.sidebar.PlayerSidebar;
import org.velas.scoreboard.api.sidebar.SharedSidebar;
import org.velas.scoreboard.api.team.Team;
import org.velas.scoreboard.core.implementation.BukkitScoreboardImpl;
import org.velas.scoreboard.core.implementation.BukkitTeamImpl;
import org.velas.scoreboard.core.sidebar.SidebarFactory;
import org.velas.scoreboard.core.util.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ScoreboardManager implements Listener {

    private final Map<UUID, Scoreboard> scoreboards = new ConcurrentHashMap<>();
    private final Map<UUID, Team> teams = new ConcurrentHashMap<>();
    private final SidebarFactory sidebarFactory;

    public ScoreboardManager(JavaPlugin plugin, SidebarFactory factory){
        this.sidebarFactory=factory;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public @Nullable Scoreboard getScoreboard(@NotNull Player player) {
        return getScoreboard(player.getUniqueId());
    }

    public @Nullable Scoreboard getScoreboard(@NotNull UUID playerUUID) {
        return scoreboards.get(playerUUID);
    }

    public @NotNull Scoreboard createScoreboard() {
        return new BukkitScoreboardImpl(this, sidebarFactory);
    }

    public @NotNull Map<UUID, Scoreboard> getScoreboards(){
        return scoreboards;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(!scoreboards.containsKey(uuid))
            Logger.info("Player: " + uuid + " doesn't have scoreboard");
        Scoreboard scoreboard = getScoreboard(uuid);

        if(scoreboard==null) return;

        org.bukkit.scoreboard.Scoreboard bukkitScore = ((BukkitScoreboardImpl) scoreboard).getBukkitScoreboard();
        player.setScoreboard(bukkitScore);

        BukkitTeamImpl team = (BukkitTeamImpl) scoreboard.getTeamOf(player);
        if(team!=null) team.addPlayer(player, AddMemberToTeamEvent.Reason.PLAYER_JOIN);
        else {
            org.bukkit.scoreboard.Team entryTeam = bukkitScore.getEntryTeam(player.getName());
            if(entryTeam!=null) entryTeam.removeEntry(player.getName());
        }

        PlayerSidebar playerSidebar = scoreboard.getPlayerSidebar(uuid);
        if(playerSidebar!=null)
            if(playerSidebar.canShowOnPlayerJoin())
                playerSidebar.show(SidebarShowEvent.Reason.PLAYER_RESPAWN);

        SharedSidebar sharedSidebar = scoreboard.getSharedSidebar(uuid);
        if(sharedSidebar!=null)
            if(sharedSidebar.canShowOnPlayerJoin())
                sharedSidebar.showTo(uuid, SidebarShowEvent.Reason.PLAYER_RESPAWN);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(!scoreboards.containsKey(uuid))
            Logger.info("Player: " + uuid + " doesn't have scoreboard");

        Scoreboard scoreboard = getScoreboard(uuid);

        if(scoreboard==null) return;

        PlayerSidebar playerSidebar = scoreboard.getPlayerSidebar(player);
        if(playerSidebar!=null) {
            playerSidebar.hide();
            return;
        }
        SharedSidebar sharedSidebar = scoreboard.getSharedSidebar(player);
        if(sharedSidebar!=null) sharedSidebar.hideFrom(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Scoreboard scoreboard = scoreboards.get(playerUUID);
        if(scoreboard==null) return;

        PlayerSidebar playerSidebar = scoreboard.getPlayerSidebar(playerUUID);
        if(playerSidebar!=null)
            if(playerSidebar.canShowOnPlayerRespawn())
                playerSidebar.show(SidebarShowEvent.Reason.PLAYER_RESPAWN);

        SharedSidebar sharedSidebar = scoreboard.getSharedSidebar(playerUUID);
        if(sharedSidebar!=null)
            if(sharedSidebar.canShowOnPlayerRespawn())
                sharedSidebar.showTo(playerUUID, SidebarShowEvent.Reason.PLAYER_RESPAWN);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        UUID playerUUID = player.getUniqueId();
        Scoreboard scoreboard = scoreboards.get(playerUUID);
        if(scoreboard==null) return;

        PlayerSidebar playerSidebar = scoreboard.getPlayerSidebar(playerUUID);
        if(playerSidebar!=null) playerSidebar.hide();
        SharedSidebar sharedSidebar = scoreboard.getSharedSidebar(playerUUID);
        if(sharedSidebar!=null) sharedSidebar.hideFrom(playerUUID);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if(!this.teams.containsKey(entity.getUniqueId()) || !this.teams.containsKey(damager.getUniqueId())) return;
        Team team = teams.get(entity.getUniqueId());
        if(team!=this.teams.get(damager.getUniqueId())) return;
        if(team.isEntitiesFriendlyFireEnabled()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity instanceof Player) return;
        Team team = this.teams.get(entity.getUniqueId());
        if(team==null) return;
        team.removeEntity(entity);
    }

    @EventHandler
    public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent event){
        Entity entity = event.getEntity();
        if(!(entity instanceof Mob mob)) return;
        LivingEntity target = event.getTarget();
        if(target==null) return;
        if(!this.teams.containsKey(entity.getUniqueId()) || !this.teams.containsKey(target.getUniqueId())) return;
        Team team = teams.get(entity.getUniqueId());
        if(team!=this.teams.get(target.getUniqueId())) return;
        if(team.isEntitiesFriendlyFireEnabled()) return;
        mob.setTarget(null);
        event.setCancelled(true);
    }

    @EventHandler
    public void onMemberAddToTeam(AddMemberToTeamEvent event){
        if(event.getTeam().isEntitiesFriendlyFireEnabled()) return;
        teams.entrySet().stream().filter(entry -> entry.getValue()==event.getTeam()).forEach(uuid -> {
            Entity entity = Bukkit.getEntity(uuid.getKey());
            if(entity instanceof Mob mob){
                if(mob.getTarget()!=null && mob.getTarget().getUniqueId().equals(event.getUniqueIdOfMember())) mob.setTarget(null);
            }
        });
    }

    public void setTeam(@NotNull UUID uuid, @NotNull Team team){
        this.teams.put(uuid, team);
    }

    public void removeTeam(@NotNull UUID uuid, @NotNull Team team){
        this.teams.remove(uuid, team);
    }

    public void afterTeamDelete(@NotNull Team team){
        teams.entrySet().removeIf(entry -> entry.getValue()==team);
    }
}
