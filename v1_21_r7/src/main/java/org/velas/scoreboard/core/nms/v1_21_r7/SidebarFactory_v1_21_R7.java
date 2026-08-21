package org.velas.scoreboard.core.nms.v1_21_r7;

import org.jetbrains.annotations.NotNull;
import org.velas.scoreboard.api.sidebar.PlayerSidebar;
import org.velas.scoreboard.api.sidebar.SharedSidebar;
import org.velas.scoreboard.core.implementation.BukkitScoreboardImpl;
import org.velas.scoreboard.core.sidebar.*;

class SidebarFactory_v1_21_R7 implements SidebarFactory {

    SidebarFactory_v1_21_R7(){}

    @Override
    public @NotNull PlayerSidebar createPlayerSidebar(@NotNull BukkitScoreboardImpl scoreboard, @NotNull String title) {
        String id = "ps_"+System.currentTimeMillis();
        SidebarElements elements = new SidebarElements(id, scoreboard, title);
        PacketSender_v1_21_R7 sender = new PacketSender_v1_21_R7(elements);
        SidebarElementsHandler handler = new SEH_v1_21_R7(elements, sender);
        sender.init(handler);
        elements.init(handler, sender);
        return new BasePlayerSidebar(elements, sender);
    }

    @Override
    public @NotNull SharedSidebar createSharedSidebar(@NotNull String id, @NotNull BukkitScoreboardImpl scoreboard, @NotNull String title) {
        SidebarElements elements = new SidebarElements(id, scoreboard, title);
        PacketSender_v1_21_R7 sender = new PacketSender_v1_21_R7(elements);
        SidebarElementsHandler handler = new SEH_v1_21_R7(elements, sender);
        sender.init(handler);
        elements.init(handler, sender);
        return new BaseSharedSidebar(elements, sender);
    }
}
