package org.dawnoftime.dawnoftime.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.DoTBCommon;

import java.util.function.Supplier;

public final class DoTBProperties {
    private static final ThreadLocal<String> CURRENT_ID = new ThreadLocal<>();

    private DoTBProperties() {}

    public static <T> T withId(String id, Supplier<T> factory) {
        String previous = CURRENT_ID.get();
        CURRENT_ID.set(id);
        try {
            return factory.get();
        } finally {
            CURRENT_ID.set(previous);
        }
    }

    private static Identifier currentId() {
        String id = CURRENT_ID.get();
        if (id == null) throw new IllegalStateException("Properties created outside of a DoT registration");
        return Identifier.fromNamespaceAndPath(DoTBCommon.MOD_ID, id);
    }

    public static BlockBehaviour.Properties copy(Block block) {
        return BlockBehaviour.Properties.ofFullCopy(block).setId(ResourceKey.create(Registries.BLOCK, currentId()));
    }

    public static BlockBehaviour.Properties of() {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, currentId()));
    }

    public static Item.Properties item() {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, currentId()));
    }

    public static Item.Properties blockItem(Block block) {
        Identifier id = BuiltInRegistries.BLOCK.getKey(block);
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)).useBlockDescriptionPrefix();
    }
}
