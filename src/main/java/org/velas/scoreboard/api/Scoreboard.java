package org.velas.scoreboard.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.scoreboard.api.sidebar.PlayerSidebar;
import org.velas.scoreboard.api.sidebar.SharedSidebar;
import org.velas.scoreboard.api.sidebar.Sidebar;
import org.velas.scoreboard.api.team.Team;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents the central scoreboard manager for handling players,
 * teams, and sidebars.
 *
 * <p>This interface acts as the main entry point of the API, providing
 * methods to manage:</p>
 *
 * <ul>
 *     <li>Registered players</li>
 *     <li>Teams and their members</li>
 *     <li>Player-specific and shared sidebars</li>
 * </ul>
 *
 * <p>All scoreboard-related operations should be performed through this interface.</p>
 */
public interface Scoreboard {

    /**
     * Gets all players currently registered in this scoreboard.
     *
     * @return a collection of player UUIDs
     */
    @NotNull Collection<UUID> getPlayers();

    /**
     * Registers a player into this scoreboard.
     *
     * <p>This allows the player to use teams and sidebars managed by this API.</p>
     *
     * @param player the player to add
     */
    void addPlayer(@NotNull Player player);

    /**
     * Registers a player into this scoreboard using their UUID even if they are offline.
     *
     * <p>This allows the player to use teams and sidebars(of this scoreboard) managed by this API.</p>
     *
     * @param playerUUID the UUID of the player to add
     */
    void addPlayer(@NotNull UUID playerUUID);

    /**
     * Removes a player from this scoreboard system.
     *
     * <p>This will also remove the player from any teams or sidebars.</p>
     *
     * @param player the player to remove
     */
    void removePlayer(@NotNull Player player);

    /**
     * Removes a player from this scoreboard system using their UUID even if they are offline.
     *
     * <p>This will also remove the player from any teams or sidebars.</p>
     *
     * @param playerUUID the UUID of the player to remove
     */
    void removePlayer(@NotNull UUID playerUUID);

    /**
     * Creates or retrieves a team with the given name.
     *
     * <p>If a team with the specified name already exists, it may be returned
     * instead of creating a new one.</p>
     *
     * @param name the team name
     * @return the team instance
     */
    @NotNull Team team(@NotNull String name);

    /**
     * Removes a team by name and removes all its members from it.
     *
     * @param name the team name
     */
    void removeTeam(@NotNull String name);

    /**
     * Gets a team by name.
     *
     * @param name the team name
     * @return the team, or null if not found
     */
    Team getTeam(@NotNull String name);

    /**
     * Gets the team, a specific player belongs to.
     *
     * @param player the player
     * @return the team, or null if the player is not in any team or not associated with this scoreboard.
     */
    @Nullable Team getTeamOf(@NotNull Player player);

    /**
     * Gets the team, a specific UUID(entity or player) belongs to.
     *
     * @param uuid the UUID of the player
     * @return the team, or null if the player is not in any team
     *         or not associated with this scoreboard
     */
    @Nullable Team getTeamOf(@NotNull UUID uuid);

    /**
     * Checks whether a team with the given name exists.
     *
     * @param name the team name
     * @return true if the team exists
     */
    boolean hasTeam(@NotNull String name);

    /**
     * Removes a player from their current team.
     *
     * @param player the player
     */
    void removePlayerFromTeam(@NotNull Player player);

    /**
     * Removes a player from their current team using their UUID even if they are offline.
     *
     * @param playerUUID the UUID of the player
     */
    void removePlayerFromTeam(@NotNull UUID playerUUID);

    /**
     * Removes all teams from this scoreboard.
     */
    void clearTeams();

    /**
     * Gets the PlayerSidebar associated with a player.
     *
     * @param player the player
     * @return the player's sidebar, or null if none exists
     */
    @Nullable PlayerSidebar getPlayerSidebar(@NotNull Player player);

    /**
     * Gets the PlayerSidebar associated with a player by UUID.
     *
     * @param playerUUID the UUID of the player
     * @return the player's sidebar, or null if none exists
     */
    @Nullable PlayerSidebar getPlayerSidebar(@NotNull UUID playerUUID);

    /**
     * Gets the shared sidebar that contains the specified player.
     *
     * @param player the player
     * @return the shared sidebar containing the player, or null if none exists
     */
    @Nullable SharedSidebar getSharedSidebar(@NotNull Player player);

    /**
     * Gets the shared sidebar that contains the specified player UUID.
     *
     * @param playerUUID the UUID of the player
     * @return the shared sidebar containing the player, or null if none exists
     */
    @Nullable SharedSidebar getSharedSidebar(@NotNull UUID playerUUID);

    /**
     * Gets a SharedSidebar by its unique ID.
     *
     * @param id the sidebar ID
     * @return the shared sidebar, or null if not found
     */
    @Nullable SharedSidebar getSharedSidebar(@NotNull String id);

    /**
     * Gets any sidebar ({@link PlayerSidebar} or {@link SharedSidebar}) associated with a player.
     *
     * @param player the player
     * @return the sidebar, or null if none exists
     */
    @Nullable Sidebar getSidebar(@NotNull Player player);

    /**
     * Gets any sidebar ({@link PlayerSidebar} or {@link SharedSidebar})
     * associated with a player by UUID.
     *
     * @param playerUUID the UUID of the player
     * @return the sidebar, or null if none exists
     */
    @Nullable Sidebar getSidebar(@NotNull UUID playerUUID);

    /**
     * Checks whether a player currently has a sidebar.
     *
     * @param player the player
     * @return true if the player has a sidebar
     */
    boolean hasSidebar(@NotNull Player player);

    /**
     * Checks whether a player with the given UUID currently has a sidebar.
     *
     * @param playerUUID the UUID of the player
     * @return true if the player has a sidebar
     */
    boolean hasSidebar(@NotNull UUID playerUUID);

    /**
     * Removes a player from their current sidebar.
     *
     * @param player the player
     */
    void removePlayerFromSidebar(@NotNull Player player);

    /**
     * Removes a player from their current sidebar using their UUID even if they are offline.
     *
     * @param playerUUID the UUID of the player
     */
    void removePlayerFromSidebar(@NotNull UUID playerUUID);

    /**
     * Creates a new PlayerSidebar with the given title.
     *
     * @param title the sidebar title
     * @return the created PlayerSidebar
     */
    @NotNull PlayerSidebar createPlayerSidebar(@NotNull String title);

    /**
     * Creates a new SharedSidebar with an auto-generated ID.
     *
     * @param title the sidebar title
     * @return the created SharedSidebar
     */
    @NotNull SharedSidebar createSharedSidebar(@NotNull String title);

    /**
     * Creates a new SharedSidebar with a specific ID and title.
     *
     * @param id    the unique sidebar ID
     * @param title the sidebar title
     * @return the created SharedSidebar
     */
    @NotNull SharedSidebar createSharedSidebar(@NotNull String id, @NotNull String title);
}
