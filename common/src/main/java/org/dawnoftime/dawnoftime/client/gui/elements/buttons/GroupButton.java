package org.dawnoftime.dawnoftime.client.gui.elements.buttons;

import net.minecraft.util.ARGB;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;


public class GroupButton extends Button {
    private final Identifier iconResource;
    private final int iconU;
    private final int iconV;

    public GroupButton(int x, int y, Component message, OnPress pressable, Identifier iconResource, int iconU, int iconV) {
        super(x, y, 20, 20, message, pressable, DEFAULT_NARRATION);
        this.iconResource = iconResource;
        this.iconU = iconU;
        this.iconV = iconV;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.extractDefaultSprite(guiGraphics);
        int color = this.active ? ARGB.white(this.alpha) : ARGB.color(255, 128, 128, 128);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, iconResource, this.getX() + 2, this.getY() + 2, (float) this.iconU, (float) this.iconV, 16, 16, 256, 256, color);
    }
}
