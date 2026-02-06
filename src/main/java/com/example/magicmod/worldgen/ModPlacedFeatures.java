package com.example.magicmod.worldgen;

import com.example.magicmod.MagicMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> MANA_CRYSTAL_PLACED =
            registerKey("mana_crystal_block_placed");
    public static final ResourceKey<PlacedFeature> FIRE_CRYSTAL_PLACED =
            registerKey("fire_crystal_block_placed");
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(
                context,
                MANA_CRYSTAL_PLACED,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_MANA_CRYSTAL_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(4),
                        CountPlacement.of(20),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(
                                VerticalAnchor.absolute(-64),
                                VerticalAnchor.absolute(30)
                        ),

                        // only place if target block is air
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.matchesBlocks(
                                        BlockPos.ZERO,
                                        Blocks.AIR
                                )
                        ),

                        // only place if block below is stone
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.hasSturdyFace(
                                        new BlockPos(0, -1, 0),
                                        Direction.UP
                                )
                        )
                )
        );
        register(
                context,
                FIRE_CRYSTAL_PLACED,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_FIRE_CRYSTAL_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(4),
                        CountPlacement.of(20),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(
                                VerticalAnchor.absolute(-64),
                                VerticalAnchor.absolute(30)
                        ),
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.matchesBlocks(
                                        BlockPos.ZERO,
                                        Blocks.AIR
                                )
                        ),
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.hasSturdyFace(
                                        new BlockPos(0, -1, 0),
                                        Direction.UP
                                )
                        )
                )
        );

    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(MagicMod.MODID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
