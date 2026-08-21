package org.velas.scoreboard.core.sidebar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.scoreboard.api.event.SidebarShowEvent;
import org.velas.scoreboard.api.sidebar.SharedSidebar;
import org.velas.scoreboard.core.exception.ScoreboardException;
import org.velas.scoreboard.core.implementation.BukkitScoreboardImpl;

import java.util.*;

public class BaseSharedSidebar implements SharedSidebar {

    private final List<UUID> players = new ArrayList<>();
    private final List<UUID> viewers = new ArrayList<>();
    private final PacketSender sender;
    private final SidebarElements elements;
    private final BukkitScoreboardImpl scoreboard;
    private boolean canShowOnPlayerRespawn;
    private boolean canShowOnPlayerJoin;

    public BaseSharedSidebar(@NotNull SidebarElements elements, @NotNull PacketSender sender){
        this.sender=sender;
        this.elements=elements;
        this.scoreboard=elements.getScoreboard();
        this.canShowOnPlayerJoin=true;
        this.canShowOnPlayerRespawn=true;
    }


    @Override
    public @NotNull String getId() {
        return elements.getId();
    }

    @Override
    public @NotNull Set<UUID> getPlayers() {
        elements.checkNotRemoved();
        return new HashSet<>(this.players);
    }

    @Override
    public @NotNull Set<UUID> getViewers() {
        elements.checkNotRemoved();
        return new HashSet<>(this.viewers);
    }

    @Override
    public void show() {
        elements.checkNotRemoved();
        players.stream().filter(uuid ->
                !viewers.contains(uuid)).toList().forEach(this::showTo);
    }

    @Override
    public void showTo(@NotNull UUID playerUUID) {
        elements.checkNotRemoved();
        Player player = elements.checkPlayerOnline(playerUUID);
        if(!players.contains(playerUUID))
            throw new ScoreboardException(String.format(
                    "Player [%s] was not found in shared sidebar, first add player before showing sidebar to it",
                    player.getName()
            ));
        showTo(playerUUID, SidebarShowEvent.Reason.MANUALLY);
    }

    @Override
    public void showTo(@NotNull UUID playerUUID, SidebarShowEvent.Reason reason) {
        elements.checkNotRemoved();
        Player player = elements.checkPlayerOnline(playerUUID);

        if(!players.contains(playerUUID))
            throw new ScoreboardException(String.format(
                    "Player [%s] was not found in shared sidebar, first add player before showing sidebar to it",
                    player.getName()
            ));

        if(!elements.canView()) return;
        if(isViewedBy(playerUUID)) return;

        SidebarShowEvent event = new SidebarShowEvent(player, this, reason);
        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled()) return;

        this.viewers.add(playerUUID);
        sender.sendCompleteObjectiveSetup(playerUUID);
    }

    @Override
    public void hide() {
        elements.checkNotRemoved();
        if(!isBeingViewed()) return;

        new ArrayList<>(viewers).forEach(this::hideFrom);
    }

    @Override
    public void hideFrom(@NotNull UUID playerUUID) {
        elements.checkNotRemoved();
        if(!isViewedBy(playerUUID)) return;

        viewers.remove(playerUUID);
        sender.sendHideObjectivePacket(playerUUID);
    }

    @Override
    public boolean isViewedBy(@NotNull UUID playerUUID) {
        elements.checkNotRemoved();
        elements.checkPlayerInScoreboard(playerUUID);
        return viewers.contains(playerUUID);
    }

    @Override
    public boolean isBeingViewedByAll() {
        elements.checkNotRemoved();
        return players.size() == viewers.size();
    }

    @Override
    public void addPlayer(@NotNull Player player) {
        this.addPlayer(player.getUniqueId());
    }

    @Override
    public void addPlayer(@NotNull UUID playerUUID) {
        elements.checkNotRemoved();
        elements.checkPlayerInScoreboard(playerUUID);
        scoreboard.getSidebarManager().updateSidebarEntry(playerUUID, this);
        players.add(playerUUID);
    }

    @Override
    public void removePlayer(@NotNull Player player) {
        UUID playerUUID = player.getUniqueId();
        removePlayer(playerUUID);
    }

    @Override
    public void removePlayer(@NotNull UUID playerUUID) {
        elements.checkNotRemoved();
        if(!players.contains(playerUUID)) return;
        players.remove(playerUUID);
        viewers.remove(playerUUID);

        sender.sendRemoveObjectivePacket(playerUUID);
    }

    @Override
    public @NotNull String getTitle() {
        elements.checkNotRemoved();
        return elements.getTitle();
    }

    @Override
    public void setTitle(@NotNull String title) {
        elements.checkNotRemoved();
        elements.setTitle(title);
        for(UUID playerUUID: viewers) sender.sendCompleteObjectiveSetup(playerUUID);
    }

    @Override
    public void setLine(int line, @NotNull String text) {
        elements.checkNotRemoved();
        elements.validateLineNumber(line);

        if(elements.getLines().containsKey(line))
            elements.getLines().get(line).updateForAll(viewers, text);
        else{
            Line lineData = elements.getOrCreateLine(line, text);
            viewers.forEach(playerUUID -> sender.sendLineSetupPacket(playerUUID, lineData));
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

        Line lineData = elements.getLines().get(line);
        if(lineData==null) return;
        viewers.forEach(playerUUID -> sender.sendRemoveLinePacket(playerUUID, lineData));
        elements.removeLineElement(line);
    }

    @Override
    public void clearLines() {
        elements.checkNotRemoved();
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
        elements.checkNotRemoved();
        elements.setCanView(can);
        if(!can) this.hide();
    }

    @Override
    public boolean isBeingViewed() {
        return !viewers.isEmpty();
    }

    @Override
    public void remove() {
        elements.checkNotRemoved();
        clearLines();
        viewers.forEach(sender::sendRemoveObjectivePacket);
        viewers.clear();
        new ArrayList<>(players).forEach(this::removePlayer);
        this.scoreboard.getSidebarManager().onSharedSidebarRemoved(this);
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
        elements.checkNotRemoved();
        return players.contains(playerUUID);
    }
}
