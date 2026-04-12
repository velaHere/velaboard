package org.velas.scoreboard.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.api.team.Team;

import java.util.UUID;

/**
 * Called when a member (player or entity) is added to a {@link Team}.
 *
 * <p>This event is cancellable. Cancelling it will prevent the member from being added to the team.</p>
 */
public class AddMemberToTeamEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;

    private final UUID uuid;
    private final Team team;
    private final Reason reason;

    /**
     * Creates a new AddMemberToTeamEvent.
     *
     * @param uuid   the UUID of the member being added
     * @param team   the team to which the member is being added
     * @param reason the reason why the member is being added
     */
    public AddMemberToTeamEvent(@NotNull UUID uuid, @NotNull Team team, @NotNull Reason reason){
        this.uuid=uuid;
        this.team=team;
        this.reason=reason;
        this.isCancelled=false;
    }

    /**
     * Gets the UUID of the member being added.
     *
     * @return the member's UUID
     */
    public UUID getUniqueIdOfMember() {
        return uuid;
    }

    /**
     * Gets the team involved in this event.
     *
     * @return the team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Gets the reason why the member is being added.
     *
     * @return the reason
     */
    public Reason getReason() {
        return reason;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled=b;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Represents the reason for adding a member to a team.
     */
    public enum Reason{
        PLAYER_JOIN,
        PLAYER_ADD,
        ENTITY_ADD,
        OTHER
    }
}
