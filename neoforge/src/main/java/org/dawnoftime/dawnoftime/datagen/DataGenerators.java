package org.dawnoftime.dawnoftime.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.dawnoftime.dawnoftime.DoTBCommon;

@EventBusSubscriber(modid = DoTBCommon.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Server event) {
        event.createProvider(DoTBBlockTagGenerator::new);
    }
}
