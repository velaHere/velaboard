package org.velas.scoreboard.core.sidebar;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PacketSender {
    void sendCreateObjectivePacket(@NotNull UUID playerUUID);
    void sendRemoveObjectivePacket(@NotNull UUID playerUUID);
    void sendDisplayObjectivePacket(@NotNull UUID playerUUID);
    void sendHideObjectivePacket(@NotNull UUID playerUUID);
    void sendSetScorePacket(@NotNull UUID playerUUID, @NotNull Line line);
    void sendRemoveScorePacket(@NotNull UUID playerUUID, @NotNull Line line);
    void sendCompleteObjectiveSetup(@NotNull UUID playerUUID);
    void sendLineSetupPacket(@NotNull UUID playerUUID, @NotNull Line line);
    void sendLineUpdatePacket(@NotNull UUID playerUUID, @NotNull Line line);
    void sendRemoveLinePacket(@NotNull UUID playerUUID, @NotNull Line line);
}
