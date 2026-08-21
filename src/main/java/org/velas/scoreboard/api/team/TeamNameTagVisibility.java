package org.velas.scoreboard.api.team;

import org.bukkit.scoreboard.Team;

/**
 * Represents the visibility rules for team name tags.
 *
 * <p>This determines when player name tags are visible in relation to
 * team membership.</p>
 *
 * <p>This enum acts as an abstraction over Bukkit's
 * {@link Team.OptionStatus}.</p>
 */
public enum TeamNameTagVisibility {

    /**
     * Name tags are never visible to any players.
     */
    NEVER,

    /**
     * Name tags are only visible to players from other teams.
     */
    FOR_OTHER_TEAMS,

    /**
     * Name tags are only visible to members of the same team.
     */
    FOR_OWN_TEAM,

    /**
     * Name tags are always visible to all players.
     */
    ALWAYS;

    /**
     * Converts this API enum to Bukkit's {@link Team.OptionStatus}.
     *
     * @return the corresponding Bukkit name tag visibility
     */
    public Team.OptionStatus toBukkitStatus() {
        return switch (this) {
            case NEVER -> Team.OptionStatus.NEVER;
            case FOR_OTHER_TEAMS -> Team.OptionStatus.FOR_OTHER_TEAMS;
            case FOR_OWN_TEAM -> Team.OptionStatus.FOR_OWN_TEAM;
            case ALWAYS -> Team.OptionStatus.ALWAYS;
        };
    }

    /**
     * Converts a Bukkit {@link Team.OptionStatus}
     * to this API's {@link TeamNameTagVisibility}.
     *
     * @param status the Bukkit name tag visibility
     * @return the corresponding API visibility rule
     */
    public static TeamNameTagVisibility fromBukkitStatus(Team.OptionStatus status) {
        return switch (status) {
            case NEVER -> NEVER;
            case FOR_OTHER_TEAMS -> FOR_OTHER_TEAMS;
            case FOR_OWN_TEAM -> FOR_OWN_TEAM;
            case ALWAYS -> ALWAYS;
        };
    }
}
