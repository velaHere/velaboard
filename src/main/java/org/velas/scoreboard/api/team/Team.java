package org.velas.scoreboard.api.team;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.event.AddMemberToTeamEvent;

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
     * @param enabled true to enable friendly fire
     * @return this team instance
     */
    @NotNull Team setFriendlyFire(boolean enabled);

    /**
     * Enables or disables friendly fire between entities and between entities and players in this team.
     *
     * @param enabled true to enable entity friendly fire
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
     * Adds a player to this team with specific reason.
     * The player must be part of the same scoreboard to which this team belongs.
     *
     * @param player the player to add
     * @return this team instance
     */
    @NotNull Team addPlayer(@NotNull Player player, @NotNull AddMemberToTeamEvent.Reason reason);

    /**
     * Removes a player from this team.
     *
     * @param player the player to remove
     * @return this team instance
     */
    @NotNull Team removePlayer(@NotNull Player player);

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
     * Checks if a player is part of this team.
     *
     * @param player the player
     * @return true if the player is in the team
     */
    boolean hasPlayer(@NotNull Player player);

    /**
     * Checks if an entity is part of this team.
     *
     * @param entity the entity
     * @return true if the entity is in the team
     */
    boolean hasEntity(@NotNull Entity entity);
}