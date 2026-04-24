package org.velas.scoreboard.api.sidebar;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents a generic scoreboard sidebar.
 *
 * <p>A sidebar consists of a title and multiple lines (0–14).
 * This interface provides methods to manage content and visibility.</p>
 */
public interface Sidebar {

    /**
     * Gets the title of the sidebar.
     *
     * @return the title
     */
    @NotNull String getTitle();

    /**
     * Sets the title of the sidebar and updates it for player(s) viewing this sidebar.
     *
     * @param title the new title
     */
    void setTitle(@NotNull String title);

    /**
     * Sets the text of a specific line of sidebar and updates it for player(s) viewing this sidebar. .
     *
     * @param line the line index (0–14)
     * @param text the text to display
     */
    void setLine(int line, @NotNull String text);

    /**
     * Gets the text of a specific line.
     *
     * @param line the line index
     * @return the line text or null if not set
     */
    @Nullable String getLine(int line);

    /**
     * Removes a specific line from the sidebar and updates it for player(s) viewing this sidebar.
     *
     * @param line the line index
     */
    void removeLine(int line);

    /**
     * Clears all lines from the sidebar and updates it for player(s) viewing this sidebar.
     */
    void clearLines();

    /**
     * Checks whether this sidebar is allowed to be displayed.
     *
     * <p>If false, the sidebar will not be visible to players
     * even if they are associated with it, even if {@link PlayerSidebar#show()} or {@link SharedSidebar#show()} is used.</p>
     *
     * @return true if the sidebar can be viewed
     */
    boolean canView();

    /**
     * Sets whether this sidebar is allowed to be displayed.
     *
     * <p>Disabling this will hide the sidebar from all associated player(s).</p>
     *
     * @param can true to allow viewing, false to hide it
     */
    void setCanView(boolean can);

    /**
     * Checks whether this sidebar is currently being displayed
     * to at least one player.
     *
     * @return true if at least one player is viewing this sidebar
     */
    boolean isBeingViewed();

    /**
     * Permanently removes this sidebar.
     *
     * <p>After removal, the sidebar should no longer be used,
     * and all associated players should be detached from it.</p>
     */
    void remove();

    /**
     * Checks whether this sidebar has been permanently removed.
     *
     * @return true if the sidebar is removed and no longer usable
     */
    boolean isRemoved();

    /**
     * Checks whether this sidebar should automatically be shown
     * to players when they join the server.
     *
     * @return true if the sidebar will be shown on player join
     */
    boolean canShowOnPlayerJoin();

    /**
     * Sets whether this sidebar should automatically be shown
     * to players when they join the server.
     *
     * @param can true to enable showing on join, false to disable
     */
    void setCanShowOnPlayerJoin(boolean can);

    /**
     * Checks whether this sidebar should automatically be shown
     * to players when they respawn.
     *
     * @return true if the sidebar will be shown on player respawn
     */
    boolean canShowOnPlayerRespawn();

    /**
     * Sets whether this sidebar should automatically be shown
     * to players when they respawn.
     *
     * @param can true to enable showing on respawn, false to disable
     */
    void setCanShowOnPlayerRespawn(boolean can);

    /**
     * Checks if a player is associated with this sidebar.
     *
     * @param player the player
     * @return true if associated
     */
    boolean hasPlayer(@NotNull Player player);

    /**
     * Checks if a player with the given UUID is associated with this sidebar.
     *
     * @param playerUUID the UUID of the player
     * @return true if associated
     */
    boolean hasPlayer(@NotNull UUID playerUUID);

    /**
     * Checks if the sidebar is {@link PlayerSidebar}
     */
    default boolean isPlayerSidebar(){
        return this instanceof PlayerSidebar;
    }

    /**
     * Checks if the sidebar is {@link SharedSidebar}
     */
    default boolean isSharedSidebar(){
        return this instanceof SharedSidebar;
    }

    /**
     * Casts this sidebar to PlayerSidebar if possible.
     */
    default Optional<PlayerSidebar> asPlayerSidebar(){
        return isPlayerSidebar() ? Optional.of((PlayerSidebar) this) : Optional.empty();
    }

    /**
     * Casts this sidebar to SharedSidebar if possible.
     */
    default Optional<SharedSidebar> asSharedSidebar(){
        return isSharedSidebar() ? Optional.of((SharedSidebar) this) : Optional.empty();
    }
}
