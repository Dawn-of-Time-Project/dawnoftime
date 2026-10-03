package org.dawnoftime.dawnoftime;

import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.dawnoftime.block.IFlammable;
import org.dawnoftime.dawnoftime.client.gui.StoneOvenScreen;
import org.dawnoftime.dawnoftime.client.gui.creative.CreativeInventoryCategories;
import org.dawnoftime.dawnoftime.client.renderer.blockentity.DisplayerBERenderer;
import org.dawnoftime.dawnoftime.client.renderer.entity.ChairRenderer;
import org.dawnoftime.dawnoftime.item.IconItem;
import org.dawnoftime.dawnoftime.registry.*;
import org.dawnoftime.dawnoftime.util.DoTBProperties;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegistryImpls {
    private static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(DoTBCommon.MOD_ID, name);
    }

    public static class FabricMenusRegistry extends DoTBMenusRegistry {
        @Override
        public <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String name, BiFunction<Integer, Inventory, T> factory) {
            MenuType<T> menuType = Registry.register(BuiltInRegistries.MENU, id(name), new MenuType<T>(factory::apply, FeatureFlags.DEFAULT_FLAGS));
            return () -> menuType;
        }
    }

    public static class FabricBlockEntitiesRegistry extends DoTBBlockEntitiesRegistry {
        @Override
        public <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BiFunction<BlockPos, BlockState, T> factoryIn, Supplier<Block[]> validBlocksSupplier) {
            BlockEntityType<T> blockEntity = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id(name), FabricBlockEntityTypeBuilder.<T>create(factoryIn::apply, validBlocksSupplier.get()).build());
            return () -> blockEntity;
        }
    }

    public static class FabricBlocksRegistry extends DoTBBlocksRegistry {
        public FabricBlocksRegistry() {
            postRegister();
            for (Map.Entry<ResourceKey<Block>, Block> resourceKeyBlockEntry : BuiltInRegistries.BLOCK.entrySet()) {
                Block block = resourceKeyBlockEntry.getValue();
                if (block instanceof IFlammable flammable) {
                    FlammableBlockRegistry.getDefaultInstance().add(block, flammable.getFireSpreadSpeed(block.defaultBlockState(), null, null, null), flammable.getFlammability(block.defaultBlockState(), null, null, null));
                }
            }
        }

        @SafeVarargs
        @Override
        public final <T extends Block, Y extends Item> Supplier<T> registerWithItem(String id, Supplier<T> block, Function<T, Y> item, TagKey<Block>... tags) {
            T registryBlock = Registry.register(BuiltInRegistries.BLOCK, id(id), DoTBProperties.withId(id, block));
            if (item != null) {
                Registry.register(BuiltInRegistries.ITEM, id(id), item.apply(registryBlock));
            }
            if (tags.length == 0) {
                addBlockTag(() -> registryBlock, BlockTags.MINEABLE_WITH_PICKAXE);
            } else {
                for (TagKey<Block> tag : tags) {
                    addBlockTag(() -> registryBlock, tag);
                }
            }
            return () -> registryBlock;
        }
    }

    public static class FabricItemsRegistry extends DoTBItemsRegistry {
        @Override
        public <T extends Item> Supplier<Item> register(String name, Supplier<T> itemSupplier) {
            T item = Registry.register(BuiltInRegistries.ITEM, id(name), DoTBProperties.withId(name, itemSupplier));
            return () -> item;
        }
    }

    public static class FabricEntitiesRegistry extends DoTBEntitiesRegistry {
        @Override
        public <T extends Entity> Supplier<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
            EntityType<T> entity = Registry.register(BuiltInRegistries.ENTITY_TYPE, id(name), builder.get().build(ResourceKey.create(Registries.ENTITY_TYPE, id(name))));
            return () -> entity;
        }
    }

    public static class FabricRecipeSerializersRegistry extends DoTBRecipeSerializersRegistry {
        @Override
        public <T extends RecipeSerializer<? extends Recipe<?>>> Supplier<T> register(String name, Supplier<T> recipeSerializer) {
            T recipe = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id(name), recipeSerializer.get());
            return () -> recipe;
        }
    }

    public static class FabricRecipeTypesRegistry extends DoTBRecipeTypesRegistry {
        @Override
        public <T extends Recipe<?>> Supplier<RecipeType<T>> register(String name) {
            RecipeType<T> type = Registry.register(BuiltInRegistries.RECIPE_TYPE, id(name), new RecipeType<T>() {
                @Override
                public String toString() {
                    return id(name).toString();
                }
            });
            return () -> type;
        }
    }

    public static class FabricCreativeModeTabsRegistry extends DoTBCreativeModeTabsRegistry {
        @Override
        public <T extends CreativeModeTab> Supplier<CreativeModeTab> register(String name, Supplier<ItemStack> iconSupplier, Component title) {
            CreativeModeTab group = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id(name), FabricCreativeModeTab.builder().icon(iconSupplier).title(title).displayItems((itemDisplayParameters, output) -> {
                Set<Item> addedItems = new HashSet<>();
                for (CreativeInventoryCategories category : CreativeInventoryCategories.values()) {
                    for (Item item : category.getItems()) {
                        if (!(item instanceof IconItem) && addedItems.add(item)) {
                            output.accept(item);
                        }
                    }
                }
            }).build());
            return () -> group;
        }
    }

    public static class FabricTagsRegistry extends DoTBTags {
        @Override
        public TagKey<Block> registerBlock(Identifier id) {
            return TagKey.create(Registries.BLOCK, id);
        }

        @Override
        public TagKey<Item> registerItem(Identifier id) {
            return TagKey.create(Registries.ITEM, id);
        }
    }

    public static void initClient() {
        EntityRendererRegistry.register(DoTBEntitiesRegistry.INSTANCE.CHAIR_ENTITY.get(), ChairRenderer::new);
        BlockEntityRendererRegistry.register(DoTBBlockEntitiesRegistry.INSTANCE.DISPLAYER.get(), DisplayerBERenderer::new);
        MenuScreens.register(DoTBMenusRegistry.INSTANCE.STONE_OVEN.get(), StoneOvenScreen::new);

        DoTBColorsRegistry.initialize();
        DoTBColorsRegistry.getBlocksColorRegistry().forEach((tintSource, blocks) ->
                BlockColorRegistry.register(List.of(tintSource), blocks.stream().map(Supplier::get).toArray(Block[]::new)));
    }

    public static void init() {
        DoTBEntitiesRegistry.INSTANCE = new FabricEntitiesRegistry();
        DoTBBlocksRegistry.INSTANCE = new FabricBlocksRegistry();
        DoTBItemsRegistry.INSTANCE = new FabricItemsRegistry();
        DoTBBlockEntitiesRegistry.INSTANCE = new FabricBlockEntitiesRegistry();
        DoTBMenusRegistry.INSTANCE = new FabricMenusRegistry();
        DoTBRecipeSerializersRegistry.INSTANCE = new FabricRecipeSerializersRegistry();
        DoTBRecipeTypesRegistry.INSTANCE = new FabricRecipeTypesRegistry();
        DoTBTags.INSTANCE = new FabricTagsRegistry();
        DoTBCreativeModeTabsRegistry.INSTANCE = new FabricCreativeModeTabsRegistry();
    }
}
