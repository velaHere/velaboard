package org.velas.scoreboard.core.nms.v1_20_r3;

import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.ScoreHolder;
import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.core.sidebar.Line;
import org.velas.scoreboard.core.sidebar.SidebarElements;
import org.velas.scoreboard.core.sidebar.SidebarElementsHandler;

import java.util.HashMap;
import java.util.Map;

class SEH_v1_20_R3 extends SidebarElementsHandler {

    private final Map<Integer, PlayerTeam> teams = new HashMap<>();
    private final ServerScoreboard serverScoreboard;
    private final @NotNull Objective objective;

    SEH_v1_20_R3(@NotNull SidebarElements elements, @NotNull PacketSender_v1_20_R3 sender) {
        super(elements);
        this.serverScoreboard=sender.getServerScoreboard();
        this.objective=sender.getObjective();
    }

    @NotNull
    PlayerTeam getOrCreatePlayerTeam(Line line){
        PlayerTeam team = teams.get(line.SCORE);
        if(team==null) {
            team = serverScoreboard.addPlayerTeam(this.elements.getId()+"_l:"+line.SCORE);
            teams.put(line.SCORE, team);
        }
        return team;
    }

    @Override
    protected void setDisplayNameOfObjective(@NotNull String title) {
        objective.setDisplayName(Component.literal(title));
    }

    @Override
    protected @NotNull Line getOrCreateLine(int line, @NotNull String text) {
        Line lineData = elements.getLines().get(line);
        if(lineData!=null) return lineData;
        String entry = getUniqueInvisibleEntryHex(line);
        lineData = new Line(line, entry, text, this, getSender());

        PlayerTeam team = getOrCreatePlayerTeam(lineData);
        serverScoreboard.addPlayerToTeam(entry, team);
        serverScoreboard.getOrCreatePlayerScore(ScoreHolder.forNameOnly(entry), objective).set(line);
        team.setPlayerPrefix(Component.literal(text));

        elements.getLines().put(line, lineData);

        return lineData;
    }

    @Override
    protected void removeLineElement(int line) {
        Line lineData = elements.getLines().get(line);
        if(lineData==null) return;
        PlayerTeam team = getOrCreatePlayerTeam(lineData);
        serverScoreboard.removePlayerFromTeam(lineData.ENTRY, team);
        serverScoreboard.removePlayerTeam(team);
        elements.getLines().remove(line, lineData);
        teams.remove(line);
    }

    @Override
    protected void updateTeamPrefix(@NotNull Line lineData, @NotNull String newPrefix) {
        PlayerTeam team = getOrCreatePlayerTeam(lineData);
        team.setPlayerPrefix(Component.literal(newPrefix));
    }
}
