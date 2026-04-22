package org.velas.scoreboard.api.team;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.event.AddMemberToTeamEvent;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a {@link Scoreboard} team.
 *
 * <p>A team allows grouping of players and entities with shared properties such as
 * color, prefix/suffix, collision rules,friendly fire(also includes entities), and visibility settings.</p>
 *
 * <p>It provides full control over team behavior and member management.</p>
 */
public interface Team {

    /**
     * Gets the name of this team.
     *
     * @return the team name
     */
    @NotNull String getName();

    /**
     * Sets the color of this team.
     *
     * @param color the team color
     * @return this team instance
     */
    @NotNull Team setColor(@NotNull TeamColor color);

    /**
     * Gets the color of this team.
     *
     * @return the team color
     */
    @NotNull TeamColor getColor();

    /**
     * Sets the prefix displayed before member's names in this team.
     *
     * @param prefix the prefix text
     * @return this team instance
     */
    @NotNull Team setPrefix(@NotNull String prefix);

    /**
     * Gets the prefix of this team.
     *
     * @return the prefix text
     */
    @NotNull String getPrefix();

    /**
     * Sets the suffix displayed after member's names in this team.
     *
     * @param suffix the suffix text
     * @return this team instance
     */
    @NotNull Team setSuffix(@NotNull String suffix);

    /**
     * Gets the suffix of this team.
     *
     * @return the suffix text
     */
    @NotNull String getSuffix();

    /**
     * Enables or disables friendly fire between players in this team.
     *
     * <p>When enabled, players in the same team can damage each other directly</p>
     *
     * <p>This setting only affects interactions between players. For entity-related
     * interactions, see {@link #setEntitiesFriendlyFire(boolean)}.</p>
     *
     * @param enabled true to allow player-vs-player damage within the team
     * @return this team instance
     */
    @NotNull Team setFriendlyFire(boolean enabled);

    /**
     * Enables or disables friendly fire involving non-player entities in this team.
     *
     * <p>When enabled, damage is allowed in the following cases:</p>
     * <ul>
     *     <li>Entity vs entity (e.g., mob attacking another mob in the same team)</li>
     *     <li>Player vs entity (e.g., player attacking a team entity)</li>
     *     <li>Entity vs player (e.g., mob damaging a team player)</li>
     * </ul>
     *
     *
     * <p>This setting does not affect direct player-vs-player damage.
     * For that, see {@link #setFriendlyFire(boolean)}.</p>
     *
     * @param enabled true to allow entity-related damage within the team
     * @return this team instance
     */
    @NotNull Team setEntitiesFriendlyFire(boolean enabled);

    /**
     * Checks if friendly fire between players is enabled.
     *
     * @return true if enabled
     */
    boolean isFriendlyFireEnabled();

    /**
     * Checks whether friendly fire is enabled between entities and between entities and players.
     *
     * @return true if enabled
     */
    boolean isEntitiesFriendlyFireEnabled();

    /**
     * Sets whether team members can see invisible teammates.
     *
     * @param enabled true to allow visibility of invisible teammates
     * @return this team instance
     */
    @NotNull Team setCanSeeFriendlyInvisibilities(boolean enabled);

    /**
     * Checks if invisible teammates are visible to team members.
     *
     * @return true if visible
     */
    boolean canSeeFriendlyInvisibilities();

    /**
     * Sets the collision rule for this team.
     *
     * @param rule the collision rule
     * @return this team instance
     */
    @NotNull Team setTeamCollisionRule(@NotNull TeamCollisionRule rule);

    /**
     * Gets the collision rule of this team.
     *
     * @return the collision rule
     */
    @NotNull TeamCollisionRule getCollisionRule();

    /**
     * Sets the name tag visibility rule for this team.
     *
     * @param visibility the visibility rule
     * @return this team instance
     */
    @NotNull Team setNameTagVisibility(@NotNull TeamNameTagVisibility visibility);

