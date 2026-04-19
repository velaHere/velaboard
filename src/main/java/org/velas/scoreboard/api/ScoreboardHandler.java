package org.velas.scoreboard.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Central manager responsible for creating and retrieving scoreboard instances.
 *
 * <p>This handler acts as the internal bridge between the API and the underlying
 * scoreboard system implementation.</p>
 *
 * <p>It is typically accessed through {@link ScoreboardAPI#getHandler()}.</p>
 */
public interface ScoreboardHandler {

    /**
     * Gets the scoreboard associated with a specific player.
     *
     * <p>This returns the active scoreboard instance assigned to the player,
     * if one exists.</p>
     *
     * @param player the player whose scoreboard is requested
     * @return the player's scoreboard, or {@code null} if none exists
     */
    @Nullable Scoreboard getScoreboard(@NotNull Player player);

    /**
     * Creates a new scoreboard instance.
     *
     * <p>This scoreboard is not automatically assigned to any player.
     * Player must be manually registered or assigned using the API.</p>
     *
     * @return a new Scoreboard instance
     */
    @NotNull Scoreboard createScoreboard();
}
