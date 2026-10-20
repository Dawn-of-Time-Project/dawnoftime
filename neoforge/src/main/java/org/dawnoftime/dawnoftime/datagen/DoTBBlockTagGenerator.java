package org.dawnoftime.dawnoftime.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.dawnoftime.dawnoftime.DoTBCommon;
import org.dawnoftime.dawnoftime.registry.DoTBBlocksRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DoTBBlockTagGenerator extends BlockTagsProvider {
    public DoTBBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, DoTBCommon.MOD_ID);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider pProvider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BuiltInRegistries.BLOCK.getResourceKey(DoTBBlocksRegistry.INSTANCE.REINFORCED_BLACK_WROUGHT_IRON_FENCE.get()).orElseThrow());
        for(TagKey<Block> tag : DoTBBlocksRegistry.blockTagsMap.keySet()){
            DoTBBlocksRegistry.blockTagsMap.get(tag).forEach(block -> this.tag(tag).add(BuiltInRegistries.BLOCK.getResourceKey(block.get()).orElseThrow()));
        }
        //blockTagsMap.clear();
        int a = 1;
    }
}
