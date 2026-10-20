package org.dawnoftime.dawnoftime.client.gui.elements.buttons;

import net.minecraft.util.ARGB;
import net.minecraft.client.renderer.RenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.dawnoftime.dawnoftime.DoTBCommon.MOD_ID;

public class PlaylistButton extends Button {
    private final Identifier buttonTexture;

    public PlaylistButton(int x, int y, OnPress pressable) {
        this(x, y, pressable, Identifier.fromNamespaceAndPath(MOD_ID, "textures/gui/youtube.png"));
    }

    public PlaylistButton(int x, int y, OnPress pressable, Identifier texture) {
        super(x, y, 12, 12, Component.empty(), pressable, DEFAULT_NARRATION);
        this.buttonTexture = texture;
    }

    @Override
    protected void extractContents(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        int color = (this.isHovered() && this.active) ? ARGB.color(255, 179, 179, 179) : ARGB.white(this.alpha);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, buttonTexture, this.getX() - 1, this.getY(), 0.0F, 0.0F, 12, 12, 12, 12, color);
    }
}
