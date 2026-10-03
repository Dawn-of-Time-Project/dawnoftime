# Dawn of Time 26.1.2 port - summary for the DoT owner

Repository: https://github.com/Blazelow/Dawn-of-Time-26.1.2 (branch `26.1.2`, one commit `9d14337` on top of your `1.20.1-master` history)

This is a multiloader port of `1.20.1-master` (v1.5.53) to **Minecraft 26.1.2**, for **NeoForge** and **Fabric** (Forge dropped). It already includes the content of your newest 1.20.1 commit `f3a37307` (blue/green/yellow plastered stone window and column, the six waxed oak recipes, the canopy bed removal) and the chair seat fix from your 1.21 commit `7bf4d9e8`. The port's `version` is still 1.5.53; your 1.5.54 version bump, patch notes and README refactor are not synced.

Toolchain: Gradle 9.7.1, JDK 25, NeoForge 26.1.2.112 (ModDevGradle 2.0.148), Fabric loader 0.19.5 + Fabric API 0.155.3+26.1.2 (Loom 1.18.2). No Parchment/mappings needed (26.1 is unobfuscated). Build with `sh gradlew :neoforge:build :fabric:build`.

## 1. How much was tested

Played by Blazelow, NeoForge 26.1.2 (with Fusion 1.3.15 and shaders) and a Fabric 26.1.2 launch:
- the mod loads on both loaders; the log is free of data errors from DoT after the fixes below
- banner patterns: craft the pattern item, use it in the loom, the emblem renders on the banner item and on the placed banner; a `/give` banner with an emblem keeps it
- confirmed working in game: irori (after the cullface fix), chairs (seat height), small tatami mat no longer going on a small tatami floor, lit/unlit/stacked fireplaces, the Chinese stone oven (renders, lights, smokes, appears to cook)
- built and checked by script only, not yet seen in game: the painted stair corners, the new blue/green/yellow plastered windows and columns, the six waxed oak recipes, the sculpted waxed oak stairs recipe fix
- futon: placing it no longer crashes and no longer draws a vanilla bed over it; sleeping on it after the last fix is untested

Not tested yet: Fabric beyond launch, dedicated server, creative DoT tab buttons and tooltips, oven GUI details (slots/arrow), portcullis and shutters (their `neighborChanged` was rewritten), pools/faucets/water jets, loot tables in survival, the new pathfinding flag in game, creative clone of banners, Beyond Vanilla (no 26.1 build here).

## 2. What had to change for 26.1 (the port itself)

- **Registry ids on properties.** Every block and item now needs its registry id on its `Properties` before it is built. New `util/DoTBProperties` sets the id (thread-local) while each supplier runs in the loader `RegistryImpls`, so the 580 `Block.Properties.copy(...)` calls only became `DoTBProperties.copy(...)`.
- **Block API signatures:** `use` became `useItemOn`, `updateShape` takes `LevelReader` + `ScheduledTickAccess` + `RandomSource`, `neighborChanged` takes an `Orientation` (no more `fromPos`: `PortcullisBlock` now treats "same plane" as always true and the fireplace propagation checks both in-plane neighbours), `onRemove` became `affectNeighborsAfterRemoval` / `preRemoveSideEffects` (the displayer drops its items through its `Container`), `entityInside`, `canPlaceLiquid`, light/occlusion methods, `playerWillDestroy` returns `BlockState`, `codec()` for `HorizontalBlockDoT` and `StoneOvenBlock`.
- **Tooltips:** blocks have no `appendHoverText` any more. New `IBlockTooltip` + `DoTBBlockItem` keep every existing override working.
- **Tints:** vanilla `BlockTintSources.water()/foliage()`; item tints are constants in the item definitions.
- **Banner patterns:** now data (`data/dawnoftimebuilder/banner_pattern/*.json`), items use the banner-pattern component. 26.1 also needs the item tag `minecraft:loom_patterns` or the loom refuses them (added).
- **Rendering:** the displayer block entity renderer and the chair renderer use the render-state/submit API; GUI buttons and `CreativeInventoryMixin` use `GuiGraphicsExtractor`. Block render layers now come from the texture, so the Fabric `RenderLayers` class is gone.
- **Block entities:** `ValueInput/ValueOutput` for the displayer, `ChairEntity` ported (`hurtServer`, `startRiding(..., false, true)`).
- **Resources:** data folders are singular (`recipe`, `loot_table`, `advancement`, `tags/block`, `tags/item`), recipes use the new ingredient/result format, advancement icons, 611 `assets/dawnoftimebuilder/items/*.json` definitions (with constant tints for the leaf and water items), `pack.mcmeta` with `min_format/max_format`.
- **Loaders:** NeoForge/Fabric registries, Fabric dependency id is `fabric-api` (not `fabric`), the `data/forge` tags were dropped.
- **Datagen:** NeoForge block tag generator ported; the empty item tag provider was removed.

## 3. Bugs that exist in the original 1.20.1 and are fixed in the port

You will probably want the same fixes upstream:

