package org.velas.scoreboard.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.api.sidebar.PlayerSidebar;
import org.velas.scoreboard.api.sidebar.SharedSidebar;
import org.velas.scoreboard.api.sidebar.Sidebar;

/**
 * Called when a {@link Sidebar} ({@link PlayerSidebar} or {@link SharedSidebar}) is shown to a player.
 *
 * <p>This event is cancellable. Cancelling it will prevent the sidebar from being shown to the player.</p>
 */
public final class SidebarShowEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    private final Player player;
    private final Sidebar sidebar;
    private final Reason reason;

    /**
     * Creates a new AddMemberToTeamEvent.
     *
     * @param player   the player to whom the sidebar is shown
     * @param sidebar  the sidebar to be displayed
     * @param reason   the reason for displaying the sidebar
     */
    public SidebarShowEvent(@NotNull Player player,
                            @NotNull Sidebar sidebar,
                            @NotNull Reason reason){
        this.player=player;
        this.sidebar=sidebar;
        this.reason=reason;
    }

    /**
     * Gets the player to whom the sidebar is shown.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the sidebar involved in the event
     *
     * @return the sidebar ({@link PlayerSidebar} or {@link SharedSidebar})
     */
    public Sidebar getSidebar() {
        return sidebar;
    }

    /**
     * Gets the reason why the sidebar is being displayed
     *
     * @return the reason
     */
    public Reason getReason() {
        return reason;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled=cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Represents the reason for displaying the sidebar to a player.
     */
    public enum Reason{
        PLAYER_JOIN,
        PLAYER_RESPAWN,
        MANUALLY,
        OTHER
    }
}
