package org.velas.scoreboard.core.sidebar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.scoreboard.api.event.SidebarShowEvent;
import org.velas.scoreboard.api.sidebar.PlayerSidebar;
import org.velas.scoreboard.core.data.PlayerData;
import org.velas.scoreboard.core.exception.ScoreboardException;
import org.velas.scoreboard.core.implementation.BukkitScoreboardImpl;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class BasePlayerSidebar implements PlayerSidebar {

    @Nullable
    private UUID playerUUID;
    @NotNull private final PacketSender sender;
    @NotNull private final SidebarElements elements;
    @NotNull private final BukkitScoreboardImpl scoreboard;
    private boolean isBeingViewed;
    private boolean canShowOnPlayerRespawn;
    private boolean canShowOnPlayerJoin;

    public BasePlayerSidebar(@NotNull SidebarElements elements, @NotNull PacketSender sender){
        this.playerUUID=null;
        this.elements=elements;
        this.scoreboard=elements.getScoreboard();
        this.sender=sender;
        this.isBeingViewed =false;
        this.canShowOnPlayerJoin=true;
        this.canShowOnPlayerRespawn=true;
    }

    @Override
    public void setPlayer(@NotNull Player player) {
        setPlayer(player.getUniqueId());
    }

    @Override
    public void setPlayer(@NotNull UUID playerUUID) {
        elements.checkNotRemoved();
        elements.checkPlayerInScoreboard(playerUUID);
        if(this.playerUUID!=null) removePlayer();
        this.playerUUID=playerUUID;

        scoreboard.getSidebarManager().updateSidebarEntry(playerUUID, this);
    }

    @Override
    public void removePlayer() {
        elements.checkNotRemoved();
        checkHasPlayer();
        assert playerUUID !=null;

        if(isBeingViewed) sender.sendRemoveObjectivePacket(playerUUID);

        Map<UUID, PlayerData> players = scoreboard.getPlayersData();
        if(!players.containsKey(playerUUID)) return;
        PlayerData data = players.get(playerUUID);
        if(!data.hasSidebar() || data.getSidebar()!=this) return;
        data.removeSidebar();

        this.playerUUID=null;
    }

    @Override
    public @Nullable Player getPlayer() {
        elements.checkNotRemoved();
        if(playerUUID==null) return null;
        return elements.checkPlayerOnline(playerUUID);
    }

    @Override
    public @Nullable UUID getPlayerUUID() {
        elements.checkNotRemoved();
        return this.playerUUID;
    }

    @Override
    public void show() {
        show(SidebarShowEvent.Reason.MANUALLY);
    }

    @Override
    public void show(SidebarShowEvent.Reason reason) {
        elements.checkNotRemoved();
        checkHasPlayer();
        Player player = elements.checkPlayerOnline(playerUUID);
        if(!elements.canView()) return;
        if(isBeingViewed) return;
        assert playerUUID != null;

        SidebarShowEvent event = new SidebarShowEvent(player, this, reason);
        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled()) return;

        isBeingViewed=true;
        sender.sendCompleteObjectiveSetup(playerUUID);
    }

    @Override
    public void hide() {
        elements.checkNotRemoved();
        checkHasPlayer();
        assert playerUUID != null;

        if(!isBeingViewed) return;
        this.isBeingViewed =false;

        sender.sendHideObjectivePacket(playerUUID);
    }

    @Override
    public @NotNull String getTitle() {
        elements.checkNotRemoved();
        return elements.getTitle();
    }

    @Override
    public void setTitle(@NotNull String title) {
        elements.checkNotRemoved();
        checkHasPlayer();
        assert playerUUID != null;
        elements.setTitle(title);
        sender.sendCompleteObjectiveSetup(playerUUID);
    }

    @Override
    public void setLine(int line, @NotNull String text) {
        elements.checkNotRemoved();
        checkHasPlayer();
        elements.validateLineNumber(line);
        assert playerUUID != null;

        if(elements.getLines().containsKey(line)){
            elements.getLines().get(line).update(playerUUID, text);
        }else{
            Line lineData = elements.getOrCreateLine(line, text);
            sender.sendLineSetupPacket(playerUUID, lineData);
        }
    }

    @Override
    public @Nullable String getLine(int line) {
        elements.checkNotRemoved();
        elements.validateLineNumber(line);
        Line lineData = elements.getLines().get(line);
        return lineData != null ? lineData.getText() : null;
    }

    @Override
    public void removeLine(int line) {
        elements.checkNotRemoved();
        checkHasPlayer();
        assert playerUUID != null;

        Line lineData = elements.getLines().get(line);
        if(lineData==null) return;
        sender.sendRemoveLinePacket(playerUUID, lineData);
        elements.removeLineElement(line);
    }

    @Override
    public void clearLines() {
        new ArrayList<>(elements.getLines().keySet()).forEach(this::removeLine);
        elements.getLines().clear();
    }

    @Override
    public boolean canView() {
        elements.checkNotRemoved();
        return elements.canView();
    }

    @Override
    public void setCanView(boolean can) {
        elements.setCanView(can);
        if(!can) this.hide();
    }

    @Override
    public boolean isBeingViewed() {
        return this.isBeingViewed;
    }

    @Override
    public void remove() {
        elements.checkNotRemoved();
        checkHasPlayer();
        assert playerUUID != null;

        clearLines();
        sender.sendRemoveObjectivePacket(playerUUID);

        removePlayer();
        this.isBeingViewed=false;
        elements.onRemove();
    }

    @Override
    public boolean isRemoved() {
        return elements.isRemoved();
    }

    @Override
    public boolean canShowOnPlayerJoin() {
        return this.canShowOnPlayerJoin;
    }

    @Override
    public void setCanShowOnPlayerJoin(boolean can) {
        this.canShowOnPlayerJoin=can;
    }

    @Override
    public boolean canShowOnPlayerRespawn() {
        return this.canShowOnPlayerRespawn;
    }

    @Override
    public void setCanShowOnPlayerRespawn(boolean can) {
        this.canShowOnPlayerRespawn=can;
    }

    @Override
    public boolean hasPlayer(@NotNull Player player) {
        return hasPlayer(player.getUniqueId());
    }

    @Override
    public boolean hasPlayer(@NotNull UUID playerUUID) {
        if(this.playerUUID==null) return false;
        return this.playerUUID.equals(playerUUID);
    }

    private void checkHasPlayer(){
        if(playerUUID==null)
            throw new ScoreboardException("Sidebar has no player in it");
    }
}
