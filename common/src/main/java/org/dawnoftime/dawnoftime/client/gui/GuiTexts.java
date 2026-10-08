package org.dawnoftime.dawnoftime.client.gui;

import net.minecraft.network.chat.Component;

import static org.dawnoftime.dawnoftime.DoTBCommon.MOD_ID;

/**
 * Single access point to every lang key used by the interface (creative tab, containers).
 * All the keys live under "gui.dawnoftimebuilder.".
 */
public final class GuiTexts {
    private static final String PREFIX = "gui." + MOD_ID + ".";

    private GuiTexts() {}

    /** Name of a culture category of the creative tab. */
    public static Component category(String name) {
        return Component.translatable(PREFIX + "category." + name);
    }

    /** Tooltip of the YouTube playlist button of a culture category. */
    public static Component youtube(String name) {
        return Component.translatable(PREFIX + "youtube." + name);
    }

    /** Tooltip of a sub tab button. */
    public static Component subtab(String name) {
        return Component.translatable(PREFIX + "subtab." + name);
    }

    /** Title of the Dawn of Time creative tab. */
    public static Component tabTitle() {
        return Component.translatable(PREFIX + "tab_title");
    }

    /** Title of the stone oven screen. */
    public static Component stoneOven() {
        return Component.translatable(PREFIX + "stone_oven");
    }
}
