package org.velas.scoreboard.api.sidebar;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.scoreboard.api.event.SidebarShowEvent;

/**
 * Represents a sidebar that is associated with a single {@link Player}.
 *
 * <p>A PlayerSidebar is designed for individual player use, allowing
 * personalized scoreboard content such as stats, game info, or UI elements.</p>
 *
 * <p>Unlike {@link SharedSidebar}, this sidebar is bound to only one player
 * at a time.</p>
 */
public interface PlayerSidebar extends Sidebar {

    /**
     * Sets the player associated with this sidebar.
     * The player must be part of the same scoreboard to which this sidebar belongs.
     *
     * <p> This does not necessarily mean the sidebar is shown immediately; {@link #show()} may be required.
     * This will bind the sidebar to the given player. If a player was already
     * set, it will be replaced.</p>
     *
     * @param player the player to associate with this sidebar
     */
    void setPlayer(@NotNull Player player);

    /**
     * Removes the currently associated player from this sidebar.
     * If the player is currently viewing the sidebar, it may also be hidden.
     * <p>After calling this, the sidebar will no longer be linked to any player.</p>
     */
    void removePlayer();

    /**
     * Gets the player associated with this sidebar.
     *
     * @return the player, or {@code null} if no player is set
     */
    @Nullable Player getPlayer();

    /**
     * Shows this sidebar to the associated player with {@link SidebarShowEvent.Reason#MANUALLY} as default reason.
     *
     * <p>If no player is set, this method may have no effect and will throw an exception.</p>
     */
    void show();

    /**
     * Shows this sidebar to the associated player with a specific reason.
     *
     * <p>This method can be used for event tracking or debugging purposes,
     * where the reason may be passed to events such as a {@link SidebarShowEvent}.</p>
     *
     * @param reason the reason for showing the sidebar
     */
    void show(SidebarShowEvent.Reason reason);

    /**
     * Hides this sidebar from the associated player.
     *
     * <p>If the sidebar is not currently visible, this method may do nothing.</p>
     */
    void hide();
}
