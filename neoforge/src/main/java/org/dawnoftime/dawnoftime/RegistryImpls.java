package org.dawnoftime.dawnoftime;

import net.minecraft.core.BlockPos;
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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.dawnoftime.dawnoftime.client.gui.creative.CreativeInventoryCategories;
import org.dawnoftime.dawnoftime.item.IconItem;
import org.dawnoftime.dawnoftime.registry.*;
import org.dawnoftime.dawnoftime.util.DoTBProperties;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegistryImpls {
    public static class NeoForgeMenusRegistry extends DoTBMenusRegistry {
        public static final DeferredRegister<MenuType<?>> MENU_TYPES_REGISTRY = DeferredRegister.create(BuiltInRegistries.MENU, DoTBCommon.MOD_ID);

        @Override
        public <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String name, BiFunction<Integer, Inventory, T> factory) {
            return MENU_TYPES_REGISTRY.register(name, () -> new MenuType<T>(factory::apply, FeatureFlags.DEFAULT_FLAGS));
        }
    }

    public static class NeoForgeBlockEntitiesRegistry extends DoTBBlockEntitiesRegistry {
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES_REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, DoTBCommon.MOD_ID);

        @Override
        public <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BiFunction<BlockPos, BlockState, T> factoryIn, Supplier<Block[]> validBlocksSupplier) {
            return BLOCK_ENTITY_TYPES_REGISTRY.register(name, () -> new BlockEntityType<T>(factoryIn::apply, validBlocksSupplier.get()));
        }
    }

    public static class NeoForgeBlocksRegistry extends DoTBBlocksRegistry {
        public static final DeferredRegister<Block> BLOCKS_REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK, DoTBCommon.MOD_ID);
        public static final DeferredRegister<Item> BLOCK_ITEMS_REGISTRY = DeferredRegister.create(BuiltInRegistries.ITEM, DoTBCommon.MOD_ID);

        public NeoForgeBlocksRegistry() {
            postRegister();
        }

        @SafeVarargs
        @Override
        public final <T extends Block, Y extends Item> Supplier<T> registerWithItem(String id, Supplier<T> block, Function<T, Y> item, TagKey<Block>... tags) {
            Supplier<T> registryBlock = BLOCKS_REGISTRY.register(id, () -> DoTBProperties.withId(id, block));
            if (item != null) {
                BLOCK_ITEMS_REGISTRY.register(id, () -> item.apply(registryBlock.get()));
            }
            if (tags.length == 0) {
                addBlockTag(registryBlock, BlockTags.MINEABLE_WITH_PICKAXE);
            } else {
                for (TagKey<Block> tag : tags) {
                    addBlockTag(registryBlock, tag);
                }
            }
            return registryBlock;
        }
    }

    public static class NeoForgeEntitiesRegistry extends DoTBEntitiesRegistry {
        public static final DeferredRegister<EntityType<?>> ENTITY_TYPES_REGISTRY = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, DoTBCommon.MOD_ID);

        @Override
        public <T extends Entity> Supplier<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
            return ENTITY_TYPES_REGISTRY.register(name, () -> builder.get().build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(DoTBCommon.MOD_ID, name))));
        }
    }

    public static class NeoForgeItemsRegistry extends DoTBItemsRegistry {
        public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(BuiltInRegistries.ITEM, DoTBCommon.MOD_ID);

        @Override
        public <T extends Item> Supplier<Item> register(String name, Supplier<T> itemSupplier) {
            return ITEMS_REGISTRY.register(name, () -> DoTBProperties.withId(name, itemSupplier));
        }
    }

    public static class NeoForgeRecipeSerializersRegistry extends DoTBRecipeSerializersRegistry {
        public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS_REGISTRY = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, DoTBCommon.MOD_ID);

        @Override
        public <T extends RecipeSerializer<? extends Recipe<?>>> Supplier<T> register(String name, Supplier<T> recipeSerializer) {
            return RECIPE_SERIALIZERS_REGISTRY.register(name, recipeSerializer);
        }
    }

    public static class NeoForgeRecipeTypesRegistry extends DoTBRecipeTypesRegistry {
        public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES_REGISTRY = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, DoTBCommon.MOD_ID);

        @Override
        public <T extends Recipe<?>> Supplier<RecipeType<T>> register(String name) {
            return RECIPE_TYPES_REGISTRY.register(name, () -> RecipeType.simple(Identifier.fromNamespaceAndPath(DoTBCommon.MOD_ID, name)));
        }
    }

    public static class NeoForgeCreativeModeTabsRegistry extends DoTBCreativeModeTabsRegistry {
        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS_REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DoTBCommon.MOD_ID);

        @Override
        public <T extends CreativeModeTab> Supplier<CreativeModeTab> register(String name, Supplier<ItemStack> iconSupplier, Component title) {
            return CREATIVE_MODE_TABS_REGISTRY.register(name, () -> CreativeModeTab.builder().icon(iconSupplier).title(title).build());
        }
    }

    public static class NeoForgeTagsRegistry extends DoTBTags {
        @Override
        public TagKey<Block> registerBlock(Identifier id) {
            return TagKey.create(Registries.BLOCK, id);
        }

        @Override
        public TagKey<Item> registerItem(Identifier id) {
            return TagKey.create(Registries.ITEM, id);
        }
    }

    public static void init(IEventBus bus) {
        DoTBEntitiesRegistry.INSTANCE = new NeoForgeEntitiesRegistry();
        NeoForgeEntitiesRegistry.ENTITY_TYPES_REGISTRY.register(bus);

        DoTBBlocksRegistry.INSTANCE = new NeoForgeBlocksRegistry();
        DoTBItemsRegistry.INSTANCE = new NeoForgeItemsRegistry();
        DoTBBlockEntitiesRegistry.INSTANCE = new NeoForgeBlockEntitiesRegistry();
        DoTBMenusRegistry.INSTANCE = new NeoForgeMenusRegistry();
        DoTBRecipeSerializersRegistry.INSTANCE = new NeoForgeRecipeSerializersRegistry();
        DoTBRecipeTypesRegistry.INSTANCE = new NeoForgeRecipeTypesRegistry();
        DoTBTags.INSTANCE = new NeoForgeTagsRegistry();
        DoTBCreativeModeTabsRegistry.INSTANCE = new NeoForgeCreativeModeTabsRegistry();

        NeoForgeBlocksRegistry.BLOCKS_REGISTRY.register(bus);
        NeoForgeBlocksRegistry.BLOCK_ITEMS_REGISTRY.register(bus);
        NeoForgeItemsRegistry.ITEMS_REGISTRY.register(bus);
        NeoForgeBlockEntitiesRegistry.BLOCK_ENTITY_TYPES_REGISTRY.register(bus);
        NeoForgeMenusRegistry.MENU_TYPES_REGISTRY.register(bus);
        NeoForgeRecipeSerializersRegistry.RECIPE_SERIALIZERS_REGISTRY.register(bus);
        NeoForgeRecipeTypesRegistry.RECIPE_TYPES_REGISTRY.register(bus);
        NeoForgeCreativeModeTabsRegistry.CREATIVE_MODE_TABS_REGISTRY.register(bus);

        bus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if (event.getTab() == DoTBCreativeModeTabsRegistry.INSTANCE.DOT_TAB.get()) {
                Set<Item> addedItems = new HashSet<>();
                for (CreativeInventoryCategories category : CreativeInventoryCategories.values()) {
                    for (Item item : category.getItems()) {
                        if (!(item instanceof IconItem) && addedItems.add(item)) {
                            event.accept(item);
                        }
                    }
                }
            }
        });
    }
}