    /**
     * Gets the name tag visibility rule of this team.
     *
     * @return the visibility rule
     */
    @NotNull TeamNameTagVisibility getNameTagVisibility();

    /**
     * Adds a player to this team with reason {@link AddMemberToTeamEvent.Reason#PLAYER_ADD} as default.
     * The player must be part of the same scoreboard to which this team belongs.
     *
     * @param player the player to add
     * @return this team instance
     */
    @NotNull Team addPlayer(@NotNull Player player);

    /**
     * Adds a player to this team using their UUID even if the player is offline.
     *
     * <p>This uses {@link AddMemberToTeamEvent.Reason#PLAYER_ADD} as the default reason.</p>
     *
     * <p>The player must be part of the same scoreboard to which this team belongs.</p>
     *
     * @param playerUUID the UUID of the player to add
     * @return this team instance
     */
    @NotNull Team addPlayer(@NotNull UUID playerUUID);

    /**
     * Removes a player from this team.
     *
     * @param player the player to remove
     * @return this team instance
     */
    @NotNull Team removePlayer(@NotNull Player player);

    /**
     * Removes a player from this team using their UUID even if the player is offline.
     *
     * @param playerUUID the UUID of the player to remove
     * @return this team instance
     */
    @NotNull Team removePlayer(@NotNull UUID playerUUID);

    /**
     * Adds an entity to this team using its UUID.
     *
     * <p>This uses {@link AddMemberToTeamEvent.Reason#ENTITY_ADD} as the default reason.</p>
     *
     * @param entityUUID the UUID of the entity to add
     * @return this team instance
     */
    @NotNull Team addEntity(@NotNull UUID entityUUID);

    /**
     * Adds an entity to this team with reason {@link AddMemberToTeamEvent.Reason#ENTITY_ADD} as default.
     *
     * @param entity the entity to add
     * @return this team instance
     */
    @NotNull Team addEntity(@NotNull Entity entity);

    /**
     * Removes an entity from this team.
     *
     * @param entity the entity to remove
     * @return this team instance
     */
    @NotNull Team removeEntity(@NotNull Entity entity);

    /**
     * Removes an entity from this team using its UUID.
     *
     * @param entityUUID the UUID of the entity to remove
     * @return this team instance
     */
    @NotNull Team removeEntity(@NotNull UUID entityUUID);

    /**
     * Checks if a player is part of this team.
     *
     * @param player the player
     * @return true if the player is in the team
     */
    boolean hasPlayer(@NotNull Player player);

    /**
     * Checks if a player with the given UUID is part of this team.
     *
     * @param playerUUID the UUID of the player
     * @return true if the player is in the team
     */
    boolean hasPlayer(@NotNull UUID playerUUID);

    /**
     * Checks if an entity is part of this team.
     *
     * @param entity the entity
     * @return true if the entity is in the team
     */
    boolean hasEntity(@NotNull Entity entity);

    /**
     * Checks if an entity with the given UUID is part of this team.
     *
     * @param entityUUID the UUID of the entity
     * @return true if the entity is in the team
     */
    boolean hasEntity(@NotNull UUID entityUUID);

    /**
     * Checks if a UUID (player or entity) is part of this team.
     *
     * <p>This method does not distinguish between players and entities.</p>
     *
     * @param uuid the UUID of the member
     * @return true if the UUID belongs to a member of this team
     */
    boolean hasMember(@NotNull UUID uuid);

    /**
     * Gets all player UUIDs that are part of this team.
     *
     * @return a collection of player UUIDs
     */
    @NotNull Collection<UUID> getPlayers();

    /**
     * Gets all entity UUIDs that are part of this team.
     *
     * @return a collection of entity UUIDs
     */
    @NotNull Collection<UUID> getEntities();

    /**
     * Gets all members of this team.
     *
     * <p>This includes both players and entities.</p>
     *
     * @return a collection of all member UUIDs
     */
    @NotNull Collection<UUID> getMembers();
}