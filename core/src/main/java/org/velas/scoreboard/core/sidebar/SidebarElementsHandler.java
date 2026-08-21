package org.velas.scoreboard.core.sidebar;

import org.jetbrains.annotations.NotNull;

public abstract class SidebarElementsHandler {

    protected SidebarElements elements;

    protected SidebarElementsHandler(@NotNull SidebarElements elements){
        this.elements=elements;
    }

    protected PacketSender getSender(){
        return this.elements.getSender();
    }

    protected String getUniqueInvisibleEntryHex(int lineIndex) { // -> 1.16.+
        int hash = (this.elements.getId().hashCode()*31 + lineIndex) & 0xFFFFFF;
        String hex = String.format("%06x", hash);

        return "§x§" + hex.charAt(0) + "§" + hex.charAt(1) + "§" +
                hex.charAt(2) + "§" + hex.charAt(3) + "§" +
                hex.charAt(4) + "§" + hex.charAt(5);
    }

    protected String getUniqueInvisibleEntryLegacy(int lineIndex) { // -> 1.8–1.15
        int sidebarHash = Math.abs(this.elements.getId().hashCode());

        int c1 = (lineIndex) % 16;
        int c2 = (lineIndex + sidebarHash) % 16;
        int c3 = (lineIndex + sidebarHash * 2) % 16;
        int c4 = (lineIndex + sidebarHash * 3) % 16;

        return "§" + Integer.toHexString(c1)
                + "§" + Integer.toHexString(c2)
                + "§" + Integer.toHexString(c3)
                + "§" + Integer.toHexString(c4);
    }

    protected abstract void setDisplayNameOfObjective(@NotNull String title);
    protected @NotNull abstract Line getOrCreateLine(int line, @NotNull String text);
    protected abstract void removeLineElement(int line);
    protected abstract void updateTeamPrefix(@NotNull Line lineData, @NotNull String newPrefix);
}
