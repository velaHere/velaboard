package org.velas.scoreboard.api.team;

import org.bukkit.ChatColor;

/**
 * Represents a color used for a team.
 *
 * <p>This enum acts as a wrapper around Bukkit's {@link ChatColor},
 * providing a cleaner and more controlled API for team color management.</p>
 *
 * <p>Each enum constant maps directly to a corresponding {@link ChatColor}.</p>
 */
public enum TeamColor {

    BLACK(ChatColor.BLACK),
    DARK_BLUE(ChatColor.DARK_BLUE),
    DARK_GREEN(ChatColor.DARK_GREEN),
    DARK_AQUA(ChatColor.DARK_AQUA),
    DARK_RED(ChatColor.DARK_RED),
    DARK_PURPLE(ChatColor.DARK_PURPLE),
    GOLD(ChatColor.GOLD),
    GRAY(ChatColor.GRAY),
    DARK_GRAY(ChatColor.DARK_GRAY),
    BLUE(ChatColor.BLUE),
    GREEN(ChatColor.GREEN),
    AQUA(ChatColor.AQUA),
    RED(ChatColor.RED),
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE),
    YELLOW(ChatColor.YELLOW),
    WHITE(ChatColor.WHITE);

    private final ChatColor chatColor;

    /**
     * Creates a new TeamColor mapped to a Bukkit {@link ChatColor}.
     *
     * @param chatColor the corresponding Bukkit color
     */
    TeamColor(ChatColor chatColor){
        this.chatColor=chatColor;
    }

    /**
     * Gets the corresponding Bukkit {@link ChatColor}.
     *
     * @return the Bukkit chat color
     */
    public ChatColor getChatColor(){
        return this.chatColor;
    }

    /**
     * Converts a Bukkit {@link ChatColor} to a {@link TeamColor}.
     *
     * <p>If no matching color is found, {@link #WHITE} is returned by default.</p>
     *
     * @param chatColor the Bukkit chat color
     * @return the corresponding TeamColor, or {@link #WHITE} if not found
     */
    public static TeamColor fromChatColor(ChatColor chatColor){
        for(TeamColor teamColor: values()){
            if(teamColor.chatColor==chatColor) return teamColor;
        }
        return WHITE;
    }
}
