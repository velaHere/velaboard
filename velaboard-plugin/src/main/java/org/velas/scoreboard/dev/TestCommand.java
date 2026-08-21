package org.velas.scoreboard.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.ScoreboardAPI;
import org.velas.scoreboard.api.ScoreboardHandler;
import org.velas.scoreboard.api.sidebar.PlayerSidebar;
import org.velas.scoreboard.api.sidebar.SharedSidebar;
import org.velas.scoreboard.api.sidebar.Sidebar;
import org.velas.scoreboard.api.team.Team;
import org.velas.scoreboard.core.util.Logger;

import java.util.UUID;

public class TestCommand implements CommandExecutor {

    private Scoreboard scoreboard;
    private UUID subject;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        if(strings.length<1) return false;

        Logger.info("Command ran");

        ScoreboardHandler handler = ScoreboardAPI.getHandler();

        switch (strings[0]) {
            case "test" -> {
                if(subject==null) return false;
                switch (strings[1]){
                    case "removeFromSc" ->
                        scoreboard.removePlayer(subject);

                    case "removeFromTeam" -> scoreboard.removePlayerFromTeam(subject);

                    case "addToTeam" -> {
                        Team team = scoreboard.getTeam(strings[2]);
                        if(team==null) return false;
                        team.addPlayer(subject);
                    }

                    case "addToSc" -> {
                        scoreboard.addPlayer(subject);
                    }
                }
            }
            case "give" -> {
                if(strings.length<3) return false;
                switch (strings[1]){
                    case "sidebar" -> {
                        if(strings.length<4) return false;
                        Scoreboard scoreboard = handler.getScoreboard(player);
                        if(scoreboard==null) return false;

                        Player target = Bukkit.getPlayer(strings[3]);
                        if(target==null) return false;

                        switch(strings[2]){
                            case "player" -> {
                                PlayerSidebar sidebar = scoreboard.createPlayerSidebar("Testing");
                                sidebar.setPlayer(target);
                                sidebar.setLine(2, ChatColor.GREEN + "" + ChatColor.BOLD + "well");
                                sidebar.setLine(1, "hello");
                                sidebar.setLine(0, "there");
                                sidebar.show();
                            }
                            case "shared" -> {
                                if(strings.length!=5) return false;
                                SharedSidebar sidebar = scoreboard.getSharedSidebar(strings[4]);
                                if(sidebar==null) return false;
                                sidebar.addPlayer(target);
                                sidebar.showTo(target.getUniqueId());
                            }
                        }
                    }
                    case "scoreboard" -> {
                        if(strings.length!=3) return false;
                        if(this.scoreboard==null) {
                            player.sendMessage( ChatColor.RED + "There is no scoreboard to give");
                            return false;
                        }
                        Player target = Bukkit.getPlayer(strings[2]);
                        if(target==null) return false;
                        scoreboard.addPlayer(target);
                    }
                    case "team" -> {
                        if(strings.length!=4) return false;
                        Player target = Bukkit.getPlayer(strings[2]);
                        if(target==null) {
                            Team team = scoreboard.getTeam(strings[3]);
                            if(team==null) return false;
                            Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
                            team.addEntity(entity);
                            return true;
                        }
                        Scoreboard scoreboard = handler.getScoreboard(target);
                        if(scoreboard==null) return false;
                        Team team = scoreboard.getTeam(strings[3]);
                        if(team==null) return false;
                        team.addPlayer(target);
                    }
                }
            }
            case "create" -> {
                switch (strings[1]){
                    case "scoreboard" -> {
                        if(strings.length!= 2) return false;
                        this.scoreboard=handler.createScoreboard();
                    }
                    case "team" -> {
                        if(strings.length!=3) return false;
                        String teamName = strings[2];
                        Scoreboard scoreboard = handler.getScoreboard(player);
                        if(scoreboard==null) return false;
                        Team team = scoreboard.team(teamName);
                        team.setPrefix("[" + teamName + "] ");
                        team.setFriendlyFire(false);
                        team.setEntitiesFriendlyFire(false);
                    }
                    case "sharedSidebar" -> {
                        if (strings.length != 3) return false;
                        if (this.scoreboard == null) return false;
                        SharedSidebar sidebar = scoreboard.createSharedSidebar(strings[2], "TestingsShared: " + strings[2]);
                        sidebar.setLine(2, ChatColor.GREEN + "" + ChatColor.BOLD + "well-s");
                        sidebar.setLine(1, "hello-s");
                        sidebar.setLine(0, "there-s");
                    }
                }
            }
            case "remove" -> {
                if(strings.length!=3) return false;
                Player target = Bukkit.getPlayer(strings[2]);
                if(target==null) return false;

                switch (strings[1]){
                    case "sidebar" -> {
                        Scoreboard scoreboard1 = handler.getScoreboard(target);
                        if (scoreboard1 == null) return false;
                        scoreboard1.removePlayerFromSidebar(target);
                    }
                    case "scoreboard" -> {
                        Scoreboard scoreboard1 = handler.getScoreboard(target);
                        if(scoreboard1==null) return false;
                        scoreboard1.removePlayer(target);
                    }
                    case "team" -> {
                        Scoreboard scoreboard1 = handler.getScoreboard(target);
                        if(scoreboard1==null) return false;
                        scoreboard1.removePlayerFromTeam(target);
                    }
                }
            }
            case "delete" -> {
                switch (strings[1]){
                    case "team" -> scoreboard.removeTeam(strings[2]);
                    case "allTeams" -> scoreboard.clearTeams();
                    case "line" -> {
                        int lineNo = Integer.parseInt(strings[2]);

                        Scoreboard scoreboard = handler.getScoreboard(player);
                        if(scoreboard==null) return false;

                        PlayerSidebar playerSidebar = scoreboard.getPlayerSidebar(player);
                        if(playerSidebar!=null) playerSidebar.removeLine(lineNo);
                        SharedSidebar sharedSidebar = scoreboard.getSharedSidebar(player);
                        if(sharedSidebar!=null) sharedSidebar.removeLine(lineNo);
                    }
                    case "allLines" -> {
                        Scoreboard scoreboard = handler.getScoreboard(player);
                        if(scoreboard==null) return false;

                        PlayerSidebar playerSidebar = scoreboard.getPlayerSidebar(player);
                        if(playerSidebar!=null) playerSidebar.clearLines();
                        SharedSidebar sharedSidebar = scoreboard.getSharedSidebar(player);
                        if(sharedSidebar!=null) sharedSidebar.clearLines();
                    }
                    case "sidebar" -> {
                        Scoreboard scoreboard = handler.getScoreboard(player);
                        if(scoreboard==null) return false;
                        Sidebar sidebar = scoreboard.getSidebar(player);
                        if(sidebar==null) return false;
                        sidebar.remove();
                    }
                }
            }
            case "add" -> {
                switch (strings[1]){
                    case "line" -> {
                        int lineNo = Integer.parseInt(strings[2]);
                        String text = strings[3];
                        Scoreboard scoreboard = handler.getScoreboard(player);
                        if (scoreboard == null) return false;
                        PlayerSidebar playerSidebar = scoreboard.getPlayerSidebar(player);
                        if (playerSidebar!=null) playerSidebar.setLine(lineNo, text);
                        SharedSidebar sharedSidebar = scoreboard.getSharedSidebar(player);
                        if(sharedSidebar!=null) sharedSidebar.setLine(lineNo, text);
                    }
                }
            }
            case "set" -> {
                switch(strings[1]){
                    case "title" -> {
                        if(strings.length!=3) return false;
                        String title = strings[2];
                        Scoreboard scoreboard = handler.getScoreboard(player);
                        if(scoreboard==null) return false;
                        PlayerSidebar playerSidebar = scoreboard.getPlayerSidebar(player);
                        if(playerSidebar!=null){
                            playerSidebar.setTitle(title);
                            return true;
                        }

                        SharedSidebar sharedSidebar = scoreboard.getSharedSidebar(player);
                        if(sharedSidebar==null) return false;
                        sharedSidebar.setTitle(title);
                    }
                    case "line" -> {
                        Scoreboard scoreboard = handler.getScoreboard(player);
                        if(scoreboard==null) return false;

                        int lineNo = Integer.parseInt(strings[2]);
                        String text = strings[3];

                        PlayerSidebar playerSidebar = scoreboard.getPlayerSidebar(player);
                        if(playerSidebar!=null) playerSidebar.setLine(lineNo, text);
                        SharedSidebar sharedSidebar = scoreboard.getSharedSidebar(player);
                        if(sharedSidebar!=null) sharedSidebar.setLine(lineNo, text);
                    }
                }
            }
            case "hideSidebar" -> {
                switch(strings[1]){
                    case "ofAll" -> {
                        Scoreboard scoreboard = handler.getScoreboard(player);
                        if(scoreboard==null) return false;
                        SharedSidebar sharedSidebar = scoreboard.getSharedSidebar(player);
                        if(sharedSidebar==null) return false;
                        sharedSidebar.hide();
                    }
                    case "from" -> {
                        Player target = Bukkit.getPlayer(strings[2]);
                        if(target==null) return false;
                        Scoreboard scoreboard = handler.getScoreboard(target);
                        if(scoreboard==null) return false;

                        SharedSidebar sharedSidebar = scoreboard.getSharedSidebar(target);
                        if(sharedSidebar!=null) sharedSidebar.hideFrom(target.getUniqueId());
                        PlayerSidebar playerSidebar = scoreboard.getPlayerSidebar(target);
                        if(playerSidebar!=null) playerSidebar.hide();
                    }
                    default -> {
                        Scoreboard scoreboard = handler.getScoreboard(player);
                        if(scoreboard==null) return false;
                        PlayerSidebar playerSidebar = scoreboard.getPlayerSidebar(player);
                        if(playerSidebar==null) return false;
                        playerSidebar.hide();
                    }
                }
            }
            case "has" -> {
                switch (strings[1]){
                    case "team" -> {
                        Scoreboard scoreboard = handler.getScoreboard(player);
                        if(scoreboard==null) return false;
                        Team teamOf = scoreboard.getTeamOf(player);
                        player.sendMessage(teamOf == null ? "false" : "true");
                    }
                    case "sidebar" -> {
                        Scoreboard scoreboard = handler.getScoreboard(player);
                        if(scoreboard==null) return false;
                        player.sendMessage(scoreboard.hasSidebar(player) ? "true" : "false");
                    }
                    case "scoreboard" -> {
                        Scoreboard scoreboard1 = handler.getScoreboard(player);
                        player.sendMessage(scoreboard1==null ? "false" : "true");
                    }
                }
            }
            default -> {
                Player player1 = Bukkit.getPlayer(strings[0]);
                if(player1==null) return false;
                this.subject=player1.getUniqueId();
            }
        }
        return true;
    }
}
