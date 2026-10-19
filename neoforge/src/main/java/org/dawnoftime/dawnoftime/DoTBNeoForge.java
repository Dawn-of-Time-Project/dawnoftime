package org.dawnoftime.dawnoftime;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.dawnoftime.dawnoftime.client.tooltip.BlockTooltips;
import org.dawnoftime.dawnoftime.datagen.DataGenerators;

@Mod(DoTBCommon.MOD_ID)
public class DoTBNeoForge {
    public DoTBNeoForge(IEventBus modEventBus) {
        DoTBCommon.init();

        RegistryImpls.init(modEventBus);

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.register(DoTBNeoForgeClient.class);
            NeoForge.EVENT_BUS.addListener((ItemTooltipEvent event) -> BlockTooltips.append(event.getItemStack(), event.getToolTip()));
        }

        modEventBus.register(DataGenerators.class);
    }

}