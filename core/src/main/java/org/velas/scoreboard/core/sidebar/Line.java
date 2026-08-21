package org.velas.scoreboard.core.sidebar;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public final class Line {

    public final int SCORE;
    public final String ENTRY;
    private String text;
    private final @NotNull SidebarElementsHandler handler;
    private final @NotNull PacketSender sender;

    public Line(int score, @NotNull String entry, @NotNull String text, @NotNull SidebarElementsHandler handler, @NotNull PacketSender sender){
        this.SCORE=score;
        this.ENTRY=entry;
        this.text=text;
        this.handler=handler;
        this.sender=sender;
    }

    public void update(@NotNull UUID playerUUID, @NotNull String newText) {
        this.text=newText;
        handler.updateTeamPrefix(this, newText);
        sender.sendLineUpdatePacket(playerUUID, this);
    }

    public void updateForAll(@NotNull List<UUID> playerUUIDs, @NotNull String newText) {
        this.text=newText;
        handler.updateTeamPrefix(this, newText);
        playerUUIDs.forEach(uuid -> sender.sendLineUpdatePacket(uuid, this));
    }

    @NotNull
    public String getText(){
        return this.text;
    }
}
