package org.velas.scoreboard.api.team;

import org.bukkit.scoreboard.Team;

/**
 * Represents the collision rule for a team.
 *
 * <p>This defines how entities in this team interact (collide) with
 * other entities in the world.</p>
 *
 * <p>This enum acts as an abstraction over Bukkit's
 * {@link org.bukkit.scoreboard.Team.OptionStatus}.</p>
 */
public enum TeamCollisionRule {

    /**
     * Entities in this team will not collide with any entities.
     */
    NEVER,

    /**
     * Entities will only collide with members of their own team.
     */
    FOR_OWN_TEAM,

    /**
     * Entities will only collide with entities from other teams.
     */
    FOR_OTHER_TEAMS,

    /**
     * Entities will collide with all entities.
     */
    ALWAYS;

    /**
     * Converts this API enum to Bukkit's {@link Team.OptionStatus}.
     *
     * @return the corresponding Bukkit collision rule
     */
    public Team.OptionStatus toBukkitStatus(){
        return switch (this){
            case NEVER -> Team.OptionStatus.NEVER;
            case FOR_OWN_TEAM -> Team.OptionStatus.FOR_OWN_TEAM;
            case FOR_OTHER_TEAMS -> Team.OptionStatus.FOR_OTHER_TEAMS;
            case ALWAYS -> Team.OptionStatus.ALWAYS;
        };
    }

    /**
     * Converts a Bukkit {@link Team.OptionStatus}
     * to this API's {@link TeamCollisionRule}.
     *
     * @param status the Bukkit collision rule
     * @return the corresponding API collision rule
     */
    public static TeamCollisionRule fromBukkitStatus(Team.OptionStatus status) {
        return switch (status) {
            case NEVER -> NEVER;
            case FOR_OWN_TEAM -> FOR_OWN_TEAM;
            case FOR_OTHER_TEAMS -> FOR_OTHER_TEAMS;
            case ALWAYS -> ALWAYS;
        };
    }
}
