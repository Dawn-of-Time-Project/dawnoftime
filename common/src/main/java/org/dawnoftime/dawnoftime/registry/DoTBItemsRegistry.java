package org.dawnoftime.dawnoftime.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.dawnoftime.dawnoftime.DoTBCommon;
import org.dawnoftime.dawnoftime.item.templates.ItemDoTB;

import java.util.function.Supplier;

@SuppressWarnings({"unused"})
public abstract class DoTBItemsRegistry {
    public static DoTBItemsRegistry INSTANCE;

    // Banner pattern tag keys
    private static final TagKey<BannerPattern> CHINESE_BANNER_PATTERN_TAG      = TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(DoTBCommon.MOD_ID, "pattern_item/chinese_emblem"));
    private static final TagKey<BannerPattern> GERMAN_BANNER_PATTERN_TAG       = TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(DoTBCommon.MOD_ID, "pattern_item/german_emblem"));
    private static final TagKey<BannerPattern> FRENCH_BANNER_PATTERN_TAG       = TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(DoTBCommon.MOD_ID, "pattern_item/french_emblem"));
    private static final TagKey<BannerPattern> JAPANESE_BANNER_PATTERN_TAG     = TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(DoTBCommon.MOD_ID, "pattern_item/japanese_emblem"));
    private static final TagKey<BannerPattern> PERSIAN_BANNER_PATTERN_TAG      = TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(DoTBCommon.MOD_ID, "pattern_item/persian_emblem"));
    private static final TagKey<BannerPattern> PRECOLUMBIAN_BANNER_PATTERN_TAG = TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(DoTBCommon.MOD_ID, "pattern_item/precolumbian_emblem"));
    private static final TagKey<BannerPattern> ROMAN_BANNER_PATTERN_TAG        = TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(DoTBCommon.MOD_ID, "pattern_item/roman_emblem"));

    // Banner pattern items
    public final Supplier<Item> CHINESE_BANNER_PATTERN      = register("chinese_banner_pattern",      () -> new BannerPatternItem(CHINESE_BANNER_PATTERN_TAG,      new Item.Properties().stacksTo(1)));
    public final Supplier<Item> GERMAN_BANNER_PATTERN       = register("german_banner_pattern",       () -> new BannerPatternItem(GERMAN_BANNER_PATTERN_TAG,       new Item.Properties().stacksTo(1)));
    public final Supplier<Item> FRENCH_BANNER_PATTERN       = register("french_banner_pattern",       () -> new BannerPatternItem(FRENCH_BANNER_PATTERN_TAG,       new Item.Properties().stacksTo(1)));
    public final Supplier<Item> JAPANESE_BANNER_PATTERN     = register("japanese_banner_pattern",     () -> new BannerPatternItem(JAPANESE_BANNER_PATTERN_TAG,     new Item.Properties().stacksTo(1)));
    public final Supplier<Item> PERSIAN_BANNER_PATTERN      = register("persian_banner_pattern",      () -> new BannerPatternItem(PERSIAN_BANNER_PATTERN_TAG,      new Item.Properties().stacksTo(1)));
    public final Supplier<Item> PRECOLUMBIAN_BANNER_PATTERN = register("precolumbian_banner_pattern", () -> new BannerPatternItem(PRECOLUMBIAN_BANNER_PATTERN_TAG, new Item.Properties().stacksTo(1)));
    public final Supplier<Item> ROMAN_BANNER_PATTERN        = register("roman_banner_pattern",        () -> new BannerPatternItem(ROMAN_BANNER_PATTERN_TAG,        new Item.Properties().stacksTo(1)));

    // General
    public final Supplier<Item> DOT_ITEM = register("dawn_of_time", () -> new ItemDoTB());


    public final Supplier<Item> UNFIRED_CLAY_TILE = register("unfired_clay_tile", ItemDoTB::new);
    public final Supplier<Item> CLAY_TILE = register("clay_tile", ItemDoTB::new);
    public final Supplier<Item> CLAY_TILE_WHITE = register("clay_tile_white", ItemDoTB::new);
    public final Supplier<Item> CLAY_TILE_ORANGE = register("clay_tile_orange", ItemDoTB::new);
    public final Supplier<Item> CLAY_TILE_BLACK = register("clay_tile_black", ItemDoTB::new);
    public final Supplier<Item> CLAY_TILE_BLUE = register("clay_tile_blue", ItemDoTB::new);
    public final Supplier<Item> CLAY_TILE_CYAN = register("clay_tile_cyan", ItemDoTB::new);
    public final Supplier<Item> UNFIRED_CLAY_ROOF_TILE = register("unfired_clay_roof_tile", ItemDoTB::new);
    public final Supplier<Item> GRAY_CLAY_ROOF_TILE = register("gray_clay_roof_tile", ItemDoTB::new);
    public final Supplier<Item> UNFIRED_CLAY_ROUND_TILE = register("unfired_clay_round_tile", ItemDoTB::new);
    public final Supplier<Item> GREEN_CLAY_ROOF_TILE = register("green_clay_roof_tile", ItemDoTB::new);
    public final Supplier<Item> CALCITE_POWDER = register("calcite_powder", ItemDoTB::new);
    public final Supplier<Item> LIME = register("lime", ItemDoTB::new);
    public final Supplier<Item> YELLOW_PIGMENTED_LIME = register("yellow_pigmented_lime", ItemDoTB::new);

    public abstract <T extends Item> Supplier<Item> register(final String name, final Supplier<T> itemSupplier);

}
