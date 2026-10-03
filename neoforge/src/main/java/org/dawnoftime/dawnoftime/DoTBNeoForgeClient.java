package org.dawnoftime.dawnoftime;

import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.dawnoftime.dawnoftime.client.gui.StoneOvenScreen;
import org.dawnoftime.dawnoftime.client.renderer.blockentity.DisplayerBERenderer;
import org.dawnoftime.dawnoftime.client.renderer.entity.ChairRenderer;
import org.dawnoftime.dawnoftime.registry.DoTBBlockEntitiesRegistry;
import org.dawnoftime.dawnoftime.registry.DoTBColorsRegistry;
import org.dawnoftime.dawnoftime.registry.DoTBEntitiesRegistry;
import org.dawnoftime.dawnoftime.registry.DoTBMenusRegistry;

import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = DoTBCommon.MOD_ID, value = Dist.CLIENT)
public class DoTBNeoForgeClient {
    @SubscribeEvent
    public static void setupBlockColors(final RegisterColorHandlersEvent.BlockTintSources event) {
        DoTBColorsRegistry.getBlocksColorRegistry().forEach((tintSource, blocks) -> event.register(List.of(tintSource), blocks.stream().map(Supplier::get).toArray(Block[]::new)));
    }

    @SubscribeEvent
    public static void registerScreens(final RegisterMenuScreensEvent event) {
        event.register(DoTBMenusRegistry.INSTANCE.STONE_OVEN.get(), StoneOvenScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DoTBEntitiesRegistry.INSTANCE.CHAIR_ENTITY.get(), ChairRenderer::new);
        event.registerBlockEntityRenderer(DoTBBlockEntitiesRegistry.INSTANCE.DISPLAYER.get(), DisplayerBERenderer::new);
    }
}
