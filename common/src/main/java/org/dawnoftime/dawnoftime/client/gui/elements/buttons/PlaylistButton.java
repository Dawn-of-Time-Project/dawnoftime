package org.dawnoftime.dawnoftime.client.gui.elements.buttons;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import static org.dawnoftime.dawnoftime.DoTBCommon.MOD_ID;

public class PlaylistButton extends Button {
    private static final int HOVER_COLOR = 0xFFB3B3B3;

    private final Identifier buttonTexture;

    public PlaylistButton(int x, int y, OnPress pressable) {
        this(x, y, pressable, Identifier.fromNamespaceAndPath(MOD_ID, "textures/gui/youtube.png"));
    }

    public PlaylistButton(int x, int y, OnPress pressable, Identifier texture) {
        super(x, y, 12, 12, Component.empty(), pressable, DEFAULT_NARRATION);
        this.buttonTexture = texture;
    }

    @Override
    protected void extractContents(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        final int color = this.isHovered() && this.active ? HOVER_COLOR : 0xFFFFFFFF;
        graphics.blit(RenderPipelines.GUI_TEXTURED, this.buttonTexture, this.getX() - 1, this.getY(), 0.0F, 0.0F, 12, 12, 12, 12, color);
    }
}
