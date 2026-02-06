package com.example.magicmod.worldgen;

import com.example.magicmod.MagicMod;
import com.example.magicmod.block.register.ModBlockRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?,?>> OVERWORLD_MANA_CRYSTAL_KEY = registerKey("mana_crystal_block");
    public static final ResourceKey<ConfiguredFeature<?,?>> OVERWORLD_FIRE_CRYSTAL_KEY = registerKey("fire_crystal_block");
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context,
                OVERWORLD_MANA_CRYSTAL_KEY,
                Feature.BLOCK_PILE,
                new BlockPileConfiguration(
                        BlockStateProvider.simple(ModBlockRegister.MANA_CRYSTAL_BLOCK.get()))
        );
        register(context,
                OVERWORLD_FIRE_CRYSTAL_KEY,
                Feature.BLOCK_PILE,
                new BlockPileConfiguration(
                        BlockStateProvider.simple(ModBlockRegister.FIRE_CRYSTAL_BLOCK.get()))
        );

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(MagicMod.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