1. **Painted stone stairs** (`white_`, `red_`, `blue_`, `green_`, `yellow_painted_stone_stairs`): the 16 upside-down (`half=top`) inner/outer corner variants have the wrong `y` rotation (a quarter turn off vanilla), so corners under ceilings render wrongly. Checked by script against vanilla's stair blockstate; the other 40 stair blockstates are correct.
2. **`irori_fireplace_off.json`:** 20 `cullface` tags on faces that are not on the block edge. A solid block next to the irori hides parts of the log (glass does not). The lit model is clean.
3. **`yellow_plastered_stone` recipe** uses the tag `#minecraft:yellow_dye`, which has never existed (use the item `minecraft:yellow_dye`).
4. **Emblem block models** used item-atlas textures (`item/logo_*`); 26.1 only accepts block-atlas sprites, so the logos were copied to `textures/block/` and the models repointed.
5. **Item models:** 33 had no `particle` texture and a few block models extended `block/cube_all` while defining their own elements (log warnings only).
6. **`sculpted_waxed_oak_stairs` recipe:** the pattern `["I", "II", "III"]` has rows of different widths. That is invalid in 1.21.2 and later (your 1.21.x commit has the same pattern); I used `["I  ", "II ", "III"]`.

26.1 renames you will hit anyway: `minecraft:chain` is `minecraft:iron_chain` (the `waxed_oak_chandelier` recipe and the `iron_fancy_lantern` model).

## 4. Changes beyond a pure port (deliberate behaviour changes - your call)

- **Stacked fireplaces light together.** `ConnectedVerticalSidedPlanFireplaceBlock.updateShape` copies `LIT` from the fireplace below, so a 2+ block tall fireplace lights/extinguishes as one (same fix as in Extra Additions). In 1.20.1 only the bottom block lit.
- **A small tatami mat can no longer be placed on top of a small tatami floor** (placement is refused; existing builds are untouched). Reproduced in 1.20.1 first: the mat can go on the floor block that a mat on spruce planks turns into. The long tatami mat and floor were left as they are.
- **Mob pathfinding around DoT blocks.** Partial blocks count as open air for land mobs, so villagers try to walk through beams and get stuck (also reported on Forge 1.20.1). New opt-in flag in `BlockDoT` (`setBlocksLandPathing()`, `isPathfindable` false for LAND only) is switched on for beams (14), support beams (14), the iron/marble/sandstone/moraq columns and the 5 plastered + 4 sided columns (about 41 blocks). Not covered yet: pergolas, lattices, chandeliers/lanterns, windows, shutters, fences, fireplaces, reliefs, folding screens, paper lamps, `marble_pillar`, timber frame pillars.
- **Futon has no block entity.** `FutonBlock` returns no `BlockEntity`. In 26.1 the vanilla bed block entity only accepts registered bed blocks, so placing a futon crashed with `Invalid block entity minecraft:bed`; returning none also stops the vanilla bed renderer from drawing over the futon model. The port has **no bed mixin**, so there is no Tensura clash (the clash comes from `BedBlockEntityMixin` in your 1.21.1 branch).
- **Chair seat** raised by 0.25 via `getPassengerAttachmentPoint` (your own 1.21 fix, applied).
- **Removed** `WaxedOakCanopyBedWoodBlock` (unused). The README still lists "canopy beds" under furniture.

## 5. Found but not fixed (needs you, or other mods)

- **DoT plate + Beyond Vanilla plate do not connect** (Discord report). `PlateBlock.isBlockPlate` is `state.getBlock() instanceof PlateBlock`, and Beyond Vanilla ships its own `org.dawnoftime.dotbv...PlateBlock` class, so the check fails across the two mods. Same pattern likely applies to other shared templates. Suggestion: connect by a block tag both mods fill, or by the shared FACING/SHAPE properties.
- **81 more block models** have `cullface` tags on faces that are not on the block edge (293 tags; mostly the French `limestone_fireplace_*` set, then `stone_bricks_fireplace_*`, `fireplace_*`, fences/railings, lattice windows, pools...). A lit fireplace with solid blocks on both sides rendered fine in a test, so this is low priority, but the irori shows the bug can be visible.
- **22 leftover ids with files but no block** (also in your repo): `bamboo_drying_tray`, `blue_painted_lattice`, `charred_spruce_log_fence`, `golden_beam`, `golden_stone_frieze`, `pre_columbian`, `puuc_limestone_frieze`, `puuc_limestone_frieze_plate`, `puuc_limestone_lattice`, `puuc_limestone_sided_column`, `puuc_limestone_squares`, `red_painted_mural`, `slab_double_ochre_roof_tiles`, `slab_double_tiles`, `slab_ochre_roof_tiles`, `slab_tiles`, `spruce_log_fence`, `spruce_log_wall`, `stone_round_template`, `stone_spiral_template`, `stone_wave_template`, `thatch`. Eight of them have blockstates whose models do not exist, so they only log warnings.
- **Banner emblem lost on creative clone** (Discord report, 1.20.1). Banner patterns are plain data in 26.1, and a banner with an emblem kept it on `/give`, in the hotbar and placed; the full clone/slot-move test is still to do.
- **Dead code:** `CappedWallBlock.isPathfindable` has the old four-parameter signature and overrides nothing in 26.1 (harmless, the `walls` tag covers those blocks).

## 6. Questions for you

1. Do you want the fireplace, tatami and pathfinding behaviour changes in the official version, or only the pure port?
2. Should the pathfinding flag cover more blocks (pergolas, lattices, windows, fences...)?
3. How should plates connect across DoT and Beyond Vanilla (a shared tag?)
4. What should happen to the 22 leftover ids: delete the broken ones, finish them, or leave them?
5. Should I sync the port with your 1.5.54 (version, patch notes, README)?
