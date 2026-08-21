package org.velas.scoreboard.core.nms.v1_20_r4;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.scoreboard.CraftScoreboard;
import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.core.exception.ScoreboardException;
import org.velas.scoreboard.core.sidebar.Line;
import org.velas.scoreboard.core.sidebar.PacketSender;
import org.velas.scoreboard.core.sidebar.SidebarElements;
import org.velas.scoreboard.core.sidebar.SidebarElementsHandler;
import org.velas.scoreboard.core.util.Logger;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PacketSender_v1_20_R4 implements PacketSender {

    private final @NotNull Objective objective;
    private final @NotNull SidebarElements elements;
    private SEH_v1_20_R4 handler;
    private final @NotNull ServerScoreboard serverScoreboard;

    PacketSender_v1_20_R4(@NotNull SidebarElements elements) {
        this.elements=elements;
        this.serverScoreboard = (ServerScoreboard) ((CraftScoreboard) elements.getScoreboard().getBukkitScoreboard()).getHandle();
        if(serverScoreboard ==null)
            throw new ScoreboardException("Failed to initialize Sidebar");
        this.objective = this.serverScoreboard.addObjective(
                "sb_" + System.nanoTime() % 100000,
                ObjectiveCriteria.DUMMY,
                Component.literal(elements.getTitle()),
                ObjectiveCriteria.RenderType.INTEGER,
                false, null
        );
        if(this.objective==null)
            throw new ScoreboardException("Failed to initialize Sidebar");
    }

    void init(SidebarElementsHandler handler){
        if(!(handler instanceof SEH_v1_20_R4 handle))
            throw new ScoreboardException("Failed to initialize Sidebar");
        this.handler=handle;
    }

    @NotNull ServerScoreboard getServerScoreboard(){
        return this.serverScoreboard;
    }

    @NotNull Objective getObjective(){
        return this.objective;
    }

    @Override
    public void sendCreateObjectivePacket(@NotNull UUID playerUUID) {
        sendPacket(playerUUID, new ClientboundSetObjectivePacket(
                objective,
                ClientboundSetObjectivePacket.METHOD_ADD
        ));
        Logger.info("Sent CREATE objective packet to player: " + playerUUID);
    }

    @Override
    public void sendRemoveObjectivePacket(@NotNull UUID playerUUID) {
        sendPacket(playerUUID, new ClientboundSetObjectivePacket(
                objective,
                ClientboundSetObjectivePacket.METHOD_REMOVE
        ));
        Logger.info("Send REMOVE objective packet to player: " + playerUUID);
    }

    @Override
    public void sendDisplayObjectivePacket(@NotNull UUID playerUUID) {
        sendPacket(playerUUID, new ClientboundSetDisplayObjectivePacket(
                DisplaySlot.SIDEBAR,
                objective
        ));
        Logger.info("Send DISPLAY objective packet to player: " + playerUUID);
    }

    @Override
    public void sendHideObjectivePacket(@NotNull UUID playerUUID) {
        sendPacket(playerUUID, new ClientboundSetDisplayObjectivePacket(
                DisplaySlot.SIDEBAR,
                null
        ));
        Logger.info("Send HIDE objective packet to player: " + playerUUID);
    }

    @Override
    public void sendSetScorePacket(@NotNull UUID playerUUID, @NotNull Line line) {
        sendPacket(playerUUID, new ClientboundSetScorePacket(
                line.ENTRY,
                objective.getName(),
                line.SCORE,
                Optional.empty(), Optional.empty()
        ));
        Logger.info("Sent ADD score packet: entry=" + line.ENTRY + " to player: " + playerUUID);
    }

    @Override
    public void sendRemoveScorePacket(@NotNull UUID playerUUID, @NotNull Line line) {
        sendPacket(playerUUID, new ClientboundResetScorePacket(line.ENTRY, objective.getName()));
        Logger.info("Sent REMOVE score packet: entry=" + line.ENTRY + " to player: " + playerUUID);
    }

    @Override
    public void sendCompleteObjectiveSetup(@NotNull UUID playerUUID) {
        sendRemoveObjectivePacket(playerUUID);
        sendCreateObjectivePacket(playerUUID);
        if(elements.canView()) sendDisplayObjectivePacket(playerUUID);
        for(Line line: elements.getLines().values()){
            PlayerTeam team = handler.getOrCreatePlayerTeam(line);
            sendCreateOrUpdateTeamPacket(playerUUID, team);
            sendSetScorePacket(playerUUID, line);
        }
        Logger.info("Sent COMPLETE objective setup to player: " + playerUUID);
    }

    @Override
    public void sendLineSetupPacket(@NotNull UUID playerUUID, @NotNull Line line) {
        sendCreateOrUpdateTeamPacket(playerUUID, handler.getOrCreatePlayerTeam(line));
        sendSetScorePacket(playerUUID, line);
        Logger.info("Sent LINE setup for line: " + line.SCORE + " to player: " + playerUUID);
    }

    @Override
    public void sendLineUpdatePacket(@NotNull UUID playerUUID, @NotNull Line line) {
        sendCreateOrUpdateTeamPacket(playerUUID, handler.getOrCreatePlayerTeam(line));
        Logger.info("Sent LINE UPDATE to player: " + playerUUID);
    }

    @Override
    public void sendRemoveLinePacket(@NotNull UUID playerUUID, @NotNull Line line) {
        sendRemoveScorePacket(playerUUID, line);
        sendRemoveTeamPacket(playerUUID, handler.getOrCreatePlayerTeam(line));
        Logger.info("Sent REMOVE line to player: " + playerUUID);
    }

    protected void sendCreateOrUpdateTeamPacket(@NotNull UUID playerUUID, @NotNull PlayerTeam team){
        sendPacket(playerUUID, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));
        Logger.info("Sent CREATE/UPDATE team packet for team: " + team.getName() + " to player: " + playerUUID);
    }

    protected void sendRemoveTeamPacket(@NotNull UUID playerUUID, @NotNull PlayerTeam team){
        sendPacket(playerUUID, ClientboundSetPlayerTeamPacket.createRemovePacket(team));
        Logger.info("Sent REMOVE team packet for team: " + team.getName() + " to player: " + playerUUID);
    }

    protected void sendPacket(@NotNull UUID playerUUID, @NotNull Packet<?> packet){
        try{
            ServerPlayer serverPlayer = ((CraftPlayer) Objects.requireNonNull(Bukkit.getPlayer(playerUUID))).getHandle();
            serverPlayer.connection.send(packet);
        }catch (Exception e){
            Logger.severe("Failed to send Packet: " + e.getMessage());
        }
    }
}
