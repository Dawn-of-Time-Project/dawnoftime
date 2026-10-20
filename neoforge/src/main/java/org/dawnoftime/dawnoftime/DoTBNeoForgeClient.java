package org.dawnoftime.dawnoftime;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.dawnoftime.dawnoftime.client.tooltip.BlockTooltips;
import org.dawnoftime.dawnoftime.client.gui.StoneOvenScreen;
import org.dawnoftime.dawnoftime.registry.DoTBMenusRegistry;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.dawnoftime.dawnoftime.client.renderer.blockentity.DisplayerBERenderer;
import org.dawnoftime.dawnoftime.client.renderer.entity.ChairRenderer;
import org.dawnoftime.dawnoftime.registry.DoTBBlockEntitiesRegistry;
import org.dawnoftime.dawnoftime.registry.DoTBColorsRegistry;
import org.dawnoftime.dawnoftime.registry.DoTBEntitiesRegistry;

import java.util.function.Supplier;

@EventBusSubscriber(modid = DoTBCommon.MOD_ID, value = Dist.CLIENT)
public class DoTBNeoForgeClient {
    public DoTBNeoForgeClient() {}
    @SubscribeEvent
    public static void setupBlockColors(final RegisterColorHandlersEvent.Block event) {
        DoTBColorsRegistry.getBlocksColorRegistry().forEach((blockColor, blocks) -> event.register(blockColor, blocks.stream().map(Supplier::get).toArray(Block[]::new)));
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(DoTBMenusRegistry.INSTANCE.STONE_OVEN.get(), StoneOvenScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DoTBEntitiesRegistry.INSTANCE.CHAIR_ENTITY.get(), ChairRenderer::new);
        event.registerBlockEntityRenderer(DoTBBlockEntitiesRegistry.INSTANCE.DISPLAYER.get(), DisplayerBERenderer::new);
    }

    @SubscribeEvent
    public static void appendBlockTooltips(ItemTooltipEvent event) {
        BlockTooltips.append(event.getItemStack(), event.getToolTip());
    }
}
