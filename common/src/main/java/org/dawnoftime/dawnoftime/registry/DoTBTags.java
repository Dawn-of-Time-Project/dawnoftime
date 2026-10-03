package org.dawnoftime.dawnoftime.registry;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.dawnoftime.dawnoftime.DoTBCommon;

public abstract class DoTBTags {
    public static DoTBTags INSTANCE;
    //Item tags
    public final TagKey<Item> LIGHTERS = registerItem(Identifier.fromNamespaceAndPath(DoTBCommon.MOD_ID, "lighters"));
    //Block tags
    public final TagKey<Block> COVERED_BLOCKS = registerBlock(Identifier.fromNamespaceAndPath(DoTBCommon.MOD_ID, "covered_blocks"));


    public abstract TagKey<Block> registerBlock(Identifier id);
    public abstract TagKey<Item> registerItem(Identifier id);
}
