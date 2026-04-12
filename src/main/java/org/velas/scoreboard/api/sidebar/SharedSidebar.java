package org.velas.scoreboard.api.sidebar;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.api.event.SidebarShowEvent;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a sidebar that can be shared between multiple players.
 *
 * <p>A SharedSidebar allows the same sidebar content to be displayed
 * to multiple players simultaneously. It supports managing viewers,
 * adding/removing players, and controlling visibility per player.</p>
 *
 * <p>Unlike {@link PlayerSidebar}, this sidebar is not limited to a single player
 * and can be shown to multiple players at once.</p>
 */
public interface SharedSidebar extends Sidebar {

    /**
     * Gets the unique identifier of this sidebar.
     *
     * <p>This ID can be used to distinguish between different shared sidebars, any two shared sidebars cannot hold the same id.</p>
     *
     * @return the unique sidebar ID
     */
    @NotNull String getId();

    /**
     * Gets all players associated with this sidebar.
     *
     * <p>This includes players who are registered to this sidebar,
     * regardless of whether they are currently viewing it.</p>
     *
     * @return a collection of player UUIDs
     */
    @NotNull Collection<UUID> getPlayers();

    /**
     * Gets all players currently viewing this sidebar.
     *
     * @return a collection of player UUIDs who are viewing the sidebar
     */
    @NotNull Collection<UUID> getViewers();

    /**
     * Shows this sidebar to all associated players with reason {@link org.velas.scoreboard.api.event.SidebarShowEvent.Reason#MANUALLY} for all.
     */
    void show();

    /**
     * Shows this sidebar to a specific player with {@link SidebarShowEvent.Reason#MANUALLY} as default reason.
     *
     * @param playerUUID the UUID of the player
     */
    void showTo(@NotNull UUID playerUUID);

    /**
     * Shows this sidebar to a specific player with a specific reason.
     *
     * <p>This can be used for event handling or debugging purposes.</p>
     *
     * @param playerUUID the UUID of the player
     * @param reason     the reason for showing the sidebar
     */
    void showTo(@NotNull UUID playerUUID, SidebarShowEvent.Reason reason);

    /**
     * Hides this sidebar from all viewers.
     */
    void hide();

    /**
     * Hides this sidebar from a specific player.
     *
     * @param playerUUID the UUID of the player
     */
    void hideFrom(@NotNull UUID playerUUID);

    /**
     * Checks whether a specific player is currently viewing this sidebar.
     *
     * @param playerUUID the UUID of the player
     * @return true if the player is viewing the sidebar
     */
    boolean isViewedBy(@NotNull UUID playerUUID);

    /**
     * Checks whether this sidebar is being viewed by all associated players.
     *
     * @return true if all players are viewing the sidebar
     */
    boolean isBeingViewedByAll();

    /**
     * Adds a player to this sidebar.
     * The player must be part of the same scoreboard to which this sidebar belongs.
     *
     * <p>This does not necessarily mean the sidebar is shown immediately;
     * {@link #show()} or {@link #showTo(UUID)} may be required.</p>
     *
     * @param player the player to add
     */
    void addPlayer(@NotNull Player player);

    /**
     * Removes a player from this sidebar.
     *
     * <p>If the player is currently viewing the sidebar, it may also be hidden.</p>
     *
     * @param player the player to remove
     */
    void removePlayer(@NotNull Player player);
}
