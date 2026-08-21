package org.velas.scoreboard.core.sidebar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.core.exception.ScoreboardException;
import org.velas.scoreboard.core.implementation.BukkitScoreboardImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SidebarElements {

    protected static final int MAX_LINES = 15;

    private final String id;
    private final BukkitScoreboardImpl scoreboard;
    private final Map<Integer, Line> lines = new HashMap<>();
    private String title;
    private boolean canView;
    private boolean isRemoved;
    private SidebarElementsHandler handler;
    private PacketSender sender;

    public SidebarElements(@NotNull String id, @NotNull BukkitScoreboardImpl scoreboard, @NotNull String title){
        this.scoreboard=scoreboard;
        this.id=id;
        this.title=title;
        this.isRemoved = false;
        this.canView=true;
    }

    public void init(@NotNull SidebarElementsHandler handler, @NotNull PacketSender sender){
        if(this.handler!=null && this.sender != null) return;
        this.handler=handler;
        this.sender=sender;
    }

    PacketSender getSender() {
        return sender;
    }

    @NotNull
    public BukkitScoreboardImpl getScoreboard(){
        return this.scoreboard;
    }

    public boolean canView(){
        return this.canView;
    }

    void setCanView(boolean canView){
        this.canView=canView;
    }

    @NotNull
    public String getId(){
        return this.id;
    }

    @NotNull
    public String getTitle(){
        return this.title;
    }

    void setTitle(@NotNull String title){
        this.title=title;
        handler.setDisplayNameOfObjective(title);
    }

    @NotNull
    public Map<Integer, Line> getLines(){
        return this.lines;
    }

    void removeLineElement(int line){
        try {
            handler.removeLineElement(line);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to remove the line from the sidebar. Report this here -> https://github.com/velaHere/velaboard/issues");
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    @NotNull
    Line getOrCreateLine(int line, @NotNull String text){
        return handler.getOrCreateLine(line, text);
    }

    void onRemove(){
        this.canView=false;
        this.isRemoved=true;
    }

    boolean isRemoved(){
        return this.isRemoved;
    }

    void checkNotRemoved(){
        if(isRemoved)
            throw new ScoreboardException("Side bar has been removed and cannot be reused");
    }

    @NotNull
    Player checkPlayerOnline(UUID playerUUID){
        Player player = Bukkit.getPlayer(playerUUID);
        if(player == null)
            throw new ScoreboardException("Player with uuid " + playerUUID + " is not online");
        return player;
    }

    void checkPlayerInScoreboard(UUID playerUUID){
        if(!scoreboard.getPlayers().contains(playerUUID))
            throw new ScoreboardException("Player is not in this scoreboard");
    }

    void validateLineNumber(int line){
        if(line<0 || line>=MAX_LINES)
            throw new IllegalArgumentException("Line must be between 0 and " + (MAX_LINES-1));
    }
}
