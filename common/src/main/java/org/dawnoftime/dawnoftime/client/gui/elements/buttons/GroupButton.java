package org.dawnoftime.dawnoftime.client.gui.elements.buttons;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;

public class GroupButton extends Button {
    private static final Identifier SPRITE = Identifier.withDefaultNamespace("widget/button");
    private static final Identifier SPRITE_HIGHLIGHTED = Identifier.withDefaultNamespace("widget/button_highlighted");
    private static final Identifier SPRITE_DISABLED = Identifier.withDefaultNamespace("widget/button_disabled");

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
    protected void extractContents(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        final Identifier sprite = !this.active ? SPRITE_DISABLED : this.isHoveredOrFocused() ? SPRITE_HIGHLIGHTED : SPRITE;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, this.getX(), this.getY(), this.width, this.height, ARGB.white(this.alpha));

        final int iconColor = this.active ? 0xFFFFFFFF : 0xFF808080;
        graphics.blit(RenderPipelines.GUI_TEXTURED, this.iconResource, this.getX() + 2, this.getY() + 2, (float) this.iconU, (float) this.iconV, 16, 16, 256, 256, iconColor);
    }
}
