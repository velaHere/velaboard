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
     * Checks whether this sidebar can currently be viewed.
     *
     * @return true if viewable
     */
    boolean canView();

    /**
     * Sets whether this sidebar can be viewed.
     *
     * @param can true to allow viewing
     */
    void setCanView(boolean can);

    /**
     * Checks if the sidebar is currently being viewed by player(s).
     *
     * @return true if being viewed
     */
    boolean isBeingViewed();

    /**
     * Permanently removes the sidebar, preventing any further use.
     */
    void remove();

    /**
     * Checks if the sidebar has been removed.
     *
     * @return true if removed
     */
    boolean isRemoved();

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
